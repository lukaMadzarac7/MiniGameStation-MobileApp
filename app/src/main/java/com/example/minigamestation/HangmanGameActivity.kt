package com.example.minigamestation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date

class HangmanGameActivity : AppCompatActivity() {

    private val hangmanGameManager = HangmanGameManager()

    private lateinit var wordTextView: TextView
    private lateinit var lettersUsedTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var gameLostTextView: TextView
    private lateinit var gameWonTextView: TextView
    private lateinit var newGameButton: Button
    private lateinit var lettersLayout: ConstraintLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        imageView = findViewById(R.id.imageView)
        wordTextView = findViewById(R.id.wordTextView)
        lettersUsedTextView = findViewById(R.id.lettersUsedTextView)
        gameLostTextView = findViewById(R.id.gameLostTextView)
        gameWonTextView = findViewById(R.id.gameWonTextView)
        newGameButton = findViewById(R.id.newGameButton)
        lettersLayout = findViewById(R.id.lettersLayout)

        var timer = findViewById<Chronometer>(R.id.view_timer)
        timer.isCountDown = false
        timer.base = SystemClock.elapsedRealtime() + 0
        timer.start()

        newGameButton.setOnClickListener {

            timer.base = SystemClock.elapsedRealtime() + 0

            startNewGame(timer)
        }


        val gameState = hangmanGameManager.startNewGame()
        updateUI(gameState, timer)


        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    val gameState = hangmanGameManager.play((letterView).text[0])
                    updateUI(gameState, timer)
                    letterView.visibility = View.GONE
                }
            }
        }
    }

    private fun updateUI(hangmanGameState: HangmanGameState, timer: Chronometer) {
        when (hangmanGameState) {
            is HangmanGameState.Lost -> showGameLost(hangmanGameState.wordToGuess, timer)
            is HangmanGameState.Running -> {
                wordTextView.text = hangmanGameState.underscoreWord
                lettersUsedTextView.text = "Iskorištena slova: ${hangmanGameState.lettersUsed}"
                imageView.setImageDrawable(ContextCompat.getDrawable(this, hangmanGameState.drawable))
            }
            is HangmanGameState.Won -> showGameWon(hangmanGameState.wordToGuess, timer)
        }
    }

    private fun showGameLost(wordToGuess: String, timer: Chronometer) {

        wordTextView.text = wordToGuess
        gameLostTextView.visibility = View.VISIBLE
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.game7))
        lettersLayout.visibility = View.GONE
        Toast.makeText(this, "Izgubio si!", Toast.LENGTH_SHORT).show()
        timer.stop()
        val elapsedTimeInSec = ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000)
        val score = elapsedTimeInSec
        Toast.makeText(this, "Seconds: " + score, Toast.LENGTH_SHORT).show()
    }

    private fun showGameWon(wordToGuess: String, timer: Chronometer) {
        val mAuth: FirebaseAuth
        var mAuthListener: FirebaseAuth.AuthStateListener
        mAuth = FirebaseAuth.getInstance()

        var email: String? = null

        val theUser: FirebaseUser? = mAuth.getCurrentUser()
        if (theUser != null){
            val _UID = theUser!!.email.toString()
            email = _UID
        }
        wordTextView.text = wordToGuess
        gameWonTextView.visibility = View.VISIBLE
        lettersLayout.visibility = View.GONE
        Toast.makeText(this, "Pobjedio si!", Toast.LENGTH_SHORT).show()
        timer.stop()
        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val time = simpleDate.format(Date())
        val elapsedTimeInSec = ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000)
        val score = elapsedTimeInSec

        Toast.makeText(this, "Seconds: " + score, Toast.LENGTH_SHORT).show()



        val db = Firebase.firestore

        val played = hashMapOf(
            "email" to email,
            "time" to time,
            "score" to score
        )


        db.collection("HangmanScores")
            .add(played)
    }

    private fun startNewGame(timer: Chronometer) {
        gameLostTextView.visibility = View.GONE
        gameWonTextView.visibility = View.GONE
        val gameState = hangmanGameManager.startNewGame()
        lettersLayout.visibility = View.VISIBLE
        lettersLayout.children.forEach { letterView ->
            letterView.visibility = View.VISIBLE
        }
        updateUI(gameState, timer)
    }
}