package com.elanyudho.core.util.extension

import android.R
import android.content.res.Resources
import android.graphics.Color
import java.text.DecimalFormat

val Int.dp : Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
