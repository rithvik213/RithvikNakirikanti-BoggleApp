package com.example.rithviknakirikanti_boggleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ScoreResetFragment : Fragment() {

    private lateinit var scoreTextView: TextView
    private var listener: OnNewGameRequestedListener? = null

    interface OnNewGameRequestedListener {
        fun onNewGameRequested()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_score_reset_fragment, container, false)

        scoreTextView = view.findViewById(R.id.scoreTextView)

        view.findViewById<Button>(R.id.newGameButton).setOnClickListener {
            listener?.onNewGameRequested()
        }

        return view
    }

    fun updateScore(score: Int) {
        scoreTextView.text = "Score: $score"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNewGameRequestedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnNewGameRequestedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}

