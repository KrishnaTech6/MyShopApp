package com.example.myshop.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshop.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loadUserPicture(imageURI: Uri, imageView: ImageView ){
        try {
            //load the user image in imageView
            Glide
                .with(context)
                .load(Uri.parse(imageURI.toString()))//uri of image
                .centerCrop()//scale type of image
                .placeholder(R.drawable.ic_user_placeholder)//a default placeholder if image failed to load
                .into(imageView) //the view in  which image will be loaded

        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}