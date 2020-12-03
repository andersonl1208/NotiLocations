package com.notilocations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SwipeToDeleteCallback(context: Context, private val adapter: TaskListAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)
    private val intrinsicWidth = icon?.intrinsicWidth ?: 0
    private val intrinsicHeight = icon?.intrinsicHeight ?: 0
    private val background: ColorDrawable = ColorDrawable(Color.RED)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.removeLocationTask(viewHolder.bindingAdapterPosition)
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

        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )

        background.draw(c)

        // Calculate position of delete icon
        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - intrinsicWidth
        val iconRight = itemView.right - iconMargin
        val iconBottom = iconTop + intrinsicHeight

        // Draw the delete icon
        icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon?.draw(c)


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}