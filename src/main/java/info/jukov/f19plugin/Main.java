package info.jukov.f19plugin;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.Optional;

/**
 * User: jukov
 * Date: 06.07.2017
 * Time: 23:20
 */
@Plugin(id = "f19plugin", name = "F19 Plugin", version = "1.0")
public class Main {

	@Inject
	public Logger logger;

	@Listener
	public void onPlayerJoin(final ClientConnectionEvent.Join join) {
		logger.info("onPlayerJoin " + ModExtractHelper.getPlayerMods(join.getTargetEntity()));
	}

	@Listener
	public void onPlayerLogin(final ClientConnectionEvent.Login login) {
		final Optional<Player> optionalPlayer = login.getTargetUser().getPlayer();

		if (optionalPlayer.isPresent()) {
			logger.info("onPlayerLogin " + ModExtractHelper.getPlayerMods(optionalPlayer.get()));
		}
	}

}
