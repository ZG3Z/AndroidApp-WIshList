package com.example.prm1.data.model

import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM product;")
    fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    fun getProduct(id: Long): ProductEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(newProduct: ProductEntity)

    @Query("DELETE FROM product WHERE id = :id")
    fun deleteProduct(id: Long)
}