package com.fahomid.quizgame

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var startButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var answersRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var restartButton: Button

    private val allQuestions = arrayOf(
        "What is the capital of Canada?",
        "What is the national sport of Canada?",
        "Which Canadian city is known as the City of Gardens?",
        "What is the largest province in Canada by area?",
        "What year did Canada become a country?",
        "Which Canadian city is known as the Paris of North America?",
        "What is the longest river in Canada?",
        "What is the national animal of Canada?",
        "Which province is Canada's largest producer of oil and gas?",
        "What is the official language of Quebec?",
        "What is the highest mountain in Canada?",
        "Which Canadian province has the most lakes?",
        "What is the oldest national park in Canada?",
        "Which ocean borders Canada to the west?",
        "What is the population of Canada?",
        "What is the currency of Canada?",
        "What is the national flower of Canada?",
        "Which Canadian city hosted the 2010 Winter Olympics?",
        "What is the smallest province in Canada?",
        "What is the name of the national anthem of Canada?",
        "What is the capital city of Alberta?",
        "What is the name of the largest island in Canada?",
        "Which province is known as the breadbasket of Canada?",
        "What is the primary industry in Newfoundland and Labrador?",
        "Which Canadian city is known for its Stampede?",
        "What is the national tree of Canada?",
        "Which Canadian province is known for its French heritage?",
        "What is the official sport of Canada?",
        "What is the tallest building in Canada?",
        "What is the name of the first Prime Minister of Canada?"
    )

    private val allAnswers = arrayOf(
        arrayOf("Ottawa", "Toronto", "Montreal", "Vancouver"),
        arrayOf("Ice Hockey", "Basketball", "Lacrosse", "Soccer"),
        arrayOf("Vancouver", "Toronto", "Victoria", "Calgary"),
        arrayOf("Ontario", "Quebec", "British Columbia", "Nunavut"),
        arrayOf("1867", "1876", "1888", "1901"),
        arrayOf("Montreal", "Toronto", "Vancouver", "Quebec City"),
        arrayOf("Mackenzie River", "St. Lawrence River", "Fraser River", "Nelson River"),
        arrayOf("Beaver", "Moose", "Caribou", "Bear"),
        arrayOf("Alberta", "Ontario", "Quebec", "British Columbia"),
        arrayOf("French", "English", "Both", "None"),
        arrayOf("Mount Logan", "Mount Robson", "Mount St. Elias", "Mount Waddington"),
        arrayOf("Ontario", "Quebec", "British Columbia", "Manitoba"),
        arrayOf("Banff National Park", "Jasper National Park", "Yoho National Park", "Kootenay National Park"),
        arrayOf("Pacific Ocean", "Atlantic Ocean", "Arctic Ocean", "Indian Ocean"),
        arrayOf("38 million", "40 million", "35 million", "30 million"),
        arrayOf("Canadian Dollar", "US Dollar", "Euro", "Pound"),
        arrayOf("Maple Leaf", "Rose", "Lily", "Daisy"),
        arrayOf("Vancouver", "Calgary", "Toronto", "Montreal"),
        arrayOf("Prince Edward Island", "Nova Scotia", "New Brunswick", "Newfoundland"),
        arrayOf("O Canada", "God Save the Queen", "The Maple Leaf Forever", "Hail Canada"),
        arrayOf("Edmonton", "Calgary", "Red Deer", "Lethbridge"),
        arrayOf("Baffin Island", "Vancouver Island", "Newfoundland", "Victoria Island"),
        arrayOf("Saskatchewan", "Alberta", "Manitoba", "Ontario"),
        arrayOf("Fishing", "Mining", "Tourism", "Agriculture"),
        arrayOf("Calgary", "Edmonton", "Toronto", "Vancouver"),
        arrayOf("Maple", "Oak", "Pine", "Birch"),
        arrayOf("Quebec", "Ontario", "New Brunswick", "Nova Scotia"),
        arrayOf("Ice Hockey and Lacrosse", "Soccer", "Basketball", "Baseball"),
        arrayOf("CN Tower", "Scotia Plaza", "Trump International Hotel", "First Canadian Place"),
        arrayOf("John A. Macdonald", "Alexander Mackenzie", "John Abbott", "Robert Borden")
    )

    private val correctAnswers = intArrayOf(0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    private lateinit var selectedQuestions: Array<String>
    private lateinit var selectedAnswers: Array<Array<String>>
    private lateinit var selectedCorrectAnswers: IntArray

    private var currentQuestion = 0
    private var score = 0
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize UI components
        startButton = view.findViewById(R.id.start_button)
        timerTextView = view.findViewById(R.id.timer)
        questionTextView = view.findViewById(R.id.question)
        answersRadioGroup = view.findViewById(R.id.answers)
        submitButton = view.findViewById(R.id.submit)
        restartButton = view.findViewById(R.id.restart_button)

        // Set click listeners for buttons
        startButton.setOnClickListener {
            startQuiz()
        }

        submitButton.setOnClickListener {
            checkAnswer()
        }

        restartButton.setOnClickListener {
            restartQuiz()
        }

        return view
    }

    // Start the quiz by initializing variables and making relevant UI components visible
    private fun startQuiz() {
        startButton.visibility = View.GONE
        timerTextView.visibility = View.VISIBLE
        questionTextView.visibility = View.VISIBLE
        answersRadioGroup.visibility = View.VISIBLE
        submitButton.visibility = View.VISIBLE
        restartButton.visibility = View.GONE
        currentQuestion = 0
        score = 0
        selectRandomQuestions()
        loadQuestion()
    }

    // Select 10 random questions from the pool of 30 questions
    private fun selectRandomQuestions() {
        val indices = allQuestions.indices.shuffled().take(10)
        selectedQuestions = indices.map { allQuestions[it] }.toTypedArray()
        selectedAnswers = indices.map { allAnswers[it] }.toTypedArray()
        selectedCorrectAnswers = indices.map { correctAnswers[it] }.toIntArray()
    }

    // Load the current question and its answers
    private fun loadQuestion() {
        if (currentQuestion < selectedQuestions.size) {
            questionTextView.text = "Question ${currentQuestion + 1} out of ${selectedQuestions.size}: ${selectedQuestions[currentQuestion]}"
            answersRadioGroup.clearCheck()
            for (i in selectedAnswers[currentQuestion].indices) {
                (answersRadioGroup.getChildAt(i) as RadioButton).text = selectedAnswers[currentQuestion][i]
            }
            startTimer()
        } else {
            showResult()
        }
    }

    // Start the 30-second countdown timer for the current question
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the timer text with the remaining seconds
                timerTextView.text = ((millisUntilFinished / 1000) + 1).toString()
            }

            override fun onFinish() {
                // Handle the timer finishing: show "Time up!" message and load the next question
                timerTextView.text = "0"
                activity?.let {
                    Toast.makeText(it, "Time up!", Toast.LENGTH_SHORT).show()
                }
                currentQuestion++
                loadQuestion()
            }
        }.start()
    }

    // Check the selected answer and update the score
    private fun checkAnswer() {
        val selectedId = answersRadioGroup.checkedRadioButtonId
        if (selectedId != -1) { // Check if an answer is selected
            countDownTimer.cancel()
            val selectedAnswer = answersRadioGroup.indexOfChild(view?.findViewById(selectedId))
            if (selectedAnswer == selectedCorrectAnswers[currentQuestion]) {
                score++
                activity?.let {
                    Toast.makeText(it, "Correct!", Toast.LENGTH_SHORT).show()
                }
            } else {
                activity?.let {
                    Toast.makeText(it, "Wrong!", Toast.LENGTH_SHORT).show()
                }
            }
            currentQuestion++
            loadQuestion()
        } else {
            activity?.let {
                Toast.makeText(it, "Please select an answer", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Show the final score when the quiz is completed
    private fun showResult() {
        timerTextView.visibility = View.GONE
        questionTextView.text = "Your score is $score out of ${selectedQuestions.size}."
        answersRadioGroup.visibility = View.GONE
        submitButton.visibility = View.GONE
        restartButton.visibility = View.VISIBLE
    }

    // Restart the quiz by resetting variables and UI components
    private fun restartQuiz() {
        startButton.visibility = View.VISIBLE
        timerTextView.visibility = View.GONE
        questionTextView.visibility = View.GONE
        answersRadioGroup.visibility = View.GONE
        submitButton.visibility = View.GONE
        restartButton.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cancel the timer when the view is destroyed
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}
