package com.elanyudho.core.model.usecase

import com.elanyudho.core.abstraction.UseCase
import com.elanyudho.core.model.model.Source
import com.elanyudho.core.model.repository.NewsRepository
import com.elanyudho.core.util.exception.Failure
import com.elanyudho.core.util.vo.Either
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSourcesByCategoryUseCase @Inject constructor(private val repo: NewsRepository) : UseCase<List<Source>, GetSourcesByCategoryUseCase.Params>() {

    data class Params(
        val category: String,
        val page: Int
    )

    override suspend fun run(params: Params): Either<Failure, List<Source>> {
        return repo.getSourcesByCategory(params.category, params.page)
    }
}