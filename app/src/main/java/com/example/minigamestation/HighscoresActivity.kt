package com.example.minigamestation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HighscoresActivity : AppCompatActivity() {
    @SuppressLint("Range", "MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscores)

        val mAuth: FirebaseAuth
        var mAuthListener: AuthStateListener
        mAuth = FirebaseAuth.getInstance()

        val user = findViewById<TextView>(R.id.user)

        val theUser: FirebaseUser? = mAuth.getCurrentUser()
        if (theUser != null){
            val _UID = theUser!!.email.toString()
            user.text = _UID
        }


        val mgNum1 = findViewById<TextView>(R.id.mgNum1)
        val mgNum2 = findViewById<TextView>(R.id.mgNum2)
        val mgNum3 = findViewById<TextView>(R.id.mgNum3)

        val haNum1 = findViewById<TextView>(R.id.haNum1)
        val haNum2 = findViewById<TextView>(R.id.haNum2)
        val haNum3 = findViewById<TextView>(R.id.haNum3)

        val db = Firebase.firestore


        //HIGHSCORE SYSTEM FOR HANGMAN

        var ha_highscore1 = 100000
        var ha_highscore2 = 100000
        var ha_highscore3 = 100000
        var ha_num1 = "num1"
        var ha_num2 = "num2"
        var ha_num3 = "num3"




        db.collection("HangmanScores")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if ( Integer.parseInt("${document.data["score"]}") < ha_highscore1){
                        ha_num1 = ("${document.data["email"]}").toString()
                        ha_highscore1 = Integer.parseInt("${document.data["score"]}")
                    }else{
                        if ( Integer.parseInt("${document.data["score"]}") < ha_highscore2) {
                            ha_num2 = ("${document.data["email"]}").toString()
                            ha_highscore2 = Integer.parseInt("${document.data["score"]}")

                        }else{
                            if ( Integer.parseInt("${document.data["score"]}") < ha_highscore3) {
                                ha_num3 = ("${document.data["email"]}").toString()
                                ha_highscore3 = Integer.parseInt("${document.data["score"]}")

                            }
                        }
                    }
                }

                haNum1.text = "1. "+ ha_num1 + " -> " + ha_highscore1 + "s"
                haNum2.text = "2. "+ ha_num2 + " -> " + ha_highscore2 + "s"
                haNum3.text = "3. "+ ha_num3 + " -> " + ha_highscore3 + "s"



            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        //END OF HS FOR HANGMAN




        //HIGHSCORE SYSTEM FOR MEMORY GAME

        var mg_highscore1 = 100000
        var mg_highscore2 = 100000
        var mg_highscore3 = 100000
        var mg_num1 = "num1"
        var mg_num2 = "num2"
        var mg_num3 = "num3"




        db.collection("MemoryGameScores")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if ( Integer.parseInt("${document.data["score"]}") < mg_highscore1){
                        mg_num1 = ("${document.data["email"]}").toString()
                        mg_highscore1 = Integer.parseInt("${document.data["score"]}")
                    }else{
                        if ( Integer.parseInt("${document.data["score"]}") < mg_highscore2) {
                            mg_num2 = ("${document.data["email"]}").toString()
                            mg_highscore2 = Integer.parseInt("${document.data["score"]}")

                        }else{
                            if ( Integer.parseInt("${document.data["score"]}") < mg_highscore3) {
                                mg_num3 = ("${document.data["email"]}").toString()
                                mg_highscore3 = Integer.parseInt("${document.data["score"]}")

                            }
                        }
                    }
                }

                mgNum1.text = "1. "+ mg_num1 + " -> " + mg_highscore1 + "s"
                mgNum2.text = "2. "+ mg_num2 + " -> " + mg_highscore2 + "s"
                mgNum3.text = "3. "+ mg_num3 + " -> " + mg_highscore3 + "s"



            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        // END OF HS FOR MEMORY GAME





        }

}