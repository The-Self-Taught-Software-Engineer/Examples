package codes.jakob.tstse.example.common

import kotlin.reflect.KProperty1

abstract class Query<T>(protected val value: T) {
    abstract fun isEqualTo(value: T): Query<T>

    abstract fun <T> contains(element: T): Query<T>

    companion object {
        @JvmName("where")
        fun <T, V> where(property: KProperty1<T, V>): Query<V> {
            TODO("Not yet implemented")
        }

        @JvmName("whereCollection")
        fun <T, V> where(property: KProperty1<T, Collection<V>>): Query<V> {
            TODO("Not yet implemented")
        }
    }
}
