package com.kts6056.viewstate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kts6056.viewstate.base.BaseFragment
import com.kts6056.viewstate.ext.viewLifecycleScope

abstract class MVIFragment<BINDING : ViewBinding, I : ViewIntent, S : ViewState, E : ViewEffect>(
    inflater: (LayoutInflater, ViewGroup?, Boolean) -> BINDING
) : BaseFragment<BINDING>(inflater), MviView<I, S, E> {
    abstract val viewModel: MVIViewModel<I, S, E>

    override fun setIntent(intent: I) {
        viewModel.dispatch(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initCollect()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initCollect() {
        viewLifecycleScope.launchWhenResumed {
            viewModel.state.collect {
                processState(it)
            }
        }
        viewLifecycleScope.launchWhenResumed {
            viewModel.effect.collect {
                processEffect(it)
            }
        }
    }
}
