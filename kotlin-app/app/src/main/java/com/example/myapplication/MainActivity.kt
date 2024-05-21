package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.app.Dialog
import android.app.DatePickerDialog
import java.util.*
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.widget.NumberPicker
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.ImageView
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {

    // setting the date and time of the booking table to be null
    private var dateTimeDisplay: String = " "

    //initiates the image slider to show the 5 pictures that switch every 2 seconds
    private var imageSlider: ImageView? = null
    private val imageResources = arrayOf( // creating the array of the 5 pictures
        R.drawable.vibe1, R.drawable.vibe2, R.drawable.vibe3,
        R.drawable.vibe4, R.drawable.vibe5, R.drawable.vibe6
    )
    private var currentImageIndex = 0

    private val sliderHandler = Handler(Looper.getMainLooper())
    private val imageSwitcherRunnable = object : Runnable { // switch the 5 pictures every 2 seconds
        override fun run() {
            currentImageIndex = (currentImageIndex + 1) % imageResources.size
            imageSlider?.setImageResource(imageResources[currentImageIndex])
            sliderHandler.postDelayed(this, 2000)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Set the content view to my layout
        imageSlider = findViewById(R.id.imageSlider)


        val toolbar: Toolbar = findViewById(R.id.toolbar) // creates my tool bar val
        setSupportActionBar(toolbar)


        // Initialize the ImageView for image slider
        imageSlider = findViewById(R.id.imageSlider)

        // Start the image switching
        sliderHandler.post(imageSwitcherRunnable)

        // sets my radioGroup button for the "Are you vegan" question
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
        }


        val volumeText: TextView = findViewById(R.id.volume)
        val seekBar: SeekBar = findViewById(R.id.seekBar) // the seek bar of the how many people are you when booking a table
        val inputRemarks: EditText = findViewById(R.id.input_remarks) // the text edited by the user


        // Set initial text based on initial progress of SeekBar
        volumeText.text = (seekBar.progress + 1).toString()
        var answer_howmany = volumeText.text  // Initialize with initial value

        //creating a variable from the seekbar and if the number is 9, a dialog alert jumps
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val currentNumber = progress + 1
                volumeText.text = currentNumber.toString()
                answer_howmany = volumeText.text  // Update answer_howmany on each change

                if (currentNumber == 9) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(getString(R.string.alert_dialog_title))
                        .setMessage(getString(R.string.alert_dialog_message))
                        .setPositiveButton(getString(R.string.button_ok)) { dialog, which ->
                            // Optional: handle the OK button action
                        }
                        .show()
                }
                else{

                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optionally handle start of touch
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optionally handle end of touch
            }
        })


        val paymentPicker = findViewById<NumberPicker>(R.id.paymentPicker) // the choice of payment
        val paymentOptions = arrayOf("Cash", "Visa", "Debit Card", "MasterCard") // all the options for the payment

        paymentPicker.minValue = 0
        paymentPicker.maxValue = paymentOptions.size - 1
        paymentPicker.displayedValues = paymentOptions

        // Optional: set a listener to get the selected value
        paymentPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            // Use newVal to get the selected payment option from the paymentOptions array
        }
        // when we click on the "chose date and time" button when booking a table a calendar jumps (it is the dialog)
        val dateTimeButton = findViewById<Button>(R.id.date)
        dateTimeButton.setOnClickListener {
            showDatePickerDialog()
        }

        val answer_payment = findViewById<NumberPicker>(R.id.paymentPicker)


        val submitButton = findViewById<Button>(R.id.submitAnswersButton) // the "reserve seats" button to submit all booking table choices

        // when we click on the "reserve seats" button, ann list of all the answers is created and showed in a dialog
        submitButton.setOnClickListener {
            val remarksText = inputRemarks.text.toString() // the text from the editText remarks
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            val radioText = if (selectedRadioButtonId != -1) {
                val radioButton = findViewById<RadioButton>(selectedRadioButtonId)
                radioButton.text.toString()
            } else "No option selected"


            // Find the checkboxes of "are you celebrating"
            val birthday: CheckBox = findViewById(R.id.birthday)
            val engagement: CheckBox = findViewById(R.id.engagement)
            val nothing: CheckBox = findViewById(R.id.nothing)


            // Prepare the text from the checkboxes
            val checkBoxTexts = mutableListOf<String>()
            if (birthday.isChecked) {
                checkBoxTexts.add(birthday.text.toString())
            }
            if (engagement.isChecked) {
                checkBoxTexts.add(engagement.text.toString())
            }
            if (nothing.isChecked) {
                checkBoxTexts.add(nothing.text.toString())
            }

            // Join the checked options with a comma if needed, or provide a default text if none are checked
            val checkBoxResults = if (checkBoxTexts.isNotEmpty()) checkBoxTexts.joinToString(", ") else "No checkboxes selected"


            Log.d("Debug", "Number of Seats: ${answer_howmany}")
            Log.d("Debug", "Payment Option: ${paymentOptions[answer_payment.value]}")
            Log.d("Debug", "Date and Time: $dateTimeDisplay")
            Log.d("Debug", "Are You Vegan: $radioText")
            Log.d("Debug", "Remarks: $remarksText")
            Log.d("Debug", "Checkbox Selections: $checkBoxResults")

            // the list of all answers
            val answers = listOf(
                getString(R.string.number_of_seats) + "${answer_howmany}",
                getString(R.string.payment_choice) + "${paymentOptions[answer_payment.value]}",
                getString(R.string.date_and_time) + "$dateTimeDisplay",
                getString(R.string.are_you_vegan) + "$radioText",
                getString(R.string.input_remarks) + " $remarksText",
                getString(R.string.celebrating) + "$checkBoxResults"
            )
            showAnswersDialog(answers)

        }

        // initiates each button from the menu to show a dialog with the meal details when clicked
        val button1 : Button = findViewById(R.id.button1)
        button1.setOnClickListener{
            val message: String? = getString(R.string.info1)
            showCustomDialogBox(message)
        }
        val button2 : Button = findViewById(R.id.button2)
        button2.setOnClickListener{
            val message: String? = getString(R.string.info2)
            showCustomDialogBox(message)
        }
        val button3 : Button = findViewById(R.id.button3)
        button3.setOnClickListener{
            val message: String? = getString(R.string.info3)
            showCustomDialogBox(message)
        }
        val button4 : Button = findViewById(R.id.button4)
        button4.setOnClickListener{
            val message: String? =  getString(R.string.info4)
            showCustomDialogBox(message)

        }
        val button5 : Button = findViewById(R.id.button5)
        button5.setOnClickListener{
            val message: String? = getString(R.string.info5)
            showCustomDialogBox(message)
        }
        val button6 : Button = findViewById(R.id.button6)
        button6.setOnClickListener{
            val message: String? =  getString(R.string.info6)
            showCustomDialogBox(message)
        }
        val button7 : Button = findViewById(R.id.button7)
        button7.setOnClickListener{
            val message: String? = getString(R.string.info7)
            showCustomDialogBox(message)
        }
        val button8 : Button = findViewById(R.id.button8)
        button8.setOnClickListener{
            val message: String? = getString(R.string.info8)
            showCustomDialogBox(message)
        }
        val button9 : Button = findViewById(R.id.button9)
        button9.setOnClickListener{
            val message: String? = getString(R.string.info9)
            showCustomDialogBox(message)
        }
        val button10 : Button = findViewById(R.id.button10)
        button10.setOnClickListener{
            val message: String? = getString(R.string.info10)
            showCustomDialogBox(message)
        }
        val button11 : Button = findViewById(R.id.button11)
        button11.setOnClickListener{
            val message: String? = getString(R.string.info11)
            showCustomDialogBox(message)
        }
        val button12 : Button = findViewById(R.id.button12)
        button12.setOnClickListener{
            val message: String? = getString(R.string.info12)
            showCustomDialogBox(message)
        }
    }
    // the function that shows the dialog of the answers of the booking table choices
    private fun showAnswersDialog(answers: List<String>) {
        val message = answers.joinToString("\n")
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))  // Using string resource for title
            .setMessage(message)
            .setPositiveButton(getString(R.string.button_ok)) { dialog, _ -> dialog.dismiss() }  // Using string resource for button text
            .create()
            .show()
    }
    // the calendar dialog that show up when we click on the "select date and time" button
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            showTimePickerDialog(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }
    // the clock dialog that show up when we click on the "select date and time" button

    private fun showTimePickerDialog(calendar: Calendar) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)

            dateTimeDisplay = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.time)
            Log.d("MainActivity", "DateTime set: $dateTimeDisplay") // Log to check if the datetime is set correctly
        }, hour, minute, true) // 'true' for 24-hour time format

        timePickerDialog.show()
    }
    // the dialog created for each meal with details about the meal
    private fun showCustomDialogBox(message: String?){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_custome_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val info : TextView = dialog.findViewById(R.id.info)
        val  close_button : Button = dialog.findViewById(R.id.close_button)

        info.text = message
        close_button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
    // when we click on a radioButton (Are you vegan), the answer appears on the screen for a short time
    fun onRadioButtonClicked(view: View) {
        val checked = (view as RadioButton).isChecked
        var str = ""
        when (view.id) {
            R.id.yes -> if (checked) {
                str = getString(R.string.button_yes)
            }
            R.id.no -> if (checked) {
                str = getString(R.string.button_no)
            }
        }
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }




    override fun onDestroy() {
        super.onDestroy()
        sliderHandler.removeCallbacks(imageSwitcherRunnable) // Stop the handler when activity is destroyed
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // when we click on the "menu" option on the toolbar, this function is called and scrolls to the menu
    private fun scrollToMenu() {
        val menuLayout = findViewById<LinearLayout>(R.id.menuLayout)
        val scrollView = findViewById<ScrollView>(R.id.main)

        scrollView.post {
            // Smooth scroll to the Y position of the menu
            scrollView.smoothScrollTo(0, menuLayout.top)
        }
    }
    // when we click on the "book a table" option on the toolbar, this function is called and scrolls to the book a table part of the screen

    private fun scrollToBook() {
        val bookLayout = findViewById<TextView>(R.id.book_table)
        val scrollView = findViewById<ScrollView>(R.id.main)

        scrollView.post {
            // Smooth scroll to the Y position of the menu
            scrollView.smoothScrollTo(0, bookLayout.top)
        }
    }
    // when we click on the "contact us" option on the toolbar, this function is called and scrolls to the contact us part of the screen

    private fun scrollToContact() {
        val contactLayout = findViewById<TextView>(R.id.contact)
        val scrollView = findViewById<ScrollView>(R.id.main)

        scrollView.post {
            // Smooth scroll to the Y position of the menu
            scrollView.smoothScrollTo(0, contactLayout.top)
        }
    }
    // when we click on an item in the tool bar, this function calls for one of the functions to scroll to the right place depending on what options of the toolbar has been chosen
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_menu -> {
                scrollToMenu()
                true
            }
            R.id.menu_book -> {
                scrollToBook()
                true
            }
            R.id.menu_contact -> {
                scrollToContact()
                true
            }
            // Handle other menu items if necessary
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val imageView = findViewById<ImageView>(R.id.imageSlider)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                resources.getDimensionPixelSize(R.dimen.landscape_image_height)
            )
        } else {
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

}
