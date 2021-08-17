package com.readbeard.openbrewery.app.base.utils

sealed class CustomResult<out T : Any> {
    object Loading : CustomResult<Nothing>()
    class Success<out T : Any>(@JvmField val value: T) : CustomResult<T>()
    class Error(@JvmField val error: Throwable) : CustomResult<Nothing>()
}
