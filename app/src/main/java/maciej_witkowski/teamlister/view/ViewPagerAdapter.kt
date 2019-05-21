package maciej_witkowski.teamlister.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PickResultFragment()
            }
            else -> {
                return RawTeamFragment()
            }
        }
    }
    override fun getCount(): Int {
        return 2

    }
}