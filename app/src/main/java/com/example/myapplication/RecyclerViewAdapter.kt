package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.ItemMoveCallback.ItemTouchHelperContract
import com.example.myapplication.RecyclerViewAdapter.MyViewHolder
import java.util.*


class RecyclerViewAdapter(
    private val mStartDragListener: StartDragListener
) : ListAdapter<Info, MyViewHolder>(DiffCallback()), ItemTouchHelperContract {

    private var selectedItems = mutableListOf<Info>()
    private var unselectedItems = mutableListOf<Info>()

    inner class MyViewHolder(var rowView: View) : ViewHolder(rowView) {
        val mTitle: TextView = rowView.findViewById(R.id.txtTitle)
        val checkBox: CheckBox = rowView.findViewById(R.id.checkbox)

        val listener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            val info  = getItem(adapterPosition)
            info.isSelected = isChecked
            if (isChecked) {
                unselectedItems.remove(info)
                selectedItems.add(info)
            } else {
                selectedItems.remove(info)
                unselectedItems.add(0, info)
            }

            val newList = selectedItems + unselectedItems

            submitList(newList)
        }
    }

    fun setData(data: List<Info>) {
        selectedItems = data.filter { it.isSelected }.toMutableList()
        unselectedItems = data.filter { !it.isSelected }.toMutableList()

        val newList = selectedItems + unselectedItems

        submitList(newList)
    }

    private class DiffCallback: DiffUtil.ItemCallback<Info>() {
        override fun areItemsTheSame(oldItem: Info, newItem: Info): Boolean {
            if (oldItem.text != newItem.text) return false
            return oldItem.isSelected == newItem.isSelected
        }

        override fun areContentsTheSame(oldItem: Info, newItem: Info): Boolean {

            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitle.text = getItem(position).text
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = getItem(position).isSelected
        holder.checkBox.setOnCheckedChangeListener(holder.listener)


        holder.itemView.setOnLongClickListener {
            mStartDragListener.requestDrag(holder)
            true
        }

        ViewCompat.setAccessibilityDelegate(holder.itemView, object : AccessibilityDelegateCompat() {
            override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                val description = "drag and drop"
                val customClick = AccessibilityActionCompat(AccessibilityNodeInfoCompat.ACTION_LONG_CLICK, description)
                info.addAction(customClick)
            }
        })
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        //if (!isDropAllowed(fromPosition, toPosition)) return
        val list = currentList.toMutableList()

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }

        submitList(list)
    }

    override fun isDropAllowed(fromPosition: Int, toPosition: Int) = getItem(toPosition).isSelected == getItem(fromPosition).isSelected

    override fun onRowSelected(myViewHolder: ViewHolder) {
        (myViewHolder as MyViewHolder).rowView.isSelected = true
    }

    override fun onRowClear(myViewHolder: ViewHolder) {
        (myViewHolder as MyViewHolder).rowView.isSelected = false
    }

}