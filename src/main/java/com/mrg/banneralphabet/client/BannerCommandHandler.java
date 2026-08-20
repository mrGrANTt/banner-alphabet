package com.mrg.banneralphabet.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mrg.banneralphabet.util.config.BannerConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public final class BannerCommandHandler {
    private static final SuggestionProvider<FabricClientCommandSource> OPTIONS = (context, builder) ->
            CommandSource.suggestMatching(new String[]{"add", "rem", "replace", "save", "load"}, builder);

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("banners")
            .then(ClientCommandManager.argument("command", StringArgumentType.string())
                    .suggests(OPTIONS)
                .executes(BannerCommandHandler::execute)
            )
        );
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        String cmd = StringArgumentType.getString(context, "command");
        ItemStack item = context.getSource().getPlayer().getMainHandStack();
        ItemStack item2 = context.getSource().getPlayer().getOffHandStack();
        boolean isBanner = item.getItem() instanceof BannerItem;
        boolean isBanner2 = item2.getItem() instanceof BannerItem;
        switch (cmd) {
            case "add":
                if (!isBanner) {
                    context.getSource().sendError(Text.translatable("banner-alphabet:command.no_banner_in_arm"));
                    return 1;
                }
                context.getSource().sendFeedback(Text.translatable(BannerConfig.addBanner(item) ? "banner-alphabet:command.added" : "banner-alphabet:command.no_added"));
                break;
            case "rem":
                if (!isBanner) {
                    context.getSource().sendError(Text.translatable("banner-alphabet:command.no_banner_in_arm"));
                    return 1;
                }
                context.getSource().sendFeedback(Text.translatable(BannerConfig.remBanner(item) ? "banner-alphabet:command.removed" : "banner-alphabet:command.no_removed"));
                break;
            case "replace":
                if (!isBanner || !isBanner2) {
                    context.getSource().sendError(Text.translatable("banner-alphabet:command.no_banners_in_arms"));
                    return 1;
                }
                boolean try_ = BannerConfig.replaceBanner(item2, item);
                context.getSource().sendFeedback(Text.translatable(try_ ? "banner-alphabet:command.replace" : "banner-alphabet:command.no_replace"));
                if (!try_) context.getSource().sendError(Text.translatable("banner-alphabet:command.no_banners_in_arms"));
                break;
            case "save":
                BannerConfig.serialize();
                context.getSource().sendFeedback(Text.translatable("banner-alphabet:command.saved"));
                break;
            case "load":
                BannerConfig.deserialize();
                context.getSource().sendFeedback(Text.translatable("banner-alphabet:command.loaded"));
                break;
            default:
                return 0;
        }
        return 1;
    }
}
