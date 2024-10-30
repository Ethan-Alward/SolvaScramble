package com.example.solvascramble

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        // Set window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //if 0 level is easy if 1 level hard
        var difficulty = 0

        // Find the button by its ID
        val easyButton = findViewById<View>(R.id.easyButton)

        // Set an OnClickListener on the button
        easyButton.setOnClickListener {  difficulty = 0  }

        // Find the button by its ID
        val hardButton = findViewById<View>(R.id.hardButton)

        // Set an OnClickListener on the button
        hardButton.setOnClickListener {  difficulty = 1  }

        // Find the button by its ID
        val extremeButton = findViewById<View>(R.id.extremeButton)

        // Set an OnClickListener on the button
        extremeButton.setOnClickListener {  difficulty = 2  }

        // Find the button by its ID
        val startButton = findViewById<Button>(R.id.startButton)

        // Set an OnClickListener on the button
        startButton.setOnClickListener {

            if (difficulty == 0){
                //make a toast saying the level chosen
                Toast.makeText(this, "Level Chosen: Easy    Good Luck!",Toast.LENGTH_SHORT).show()
            } else if (difficulty == 1){
                Toast.makeText(this, "Level Chosen: Hard    Good Luck!",Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this, "Level Chosen: Extreme    Good Luck!",Toast.LENGTH_SHORT).show()
            }



            // Create an Intent to open MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("difficulty",difficulty.toString())
            startActivity(intent)  // Start MainActivity
        }
    }
}

