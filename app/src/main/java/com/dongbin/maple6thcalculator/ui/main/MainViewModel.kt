package com.dongbin.maple6thcalculator.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dongbin.maple6thcalculator.data.UserInfo
import com.dongbin.maple6thcalculator.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MainViewModel(private val repository: UserRepository): ViewModel() {

    private val _loadUser = MutableLiveData<UserInfo?>()
    var loadUser:  LiveData<UserInfo?> = _loadUser

    private val _insert = MutableLiveData<Boolean>()
    var insert:  LiveData<Boolean> = _insert

    private val _loadable = MutableLiveData<String?>()
    var loadable:  LiveData<String?> = _loadable

    class MainViewModelFactory(private val repository: UserRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    suspend fun parse (nickName: String) {
        val url = "https://maplestory.nexon.com/N23Ranking/World/Total?c=$nickName&w=0"

        Log.d("url", url)
        try {
            // HTTP GET 요청을 보내서 웹 페이지의 HTML을 가져옵니다.
            val doc = withContext(Dispatchers.IO) {
                Jsoup.connect(url).get()
            }

            // 추출
            val elements = doc.select("table.rank_table").select("tbody").select("tr")

            var photoUrl: String? = null
            var level: String? = null
            var job: String? = null

            for (element in elements) {
                // Log.d("html", element.html())
                val searchNickName = element.select("td.left").select("dl").select("dt a").first()?.text()
                Log.d("searchNickName", searchNickName.toString())
                if (element.select("td.left").select("dl").select("dt a").first()?.text()?.lowercase() == nickName.lowercase()) {
                    photoUrl = element?.select("span.char_img")?.select("img[src]")?.first()?.attr("src")?.replace("/180", "")
                    level = element?.select("td:eq(2)")?.text()
                    job = element?.select("dd")?.text()
                    break
                }
            }

            Log.d("photoUrl", photoUrl.toString())
            Log.d("level", level.toString())
            Log.d("job", job.toString())

            if ((photoUrl == null) or (level == null) or (job == null)) {
                _insert.postValue(false)
                return
            } else {
                // Url로 byteArray 생성
                val image = withContext(Dispatchers.IO) {
                    convertImageToByteArray(photoUrl!!)
                }

                // 디비 저장
                val user = UserInfo(
                    name = nickName,
                    level = Integer.parseInt(level!!.replace("Lv.", "")),
                    job = job!!,
                    image = image
                )
                withContext(Dispatchers.IO) {
                    repository.insert(user)
                }
                _insert.postValue(true)
            }

        } catch (e: Exception) {
            // 예외 처리
            Log.e("Exception", e.message.toString())
            _insert.postValue(false)
        }
    }

    suspend fun check() {
        try {
            withContext(Dispatchers.IO) {
                _loadable.postValue(repository.selectAll())
            }
        } catch (e: Exception) {
            // 예외 처리
            Log.e("Exception", e.message.toString())
            _loadable.postValue(null)
        }
    }

    suspend fun load(nickName: String) {
        try {
            withContext(Dispatchers.IO) {
                repository.select(nickName)
                _loadUser.postValue(repository.user)
            }
        } catch (e: Exception) {
            Log.e("Exception", e.message.toString())
            _loadUser.postValue(null)
        }
    }

    // 이미지를 바이트 배열로 변환하는 함수
    private fun convertImageToByteArray(imageUrl: String): ByteArray {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val inputStream = connection.inputStream
        return inputStream.readBytes()
    }
}