package com.example.myapplication

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

/**
 * This is an item decorator for applying space between items in a grid.
 * It only supports [GridLayoutManager] and applies equal spacing between items except the edge ones.
 * The decorator will NOT add space at the top of the first row items and at the bottom of the last row items.
 * The decorator does not support rows with different span than 1 per item.
 */
class GridSpaceDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = spacing
        outRect.left = spacing
        outRect.right = spacing
        outRect.bottom = spacing
    }
}