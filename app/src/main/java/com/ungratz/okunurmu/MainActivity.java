package com.ungratz.okunurmu;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ungratz.okunurmu.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    //number of selected tabs in bottom menu, value is 1 by default
    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        //BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.navView, navController);

        //I am just trying stuff, will explain later -kbc
        final LinearLayout homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout chatLayout = findViewById(R.id.chatLayout);
        final LinearLayout scheduledLayout = findViewById(R.id.scheduledLayout);
        final LinearLayout searchLayout = findViewById(R.id.searchLayout);

        final ImageView homeImage = findViewById(R.id.homeImage);
        final ImageView chatImage = findViewById(R.id.chatImage);
        final ImageView scheduledImage = findViewById(R.id.scheduledImage);
        final ImageView searchImage = findViewById(R.id.searchImage);

        final TextView homeText = findViewById(R.id.homeText);
        final TextView chatText = findViewById(R.id.chatText);
        final TextView scheduledText = findViewById(R.id.scheduledText);
        final TextView searchText = findViewById(R.id.searchText);


        //set home fragment by default

        getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                                .replace(R.id.fragmentContainer, HomeFragment.class, null)
                                        .commit();
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if home is already selected
                if(selectedTab != 1){
                    //set home fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HomeFragment.class, null)
                            .commit();

                    chatText.setVisibility(View.GONE);
                    scheduledText.setVisibility(View.GONE);
                    searchText.setVisibility(View.GONE);

                    chatImage.setImageResource(R.drawable.chat_icon);
                    scheduledImage.setImageResource(R.drawable.scheduled_icon);
                    searchImage.setImageResource(R.drawable.search_icon);

                    chatLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    scheduledLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    searchLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    //home is selected
                    homeText.setVisibility(View.VISIBLE);
                    homeImage.setImageResource(R.drawable.home_icon);
                    homeLayout.setBackgroundResource(R.drawable.round_back_home);
                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f , 1.0f, 1f, 1f , Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f  );
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);

                    //set 1st tab as selected
                    selectedTab=1;

                }
            }
        });

        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 2){
                    //set chat fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ChatFragment.class, null)
                            .commit();

                    homeText.setVisibility(View.GONE);
                    scheduledText.setVisibility(View.GONE);
                    searchText.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home_icon);
                    scheduledImage.setImageResource(R.drawable.scheduled_icon);
                    searchImage.setImageResource(R.drawable.search_icon);

                    homeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    scheduledLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    searchLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    //chat is selected
                    chatText.setVisibility(View.VISIBLE);
                    chatImage.setImageResource(R.drawable.chat_icon);
                    chatLayout.setBackgroundResource(R.drawable.round_back_home);
                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f , 1.0f, 1f, 1f , Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f  );
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    chatLayout.startAnimation(scaleAnimation);

                    //set 2st tab as selected
                    selectedTab=2;

                }
            }
        });
        scheduledLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 3){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ScheduledFragment.class, null)
                            .commit();

                    homeText.setVisibility(View.GONE);
                    chatText.setVisibility(View.GONE);
                    searchText.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home_icon);
                    chatImage.setImageResource(R.drawable.chat_icon);
                    searchImage.setImageResource(R.drawable.search_icon);

                    homeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    chatLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    searchLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    //chat is selected
                    scheduledText.setVisibility(View.VISIBLE);
                    scheduledImage.setImageResource(R.drawable.chat_icon);
                    scheduledLayout.setBackgroundResource(R.drawable.round_back_home);
                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f , 1.0f, 1f, 1f , Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f  );
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    scheduledLayout.startAnimation(scaleAnimation);

                    //set 3rd tab as selected
                    selectedTab=3;

                }
            }
        });
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 4){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, SearchFragment.class, null)
                            .commit();

                    homeText.setVisibility(View.GONE);
                    chatText.setVisibility(View.GONE);
                    scheduledText.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home_icon);
                    chatImage.setImageResource(R.drawable.chat_icon);
                    scheduledImage.setImageResource(R.drawable.scheduled_icon);

                    homeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    chatLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    scheduledLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    //chat is selected
                    searchText.setVisibility(View.VISIBLE);
                    searchImage.setImageResource(R.drawable.chat_icon);
                    searchLayout.setBackgroundResource(R.drawable.round_back_home);
                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f , 1.0f, 1f, 1f , Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f  );
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    searchLayout.startAnimation(scaleAnimation);

                    //set 4th tab as selected
                    selectedTab=4;

                }
            }
        });




    }

}