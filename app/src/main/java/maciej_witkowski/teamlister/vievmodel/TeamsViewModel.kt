package maciej_witkowski.teamlister.vievmodel

import android.graphics.Bitmap
import androidx.lifecycle.*
import maciej_witkowski.teamlister.model.RawData


class TeamsViewModel(handle: SavedStateHandle) : ViewModel(), LifecycleObserver {

    private val imageHandle: MutableLiveData<Bitmap> = handle.getLiveData<Bitmap>("Image")
    val image: LiveData<Bitmap> = imageHandle

    private val rawTeam1Handle: MutableLiveData<MutableList<RawData>> = handle.getLiveData<MutableList<RawData>>("RawTeam1")
    val rawTeam1: LiveData<MutableList<RawData>> = rawTeam1Handle

    private val team1Handle: MutableLiveData<String> = handle.getLiveData<String>("Team1")
    val team1: LiveData<String> = team1Handle

    private val rawTeam2Handle: MutableLiveData<MutableList<RawData>> = handle.getLiveData<MutableList<RawData>>("RawTeam2")
    val rawTeam2: LiveData<MutableList<RawData>> = rawTeam1Handle

    private val team2Handle: MutableLiveData<String> = handle.getLiveData<String>("Team2")
    val team2: LiveData<String> = team2Handle
}