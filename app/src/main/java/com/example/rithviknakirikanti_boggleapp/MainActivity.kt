package com.example.rithviknakirikanti_boggleapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), ScoreResetFragment.OnNewGameRequestedListener, MainGameplayFragment.GameplayActionsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadFragment(ScoreResetFragment(), R.id.scoreResetFragmentContainer)
            loadFragment(MainGameplayFragment(), R.id.gameplayFragmentContainer)
        }
    }

    private fun loadFragment(fragment: Fragment, containerId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    override fun onNewGameRequested() {
        val newGameplayFragment = MainGameplayFragment()
        loadFragment(newGameplayFragment, R.id.gameplayFragmentContainer)

        val scoreFragment = supportFragmentManager.findFragmentById(R.id.scoreResetFragmentContainer) as? ScoreResetFragment
        scoreFragment?.updateScore(0)
    }
    override fun onScoreUpdated(score: Int) {
        val scoreFragment = supportFragmentManager.findFragmentById(R.id.scoreResetFragmentContainer) as? ScoreResetFragment
        scoreFragment?.updateScore(score)
    }

}
