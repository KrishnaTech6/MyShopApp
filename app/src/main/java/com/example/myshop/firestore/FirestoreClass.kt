package com.example.myshop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myshop.models.*
import com.example.myshop.ui.activities.*
import com.example.myshop.ui.fragments.DashboardFragment
import com.example.myshop.ui.fragments.ProductsFragment
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

    fun uploadProductData(activity: AddProductActivity, productInfo: Products){

        mFirestore.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())   //set is to upload data
            //setoptions is set to merge so that if later
            //on we want other entries, it merge the data rather than replacing
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product data.",
                    e
                )
            }

    }

    fun getCurrentUserID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID: String= ""
        currentUserID = currentUser!!.uid
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

    fun editProductData(activity: Activity,productId: String ,productHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .update(productHashMap)
            .addOnSuccessListener {

                when(activity){
                    is EditProductDataActivity ->{
                        activity.editProductDataSuccess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when(activity){
                    is EditProductDataActivity ->{
                        activity.hideProgressDialog()

                        Log.e(activity.javaClass.simpleName,
                            "error while updating the product Details",
                            e)
                    }
                }
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
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
                    is AddProductActivity ->{
                        activity.imageUploadSuccess(uri.toString())
                    }
                    is EditProductDataActivity ->{
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
                    is AddProductActivity ->{
                        activity.hideProgressDialog()
                    }
                    is EditProductDataActivity ->{
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

    fun getProductList(fragment: Fragment){
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Products> = ArrayList()

                for (i in document.documents){
                    val product = i.toObject(Products::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }

                when(fragment){
                    is ProductsFragment ->{
                        fragment.successProductsListFromFirestore(productsList)
                    }
                }

            }
            .addOnFailureListener { e->
                Log.e(
                    javaClass.simpleName.toString(),
                    "Error while getting product list",
                    e
                )
            }

    }

    fun getDashboarItemsList(fragment: DashboardFragment){
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Products> = ArrayList()

                for (i in document.documents){
                    val product = i.toObject(Products::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }

                fragment.successItemsListFromFirestore(productsList)

            }
            .addOnFailureListener { e->
                Log.e(
                    javaClass.simpleName.toString(),
                    "Error while getting product list",
                    e
                )
            }

    }

    fun deleteProduct(fragment: ProductsFragment, productId: String){

        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.successProductDeletion()
            }
            .addOnFailureListener { e->
                fragment.hideProgressDialog()

                Log.e(javaClass.simpleName.toString(),
                "Error in deleting producuts ", e)
            }


    }

    fun getProductDetails(activity: Activity, productId: String){
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(javaClass.simpleName.toString(), document.toString())
                val product = document.toObject(Products::class.java)

                when(activity){
                    is ProductDetailsActivity ->{
                        if (product != null) {
                            activity.successProductDetails(product)
                        }
                    }
                    is EditProductDataActivity ->{
                        if (product != null) {
                            activity.successProductDetails(product)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->

                when (activity){
                    is ProductDetailsActivity ->{
                        activity.hideProgressDialog()
                        Log.e(javaClass.simpleName.toString(),
                            "Error in deleting producuts ", e)
                    }
                    is EditProductDataActivity ->{
                        activity.hideProgressDialog()
                        Log.e(javaClass.simpleName.toString(),
                            "Error in deleting producuts ", e)
                    }
                }
            }
        }

    fun getAllProducts(activity: Activity){
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Products> = ArrayList()

                for (i in document.documents){
                    val product = i.toObject(Products::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }

                when (activity){
                    is MyCartActivity ->{
                        activity.successAllItemsListFromFirestore(productsList)
                    }
                    is CheckoutActivity ->{
                        activity.successProductsList(productsList)
                    }


                }




            }
            .addOnFailureListener { e->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting product list",
                    e
                )
            }

    }

    fun updateCartItem(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>){

        mFirestore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .update(itemHashMap)
            .addOnSuccessListener {
                when(context){
                    is MyCartActivity ->{
                        context.itemUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e->

                when(context){
                    is MyCartActivity ->{
                        context.hideProgressDialog()
                    }
                }
                Log.e(context.javaClass.simpleName, "Error while updating cart item detail", e)
            }

    }

    fun addCartItems(activity: ProductDetailsActivity , addToCart: CartItem){
        mFirestore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart , SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while creating the document for cart",
                    e)
            }

    }

    fun checkIfItemExistInCart(activity: ProductDetailsActivity , productId: String){
        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                if (document.documents.size > 0){
                    activity.productExistsInCart()
                }
                else{
                    activity.hideProgressDialog()
                }

            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while loading cart items", e)
            }
    }

    fun getCartItemFromFirestore(activity: Activity){

        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Cart Item List", document.documents.toString())
                val cartItemsList: ArrayList<CartItem> = ArrayList()

                for (i in document.documents){
                    val cartItem = i.toObject(CartItem::class.java)
                    cartItem!!.id = i.id
                    cartItemsList.add(cartItem)
                }

                when (activity){
                    is MyCartActivity ->{
                        activity.successCartItemFromFirestore(cartItemsList)
                    }
                    is CheckoutActivity ->{
                        activity.successCartItemsList(cartItemsList)
                    }
                }



            }
            .addOnFailureListener { e->
                when (activity){
                    is MyCartActivity ->{
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,
                    "Error while creating the document for cart",
                    e)
            }

    }
    fun removeCartItem(context: Context, cart_id: String){
        mFirestore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when (context){
                    is MyCartActivity ->
                        context.successRemoveCartItem()

                }

            }
            .addOnFailureListener {

            }
    }


    fun saveAddressToFirestore(activity: AddEditAddressActivity, addressData: Address){
        mFirestore.collection(Constants.ADDRESS)
            .document()
            .set(addressData, SetOptions.merge())
            .addOnSuccessListener {
                activity.successAddressSaveToFirestore()
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName, "Error while getting address info from the server", e)
                activity.hideProgressDialog()
            }
    }

    fun getAddressList(activity: AddressListActivity){
        mFirestore.collection(Constants.ADDRESS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val addressList: ArrayList<Address> = ArrayList()

                for (i in document.documents){
                    val address = i.toObject(Address::class.java)
                    address!!.id = i.id
                    addressList.add(address)
                }

                activity.successAddressFromFirestore(addressList)
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName, "Error in getting address data", e)
                activity.hideProgressDialog()
            }
    }

    fun editAddressData(activity: AddEditAddressActivity, addressInfo: Address, addressId:String){

        mFirestore.collection(Constants.ADDRESS)
            .document(addressId)
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.successAddressSaveToFirestore()

            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName, "Error in updating address data", e)
                activity.hideProgressDialog()
            }

    }

    fun deleteAddress(activity: AddressListActivity, productId: String){
        mFirestore.collection(Constants.ADDRESS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                activity.successAddressDelete()
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while deleting address", e)

            }
    }


    fun placeOrder(activity: CheckoutActivity, order: Order){
        mFirestore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderPlacedSuccess()
            }
            .addOnFailureListener { e->

                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while uploading order in firestore", e)
            }
    }


    fun updateAllDetails(activity: CheckoutActivity, cartList: ArrayList<CartItem>){

        val writeBatch = mFirestore.batch()

        for (cartItem in cartList){

            val productHashMap = HashMap<String, Any>()
            productHashMap[Constants.STOCK_QUANTITY] =
                (cartItem.stock_quantity.toInt() - cartItem.cart_quantity.toInt()).toString()

            val documentReference =mFirestore.collection(Constants.PRODUCTS)
                .document(cartItem.product_id)

            writeBatch.update(documentReference, productHashMap)

        }
        //to delete cart item after order
        for (cartItem in cartList){
            val documentReference = mFirestore.collection(Constants.CART_ITEMS)
                .document(cartItem.id)
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {
            activity.allUpdateSuccess()
        }
            .addOnFailureListener { e->
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error in writing batch to firestore ", e)
            }

    }


    }
