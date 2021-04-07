package com.example.myapplication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.ItemMoveCallback.Companion.GRID_MOVEMENTS
import kotlinx.android.synthetic.main.fragment_grid.*


class GridFragment : Fragment(), StartDragListener {

    private lateinit var touchHelper: ItemTouchHelper

    companion object {
        @JvmStatic
        fun newInstance(): GridFragment {
            return GridFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateGridView()
    }

    private fun populateGridView() {

        val mAdapter = RecyclerViewAdapter(this)
        val callback: ItemTouchHelper.Callback = ItemMoveCallback(mAdapter, GRID_MOVEMENTS)
        touchHelper = ItemTouchHelper(callback).also { it.attachToRecyclerView(gridview) }

        gridview.layoutManager = GridLayoutManager(requireContext(), 2)
        gridview.addItemDecoration(GridSpaceDecoration(8))
        gridview.adapter = mAdapter

        mAdapter.setData(getItems())
    }

    private fun getItems() : List<Info> {
        val list = mutableListOf<Info>()
        list.add(Info("Item 1", true))
        list.add(Info("Item 2", true))
        list.add(Info("Item 3", true))
        list.add(Info("Item 4", true))
        list.add(Info("Item 5", false))
        list.add(Info("Item 6", false))
        list.add(Info("Item 7", false))
        list.add(Info("Item 8", false))
        return list
    }

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }
}