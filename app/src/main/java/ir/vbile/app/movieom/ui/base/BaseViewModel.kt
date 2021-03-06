package ir.vbile.app.movieom.ui.base

import androidx.lifecycle.*
import ir.vbile.app.movieom.other.*
import retrofit2.*

abstract class BaseViewModel : ViewModel() {


    fun <T> handleResponse(response: Response<T>, data: MutableLiveData<Resource<T>>) {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                data.postValue(Resource.Success(resultResponse))
            }
        } else {
            data.postValue(Resource.Error(response.message(), null, response.code()))
        }
    }


}