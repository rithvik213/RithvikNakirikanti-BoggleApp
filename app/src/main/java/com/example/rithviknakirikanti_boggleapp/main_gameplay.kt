package com.example.rithviknakirikanti_boggleapp
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainGameplayFragment : Fragment() {

    private lateinit var lettersGrid: GridLayout
    private lateinit var selectedLettersTextView: TextView
    private var selectedLetters = StringBuilder()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_gameplay, container, false)

        lettersGrid = view.findViewById(R.id.lettersGrid)
        selectedLettersTextView = view.findViewById(R.id.selectedLettersTextView)
        initializeGrid(view)

        val clearButton = view.findViewById<Button>(R.id.clearButton)
        clearButton.setOnClickListener {
            clearSelection()
        }

        view.findViewById<Button>(R.id.submitButton).setOnClickListener {

        }

        return view
    }

    private lateinit var gridLetters: List<Letter>

    private fun initializeGrid(view: View) {
        lettersGrid.removeAllViews()
        val context = view.context

        gridLetters = List(4) { row ->
            List(4) { col ->
                val letter = ('A'..'Z').random()
                val button = Button(context).apply {
                    text = letter.toString()
                }

                val letterButton = Letter(button, row, col)
                lettersGrid.addView(button, GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    setGravity(Gravity.FILL)
                    setMargins(5, 5, 5, 5)
                    columnSpec = GridLayout.spec(col, 1f)
                    rowSpec = GridLayout.spec(row, 1f)
                })

                button.setOnClickListener {
                    onLetterButtonClick(letterButton)
                }

                letterButton
            }
        }.flatten()
    }

    private var lastSelectedLetter: Letter? = null

    private fun onLetterButtonClick(selectedLetter: Letter) {
        if (selectedLetter.isSelected) {
            Toast.makeText(context, "This letter is already selected.", Toast.LENGTH_SHORT).show()
            return
        }

        if (lastSelectedLetter == null || isSelectedAdjacent(lastSelectedLetter!!, selectedLetter)) {
            selectedLetter.isSelected = true
            selectedLetter.button.isEnabled = false
            lastSelectedLetter = selectedLetter
            selectedLetters.append(selectedLetter.button.text)
            selectedLettersTextView.text = selectedLetters.toString()
        } else {
            Toast.makeText(context, "Invalid entry. Please choose an adjacent letter.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearSelection() {
        selectedLetters.clear()
        selectedLettersTextView.text = ""

        gridLetters.forEach { letter ->
            letter.isSelected = false

            letter.button.isEnabled = true

        }

        lastSelectedLetter = null
    }




    private fun isSelectedAdjacent(lastSelected: Letter, newSelected: Letter): Boolean {
        val rowDiff = Math.abs(lastSelected.row - newSelected.row)
        val colDiff = Math.abs(lastSelected.col - newSelected.col)
        return rowDiff <= 1 && colDiff <= 1
    }




}
