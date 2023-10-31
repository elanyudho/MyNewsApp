package com.elanyudho.core.model.usecase

import com.elanyudho.core.abstraction.UseCase
import com.elanyudho.core.model.model.Article
import com.elanyudho.core.model.repository.NewsRepository
import com.elanyudho.core.util.exception.Failure
import com.elanyudho.core.util.vo.Either
import javax.inject.Inject

class GetArticlesByQueryUseCase @Inject constructor(private val repo: NewsRepository) : UseCase<List<Article>, GetArticlesByQueryUseCase.Params>() {

    data class Params(
        val source: String,
        val page: String
    )

    override suspend fun run(params: Params): Either<Failure, List<Article>> {
        return repo.getArticlesByQuery(params.source, params.page)
    }
}