package pine.toast.library.utilities

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import pine.toast.library.utilities.fastboard.FastBoard
import java.util.*



object ScoreboardManager : Listener {

    private val scoreboards: MutableMap<UUID, FastBoard> = mutableMapOf()



    fun createNewScoreboard(
        player: Player,
        title: String,
        lines: List<String>,
    ) : FastBoard {
        val board = FastBoard(player)
        board.updateTitle(title)
        board.updateLines(lines)

        scoreboards[player.uniqueId] = board

        return board

    }

    fun removeScoreboard(player: Player) {
        val board = scoreboards[player.uniqueId]
        board?.delete()
        scoreboards.remove(player.uniqueId)
    }

    fun getScoreboards(): MutableMap<UUID, FastBoard> {
        return scoreboards
    }

}