package pine.toast.library.entities

import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.persistence.PersistentDataType
import pine.toast.library.WonderlandKeys

object EntityManager : Listener {


    private val entityHandlers: MutableMap<String, EntityHandler> = mutableMapOf()


    /**
     * Applies the key to your entity to mark it with it's handler
     * make sure the handler is registered
     * @param key String The namespace key
     * @param entity Entity The entity to apply the key
     * @param handler Class<out EntityHandler> The handler
     */
    fun injectHandler(key: String, entity: Entity) {
        entity.persistentDataContainer.set(WonderlandKeys.ENTITY_HANDLER, PersistentDataType.STRING, key)
    }

    /**
     * Registers the handler with the key into a map
     * @param key String The namespace key
     * @param handler Class<out EntityHandler> The handler
     * @throws IllegalArgumentException if the handler already exists
     */
    fun registerHandler(key: String, handler: EntityHandler) {
        if (entityHandlers.containsKey(key)) throw IllegalArgumentException("Handler already exists for key $key")
        entityHandlers[key] = handler
    }

    private fun getHandler(key: String): EntityHandler? {
        if (!entityHandlers.containsKey(key)) throw IllegalArgumentException("No handler found for key $key")
        return entityHandlers[key]
    }



    @EventHandler(priority = EventPriority.HIGH)
    private fun onEntitySpawn(event: EntitySpawnEvent) {
        val entity = event.entity
        val entityStorage = entity.persistentDataContainer

        val key = entityStorage.get(WonderlandKeys.ENTITY_HANDLER, PersistentDataType.STRING) ?: return
        val handler = getHandler(key) ?: throw IllegalArgumentException("No handler found for key $key")

        handler.onSpawn(entity, event.location)

    }

    @EventHandler(priority = EventPriority.HIGH)
    private fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val entityStorage = entity.persistentDataContainer

        val key = entityStorage.get(WonderlandKeys.ENTITY_HANDLER, PersistentDataType.STRING) ?: return
        val handler = getHandler(key) ?: throw IllegalArgumentException("No handler found for key $key")

        handler.onDeath(entity, entity.location)
    }

    @EventHandler(priority = EventPriority.HIGH)
    private fun onEntityTakesDamage(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val entityStorage = entity.persistentDataContainer
        val entityKey = entityStorage.get(WonderlandKeys.ENTITY_HANDLER, PersistentDataType.STRING) ?: return
        val entityHandler = getHandler(entityKey) ?: throw IllegalArgumentException("No handler found for key $entityKey")

        entityHandler.onTakeDamage(entity, event.cause, event.damager, event.damage)
    }

    @EventHandler(priority = EventPriority.HIGH)
    private fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val target = event.damager
        val targetStorage = target.persistentDataContainer

        val targetKey = targetStorage.get(WonderlandKeys.ENTITY_HANDLER, PersistentDataType.STRING) ?: return
        val targetHandler = getHandler(targetKey) ?: throw IllegalArgumentException("No handler found for key $targetKey")
        targetHandler.onDealDamage(target, event.entity, event.damage)

    }


}