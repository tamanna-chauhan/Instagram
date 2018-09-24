package com.acadview.instagram;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Story extends AppCompatActivity {

    ImageView storyImage;
    private int position = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storyImage = findViewById(R.id.storyImage);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        handler = new Handler();

        setImage(position);

        startHandler(position);
    }

    private void startHandler(final int pos) {
        this.position = pos;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                setImage(position);

                if(position < 7){
                    position++;
                    startHandler(position);
                }else{
                    finish();
                }
            }
        },2000);

    }

    private void setImage(int position) {

        switch (position) {
            case 0:
                Glide.with(this).load(R.drawable.imga).into(storyImage);

                break;

            case 1:
                Glide.with(this).load(R.drawable.imgaa).into(storyImage);
                break;

            case 2:
                Glide.with(this).load(R.drawable.imgb).into(storyImage);
                break;

            case 3:
                Glide.with(this).load(R.drawable.imgbb).into(storyImage);
                break;

            case 4:
                Glide.with(this).load(R.drawable.imgc).into(storyImage);
                break;
            case 5:
                Glide.with(this).load(R.drawable.imgcc).into(storyImage);
                break;
            case 6:
                Glide.with(this).load(R.drawable.imga).into(storyImage);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        handler.removeCallbacksAndMessages(null);
        finish();

    }
}

