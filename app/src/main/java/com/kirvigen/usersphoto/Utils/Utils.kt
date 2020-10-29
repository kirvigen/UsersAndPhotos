package com.kirvigen.usersphoto.Utils

import android.content.Context

class Utils {
    companion object{
        fun DpToPx(dp:Int,context: Context):Float{
            return dp * (context.resources.displayMetrics.density)
        }
    }
}