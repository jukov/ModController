package info.jukov.f19plugin;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.List;

import info.jukov.f19plugin.pojo.ModData;

/**
 * User: jukov
 * Date: 06.07.2017
 * Time: 23:20
 */
@Plugin(id = "modController",
		name = "Mod Controller",
		version = "1.0",
		description = "Plugin for make plugin banlist/whitelist.",
		authors = "jukov",
		url = "https://github.com/jukov/ModController")
public class Main {

	private static final String LOG_TAG = "[F19Plugin]";

	@Inject
	public Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfigPath;

	@Listener
	public void onPreInit(final GamePreInitializationEvent event) {
		final ConfigManager configManager = new ConfigManager(logger, defaultConfigPath);

		logger.info(configManager.getConfig().toString());
		logger.info(defaultConfigPath.toString());
	}

	@Listener
	public void onPlayerJoin(final ClientConnectionEvent.Join event) {

		logger.info("onPlayerLogin " + ModExtractHelper.getPlayerMods(event.getTargetEntity()));

	}

}
