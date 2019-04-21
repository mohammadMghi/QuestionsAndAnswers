package com.example.questionandanswer.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.questionandanswer.R;
import com.example.questionandanswer.model.ApiService;
import com.example.questionandanswer.model.SharedPrefManager;

import java.io.IOException;

public class NewQuestion extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        final TextView title = findViewById(R.id.txt_title);
        final TextView content = findViewById(R.id.txt_content);
        Button send = findViewById(R.id.btn_send);
        Button btn_add = findViewById(R.id.add_img);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        final ApiService apiService = new ApiService(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    bitmap = null;
                }

                SharedPrefManager sharedPrefManager = new SharedPrefManager(NewQuestion.this);

                apiService.sendQuestion(sharedPrefManager.getUserID(), title.getText().toString(), content.getText().toString(), bitmap, new ApiService.OnRecivedStatusQuestion() {
                    @Override
                    public void Status(boolean state) {
                        if (state) {
                            Toast.makeText(NewQuestion.this, "ok", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NewQuestion.this, "no", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.img_selected);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
