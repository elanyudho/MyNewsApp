package com.elanyudho.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elanyudho.core.model.model.Source

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSources(news: List<Source>)

    @Query("SELECT * FROM news WHERE category = :category LIMIT 10 OFFSET :offset")
    fun getSourceByCategory(category: String, offset: Int): List<Source>

    @Query("SELECT * FROM news WHERE name LIKE '%' || :name || '%' LIMIT 10 OFFSET :offset")
    fun getSourceByName(name: String, offset: Int): List<Source>
}