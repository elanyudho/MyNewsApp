package com.elanyudho.core.model.repository

import com.elanyudho.core.model.model.Article
import com.elanyudho.core.model.model.Source
import com.elanyudho.core.util.exception.Failure
import com.elanyudho.core.util.vo.Either

interface NewsRepository {
    suspend fun getSourcesByCategory(category: String, page: Int): Either<Failure, List<Source>>

    suspend fun getSourceByName(name: String, page: Int): List<Source>

    suspend fun getArticles(source: String, page: String): Either<Failure, List<Article>>

    suspend fun getArticlesByQuery(source: String, page: String): Either<Failure, List<Article>>

}