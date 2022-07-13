package com.kanbat.di

import android.content.Context
import androidx.room.Room
import com.kanbat.ui.KanbatActivity
import com.kanbat.KanbatApplication
import com.kanbat.dao.KanbatDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class KanbatApplicationModule {

    @Binds
    abstract fun context(app: KanbatApplication): Context

    @PerActivity
    @ContributesAndroidInjector(modules = [KanbatActivity.Module::class])
    abstract fun kanbatActivity(): KanbatActivity

    @Module
    companion object {

        @Provides
        @PerApp
        @JvmStatic
        fun getKanbatDatabase(app: KanbatApplication) =
            Room.databaseBuilder(app, KanbatDatabase::class.java, KanbatDatabase.DB_NAME)
                // .addMigrations(Migrations.VERSION_1_2)
                .build()
    }
}