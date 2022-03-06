package com.kts6056.viewstate.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

val Fragment.viewLifecycleScope
    get() = viewLifecycleOwner.lifecycleScope
