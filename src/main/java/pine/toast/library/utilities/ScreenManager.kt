package pine.toast.library.utilities

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player


object ScreenManager {


    /**
     * Send a giant message onto a players screen
     * @param player Player The player to send the message to
     * @param title String The title of the message
     * @param subtitle String The subtitle of the message - Can be null if you don't want a subtitle
     */
    fun sendScreenMessage(player: Player, title: String, subtitle: String?) {
        val titleComponent = Component.text(title)
        val subtitleComponent = Component.text(subtitle ?: "")
        val showTitle = Title.title(titleComponent, subtitleComponent)
        player.showTitle(showTitle)
    }


}