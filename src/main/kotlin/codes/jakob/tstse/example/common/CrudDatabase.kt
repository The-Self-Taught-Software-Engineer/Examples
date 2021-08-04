package codes.jakob.tstse.example.common

interface CrudDatabase {
    fun <T> create(item: T): T

    fun <T, R> read(query: Query<T>): R

    fun <T, R, U> update(query: Query<T>, update: Query<U>): R

    fun <T> delete(query: Query<T>): Boolean
}
