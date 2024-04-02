package pine.toast.library.events.items

import pine.toast.library.events.made.PlayerLeftClickEvent
import pine.toast.library.events.made.PlayerRightClickEvent
import java.io.Serializable

interface ItemHandler : Serializable {

    fun onPlayerLeftClick(event: PlayerLeftClickEvent)

    fun onPlayerRightClick(event: PlayerRightClickEvent)


}