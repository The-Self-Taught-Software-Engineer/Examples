package codes.jakob.tstse.example.idiomatic.collections_vs_sequences.shared

import mu.KLogger
import mu.KotlinLogging

/**
 * A [MutableCollection] that logs whenever it performs an action.
 * Note that this class should only be used for debugging purposes.
 */
class SpyCollection<E>(elements: Collection<E> = emptyList()) : AbstractMutableCollection<E>() {
    init {
        logger.debug { "Instantiated ${SpyCollection::class.simpleName} with ${elements.count()} elements" }
    }

    override val size: Int get() = backing.size
    private val backing: MutableCollection<E> = ArrayList(elements)
    private var capacity = 10

    override fun add(element: E): Boolean {
        if (size + 1 > capacity) {
            capacity += capacity / 2
            logger.debug { "Increased capacity to $capacity" }
        }
        return backing.add(element).also { logger.debug { "Added $element" } }
    }

    override fun iterator(): MutableIterator<E> {
        return backing.iterator()
    }

    companion object {
        private val logger: KLogger = KotlinLogging.logger {}

        fun <E> Sequence<E>.toSpyCollection(): SpyCollection<E> {
            val destination = SpyCollection<E>()
            for (item in this) {
                destination.add(item)
            }
            return destination
        }

        fun <E> Collection<E>.toSpyCollection(): SpyCollection<E> {
            return SpyCollection(this)
        }
    }
}
