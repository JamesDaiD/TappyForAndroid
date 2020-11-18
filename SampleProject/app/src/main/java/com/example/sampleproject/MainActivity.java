package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.logging.Level;

public class MainActivity extends AppCompatActivity
{

    Button startGameBtn; //declare button object

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGameBtn = findViewById(R.id.startButton); //hook button object to preferred button
        startGameBtn.setText("Level List");
        /*give the button an onClickListener*/
        startGameBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, LevelList.class));
            }
        }); //end setOnClickListener of startGameBtn

        //TODO: starts DB implementation
        //TODO: get username
            //TODO: check if username already exists in DB and act accordingly
    }

    //return home on back pressed
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}