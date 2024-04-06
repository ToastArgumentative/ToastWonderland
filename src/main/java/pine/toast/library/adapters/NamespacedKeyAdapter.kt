package pine.toast.library.adapters

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class NamespacedKeyAdapter : PersistentDataType<ByteArray, NamespacedKey> {
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<NamespacedKey> {
        return NamespacedKey::class.java
    }

    override fun fromPrimitive(bytes: ByteArray, context: PersistentDataAdapterContext): NamespacedKey {
        ByteArrayInputStream(bytes).use { byteArrayInputStream ->
            ObjectInputStream(byteArrayInputStream).use { objectInputStream ->
                return objectInputStream.readObject() as NamespacedKey
            }
        }
    }

    override fun toPrimitive(handler: NamespacedKey, context: PersistentDataAdapterContext): ByteArray {
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            ObjectOutputStream(byteArrayOutputStream).use { objectOutputStream ->
                objectOutputStream.writeObject(handler)
                return byteArrayOutputStream.toByteArray()
            }
        }
    }
}