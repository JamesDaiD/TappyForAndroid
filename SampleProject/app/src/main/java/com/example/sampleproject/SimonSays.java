package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sampleproject.SupportClasses.ButtonColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimonSays extends AppCompatActivity {

    //Pseudo-code for what I will try to do:
    /*
    An array of 7 buttons. Each position will represent one button that will be highlighted in order, from 1 to 7.
    With every onClickListener, I need to check if the user is pressing the button in the right order.

    If timer's still ticking or game hasn't started, do nothing,
    else
        If it's wrong on the order, Toast the user and reset.
        IF it is right but more buttons need to be pressed, increment stuff and do nothing
        if it's right and the last button, do the showing sequence with 1 more button or if it's the last in the array, player wins.

     How to show the player the highlighted buttons in order:
     Set up a timer, with number of seconds left ranging from 1000 to 7000 ms
     On each tick (probably 500 - 1000 ms), the next button will highlight, until it finishes
        for every highlighting I need to turn the button off before the other gets lit (bro),
        so the postDelayed stuff needs to be less than the tick.
     */

    /*
    * Random order is made. Now I need to
    * Set onclickListeners for each of the buttons
    * Make it incrementally show the pattern
    * */

    private final int NUMBER_OF_ROUNDS = 7;
    private CountDownTimer timer;
    private long timeLeft;
    private final int tickTime = 1000;
    private final List <ButtonColor> buttonColors = new ArrayList<>();
    private int roundNumber = 1;
    private int playerPresses = 0;
    private boolean rightOrder = true;
    private boolean gameStarted = false;
    private boolean patternShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon_says);

        //Adding the buttons and their respective colors so I can freely use it
        buttonColors.add(new ButtonColor((Button)findViewById(R.id.buttonGreen),R.color.lightGreen,R.color.darkGreen,"green"));
        buttonColors.add(new ButtonColor((Button)findViewById(R.id.buttonRed),R.color.lightRed,R.color.darkRed,"red"));
        buttonColors.add(new ButtonColor((Button)findViewById(R.id.buttonYellow),R.color.lightYellow,R.color.darkOrange,"yellow"));
        buttonColors.add(new ButtonColor((Button)findViewById(R.id.buttonBlue),R.color.lightBlue,R.color.darkBlue,"blue"));

        Button testButton = findViewById(R.id.buttonSimonStart);

        setOnTouchListeners();

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiating a ButtonColor list for the random ButtonColors
                List<ButtonColor> colorOrder = assignOrder();
                gameStarted = true;
                //setOnClickListeners for all colored buttons
                setButtonListeners(colorOrder);
                showPattern(colorOrder);
            }
        });

    }

    private void showPattern(final List<ButtonColor> buttons) {

        //timeLeft will be array length times tick -> REFACTOR! NEEDS TO BE CURRENT ROUND NUMBER!
        timeLeft = tickTime*roundNumber;
        patternShowing = true;

        timer = new CountDownTimer(timeLeft,tickTime) {
            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    highlightButton(buttons.get(i).getButton(),buttons.get(i).getHighlightColor());
                    unHighlightButton(buttons.get(i).getButton(),buttons.get(i).getDarkColor());
                    i++;
                } catch (Exception ex) {
                    Toast.makeText(SimonSays.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFinish() {
                patternShowing = false;
            }
        }.start();
    }

    private List<ButtonColor> assignOrder() {
        Random rand = new Random();
        List<ButtonColor> order = new ArrayList<>();
        //grab a random ButtonColor from the buttons
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
            int randomIndex = rand.nextInt(buttonColors.size());
            order.add(buttonColors.get(randomIndex));
        }
        return order;
    }

    private void highlightButton (Button button, int color) {
        button.setBackgroundColor(getResources().getColor(color));
    }

    void unHighlightButton(final Button button, final int color) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                button.setBackgroundColor(getResources().getColor(color));

            }
        }, 500);
    }

    void setButtonListeners(final List<ButtonColor> colorOrder) {

        //see the roundNumber, see the buttonPresses,
        //compare the button that has been pressed with the button in the order array
        //if it's the same button, either keep going or if it's the last button, user won.
        for (int i = 0; i < buttonColors.size(); i++) {
            final ButtonColor currentButton = buttonColors.get(i);
            buttonColors.get(i).getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //don't do anything if game has not started or pattern is still being shown
                    if (!gameStarted || patternShowing) return;
                    else  {
                        //right button
                        if (currentButton.getButtonName().equals(colorOrder.get(playerPresses).getButtonName())) {
                            playerPresses++;
                            if (playerPresses == roundNumber) {
                                roundNumber ++;
                                if (roundNumber > NUMBER_OF_ROUNDS) {
                                    //game is won!
                                    Toast.makeText(SimonSays.this, "Game Won!", Toast.LENGTH_LONG).show();
                                } else {
                                    //round won, need to show
                                    Toast.makeText(SimonSays.this, "Round Won! Next Round...", Toast.LENGTH_SHORT).show();
                                    playerPresses = 0;

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run()
                                        {
                                            showPattern(colorOrder);
                                        }
                                    }, 500);

                                }
                            }
                        }
                        //wrong button
                        else {
                            roundNumber = 1;
                            playerPresses = 0;
                            Toast.makeText(SimonSays.this, "Wrong Button! Start again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    void setOnTouchListeners() {
        for (int i = 0; i < buttonColors.size(); i++) {
            final ButtonColor currentButtonColor = buttonColors.get(i);
            final Button currentButton = currentButtonColor.getButton();
            currentButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        currentButton.setBackgroundColor(getResources().getColor(currentButtonColor.getDarkColor()));
                    }
                    else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        currentButton.setBackgroundColor(getResources().getColor(currentButtonColor.getHighlightColor()));
                    }
                    return false;
                }
            });
        }

    }


}