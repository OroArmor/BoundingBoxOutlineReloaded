package com.irtimaled.bbor.client.commands;

import com.irtimaled.bbor.client.providers.SlimeChunkProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;

public class SeedCommand {
    private static final String COMMAND = "bbor:seed";
    private static final String SEED = "seed";

    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        LiteralArgumentBuilder command = CommandManager.literal(COMMAND)
                .then(CommandManager.argument(SEED, Arguments.string())
                        .executes(context -> {
                            String argument = Arguments.getString(context, SEED);
                            handleSeedCommand(argument);
                            return 0;
                        }));
        commandDispatcher.register(command);
    }

    private static void handleSeedCommand(String argument) {
        Long seed = parseNumericSeed(argument);
        if (seed == null) {
            seed = (long) argument.hashCode();
        }
        SlimeChunkProvider.setSeed(seed);
    }

    private static Long parseNumericSeed(String argument) {
        try {
            return Long.parseLong(argument);
        } catch (final NumberFormatException ex) {
            return null;
        }
    }
}
