package com.example.movingball.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.movingball.R;
import com.example.movingball.view.BallView;

public class MainActivity extends AppCompatActivity {

    // ballView is the view including the ball and the area it can move in
    private BallView ballView;

    // Seek bar to allow the user to choose the smallest distance the ball can browse
    private SeekBar seekBar;

    // Chosen smallest distance the ball can browse
    private TextView tvChosenMinMovement;

    // Variable to store the minimum movement the ball is allowed to browse in dp
    private int minimumMovement;

    // Scale of the seek bar
    private final int SCALE_SEEK_BAR = 10;
    private final int OFFSET_SEEK_BAR = 5;

    // Lenght of 1 pix in mm
    private double one_pix_to_mm;

    // onCreate is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting the graphic components
        ballView = findViewById(R.id.id_ballView);
        seekBar = findViewById(R.id.id_seekBar);
        tvChosenMinMovement = findViewById(R.id.id_tvChosenMinMovement);

        // Displaying the current number of mm chosen via the seek bar
        one_pix_to_mm = getSizeOnePixel();
        tvChosenMinMovement.setText(String.format("%.1f", (seekBar.getProgress()*SCALE_SEEK_BAR + OFFSET_SEEK_BAR)* one_pix_to_mm));

        // Getting the number of dp chosen via the seekbar
        minimumMovement = seekBar.getProgress() + OFFSET_SEEK_BAR;
    }

    // onResume is called when the activity is ready
    @Override
    protected void onResume() {
        super.onResume(); int i=0; 

        // Setting a callback that notifies clients when the progress level has been changed
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // The progress level has changed
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                // Displaying to the user the number chosen converted in mm
                tvChosenMinMovement.setText(String.format("%.1f", (i*SCALE_SEEK_BAR + OFFSET_SEEK_BAR)* one_pix_to_mm));

                // Getting the number of dp chosen via the seekbar
                minimumMovement = i*SCALE_SEEK_BAR + OFFSET_SEEK_BAR;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Setting a touch callback that notifies clients when the ballView has been touched
        ballView.setOnTouchListener(new View.OnTouchListener() {

            // onTouch is called when an motion event on the view has been detected.
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                // Variables for the X and Y position of the click
                int eventPosX;
                int eventPosY;

                // Variables for the X and Y position of the ball
                int ballPosX;
                int ballPosY;

                // Checking the event type
                switch(event.getActionMasked()) {

                    // A pressed gesture is detected (ACTION_DOWN => press / ACTION_UP =>
                    // release)
                    case MotionEvent.ACTION_DOWN:
                        eventPosX = (int) event.getX();
                        eventPosY = (int) event.getY();
                        Log.w("tag",eventPosX+"");

                        // The x and y click position are set to the BallView class.
                        ballView.setPosition(eventPosY, eventPosX);
                        ballView.performClick();
                        return true;

                    // A change has happened during a press gesture (between ACTION_DOWN and
                    // ACTION_UP)
                    case MotionEvent.ACTION_MOVE:
                        eventPosX = (int) event.getX();
                        eventPosY = (int) event.getY();
                        ballPosX = ballView.getPosLeftDpx();
                        ballPosY = ballView.getPosTopDpx();

                        // If the pressure is detected distant of at least minimumMovement from the
                        // position of the ballView
                        // Using the circle equation (x - h)2 + (y - k)2 = r2 (with (x, y) a point
                        // of the circle, (h, k) the center of the circle and r the circle radius).
                        if((eventPosX - ballPosX)*(eventPosX - ballPosX) + (eventPosY - ballPosY)*(eventPosY - ballPosY) > (minimumMovement)*(minimumMovement)) {

                            // The x and y click position are set to the BallView class.
                            ballView.setPosition(eventPosY, eventPosX);
                            ballView.performClick();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    // The size of one pixel depends on the screen density
    double getSizeOnePixel() {
        float density = getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return 25.4/640;
        }
        else if (density >= 3.0) {
            return 25.4/480;
        }
        else if (density >= 2.0) {
            return 25.4/320;
        }
        else if (density >= 1.5) {
            return 25.4/240;
        }
        else {
            return 25.4/160;
        }
    }
}
