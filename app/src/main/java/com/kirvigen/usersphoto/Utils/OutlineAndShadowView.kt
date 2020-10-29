package com.kirvigen.usersphoto.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.LinearLayout
import com.kirvigen.usersphoto.R


class OutlineAndShadowView(context: Context, attributeSet: AttributeSet):LinearLayout(context, attributeSet) {

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val rounded = context.resources.getDimension(R.dimen.cornerRadius)
        val clipPath = Path()
        clipPath.addRoundRect(
            RectF(canvas.clipBounds),
            rounded,
            rounded,
            Path.Direction.CW
        )
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }
}