package pine.toast.library.adapters

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import pine.toast.library.items.ItemHandler
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ItemHandlerAdapter : PersistentDataType<ByteArray, ItemHandler> {
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<ItemHandler> {
        return ItemHandler::class.java
    }

    override fun fromPrimitive(bytes: ByteArray, context: PersistentDataAdapterContext): ItemHandler {
        ByteArrayInputStream(bytes).use { byteArrayInputStream ->
            ObjectInputStream(byteArrayInputStream).use { objectInputStream ->
                return objectInputStream.readObject() as ItemHandler
            }
        }
    }

    override fun toPrimitive(handler: ItemHandler, context: PersistentDataAdapterContext): ByteArray {
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            ObjectOutputStream(byteArrayOutputStream).use { objectOutputStream ->
                objectOutputStream.writeObject(handler)
                return byteArrayOutputStream.toByteArray()
            }
        }
    }


}