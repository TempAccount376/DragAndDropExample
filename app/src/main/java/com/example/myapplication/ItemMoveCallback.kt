package com.example.myapplication

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.annotation.NonNull
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.RecyclerViewAdapter.MyViewHolder


class ItemMoveCallback(
    private val mAdapter: ItemTouchHelperContract,
    private val dragFlags: Int
) : ItemTouchHelper.Callback() {

    companion object {
        const val LIST_MOVEMENTS = UP or DOWN
        const val GRID_MOVEMENTS = UP or DOWN or LEFT or RIGHT
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
        // do nothing
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(dragFlags, 0)
    }

    override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return if (mAdapter.isDropAllowed(viewHolder.adapterPosition, target.adapterPosition)) {
            mAdapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
            true
        } else {
            recyclerView.announceForAccessibility("cannot move to position ${target.adapterPosition + 1}")
            false
        }
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        recyclerView.context.interruptAccessibilityAnnouncements()
        recyclerView.announceForAccessibility("moved to position ${toPos + 1}")
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return mAdapter.isDropAllowed(
            recyclerView.getChildAdapterPosition(current.itemView),
            recyclerView.getChildAdapterPosition(target.itemView)
        )
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is MyViewHolder) {
            mAdapter.onRowSelected(viewHolder)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is MyViewHolder) {
            mAdapter.onRowClear(viewHolder)
        }
    }

    private fun Context.interruptAccessibilityAnnouncements() {
        val accessibilityManager = this.getSystemService(Context.ACCESSIBILITY_SERVICE)
            ?.let { it as? AccessibilityManager } ?: return
        if (accessibilityManager.isEnabled && accessibilityManager.isTouchExplorationEnabled) {
            accessibilityManager.interrupt()
        }
    }

    interface ItemTouchHelperContract {
        fun onRowMoved(fromPosition: Int, toPosition: Int)
        fun onRowSelected(myViewHolder: RecyclerView.ViewHolder)
        fun onRowClear(myViewHolder: RecyclerView.ViewHolder)
        fun isDropAllowed(fromPosition: Int, toPosition: Int): Boolean
    }

}