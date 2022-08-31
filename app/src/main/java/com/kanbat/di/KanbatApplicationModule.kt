/*
 * Copyright 2022 Yulia Batova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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