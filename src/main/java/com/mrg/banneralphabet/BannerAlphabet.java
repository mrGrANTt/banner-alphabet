package com.mrg.banneralphabet;

import com.mrg.banneralphabet.client.BannerCommandHandler;
import com.mrg.banneralphabet.util.config.BannerConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BannerAlphabet implements ModInitializer {
	public static final String MOD_ID = "banner-alphabet";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		BannerConfig.deserialize();
		ClientLifecycleEvents.CLIENT_STOPPING.register((mc) -> BannerConfig.serialize());

		ClientCommandRegistrationCallback.EVENT.register(
				(a,b) -> BannerCommandHandler.register(a)
		);

		LOGGER.info("Hello ABC-Banners world!");
	}
}