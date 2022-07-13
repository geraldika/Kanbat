package com.kanbat.ui.home.createdesk

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.FragmentCreateDeskBinding
import com.kanbat.model.data.Desk
import com.kanbat.model.repository.DeskRepository
import com.kanbat.ui.base.BaseDialogFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CreateDeskFragment : BaseDialogFragment<FragmentCreateDeskBinding>() {

    override fun getViewBinding() = FragmentCreateDeskBinding.inflate(layoutInflater)

    @Inject
    lateinit var deskRepository: DeskRepository

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.SlideAnimation
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {

        }
        val createDeskViewModel = ViewModelProvider(
            this,
            CreateDeskViewModel.Factory(deskRepository)
        )[CreateDeskViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            createDeskViewModel.createDesk(Desk(0L, "${UUID.randomUUID().toString()}"))
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.attributes?.let { params ->
            params.width = ActionBar.LayoutParams.MATCH_PARENT
            params.height = ActionBar.LayoutParams.MATCH_PARENT
            dialog?.window?.attributes = params
        }
    }

    @dagger.Module
    class Module
}