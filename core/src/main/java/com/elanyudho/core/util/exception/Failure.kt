package com.elanyudho.core.util.exception

import com.elanyudho.core.util.vo.RequestResults

data class  Failure(val requestResults: RequestResults, val throwable: Throwable, val code:String="")