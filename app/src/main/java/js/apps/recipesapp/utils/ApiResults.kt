package js.apps.recipesapp.utils

import java.lang.Exception

sealed class ApiResults<out R>{
    class Success<T> (val data: T): ApiResults<T>()
    class NoSuccess<T> (val message: String, val data: T? = null): ApiResults<T>()
    class Error(val exception: Exception): ApiResults<Nothing>()
    data object Loading: ApiResults<Nothing>()
    data object Finished: ApiResults<Nothing>()

}