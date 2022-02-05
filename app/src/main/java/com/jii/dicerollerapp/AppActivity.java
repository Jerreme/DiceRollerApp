package com.jii.dicerollerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding4.view.RxView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AppActivity extends AppCompatActivity {
    ImageView diceImage;
    TextView currentResult;
    LinearLayout container;
    Button btn;

    Animation rotate;
    String results[] = {"Try Again!","One", "Two", "Three", "Four", "Five", "Six"};

    String lastResult, img;
    int rollNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        diceImage = findViewById(R.id.image);
        currentResult = findViewById(R.id.result_text);
        container = findViewById(R.id.prev_container);
        btn = findViewById(R.id.button);

        container.removeView(findViewById(R.id.prev1));
        container.removeView(findViewById(R.id.prev2));
        container.removeView(findViewById(R.id.prev3));

        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//        getWindow().setStatusBarColor(ContextCompat.getColor(AppActivity.this,R.color.white));// set status background white

        rollClicked(btn);
    }


    TextView itemTemp, item1, item2, item3;
    boolean started = false;
    private void addItem(String textResult) {
        if(item3 != null) {
            container.removeView(item3);
        }
        if (item2 != null) {
            item3 = item2;
        }
        if (item1 != null) {
            item2 = item1;
        }

        TextView item = new TextView(this);
        item.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        item.setPadding(0,5,0,5);
        item.setGravity(Gravity.CENTER);
        item.setText(textResult);

        item1 = item;
        container.addView(item1,1);
    }

    public void rollClicked(View view) {
        RxView.clicks(btn).throttleFirst(1300, TimeUnit.MILLISECONDS).subscribe(empty -> {
            // action on click
            diceImage.setImageResource(R.drawable.dice_6_template);
            diceImage.startAnimation(rotate);
            rollNumber = (int) (Math.random() * 6 + 1);
            rollNumber = new Random().nextInt(6) + 1; // [0, 5] + 1

            String res;

            switch (rollNumber) {
                case 1:
                    img = "@drawable/dice_1";
                    res = results[1];
                    break;
                case 2:
                    img = "@drawable/dice_2";
                    res = results[2];
                    break;
                case 3:
                    img = "@drawable/dice_3";
                    res = results[3];
                    break;
                case 4:
                    img = "@drawable/dice_4";
                    res = results[4];
                    break;
                case 5:
                    img = "@drawable/dice_5";
                    res = results[5];
                    break;
                case 6:
                    img = "@drawable/dice_6";
                    res = results[6];
                    break;
                default:
                    img = "@drawable/empty_dice";
                    res = results[0];
//                    diceImage.setImageResource(R.drawable.empty_dice);
//                    Toast.makeText(this, "four", Toast.LENGTH_SHORT).show();
            }





            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5000ms
                    int imageResource = getResources().getIdentifier(img, null, getPackageName());
                    diceImage.setImageResource(imageResource);
                    Toast.makeText(AppActivity.this, res, Toast.LENGTH_SHORT).show();

                    if (started)
                        addItem(lastResult);

                    started = true;
                    lastResult = res;
                    currentResult.setText(res);
                }
            }, 600);

        });
    }

}