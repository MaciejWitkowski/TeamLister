package maciej_witkowski.teamlister.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_processed_team.*
import maciej_witkowski.teamlister.R
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel
import java.util.concurrent.TimeUnit

private const val TAG = "ListFragment"

class ListFragment : Fragment() {
    private lateinit var viewModel: TeamsViewModel
    private var activeTeam = 1
    private var cursor = 0
    private val teamObserver = Observer<String> { value ->
            value?.let {
                tvTeam.setText(value)
                tvTeam.setSelection(cursor)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "on create")
        viewModel = ViewModelProviders.of(requireActivity(), SavedStateVMFactory(requireActivity()))
            .get(TeamsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "on create view")
        return inflater.inflate(R.layout.fragment_processed_team, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "on view created")
        viewModel.team1.observe(this, teamObserver)
        btnTeam1.setOnClickListener {
            viewModel.team2.removeObserver(teamObserver)
            viewModel.team1.observe(this, teamObserver)
            activeTeam = 1
        }
        btnTeam2.setOnClickListener {
            viewModel.team1.removeObserver(teamObserver)
            viewModel.team2.observe(this, teamObserver)
            activeTeam = 2
        }
        btnSave.setOnClickListener {
            viewModel.saveToFiles()
        }
        RxTextView.textChanges(tvTeam)
            .debounce(1500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(textChangeObserver)
    }

    private val textChangeObserver = object : DisposableObserver<CharSequence>() {
        override fun onNext(charSequence: CharSequence) {
            cursor = tvTeam.selectionStart
            viewModel.updateProcessedTeam(charSequence.toString(), activeTeam)
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "onError: ")
        }

        override fun onComplete() {
            Log.e(TAG, "onComplete: All Done!")
        }
    }

    override fun onPause() {
        super.onPause()
        textChangeObserver.dispose()
    }
}
