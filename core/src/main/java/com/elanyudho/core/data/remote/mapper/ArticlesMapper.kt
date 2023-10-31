package com.elanyudho.core.data.remote.mapper

import com.elanyudho.core.abstraction.BaseMapper
import com.elanyudho.core.data.remote.response.ArticlesResponse
import com.elanyudho.core.model.model.Article
import com.elanyudho.core.util.extension.NOT_AVAILABLE


class ArticlesMapper : BaseMapper<ArticlesResponse, List<Article>> {

    override fun mapToDomain(raw: ArticlesResponse): List<Article> {
        return raw.articles?.map {
            Article(it?.source?.name ?: NOT_AVAILABLE, it?.title ?: NOT_AVAILABLE, it?.description ?: NOT_AVAILABLE, it?.urlToImage ?: NOT_AVAILABLE, it?.url ?: NOT_AVAILABLE, it?.publishedAt ?: NOT_AVAILABLE)
        } ?: emptyList()
    }

    override fun mapToRaw(domain: List<Article>): ArticlesResponse {
        return ArticlesResponse()
    }


}