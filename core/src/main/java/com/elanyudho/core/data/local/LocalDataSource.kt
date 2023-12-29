package com.elanyudho.core.data.local

import com.elanyudho.core.data.local.room.NewsDao
import com.elanyudho.core.model.model.Source
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val newsDao: NewsDao) {

    fun insertSources(sources: List<Source>) = newsDao.insertSources(sources)

    fun getSourcesByCategory(category: String, offset: Int): List<Source> = newsDao.getSourceByCategory(category, offset)

    fun getSourcesByName(name: String, offset: Int): List<Source> = newsDao.getSourceByName(name, offset)

}