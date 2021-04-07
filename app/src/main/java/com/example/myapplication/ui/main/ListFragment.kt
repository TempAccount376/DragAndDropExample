package com.example.myapplication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.ItemMoveCallback.Companion.LIST_MOVEMENTS
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment(), StartDragListener {

    private lateinit var touchHelper: ItemTouchHelper

    companion object {
        @JvmStatic
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateRecyclerView()
    }

    private fun populateRecyclerView() {
        val mAdapter = RecyclerViewAdapter(this)
        val callback: ItemTouchHelper.Callback = ItemMoveCallback(mAdapter, LIST_MOVEMENTS)
        touchHelper = ItemTouchHelper(callback).also { it.attachToRecyclerView(recyclerView) }
        recyclerView.adapter = mAdapter

        mAdapter.setData(getItems())
    }

    private fun getItems() : List<Info> {
        val list = mutableListOf<Info>()
        list.add(Info("Item 1", true))
        list.add(Info("Item 2", true))
        list.add(Info("Item 3", true))
        list.add(Info("Item 4", false))
        list.add(Info("Item 5", false))
        list.add(Info("Item 6", false))
        return list
    }

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }
}