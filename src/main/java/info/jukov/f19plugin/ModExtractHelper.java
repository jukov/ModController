package info.jukov.f19plugin;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;

import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;

/**
 * User: jukov
 * Date: 08.07.2017
 * Time: 21:48
 */
public final class ModExtractHelper {

	public static final String FIELD_71135_A = "field_71135_a";
	public static final String FIELD_147371_A = "field_147371_a";

	private ModExtractHelper() {
	}

	@Nullable
	public static SortedSet<String> getPlayerMods(final Player player) {
		if (player == null) {
			return null;
		}

		final EntityPlayerMP playerMP = (EntityPlayerMP) player;
		final NetworkManager networkManager;

		try {
			final NetHandlerPlayServer handlerPlayServer = (NetHandlerPlayServer) playerMP.getClass().getField(FIELD_71135_A).get(playerMP);
			networkManager = (NetworkManager) handlerPlayServer.getClass().getField(FIELD_147371_A).get(handlerPlayServer);
		} catch (final IllegalAccessException | NoSuchFieldException e) {
			return null;
		}

		if (networkManager == null) {
			return null;
		}

		final NetworkDispatcher networkDispatcher = NetworkDispatcher.get(networkManager);

		if (networkDispatcher == null) {
			return null;
		}

		final Map<String, String> modMap;

		try {
			modMap = networkDispatcher.getModList();
		} catch (@SuppressWarnings("ProhibitedExceptionCaught") final NullPointerException e) {
			return null;
		}

		final TreeSet<String> modSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

		modMap.entrySet().forEach(entry -> modSet.add(entry.getKey()));

		return modSet;
	}
}
