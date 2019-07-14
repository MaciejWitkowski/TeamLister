package maciej_witkowski.teamlister.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
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

private val TAG  = ProcessedTeamFragment::class.java.simpleName

class ProcessedTeamFragment : Fragment() {
    private lateinit var viewModel: TeamsViewModel
    private var activeTeam = 1
    private var cursor = 0
    private val teamObserver = Observer<String> { value ->
        value?.let {
            tv_team_processed.setText(value)
            tv_team_processed.setSelection(cursor)
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
        (activity as? AppCompatActivity)?.supportActionBar?.title =getString(R.string.app_title_team_lists)
        viewModel.team1.observe(this, teamObserver)
        styleTeam1Button()
        btn_team_picker_1.setOnClickListener {
            viewModel.team2.removeObserver(teamObserver)
            viewModel.team1.observe(this, teamObserver)
            styleTeam1Button()
            activeTeam = 1
            cursor = 0
        }
        btn_team_picker_2.setOnClickListener {
            viewModel.team1.removeObserver(teamObserver)
            viewModel.team2.observe(this, teamObserver)
            styleTeam2Button()
            activeTeam = 2
            cursor = 0
        }
        btn_save.setOnClickListener {
            viewModel.saveToFiles()
        }
        RxTextView.textChanges(tv_team_processed)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(textChangeObserver)
    }

    private fun styleTeam1Button() {
        btn_team_picker_1.textSize = 30f
        btn_team_picker_2.textSize = 12f
    }

    private fun styleTeam2Button() {
        btn_team_picker_1.textSize = 12f
        btn_team_picker_2.textSize = 30f
    }


    private val textChangeObserver = object : DisposableObserver<CharSequence>() {//TODO keyboard is blinking on text save/reload
        override fun onNext(charSequence: CharSequence) {
            if (activeTeam == 1 && !charSequence.toString().equals(viewModel.team1.value) && !charSequence.toString().isNullOrEmpty() && !charSequence.toString().equals(
                    viewModel.team2.value)
            ) {
                cursor = tv_team_processed.selectionStart
                viewModel.updateProcessedTeam(charSequence.toString(), activeTeam)

            } else if (activeTeam == 2 && !charSequence.toString().equals(viewModel.team2.value) && !charSequence.toString().isNullOrEmpty() && !charSequence.toString().equals(
                    viewModel.team1.value)
            ) {
                cursor = tv_team_processed.selectionStart
                viewModel.updateProcessedTeam(charSequence.toString(), activeTeam)
            }
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
