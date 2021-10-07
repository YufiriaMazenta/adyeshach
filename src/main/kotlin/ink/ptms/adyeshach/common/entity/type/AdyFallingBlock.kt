package ink.ptms.adyeshach.common.entity.type

import com.google.gson.annotations.Expose
import ink.ptms.adyeshach.api.AdyeshachAPI
import ink.ptms.adyeshach.api.nms.NMS
import ink.ptms.adyeshach.common.entity.ClientEntity
import ink.ptms.adyeshach.common.entity.EntityTypes
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author sky
 * @date 2020/8/4 23:15
 */
class AdyFallingBlock : AdyEntity(EntityTypes.FALLING_BLOCK) {

    @Expose
    var material = Material.STONE
        private set

    @Expose
    var data = 0.toByte()
        private set

    init {
//        naturalEditor("block")
//            .reset { _, _ ->
//                material = Material.STONE
//                data = 0.toByte()
//            }
//            .modify { player, entity, _ ->
//                player.openMenu<Basic>("Adyeshach Editor : Input") {
//                    rows(1)
//                    map("####@####")
//                    set('#', XMaterial.BLACK_STAINED_GLASS_PANE) {
//                        name = "§f"
//                    }
//                    set('@', ItemStack(material, 1, data.toShort()))
//                    onClick('#')
//                    onClose {
//                        val item = it.inventory.getItem(4)
//                        if (item.isNotAir()) {
//                            material = item!!.type
//                            data = item.durability.toByte()
//                        }
//                        destroy()
//                        spawn(getLocation())
//                        entity.openEditor(player)
//                    }
//                }
//            }
//            .display { player, _, _ ->
//                ItemStack(material, 1, data.toShort()).getName(player)
//            }
    }

    override fun visible(viewer: Player, visible: Boolean) {
        if (visible) {
            spawn(viewer) {
                val clientId = UUID.randomUUID()
                AdyeshachAPI.clientEntityMap.computeIfAbsent(viewer.name) { ConcurrentHashMap() }[index] = ClientEntity(this, clientId)
                NMS.INSTANCE.spawnEntityFallingBlock(viewer, index, clientId, position.toLocation(), material, data)
            }
        } else {
            destroy(viewer) {
                NMS.INSTANCE.destroyEntity(viewer, index)
                AdyeshachAPI.clientEntityMap[viewer.name]?.remove(index)
            }
        }
    }

    fun setMaterial(material: Material, data: Byte) {
        this.material = material
        this.data = data
        respawn()
    }

    fun setMaterial(material: Material) {
        this.material = material
        respawn()
    }

    fun setData(data: Byte) {
        this.data = data
        respawn()
    }
}