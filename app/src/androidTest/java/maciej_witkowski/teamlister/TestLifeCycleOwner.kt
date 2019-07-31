package maciej_witkowski.teamlister

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class TestLifecycleOwner : LifecycleOwner {
    private val mLifecycle: LifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return mLifecycle
    }

    fun handleEvent(event: Lifecycle.Event) {
        mLifecycle.handleLifecycleEvent(event)
    }
}