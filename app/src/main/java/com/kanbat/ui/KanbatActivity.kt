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

package com.kanbat.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.samples.gridtopager.R
import com.kanbat.di.PerFragment
import com.kanbat.ui.addtask.AddTaskFragment
import com.kanbat.ui.base.BaseKanbatActivity
import com.kanbat.ui.createdesk.CreateDeskFragment
import com.kanbat.ui.desk.DeskFragment
import com.kanbat.ui.edit.EditTaskFragment
import com.kanbat.ui.home.HomeFragment
import com.kanbat.viewmodel.KanbatViewModel
import dagger.android.ContributesAndroidInjector

class KanbatActivity : BaseKanbatActivity() {

    private val kanbatViewModel: KanbatViewModel by lazy {
        ViewModelProvider(
            this,
            KanbatViewModel.Factory(applicationContext)
        )[KanbatViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanbat)
        val navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @dagger.Module
    abstract class Module {
        @PerFragment
        @ContributesAndroidInjector(modules = [HomeFragment.Module::class])
        abstract fun homeFragment(): HomeFragment

        @PerFragment
        @ContributesAndroidInjector(modules = [DeskFragment.Module::class])
        abstract fun deskFragment(): DeskFragment

        @PerFragment
        @ContributesAndroidInjector(modules = [CreateDeskFragment.Module::class])
        abstract fun createDeskFragment(): CreateDeskFragment

        @PerFragment
        @ContributesAndroidInjector(modules = [AddTaskFragment.Module::class])
        abstract fun addTaskFragment(): AddTaskFragment

        @PerFragment
        @ContributesAndroidInjector(modules = [EditTaskFragment.Module::class])
        abstract fun editTaskFragment(): EditTaskFragment
    }
}