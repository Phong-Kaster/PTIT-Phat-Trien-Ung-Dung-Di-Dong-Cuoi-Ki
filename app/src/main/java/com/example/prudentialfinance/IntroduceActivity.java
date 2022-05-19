package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prudentialfinance.Activities.Auth.LoginActivity;
import com.example.prudentialfinance.Adapter.SliderAdapter;
import com.example.prudentialfinance.Model.GlobalVariable;

public class IntroduceActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;

    private TextView[] mDots;

    private int currentPage;

    AppCompatButton nextBtn;
    TextView skipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        setControl();
        setEvent();




    }
    private void setEvent() {
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);


        mSlideViewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
                currentPage = position;

                if(currentPage == mDots.length - 1 ){
                    nextBtn.setText(R.string.start);
                }else{
                    nextBtn.setText(R.string.next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        GlobalVariable state = ((GlobalVariable) this.getApplication());
        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), this.MODE_PRIVATE);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage == mDots.length - 1) {
                    preferences.edit().putBoolean("isFirstOpen", false).apply();
                    StartLogin();
                }else{
                    mSlideViewPager.setCurrentItem(currentPage + 1);
                }
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putBoolean("isFirstOpen", false).apply();
                StartLogin();
            }
        });

    }

    private void StartLogin(){
        Intent i = new Intent(IntroduceActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void setControl() {
        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        nextBtn = (AppCompatButton) findViewById(R.id.introduceButtonNext);
        skipBtn = (TextView) findViewById(R.id.introduceButtonSkip);


    }

    private void addDotsIndicator(int position){
        mDots = new TextView[2];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        if(position > -1 && position < mDots.length){
            mDots[position].setTextColor(getResources().getColor(R.color.colorTheme));
        }
    }

}