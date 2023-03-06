package com.example.myshop.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPTextViewBold(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs){

    init {
        //calls the function to apply fonts to the function
        applyFont()
    }

    private fun applyFont(){
        //Applying font
        val typeface: Typeface = Typeface
            .createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }

}