package com.example.myshop.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MSPButton(context: Context, attrs: AttributeSet): AppCompatButton(context, attrs) {

    init {
        applyFont()
    }

    private fun applyFont(){
        //Applying font
        val typeface: Typeface = Typeface
            .createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}