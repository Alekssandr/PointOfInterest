package com.szczecin.pointofinterest.common.lifecircle.extensions

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.observeLifecyclesIn(observers: List<LifecycleObserver>) =
    observers.forEach {
        lifecycle.addObserver(it)
    }