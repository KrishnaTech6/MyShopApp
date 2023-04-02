package com.example.myshop.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R


abstract class SwipeToDeleteCallBack(context: Context)
    : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_vector_delete_white)
    private val intrinsicWidth = deleteIcon!!.intrinsicWidth
    private val intrinsicHeight = deleteIcon!!.intrinsicHeight

    val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#F4442D")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

     override fun getMovementFlags(
         recyclerView: RecyclerView,
         viewHolder: RecyclerView.ViewHolder
     ): Int {
        /* To disable swipe for any item , code here */

//         - Here we set the direction of swipe.
//         We return the direction flag in a static method makeMovementFlags.
//         onMove - This is used for drag and drop. If not needed, return false here.

         if (viewHolder.adapterPosition==10) return 0
         return super.getMovementFlags(recyclerView, viewHolder)
     }

     override fun onMove(
         recyclerView: RecyclerView,
         viewHolder: RecyclerView.ViewHolder,
         target: RecyclerView.ViewHolder
     ): Boolean {
         return false
         //onMove - This is used for drag and drop. If not needed, return false here.
     }

     override fun onChildDraw(
         c: Canvas,
         recyclerView: RecyclerView,
         viewHolder: RecyclerView.ViewHolder,
         dX: Float,
         dY: Float,
         actionState: Int,
         isCurrentlyActive: Boolean
     ) {

         val itemView = viewHolder.itemView
         val itemHeight = itemView.bottom - itemView.top
         val isCanceled= dX ==0f && !isCurrentlyActive
         /*
          If both of these conditions are true, then the isCanceled variable is assigned the value true,
          indicating that the swipe gesture has been canceled.
          This might be used to handle cases where the user has started a swipe gesture
          but then canceled it before completing it.
          */
         if (isCanceled){
             clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
             super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
             return
         }

         //draw the green edit background
         background.color = backgroundColor
         background.setBounds(itemView.right + dX.toInt(),itemView.top, itemView.right, itemView.bottom )
         background.draw(c)

         //calculate position of edit icon
         val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
         val deleteIconMargin = (itemHeight - intrinsicHeight)/2
         val deleteIconLeft = itemView.right - intrinsicWidth-deleteIconMargin
         val deleteIconRight = itemView.right - deleteIconMargin
         val deleteIconBottom = deleteIconTop + intrinsicHeight

         // Draw the delete icon
         deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
         deleteIcon.draw(c)

         super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

     }

     private fun clearCanvas(c:Canvas?, left: Float, top: Float, right:Float, bottom: Float){
         c?.drawRect(left, top, right, bottom, clearPaint)
     }

}