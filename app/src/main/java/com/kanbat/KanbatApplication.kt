package com.kanbat

import android.annotation.SuppressLint
import com.kanbat.di.DaggerKanbatApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class KanbatApplication : dagger.android.support.DaggerApplication() {

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerKanbatApplicationComponent.builder().create(this)
    }
}