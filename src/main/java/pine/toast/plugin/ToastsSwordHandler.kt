package pine.toast.plugin

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import pine.toast.library.events.items.ItemHandler
import pine.toast.library.events.made.PlayerLeftClickEvent
import pine.toast.library.events.made.PlayerRightClickEvent
import pine.toast.library.utilities.CooldownManager
import java.io.Serializable

class ToastsSwordHandler : ItemHandler, Serializable {
    override fun onPlayerLeftClick(event: PlayerLeftClickEvent) {
        if (CooldownManager.isOnCooldownItem(event.getMainHand())) {
            val cooldownTime = CooldownManager.getCooldownTimeItem(event.getMainHand())
            val time = (cooldownTime - System.currentTimeMillis()) / 1000
            event.getPlayer().sendMessage("Item on cooldown for $time seconds")
        } else {
            CooldownManager.applyCooldownItem(event.getMainHand(), 10)
            event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.JUMP, 20 * 10, 1, true, false))
            event.getPlayer().sendMessage("Applied Jump Boost!!!")
        }

    }

    override fun onPlayerRightClick(event: PlayerRightClickEvent) {
        event.getPlayer().sendMessage("Right click! --------- ")
    }
}