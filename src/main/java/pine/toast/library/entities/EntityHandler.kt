package pine.toast.library.entities

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.entity.EntityDamageEvent

abstract class EntityHandler {

    abstract fun onSpawn(entity: Entity, location: Location)

    abstract fun onDeath(entity: Entity, location: Location)

    abstract fun onTakeDamage(entity: Entity, cause: EntityDamageEvent.DamageCause, causedBy: Entity, amount: Double)

    abstract fun onDealDamage(entity: Entity, target: Entity, amount: Double)


}