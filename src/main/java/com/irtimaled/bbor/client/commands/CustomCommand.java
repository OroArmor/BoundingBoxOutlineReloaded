package com.irtimaled.bbor.client.commands;

import com.irtimaled.bbor.client.providers.CustomBeaconProvider;
import com.irtimaled.bbor.client.providers.CustomBoxProvider;
import com.irtimaled.bbor.client.providers.CustomLineProvider;
import com.irtimaled.bbor.client.providers.CustomSphereProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;

public class CustomCommand {
    private static final String COMMAND = "bbor:custom";
    private static final String BOX = "box";
    private static final String BEACON = "beacon";
    private static final String LINE = "line";
    private static final String SPHERE = "sphere";

    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        LiteralArgumentBuilder command = CommandManager.literal(COMMAND)
                .then(BoxCommandBuilder.build(BOX))
                .then(BeaconCommandBuilder.build(BEACON))
                .then(LineCommandBuilder.build(LINE))
                .then(SphereCommandBuilder.build(SPHERE))
                .then(CommandManager.literal(ArgumentNames.CLEAR)
                        .executes(context -> {
                            CustomBoxProvider.clear();
                            CustomBeaconProvider.clear();
                            CustomLineProvider.clear();
                            CustomSphereProvider.clear();

                            CommandHelper.feedback(context, "bbor.commands.custom.cleared.all");
                            return 0;
                        }));
        commandDispatcher.register(command);
    }
}
