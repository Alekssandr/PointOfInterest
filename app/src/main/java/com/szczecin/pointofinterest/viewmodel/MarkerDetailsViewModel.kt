package com.szczecin.pointofinterest.viewmodel

import android.util.Log
import androidx.lifecycle.*
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

    private val disposables = CompositeDisposable()
    val pageId = MutableLiveData<String>()
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val image = MutableLiveData<String>()
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
                for ((key, value) in it.query.pages) {
                    title.value = value.title
                    description.value = value.description
                    value.images.forEach { images ->
                        imagesTitles = imagesTitles.plus(images.title + "|")
                    }
                    imagesTitles = imagesTitles.substring(0, imagesTitles.length - 1)
//                    value.images.forEach(
////                        imagesTitles.append(imageTitle.)
//                    )
                    loadImages(imagesTitles)
                    imagesTitles = ""
//                    image.value = value.images[0].title
                }
                Log.d("test", it.query.toString())
            }, onError = {
                Log.d("test", it.message ?: "")
            })
    }

    private fun loadImages(id: String) {
        disposables += imageUseCase
            .execute(id)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {
                for ((_, value) in it.query.pages) {
                    image.value = value.imageInfo[0].thumburl
                    break
                }
//                Log.d("test", image.value.toString())
            }, onError = {
                Log.d("test", it.message ?: "")
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}