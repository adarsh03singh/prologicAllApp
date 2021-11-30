package com.prologic.laughinggoat.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductRoomDao {

    @Query("SELECT * FROM product_room")
    fun get(): List<ProductRoom>

    @Query("SELECT * FROM product_room")
    fun getLive(): LiveData<List<ProductRoom>>

    @Query("SELECT COUNT(id) FROM product_room")
    fun getCount(): LiveData<Int>

    @Insert
    suspend fun insert(productRoom: ProductRoom)

    @Query("UPDATE product_room SET quantity = :quantity WHERE unique_id = :unique_id")
    suspend fun update(quantity: Int, unique_id: String): Int

    @Update
    fun update(productRoom: ProductRoom): Int

    @Query("DELETE FROM product_room")
    fun deleteTableData()

    @Query("DELETE FROM product_room WHERE unique_id = :unique_id")
    fun deleteProduct(unique_id: String)

    @Query("SELECT quantity FROM product_room WHERE unique_id = :unique_id")
    fun getProduct(unique_id: String): Int

}