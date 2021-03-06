package com.lumi.submission2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.lumi.submission2.BuildConfig
import com.lumi.submission2.model.UserDetail
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel:ViewModel() {
    private val detailUsers = MutableLiveData<UserDetail>()

    fun setDetail(username: String) {

        val apiKey:String = BuildConfig.API_KEY
        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {

                val result = String(responseBody)
                Log.d("Exception", result)

                try {
                    val responseObject = JSONObject(result)

                    val user = UserDetail()
                    user.username = responseObject.getString("login")
                    user.avatar = responseObject.getString("avatar_url")
                    user.name = responseObject.getString("name")
                    user.company = responseObject.getString("company")
                    user.location = responseObject.getString("location")
                    user.follower = responseObject.getInt("followers")
                    user.following = responseObject.getInt("following")

                    detailUsers.postValue(user)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getDetails(): LiveData<UserDetail> {
        return detailUsers
    }
}