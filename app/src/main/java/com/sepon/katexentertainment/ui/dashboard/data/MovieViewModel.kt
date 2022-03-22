package com.sepon.katexentertainment.ui.dashboard.data

import androidx.annotation.LayoutRes

interface MovieViewModel{
@get:LayoutRes
val layoutId: Int
val viewType: Int
    get() = 0
}