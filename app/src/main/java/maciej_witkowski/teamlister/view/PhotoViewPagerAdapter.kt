package maciej_witkowski.teamlister.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PhotoViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CameraPhotoFragment()
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