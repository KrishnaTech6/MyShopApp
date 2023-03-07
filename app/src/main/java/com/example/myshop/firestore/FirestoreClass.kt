package com.example.myshop.firestore

import android.util.Log
import com.example.myshop.activities.RegisterActivity
import com.example.myshop.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FirestoreClass {

    private val mFirestore= FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        mFirestore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())   //set is to upload data
            //setoptions is set to merge so that if later
            //on we want other entries, it merge the data rather than replacing
            .addOnSuccessListener {
                activity.userRegisterSuccess()

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while Registering the user. ",
                    e
                )
            }
    }
}