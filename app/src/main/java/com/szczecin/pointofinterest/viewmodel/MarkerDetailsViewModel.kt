package com.szczecin.pointofinterest.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.szczecin.pointofinterest.BuildConfig
import com.szczecin.pointofinterest.articles.usecase.GetImageUseCase
import com.szczecin.pointofinterest.articles.usecase.GetMarkerDetailsUseCase
import com.szczecin.pointofinterest.common.rx.RxSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MarkerDetailsViewModel @Inject constructor(
    private val markerDetailsUseCase: GetMarkerDetailsUseCase,
    private val imageUseCase: GetImageUseCase,
    private val schedulers: RxSchedulers
) : ViewModel(), LifecycleObserver {

    val pageId = MutableLiveData<String>()
    val markerLocation = MutableLiveData<String>()
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val imageList = MutableLiveData<List<String>>()
    val link = MutableLiveData<String>()

    private val disposables = CompositeDisposable()

    private var imagesTitles: String = ""

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        pageId.observeForever {
            it?.let { pageId ->
                loadMarkerDescription(pageId)
            }
        }
    }

    private fun loadMarkerDescription(id: String) {
        disposables += markerDetailsUseCase
            .execute(id)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {

                title.value = it.title
                description.value = it.description

                link.value = BuildConfig.API_ARTICLE_URL + it.title.replace(" ", "_")
                if (!it.imageUrl.isNullOrEmpty()) {
                    loadImages(it.imageUrl.getImageTitle())
                }
                imagesTitles = ""

            }, onError = {
                Log.e("Error", it.message ?: "")
            })
    }

    private fun loadImages(id: String) {
        disposables += imageUseCase
            .execute(id)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {
                imageList.value = it.thumbUrlList
            }, onError = {
                Log.e("Error", it.message ?: "")
            })
    }

    private fun List<String>.getImageTitle(): String {
        forEach { title ->
            imagesTitles = imagesTitles.plus("$title|")
        }
        return imagesTitles.substring(0, imagesTitles.length - 1)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}