package com.notilocations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Handles when a NotiLocationTask is swiped or is being swiped.
 *
 * @param context The context from which the class is created.
 * @param adapter The adapter holding the location task that was swiped.
 *
 * @property deleteIcon The icon to draw when swiping to delete.
 * @property deleteIntrinsicWidth The intrinsic width of the delete icon.
 * @property deleteIntrinsicHeight The intrinsic height of the delete icon.
 * @property deleteBackground Color drawable holding the color to draw when swiping to delete.
 * @property completeIcon The icon to draw when swiping to complete.
 * @property completeIntrinsicWidth The intrinsic width of the complete icon.
 * @property completeIntrinsicHeight The intrinsic height of the complete icon.
 * @property completeBackground Color drawable holding the color to draw when swiping to complete.
 */
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

    /**
     * Must be over-ridden, but don't need to do anything but return false.
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    /**
     * Called when a location task is swiped. Either deletes or completes it depending on the swipe direction.
     * @param viewHolder The view holder that was swiped.
     * @param direction The direction the location task was swiped.
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.LEFT) {
            adapter.removeLocationTask(viewHolder.bindingAdapterPosition)
        } else {
            adapter.completeLocationTask(viewHolder.bindingAdapterPosition)
        }
    }

    /**
     * Draws the delete background and icon.
     * @param c The canvas to draw into.
     * @param dX The amount the holder has been swiped.
     * @param viewHolder The view holder that is being swiped.
     */
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

    /**
     * Draws the delete complete and icon.
     * @param c The canvas to draw into.
     * @param dX The amount the holder has been swiped.
     * @param viewHolder The view holder that is being swiped.
     */
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

    /**
     * Called when a view holder is being swiped. Draws the complete or delete background and icon depending on the direction of the swipe.
     *
     * @param c The canvas to draw into.
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position.
     * @param dX The amount of horizontal displacement caused by user's action.
     * @param dY The amount of vertical displacement caused by user's action.
     * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state.
     */
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




















