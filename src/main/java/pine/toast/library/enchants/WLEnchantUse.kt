package pine.toast.library.enchants

import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.*
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.player.*

abstract class WLEnchantUse {

    abstract val enchantmentProps: WLEnchantProps

    abstract fun onHit(event: EntityDamageByEntityEvent, player: Player, target: Entity)

    abstract fun onDamageTake(event: EntityDamageEvent, player: Player, cause: DamageCause)

    abstract fun onBlockBreak(event: PlayerHarvestBlockEvent, player: Player, block: Block)

    abstract fun onItemUse(event: PlayerInteractEvent, player: Player, clickedBlock: Block?, action: Action)

    abstract fun onItemConsume(event: PlayerItemConsumeEvent, player: Player)

    abstract fun onItemDrop(event: PlayerDropItemEvent)

    abstract fun onEntityKilled(event: EntityDeathEvent)

    abstract fun onProjectileHit(event: ProjectileHitEvent)

    abstract fun onProjectileLaunch(event: ProjectileLaunchEvent)

    abstract fun onFire(event: EntityCombustEvent)

    abstract fun onDamageDealt(event: EntityDamageByEntityEvent)

    abstract fun onHeal(event: EntityRegainHealthEvent)

    abstract fun onExpGain(event: PlayerExpChangeEvent)

    abstract fun onPlayerMove(event: PlayerMoveEvent)
}
