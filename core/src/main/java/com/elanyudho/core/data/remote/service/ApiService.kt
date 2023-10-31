package com.elanyudho.core.data.remote.service

import com.elanyudho.core.data.remote.response.ArticlesResponse
import com.elanyudho.core.data.remote.response.SourcesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines/sources")
    suspend fun getSourcesByCategory(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
    ): Response<SourcesResponse>

    @GET("everything")
    suspend fun getArticles(
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: String,
        @Query("pageSize") pageSize: String
    ): Response<ArticlesResponse>

    @GET("everything")
    suspend fun getArticlesByQuery(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: String,
        @Query("pageSize") pageSize: String
    ): Response<ArticlesResponse>

}