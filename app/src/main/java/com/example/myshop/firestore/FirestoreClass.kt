package com.example.myshop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.myshop.models.User
import com.example.myshop.ui.activities.LoginActivity
import com.example.myshop.ui.activities.RegisterActivity
import com.example.myshop.ui.activities.SettingsActivity
import com.example.myshop.ui.activities.UserProfileActivity
import com.example.myshop.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FirestoreClass{

    private val mFirestore= FirebaseFirestore.getInstance()


    fun registerUser(activity: RegisterActivity, userInfo: User){

        mFirestore.collection(Constants.USERS)
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

    private fun getCurrentUserID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""

        currentUserID= currentUser!!.uid

        return currentUserID
    }

    fun getUserDetails(activity: Activity){

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val user= document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYSHOP_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //key:value       logged_in_username : Krishna Rana
                // in the form of key value pairs
                editor.putString(
                    Constants.LOOGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()
                //todo: pass the result to login screen

                when(activity){
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName,
                "Error while getting user details.",
                e)
            }

    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {

                when(activity){
                    is UserProfileActivity ->{
                        activity.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when(activity){
                    is UserProfileActivity ->{
                        activity.hideProgressDialog()

                        Log.e(activity.javaClass.simpleName,
                            "error while updating the user Details",
                            e)
                    }
                }
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
                    + Constants.getFileExtension( activity, imageFileURI)
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->

            Log.e("Firebase Image Url",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )
            //get the downloadable url from task snapShot
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.e("Downloadable Image URL", uri.toString())

                when(activity){
                    is UserProfileActivity ->{
                        activity.imageUploadSuccess(uri.toString())
                    }
                }
            }

        }
            .addOnFailureListener{ exception ->

                when(activity){
                    is UserProfileActivity ->{
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }


    }

}