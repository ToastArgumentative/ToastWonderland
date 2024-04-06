package pine.toast.library.adapters

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import pine.toast.library.enchants.EnchantmentStorage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class EnchantmentStorageAdapter : PersistentDataType<ByteArray, EnchantmentStorage> {
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<EnchantmentStorage> {
        return EnchantmentStorage::class.java
    }

    override fun toPrimitive(storage: EnchantmentStorage, context: PersistentDataAdapterContext): ByteArray {
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            ObjectOutputStream(byteArrayOutputStream).use { objectOutputStream ->
                objectOutputStream.writeObject(storage)
                return byteArrayOutputStream.toByteArray()
            }
        }
    }

    override fun fromPrimitive(bytes: ByteArray, context: PersistentDataAdapterContext): EnchantmentStorage {
        ByteArrayInputStream(bytes).use { byteArrayInputStream ->
            ObjectInputStream(byteArrayInputStream).use { objectInputStream ->
                return objectInputStream.readObject() as EnchantmentStorage
            }
        }
    }


}