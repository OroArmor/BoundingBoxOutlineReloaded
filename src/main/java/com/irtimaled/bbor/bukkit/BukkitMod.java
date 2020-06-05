package com.irtimaled.bbor.bukkit;

import com.irtimaled.bbor.common.CommonProxy;
import com.irtimaled.bbor.common.messages.SubscribeToServer;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitMod extends JavaPlugin {
    private final CommonProxy commonProxy;
    private final Events events;

    public BukkitMod() {
        commonProxy = new CommonProxy();
        commonProxy.init();
        events = new Events();
    }

    @Override
    public void onEnable() {
        events.enable();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, events::onTick, 11, 11);
        getServer().getPluginManager().registerEvents(events, this);
        getServer().getMessenger().registerIncomingPluginChannel(this, SubscribeToServer.NAME, events);
    }

    @Override
    public void onDisable() {
        events.disable();
        commonProxy.clearCaches();
    }
}
