package com.elanyudho.core.abstraction

import com.elanyudho.core.util.exception.Failure
import com.elanyudho.core.util.vo.Either

abstract class UseCase<out Type, in Params> {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    object None

}