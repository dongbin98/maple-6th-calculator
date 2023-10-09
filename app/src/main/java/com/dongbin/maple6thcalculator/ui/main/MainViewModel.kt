package com.dongbin.maple6thcalculator.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dongbin.maple6thcalculator.data.AppDatabase
import com.dongbin.maple6thcalculator.data.UserInfo
import com.dongbin.maple6thcalculator.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.lang.IllegalArgumentException
import java.net.HttpURLConnection
import java.net.URL

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

        try {
            // HTTP GET 요청을 보내서 웹 페이지의 HTML을 가져옵니다.
            val html = withContext(Dispatchers.IO) {
                Jsoup.connect(url).get()
            }

            // HTML에서 레벨, 경험치, 랭킹, 사진을 추출합니다.
            val level = extractLevel(html)
            val job = extractJob(html)
            val photoUrl = extractImageUrl(html).replace("/180", "")

            // 결과 출력
            Log.d("Level", level)
            Log.d("Job", job)
            Log.d("Photo URL", photoUrl)

            // Url로 byteArray 생성
            val image = withContext(Dispatchers.IO) {
                convertImageToByteArray(photoUrl)
            }

            Log.d("check", "check1")

            // 디비 저장
            val user = UserInfo(name = nickName, level = Integer.parseInt(level.replace("Lv.", "")), job = job, image =  image)
            Log.d("check", "check2")
            withContext(Dispatchers.IO) {
                repository.insert(user)
            }
            Log.d("check", "check3")
            _insert.postValue(true)

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

    // HTML에서 레벨을 추출하는 함수
    private fun extractLevel(html: Document): String {
        val levelElement: Element? = html.select("#container > div > div > div:nth-child(4) > div.rank_table_wrap > table > tbody > tr.search_com_chk > td:nth-child(3)").first()
        return levelElement?.text() ?: ""
    }

    // HTML에서 직업을 추출하는 함수
    private fun extractJob(html: Document): String {
        val jobElement: Element? = html.select("#container > div > div > div:nth-child(4) > div.rank_table_wrap > table > tbody > tr.search_com_chk > td.left > dl > dd").first()
        return jobElement?.text()?.split("/")?.get(1)?.replace(" ", "") ?: ""
    }

    // HTML에서 사진 URL을 추출하는 함수
    private fun extractImageUrl(html: Document): String {
        val imgElement: Element? = html.select("#container > div > div > div:nth-child(4) > div.rank_table_wrap > table > tbody > tr.search_com_chk > td.left > span > img:nth-child(1)").first()
        return imgElement?.attr("src") ?: ""
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