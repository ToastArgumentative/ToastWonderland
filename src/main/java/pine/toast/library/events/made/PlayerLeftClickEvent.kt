package pine.toast.library.events.made

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

class PlayerLeftClickEvent(
    private val player: Player,
    private val mainHand: ItemStack,
    private val offHand: ItemStack
) : Event(), Cancellable {

    private var isCancelled = false


    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }



    override fun isCancelled(): Boolean {
        return isCancelled
    }

    override fun setCancelled(p0: Boolean) {
        isCancelled = p0
    }

    fun getPlayer(): Player {
        return player
    }

    fun getMainHand(): ItemStack {
        return mainHand
    }

    fun getOffHand(): ItemStack {
        return offHand
    }


}