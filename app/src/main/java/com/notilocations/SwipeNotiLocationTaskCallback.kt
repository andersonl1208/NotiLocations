package com.notilocations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SwipeNotiLocationTaskCallback(context: Context, private val adapter: TaskListAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)
    private val deleteIntrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
    private val deleteIntrinsicHeight = deleteIcon?.intrinsicHeight ?: 0
    private val deleteBackground: ColorDrawable = ColorDrawable(Color.RED)

    private val completeIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_circle_48)
    private val completeIntrinsicWidth = completeIcon?.intrinsicWidth ?: 0
    private val completeIntrinsicHeight = completeIcon?.intrinsicHeight ?: 0
    private val completeBackground: ColorDrawable = ColorDrawable(Color.rgb(52, 152, 219))

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.LEFT) {
            adapter.removeLocationTask(viewHolder.bindingAdapterPosition)
        } else {
            adapter.completeLocationTask(viewHolder.bindingAdapterPosition)
        }
    }

    private fun drawDelete(c: Canvas, dX: Float, viewHolder: RecyclerView.ViewHolder) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        deleteBackground.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )

        deleteBackground.draw(c)

        // Calculate position of delete icon
        val iconTop = itemView.top + (itemHeight - deleteIntrinsicHeight) / 2
        val iconMargin = (itemHeight - deleteIntrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - deleteIntrinsicWidth
        val iconRight = itemView.right - iconMargin
        val iconBottom = iconTop + deleteIntrinsicHeight

        // Draw the delete icon
        deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        deleteIcon?.draw(c)

    }

    private fun drawComplete(c: Canvas, dX: Float, viewHolder: RecyclerView.ViewHolder) {
        val itemView = viewHolder.itemView

        completeBackground.setBounds(
            itemView.left,
            itemView.top,
            itemView.left + dX.toInt(),
            itemView.bottom
        )

        completeBackground.draw(c)

        val iconTop = itemView.top + (itemView.height - completeIntrinsicHeight) / 2
        val iconBottom = iconTop + completeIntrinsicHeight
        val iconMargin = (itemView.height - completeIntrinsicHeight) / 2
        val iconLeft = itemView.left + iconMargin
        val iconRight = itemView.left + iconMargin + completeIntrinsicWidth


        // Draw the delete icon
        completeIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        completeIcon?.draw(c)

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
        if (dX >= 0) {
            drawComplete(c, dX, viewHolder)
        } else {
            drawDelete(c, dX, viewHolder)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}




















