package com.example.questionandanswer.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.questionandanswer.R;
import com.example.questionandanswer.model.ApiService;
import com.example.questionandanswer.model.Question;
import com.example.questionandanswer.model.SharedPrefManager;
import com.example.questionandanswer.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApiService apiService = new ApiService(this);

        final DrawerLayout drawerLayout = findViewById(R.id.dr_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        FloatingActionButton floatingActionButton = findViewById(R.id.new_question);


        //get main question after login and sign up for main activity
        apiService.getMainQuestions(new ApiService.OnRecievedMainQuestion() {
            @Override
            public void Recivied(ArrayList<Question> questions) {
                RecyclerView recyclerView = findViewById(R.id.rec_main);
                AdapterQuestion adapterQuestion = new AdapterQuestion(MainActivity.this, questions);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                recyclerView.setAdapter(adapterQuestion);
            }
        });


        final SharedPrefManager sharedPrefManager = new SharedPrefManager(this);

        //get data of user after sing up or login
        apiService.getUserInfo(sharedPrefManager.token(), new ApiService.OnRecivedInfo() {
            @Override
            public void onRecivied(User user) {
                TextView username = findViewById(R.id.txtusername);
                ImageView imgprofile = findViewById(R.id.imageView);
                username.setText(user.getName());
                sharedPrefManager.saveUserID(user.getId());

                if (!(user.getURL_prifle() == "null"))
                    Picasso.get().load("http://192.168.1.4:8000/storage/" + user.getURL_prifle()).into(imgprofile);
                else
                    imgprofile.setImageResource(R.drawable.default_profile);

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_question:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_my_question:
                        Intent intent = new Intent(MainActivity.this, MyQuestions.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_contact_us:
                        Intent intent1 = new Intent(MainActivity.this, ContactusActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_contributions:
                        Intent intent2 = new Intent(MainActivity.this, ContributionsActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_log_out:
                        sharedPrefManager.clean();
                        Intent intent3 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent3);
                        finish();
                        break;
                    default:
                        break;
                }

                return true;
            }

        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewQuestion.class);
                startActivity(intent);
            }
        });

    }


}
