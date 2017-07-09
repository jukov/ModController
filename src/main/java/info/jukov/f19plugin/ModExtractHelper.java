package info.jukov.f19plugin;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;

import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.jukov.f19plugin.pojo.ModData;

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

	public static List<ModData> getPlayerMods(final Player player) {

		final EntityPlayerMP playerMP = (EntityPlayerMP) player;
		final NetworkManager networkManager;

		try {
			final NetHandlerPlayServer handlerPlayServer = (NetHandlerPlayServer) playerMP.getClass().getField(FIELD_71135_A).get(playerMP);
			networkManager = (NetworkManager) handlerPlayServer.getClass().getField(FIELD_147371_A).get(handlerPlayServer);
		} catch (final IllegalAccessException | NoSuchFieldException e) {
			throw new IllegalStateException(e);
		}

		final Map<String, String> modMap = NetworkDispatcher.get(networkManager).getModList();
		final List<ModData> modList = new ArrayList<>();

		modMap.entrySet().forEach(entry -> modList.add(new ModData(entry.getKey(), entry.getValue())));

		return modList;
	}
}
