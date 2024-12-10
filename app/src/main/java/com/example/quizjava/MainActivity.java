package com.example.quizjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Question[] questions = new Question[]{
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false),
    };
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private static final String MAIN_ACTIVITY = "MainActivity";
    private int currentIndex = 0;
    private int counter = 0;
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private boolean answerWasShown;
    private TextView questionTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MAIN_ACTIVITY, "Metoda onCreate została wywołana");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        questionTextView = findViewById(R.id.question_text_view);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        promptButton = findViewById(R.id.prompt_button);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % questions.length;
                answerWasShown = false;
                setNextQuestion();

                if (currentIndex == 0)
                    showFinalScore();
            }
        });

        promptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].isTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                startActivityForResult(intent, REQUEST_CODE_PROMPT);

            }
        });
        setNextQuestion();
    }

    private void setNextQuestion() {
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if (answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
                counter++;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void showFinalScore() {
        String resultMessage = "Twój wynik: " + counter + "/" + questions.length;
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();
        counter = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MAIN_ACTIVITY, "Metoda onDestroy została wywołana");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(MAIN_ACTIVITY, "Metoda onPause została wywołana");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(MAIN_ACTIVITY, "Metoda onStop została wywołana");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MAIN_ACTIVITY, "Metoda onResume została wywołana");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(MAIN_ACTIVITY, "Metoda onStart została wywołana");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(MAIN_ACTIVITY, "Wywołana została metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null)
                return;
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }
}