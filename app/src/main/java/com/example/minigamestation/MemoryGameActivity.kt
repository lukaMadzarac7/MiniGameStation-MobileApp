package com.example.minigamestation

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.text.SimpleDateFormat
import java.util.Date
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore



class MemoryGameActivity : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null
    //private lateinit var database : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memory_game)


        val imgbtn1 = findViewById<ImageButton>(R.id.imageButton1)
        val imgbtn2 = findViewById<ImageButton>(R.id.imageButton2)
        val imgbtn3 = findViewById<ImageButton>(R.id.imageButton3)
        val imgbtn4 = findViewById<ImageButton>(R.id.imageButton4)
        val imgbtn5 = findViewById<ImageButton>(R.id.imageButton5)
        val imgbtn6 = findViewById<ImageButton>(R.id.imageButton6)
        val imgbtn7 = findViewById<ImageButton>(R.id.imageButton7)
        val imgbtn8 = findViewById<ImageButton>(R.id.imageButton8)

        buttons = listOf(imgbtn1, imgbtn2, imgbtn3, imgbtn4, imgbtn5, imgbtn6, imgbtn7, imgbtn8)
        val images = mutableListOf(R.drawable.ic_plane, R.drawable.ic_car, R.drawable.ic_diamond, R.drawable.ic_hp)
        images.addAll(images)
        images.shuffle()


        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        var timer = findViewById<Chronometer>(R.id.view_timer)
        timer.isCountDown = false
        timer.base = SystemClock.elapsedRealtime() + 0
        timer.start()

        timer.setOnClickListener {
            restartGame()

        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Log.i(TAG, "button clicked!!")
                // Update models
                updateModels(index)
                // Update the UI for the game
                updateViews()
                checkForGameOver(timer)
            }
        }
    }

    private fun restartGame() {
        finish();
        startActivity(getIntent());
    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f
            }
            button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.ic_code)
        }
    }

    private fun updateModels(position: Int) {
        val card = cards[position]
        // Error checking:
        if (card.isFaceUp) {
            return
        }
        // Three cases
        // 0 cards previously flipped over => restore cards + flip over the selected card
        // 1 card previously flipped over => flip over the selected card + check if the images match
        // 2 cards previously flipped over => restore cards + flip over the selected card
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 selected cards previously
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            // exactly 1 card was selected previously
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier) {
            //Toast.makeText(this, "Match found!", Toast.LENGTH_SHORT).show()
            cards[position1].isMatched = true
            cards[position2].isMatched = true



        }
    }

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkForGameOver(timer: Chronometer){
        var counter: Int = 0
        for (card in cards){
            if (card.isMatched == false)
                counter = counter + 1
        }
        if(counter == 0){

            val mAuth: FirebaseAuth
            var mAuthListener: FirebaseAuth.AuthStateListener
            mAuth = FirebaseAuth.getInstance()

            var email: String? = null

            val theUser: FirebaseUser? = mAuth.getCurrentUser()
            if (theUser != null){
                val _UID = theUser!!.email.toString()
                email = _UID
            }

            Toast.makeText(this, "Game ended!", Toast.LENGTH_SHORT).show()
            timer.stop()
            val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val time = simpleDate.format(Date())
            val elapsedTimeInSec = ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000)
            val score = elapsedTimeInSec


            if (email != null) {

                val db = Firebase.firestore

                val played = hashMapOf(
                    "email" to email,
                    "time" to time,
                    "score" to score
                )


                db.collection("MemoryGameScores")
                    .add(played)


                Toast.makeText(this, "Seconds: " + score, Toast.LENGTH_SHORT).show()


            }
            }

        }

    }



