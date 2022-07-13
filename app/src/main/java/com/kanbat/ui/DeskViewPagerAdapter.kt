package com.kanbat.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kanbat.model.data.Desk
import com.kanbat.ui.desk.DeskFragment

class DeskViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var items = emptyList<Desk>()
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        val deskId = items[position].id
        return DeskFragment.newInstance(deskId)
    }
}