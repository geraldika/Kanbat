package com.kanbat.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.samples.gridtopager.R
import com.kanbat.di.PerFragment
import dagger.android.ContributesAndroidInjector
import androidx.navigation.findNavController
import com.kanbat.ui.addtask.AddTaskFragment
import com.kanbat.ui.base.BaseKanbatActivity
import com.kanbat.ui.desk.DeskFragment
import com.kanbat.ui.home.HomeFragment
import com.kanbat.ui.createdesk.CreateDeskFragment
import com.kanbat.ui.edit.EditTaskFragment
import com.kanbat.viewmodel.KanbatViewModel

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