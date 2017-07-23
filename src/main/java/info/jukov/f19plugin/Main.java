package info.jukov.f19plugin;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import info.jukov.f19plugin.pojo.IPluginConfig;

/**
 * User: jukov
 * Date: 06.07.2017
 * Time: 23:20
 */
@Plugin(id = "mod_controller",
		name = "Mod Controller",
		version = "1.0",
		description = "Plugin for make plugin banlist/whitelist.",
		authors = "jukov",
		url = "https://github.com/jukov/ModController")
public class Main {

	public static final int KICK_TIMEOUT = 1000;

	@Inject
	public Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfigPath;

	private IPluginConfig pluginConfig;

	@Listener
	public void onPreInit(final GamePreInitializationEvent event) {
		final ConfigStorageDelegate configStorageDelegate = new ConfigStorageDelegate(logger, defaultConfigPath);

		pluginConfig = new PluginConfigWithSaving(configStorageDelegate);

		CmdDelegate.register(this, logger, pluginConfig);
	}

	@Listener
	public void onPlayerJoin(final ClientConnectionEvent.Join event) {

		final Player player = event.getTargetEntity();

		final SortedSet<String> playerModsList = ModExtractHelper.getPlayerMods(player);

		logger.info("Player mods: " + playerModsList);

		if (playerModsList == null) {
			delayedKick(player, Text.of("Cannot recognize your mod list. Maybe you vanilla player?"));
			return;
		}

		switch (pluginConfig.getMode()) {
			case WHITELIST:
				if (!pluginConfig.getMods().equals(playerModsList)) {
					delayedKick(player, Text.of("Your mod list not corrsepond server's whitelist"));
				}
				break;
			case BANLIST:
				pluginConfig.getMods().forEach(modId -> {
					if (playerModsList.contains(modId)) {
						delayedKick(player, Text.of("Your mod list contains illegal mod: " + modId));
					}
				});
				break;
		}
	}

	/**
	 * Kick a player with a delay.
	 * <p>
	 * If a player kicked right after join, reason not showing.
	 */
	private static void delayedKick(final Player player, final Text reason) {
		try {
			TimeUnit.MILLISECONDS.sleep(KICK_TIMEOUT);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		player.kick(reason);
	}
}