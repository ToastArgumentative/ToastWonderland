package pine.toast.library.items

import pine.toast.library.events.PlayerLeftClickEvent
import pine.toast.library.events.PlayerRightClickEvent
import java.io.Serializable

interface ItemHandler : Serializable {

    fun onPlayerLeftClick(event: PlayerLeftClickEvent)

    fun onPlayerRightClick(event: PlayerRightClickEvent)


}