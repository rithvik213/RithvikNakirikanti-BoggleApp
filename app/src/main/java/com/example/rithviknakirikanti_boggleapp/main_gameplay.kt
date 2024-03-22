package com.example.rithviknakirikanti_boggleapp
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import kotlin.random.Random

class MainGameplayFragment : Fragment() {

    private lateinit var lettersGrid: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_gameplay, container, false)

        lettersGrid = view.findViewById(R.id.lettersGrid)
        initializeGrid(view)

        view.findViewById<Button>(R.id.clearButton).setOnClickListener {

        }

        view.findViewById<Button>(R.id.submitButton).setOnClickListener {

        }

        return view
    }

    private fun initializeGrid(view: View) {
        lettersGrid.removeAllViews()
        val letters = ('A'..'Z').toList()
        val context = view.context
        val total = 16
        val columnCount = 4
        lettersGrid.columnCount = columnCount
        lettersGrid.rowCount = 4

        for (i in 0 until total) {
            val button = Button(context).apply {
                text = letters[Random.nextInt(letters.size)].toString()
                layoutParams = GridLayout.LayoutParams(GridLayout.spec(
                    GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)).apply {
                    width = 0
                    height = 0
                    setMargins(5, 5, 5, 5)
                }
            }
            lettersGrid.addView(button)
        }
    }

}