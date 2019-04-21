package com.example.questionandanswer.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.questionandanswer.R;
import com.example.questionandanswer.model.Answers;
import com.example.questionandanswer.model.ApiService;
import com.example.questionandanswer.model.SharedPrefManager;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class QuestionContentActivity extends AppCompatActivity {
    boolean isImageFullScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_content);


        final ApiService apiService = new ApiService(this);
        Button btn_send_answer = findViewById(R.id.send_answer);
        TextView txt_answer = findViewById(R.id.txt_anaswer_content);

        final int QuestionId= getIntent().getIntExtra("ID_QUESTION",-1);
        String title= getIntent().getStringExtra("TITLE");
        String content = getIntent().getStringExtra("CONTENT");
        final String url= getIntent().getStringExtra("IMG_URL");

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(this);

        TextView txt_title = findViewById(R.id.title_c);
        TextView txt_content = findViewById(R.id.content_c);
        final ImageView img_content = findViewById(R.id.img_question_c);


        if(url!=null)
            Picasso.get().load("http://192.168.1.4:8000/storage/"+url).into(img_content);
        else
            img_content.setVisibility(View.GONE);




        img_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFullScreen) {
                    isImageFullScreen=false;
                    img_content.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                    img_content.setAdjustViewBounds(true);
                }else{
                    isImageFullScreen=true;
                    img_content.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
                    img_content.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });

        final EditText et_answer = findViewById(R.id.txt_anaswer_content);

        txt_title.setText(title);
        txt_content.setText(content);

        btn_send_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.sendAnswer(QuestionId, 45, et_answer.getText().toString(), new ApiService.PostCommand() {
                    @Override
                    public void Status(int state) {
                        if(state == 1){
                            Toast.makeText(QuestionContentActivity.this, "thanks", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(QuestionContentActivity.this, "no", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        apiService.getAnwers(42, new ApiService.OnReceivedAnswers() {
            @Override
            public void Revivied(ArrayList<Answers> answers) {
                RecyclerView recyclerView = findViewById(R.id.rec_answers);
                AdapterAnswers adapterQuestion=new AdapterAnswers(QuestionContentActivity.this,answers);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                recyclerView.setAdapter(adapterQuestion);
            }
        });
    }
}
