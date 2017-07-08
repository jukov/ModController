@file:JvmName("ModExtractHelper")
package info.jukov.f19plugin

import info.jukov.f19plugin.pojo.ModData
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.NetworkManager
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher
import org.spongepowered.api.entity.living.player.Player
import java.util.*

/**
 * User: jukov
 * Date: 06.07.2017
 * Time: 22:49
 */
const val FIELD_71135_A = "field_71135_a"
const val FIELD_147371_A = "field_147371_a"

public fun getPlayerMods(player: Player): List<ModData> {

	val playerMP = player as EntityPlayerMP
	val handlerPlayServer = playerMP.javaClass.getField(FIELD_71135_A).get(playerMP) as NetHandlerPlayServer;
	val networkManager = handlerPlayServer.javaClass.getField(FIELD_147371_A).get(handlerPlayServer) as NetworkManager;

	val modMap = NetworkDispatcher.get(networkManager).modList

	val modList = ArrayList<ModData>()

	modMap.entries.forEach {
		modList.add(ModData(it.key, it.value))
	}

	return modList
}
