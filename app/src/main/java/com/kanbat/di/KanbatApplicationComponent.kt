package com.kanbat.di

import com.kanbat.KanbatApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        KanbatApplicationModule::class
    ]
)

@PerApp
interface KanbatApplicationComponent : AndroidInjector<KanbatApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<KanbatApplication>()
}
