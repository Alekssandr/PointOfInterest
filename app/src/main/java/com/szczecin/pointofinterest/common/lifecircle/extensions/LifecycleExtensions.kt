package com.szczecin.pointofinterest.common.lifecircle.extensions

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.observeLifecycleIn(observer: LifecycleObserver) =
    lifecycle.addObserver(observer)
