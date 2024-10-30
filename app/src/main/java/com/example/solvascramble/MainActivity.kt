package com.example.solvascramble



import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


var myRand = 0
var arrayAmount = 0
var imgRotationArray = FloatArray(arrayAmount)
var winnerCheckerArray = FloatArray(arrayAmount)

var gridAmount = 0




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //now both levels use the same activity
        setContentView(R.layout.activity_main)

        val difficulty = intent.getStringExtra("difficulty")
        val imageFileName = "october24image"
        //set scramble message hhere

        var imgs: Array<Bitmap?> = splitBitmapEasy(this, imageFileName)

        var level = "temp"

        if (difficulty == "0") {//easy
            arrayAmount = 9
            gridAmount = 3
            level = "Easy"

        } else if (difficulty == "1") {//easy{//hard

            arrayAmount = 16
            gridAmount = 4
            level = "Hard"
            imgs = splitBitmapHard(this, imageFileName)
        } else {
            arrayAmount = 25
            gridAmount = 5
            level = "Extreme"
            imgs = splitBitmapExtreme(this, imageFileName)

        }

        imgRotationArray = FloatArray(arrayAmount)
        winnerCheckerArray = FloatArray(arrayAmount)

        setRotations(this)//fills imgRotationArray with random rotations  and winnerCheckerArray with 0s
        // so in the grid element on click listened if all imgRotationArray elements ar 0s the image is rotated correctly


        //Adding Timer
        val timer = findViewById<Chronometer>(R.id.timer)
        timer.start()

        var timeStopped = 0


        //setting all the images up in imageView
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        gridLayout.rowCount = gridAmount
        gridLayout.columnCount = gridAmount

        //diabling the share button
        val share = findViewById<Button>(R.id.shareButton)
        share.isEnabled = false
        share.setOnClickListener {

            // set up share score.


            val shareScore = Intent(Intent.ACTION_SEND).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_EMAIL, "ealward@unb.ca")
                putExtra(Intent.EXTRA_SUBJECT, "Scramble Score")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Level completed: ${level}, time completed: ${timeStopped}"
                )
            }
            startActivity(shareScore)

        }


        // Loop through imgs array and create an ImageView for each index
        for (i in imgs.indices) {
            val imageButton = ImageView(this)
            imageButton.setImageBitmap(imgs[i])

            val layoutParams = GridLayout.LayoutParams()
            layoutParams.width = 0
            layoutParams.height = 0
            layoutParams.columnSpec = GridLayout.spec(i % gridAmount, 1f)
            layoutParams.rowSpec = GridLayout.spec(i / gridAmount, 1f)
            imageButton.layoutParams = layoutParams

            imageButton.setRotation(imgRotationArray[i])

            imageButton.setOnClickListener {

                imgRotationArray[i] = imgRotationArray[i] + 90F
                if (imgRotationArray[i] > 359F) {
                    imgRotationArray[i] = 0F
                }
                imageButton.setRotation(imgRotationArray[i])

                //if winner
                if (imgRotationArray.contentEquals(winnerCheckerArray)) {

                    timer.stop()
                    timeStopped = timer.baseline

                    Toast.makeText(
                        this,
                        "You WONNNNNN!!!! ${level}   ${timeStopped}",
                        Toast.LENGTH_LONG
                    ).show()

                    val tv1 = findViewById<View>(R.id.pictureDescription) as TextView
//                    val string = getString(R.string.scrambleMessage)
                    tv1.text = getString(R.string.scrambleMessage)

                    //display share score
                    share.isEnabled = true


                }

            }
            // Add the ImageView to the GridLayout
            gridLayout.addView(imageButton)
        }
        //--------------------------------------------------------------------------------------------


        // Set window insets listener for layout adjustments
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the button by its ID
        val buttonToHomePage = findViewById<Button>(R.id.homeButton)

        // Set an OnClickListener on the button
        buttonToHomePage.setOnClickListener {
            timer.stop()
            // Create an Intent to open HomePage activity
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)  // Start HomePage
        }
    }

    fun setRotations(context: Context) {

        //make set the img rotation to 0 90 180 or 270 randomly
        for (i in imgRotationArray.indices) {

            winnerCheckerArray[i] = 0F

            myRand = (0..3).random()

            if (myRand == 0) {
                imgRotationArray[i] = 0F
            } else if (myRand == 1) {
                imgRotationArray[i] = 90F
            } else if (myRand == 2) {
                imgRotationArray[i] = 180F
            } else {
                imgRotationArray[i] = 270F
            }

        }

    }

    //--------------------------------------------------------------------------------------------------------
    fun splitBitmapEasy(context: Context, fileName: String): Array<Bitmap?> {
        //Get the resource ID
        val drawableResourceId =
            context.resources.getIdentifier(fileName, "drawable", context.packageName)

        //Load image as  Bitmap
        val picture = BitmapFactory.decodeResource(context.resources, drawableResourceId)

        val scaledBitmap = Bitmap.createScaledBitmap(picture, 240, 240, true)
        val imgs = arrayOfNulls<Bitmap>(9)

        // Split the bitmap into 9 parts
        imgs[0] = Bitmap.createBitmap(scaledBitmap, 0, 0, 80, 80)
        imgs[1] = Bitmap.createBitmap(scaledBitmap, 80, 0, 80, 80)
        imgs[2] = Bitmap.createBitmap(scaledBitmap, 160, 0, 80, 80)
        imgs[3] = Bitmap.createBitmap(scaledBitmap, 0, 80, 80, 80)
        imgs[4] = Bitmap.createBitmap(scaledBitmap, 80, 80, 80, 80)
        imgs[5] = Bitmap.createBitmap(scaledBitmap, 160, 80, 80, 80)
        imgs[6] = Bitmap.createBitmap(scaledBitmap, 0, 160, 80, 80)
        imgs[7] = Bitmap.createBitmap(scaledBitmap, 80, 160, 80, 80)
        imgs[8] = Bitmap.createBitmap(scaledBitmap, 160, 160, 80, 80)

        // Return the array of bitmaps
        return imgs
    }


    fun splitBitmapHard(context: Context, fileName: String): Array<Bitmap?> {
        //Get the resource ID

//        Log.d("TAG", "in image bitmap hard")
        val drawableResourceId =
            context.resources.getIdentifier(fileName, "drawable", context.packageName)

        //Load image as  Bitmap
        val picture = BitmapFactory.decodeResource(context.resources, drawableResourceId)

        val scaledBitmap = Bitmap.createScaledBitmap(picture, 240, 240, true)
        val imgs = arrayOfNulls<Bitmap>(16)

        // Split the bitmap into 9 parts
        imgs[0] = Bitmap.createBitmap(scaledBitmap, 0, 0, 60, 60)
        imgs[1] = Bitmap.createBitmap(scaledBitmap, 60, 0, 60, 60)
        imgs[2] = Bitmap.createBitmap(scaledBitmap, 120, 0, 60, 60)
        imgs[3] = Bitmap.createBitmap(scaledBitmap, 180, 0, 60, 60)
        imgs[4] = Bitmap.createBitmap(scaledBitmap, 0, 60, 60, 60)
        imgs[5] = Bitmap.createBitmap(scaledBitmap, 60, 60, 60, 60)
        imgs[6] = Bitmap.createBitmap(scaledBitmap, 120, 60, 60, 60)
        imgs[7] = Bitmap.createBitmap(scaledBitmap, 180, 60, 60, 60)
        imgs[8] = Bitmap.createBitmap(scaledBitmap, 0, 120, 60, 60)
        imgs[9] = Bitmap.createBitmap(scaledBitmap, 60, 120, 60, 60)
        imgs[10] = Bitmap.createBitmap(scaledBitmap, 120, 120, 60, 60)
        imgs[11] = Bitmap.createBitmap(scaledBitmap, 180, 120, 60, 60)
        imgs[12] = Bitmap.createBitmap(scaledBitmap, 0, 180, 60, 60)
        imgs[13] = Bitmap.createBitmap(scaledBitmap, 60, 180, 60, 60)
        imgs[14] = Bitmap.createBitmap(scaledBitmap, 120, 180, 60, 60)
        imgs[15] = Bitmap.createBitmap(scaledBitmap, 180, 180, 60, 60)

        // Return the array of bitmaps
        return imgs
    }

    fun splitBitmapExtreme(context: Context, fileName: String): Array<Bitmap?> {
        //Get the resource ID

//        Log.d("TAG", "in image bitmap hard")
        val drawableResourceId =
            context.resources.getIdentifier(fileName, "drawable", context.packageName)

        //Load image as  Bitmap
        val picture = BitmapFactory.decodeResource(context.resources, drawableResourceId)

        val scaledBitmap = Bitmap.createScaledBitmap(picture, 240, 240, true)
        val imgs = arrayOfNulls<Bitmap>(25)

        // Split the bitmap into 9 parts
        imgs[0] = Bitmap.createBitmap(scaledBitmap, 0, 0, 48, 48)
        imgs[1] = Bitmap.createBitmap(scaledBitmap, 48, 0, 48, 48)
        imgs[2] = Bitmap.createBitmap(scaledBitmap, 96, 0, 48, 48)
        imgs[3] = Bitmap.createBitmap(scaledBitmap, 144, 0, 48, 48)
        imgs[4] = Bitmap.createBitmap(scaledBitmap, 192, 0, 48, 48)
        imgs[5] = Bitmap.createBitmap(scaledBitmap, 0, 48, 48, 48)
        imgs[6] = Bitmap.createBitmap(scaledBitmap, 48, 48, 48, 48)
        imgs[7] = Bitmap.createBitmap(scaledBitmap, 96, 48, 48, 48)
        imgs[8] = Bitmap.createBitmap(scaledBitmap, 144, 48, 48, 48)
        imgs[9] = Bitmap.createBitmap(scaledBitmap, 192, 48, 48, 48)
        imgs[10] = Bitmap.createBitmap(scaledBitmap, 0, 96, 48, 48)
        imgs[11] = Bitmap.createBitmap(scaledBitmap, 48, 96, 48, 48)
        imgs[12] = Bitmap.createBitmap(scaledBitmap, 96, 96, 48, 48)
        imgs[13] = Bitmap.createBitmap(scaledBitmap, 144, 96, 48, 48)
        imgs[14] = Bitmap.createBitmap(scaledBitmap, 192, 96, 48, 48)
        imgs[15] = Bitmap.createBitmap(scaledBitmap, 0, 144, 48, 48)
        imgs[16] = Bitmap.createBitmap(scaledBitmap, 48, 144, 48, 48)
        imgs[17] = Bitmap.createBitmap(scaledBitmap, 96, 144, 48, 48)
        imgs[18] = Bitmap.createBitmap(scaledBitmap, 144, 144, 48, 48)
        imgs[19] = Bitmap.createBitmap(scaledBitmap, 192, 144, 48, 48)
        imgs[20] = Bitmap.createBitmap(scaledBitmap, 0, 192, 48, 48)
        imgs[21] = Bitmap.createBitmap(scaledBitmap, 48, 192, 48, 48)
        imgs[22] = Bitmap.createBitmap(scaledBitmap, 96, 192, 48, 48)
        imgs[23] = Bitmap.createBitmap(scaledBitmap, 144, 192, 48, 48)
        imgs[24] = Bitmap.createBitmap(scaledBitmap, 192, 192, 48, 48)


        // Return the array of bitmaps
        return imgs


    }

}