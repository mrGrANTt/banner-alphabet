package com.mrg.banneralphabet.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mrg.banneralphabet.util.config.BannerConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

public final class BannerCommandHandler {
    private static final SuggestionProvider<SharedSuggestionProvider> OPTIONS = SuggestionProviders.register(Identifier.parse("mrg:banner-alphabet/suggest"),  (context, builder) ->
            SharedSuggestionProvider.suggest(new String[]{"add", "rem", "replace", "save", "load", "help"}, builder));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("banners")
                .then(Commands.argument("command", StringArgumentType.string())
                        .suggests(SuggestionProviders.cast(OPTIONS))
                        .executes(BannerCommandHandler::execute)
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        String cmd = StringArgumentType.getString(context, "command");
        ItemStack item = context.getSource().getPlayer().getMainHandItem();
        ItemStack item2 = context.getSource().getPlayer().getOffhandItem();
        boolean isBanner = item.getItem() instanceof BannerItem;
        boolean isBanner2 = item2.getItem() instanceof BannerItem;
        switch (cmd) {
            case "add":
                if (!isBanner) {
                    context.getSource().sendFailure(Component.translatable("banner-alphabet:command.no_banner_in_arm"));
                    return 1;
                }
                context.getSource().sendSystemMessage(Component.translatable(BannerConfig.addBanner(item) ? "banner-alphabet:command.added" : "banner-alphabet:command.no_added"));
                break;
            case "rem":
                if (!isBanner) {
                    context.getSource().sendFailure(Component.translatable("banner-alphabet:command.no_banner_in_arm"));
                    return 1;
                }
                context.getSource().sendSystemMessage(Component.translatable(BannerConfig.remBanner(item) ? "banner-alphabet:command.removed" : "banner-alphabet:command.no_removed"));
                break;
            case "replace":
                if (!isBanner || !isBanner2) {
                    context.getSource().sendFailure(Component.translatable("banner-alphabet:command.no_banners_in_arms"));
                    return 1;
                }
                boolean try_ = BannerConfig.replaceBanner(item2, item);
                context.getSource().sendSystemMessage(Component.translatable(try_ ? "banner-alphabet:command.replace" : "banner-alphabet:command.no_replace"));
                if (!try_) context.getSource().sendFailure(Component.translatable("banner-alphabet:command.no_banners_in_arms"));
                break;
            case "save":
                BannerConfig.serialize();
                context.getSource().sendSystemMessage(Component.translatable("banner-alphabet:command.saved"));
                break;
            case "load":
                BannerConfig.deserialize();
                context.getSource().sendSystemMessage(Component.translatable("banner-alphabet:command.loaded"));
                break;
            case "help":
                context.getSource().sendSystemMessage(Component.translatable("banner-alphabet:command.help"));
                break;
            default:
                return 0;
        }
        return 1;
    }
}
