package com.kanbat.ui.createdesk

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.FragmentCreateDeskBinding
import com.kanbat.model.repository.DeskRepository
import com.kanbat.ui.base.BaseDialogFragment
import com.kanbat.utils.toast
import com.kanbat.viewmodel.CreateDeskViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class CreateDeskFragment : BaseDialogFragment<FragmentCreateDeskBinding>() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            CreateDeskViewModel.Factory(deskRepository)
        )[CreateDeskViewModel::class.java]
    }

    private val addDeskNameWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.deskTitle = s.toString().trim()
        }
    }

    override fun getViewBinding() = FragmentCreateDeskBinding.inflate(layoutInflater)

    @Inject
    lateinit var deskRepository: DeskRepository

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.KanbatTheme_SlideAnimation
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            launch({
                viewModel.isDeskValidUiState.collectLatest { isEnabled ->
                    createDeskButton.isEnabled = isEnabled
                }
            })

            launch({
                viewModel.isDeskCreatedUiState.collectLatest { isDeskCreated ->
                    if (isDeskCreated) {
                        requireActivity().toast(R.string.str_desk_is_created)
                        onBackPressed()
                    }
                }
            })

            deskNameInputEditText.addTextChangedListener(addDeskNameWatcher)
            createDeskButton.setOnClickListener { viewModel.onCreateDeskClicked() }
            backButton.setOnClickListener { onBackPressed() }
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