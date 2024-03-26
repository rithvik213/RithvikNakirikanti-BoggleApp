package com.example.rithviknakirikanti_boggleapp
import android.content.Context
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
import java.io.BufferedReader
import java.util.Locale
import kotlin.random.Random

class MainGameplayFragment : Fragment() {

    private lateinit var lettersGrid: GridLayout
    private lateinit var selectedLettersTextView: TextView
    private var selectedLetters = StringBuilder()
    private var currentScore: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_gameplay, container, false)

        lettersGrid = view.findViewById(R.id.lettersGrid)
        selectedLettersTextView = view.findViewById(R.id.selectedLettersTextView)

        loadDictionary(requireContext())
        initializeGrid(view)

        val clearButton = view.findViewById<Button>(R.id.clearButton)
        clearButton.setOnClickListener {
            clearSelection()
        }
        view.findViewById<Button>(R.id.submitButton).setOnClickListener {
            val currentWord = selectedLetters.toString().lowercase(Locale.getDefault())

            if (isWordValid(currentWord)) {
                val scoreForCurrentWord = calculateScore(currentWord, true)
                Toast.makeText(context, "Correct word! +$scoreForCurrentWord", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Word not found in dictionary. -10", Toast.LENGTH_SHORT).show()
            }
            clearSelection()
        }


        return view
    }

    private lateinit var gridLetters: List<Letter>
    private lateinit var dictionaryWords: Set<String>

    private fun loadDictionary(context: Context) {
        val inputStream = context.assets.open("words.txt")
        dictionaryWords = inputStream.bufferedReader().use(BufferedReader::readLines).toSet()
    }

    private fun calculateScore(word: String, isValidWord: Boolean) {
        val vowels = setOf('A', 'E', 'I', 'O', 'U')
        val specialConsonants = setOf('S', 'Z', 'P', 'X', 'Q')
        var score = 0

        if (isValidWord) {
            word.uppercase(Locale.ROOT).forEach { char ->
                score += when (char) {
                    in vowels -> 5
                    else -> 1
                }
            }

            if (word.any { it.uppercaseChar() in specialConsonants }) score *= 2
        } else {
            score = -10
        }

        currentScore += score

        gameplayActionsListener?.onScoreUpdated(currentScore)    }


    private fun initializeGrid(view: View) {
        lettersGrid.removeAllViews()
        val context = view.context

        val vowels = listOf('A', 'E', 'I', 'O', 'U')
        val totalCells = 16
        val vowelPositions = listOf((0 until totalCells).random(), (0 until totalCells).random())

        gridLetters = List(4) { row ->
            List(4) { col ->
                val position = row * 4 + col

                val letter = if (position in vowelPositions) {
                    vowels.random()
                } else {
                    (('A'..'Z') - vowels).random()
                }

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

    private fun isWordValid(word: String): Boolean {
        return dictionaryWords.contains(word.lowercase())
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

    interface GameplayActionsListener {
        fun onScoreUpdated(score: Int)
    }

    private var gameplayActionsListener: GameplayActionsListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GameplayActionsListener) {
            gameplayActionsListener = context
        } else {
            throw RuntimeException("$context must implement GameplayActionsListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        gameplayActionsListener = null
    }

}


