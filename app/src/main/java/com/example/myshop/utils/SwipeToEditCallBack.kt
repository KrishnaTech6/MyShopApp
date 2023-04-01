package com.example.myshop.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R


 abstract class SwipeToEditCallBack(context: Context)
    : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val editIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_edit_24)
    private val intrinsicWidth = editIcon!!.intrinsicWidth
    private val intrinsicHeight = editIcon!!.intrinsicHeight

    val background = ColorDrawable()
    val backgroundColor = Color.parseColor("#24AE05")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

     override fun getMovementFlags(
         recyclerView: RecyclerView,
         viewHolder: RecyclerView.ViewHolder
     ): Int {
        /* To disable swipe for any item , code here */

         if (viewHolder.adapterPosition==0) return 0
         return super.getMovementFlags(recyclerView, viewHolder)
     }

     override fun onMove(
         recyclerView: RecyclerView,
         viewHolder: RecyclerView.ViewHolder,
         target: RecyclerView.ViewHolder
     ): Boolean {
         return false
         //won't allow the items to move
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
             clearCanvas(c, itemView.left + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
             super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
             return
         }

         //draw the green edit background
         background.color = backgroundColor
         background.setBounds(itemView.left + dX.toInt(),itemView.top, itemView.left, itemView.bottom )
         background.draw(c)

         //calculate position of edit icon
         val editIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
         val editIconMargin = (itemHeight - intrinsicHeight)
         val editIconLeft = itemView.left + editIconMargin - intrinsicWidth
         val editIconRight = itemView.left + editIconMargin
         val editIconBottom = editIconTop + intrinsicHeight

         // Draw the delete icon
         editIcon!!.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
         editIcon.draw(c)

         super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

     }

     private fun clearCanvas(c:Canvas?, left: Float, top: Float, right:Float, bottom: Float){
         c?.drawRect(left, top, right, bottom, clearPaint)
     }

}