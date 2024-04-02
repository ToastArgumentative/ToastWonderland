package pine.toast.plugin

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import pine.toast.library.events.items.ItemHandler
import pine.toast.library.events.made.PlayerLeftClickEvent
import pine.toast.library.events.made.PlayerRightClickEvent
import java.io.Serializable

class ToastsSwordHandler : ItemHandler, Serializable {
    override fun onPlayerLeftClick(event: PlayerLeftClickEvent) {
        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.JUMP, 20 * 10, 1, true, false))
    }

    override fun onPlayerRightClick(event: PlayerRightClickEvent) {
        event.getPlayer().sendMessage("Right click! --------- ")
    }
}