package com.example.minigamestation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser


class TitleScreenActivity : AppCompatActivity() {
    @SuppressLint("Range", "MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.titlescreen)

        val mAuth: FirebaseAuth
        var mAuthListener: AuthStateListener
        mAuth = FirebaseAuth.getInstance()

        val user = findViewById<TextView>(R.id.user)

        val theUser: FirebaseUser? = mAuth.getCurrentUser()
        if (theUser != null){
            val _UID = theUser!!.email.toString()
            user.text = _UID
        }


        val buttonClick1 = findViewById<Button>(R.id.com_button)
        buttonClick1.setOnClickListener {
            val intent = Intent(this, ChooseAGame::class.java)
            startActivity(intent)
        }

        val buttonClick2 = findViewById<Button>(R.id.aa_button3)
        buttonClick2.setOnClickListener {
            val intent = Intent(this, AboutAppActivity::class.java)
            startActivity(intent)
        }

        val buttonClick3 = findViewById<Button>(R.id.gh_button)
        buttonClick3.setOnClickListener {
            val intent = Intent(this, HighscoresActivity::class.java)
            startActivity(intent)
        }
    }
}