package com.example.questionandanswer.view;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.questionandanswer.R;
import com.example.questionandanswer.model.ApiService;
import com.example.questionandanswer.model.SharedPrefManager;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

public class SettingActivity extends AppCompatActivity {
    public static final int mREQUEST_CODE_TAKE_PICTURE = 1;
    private ImageView imgProfile;
    private Bitmap bitmapImgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        String url = getIntent().getStringExtra("URL_PROFILE");
        imgProfile = findViewById(R.id.img_profile);
        if (!url.equals("null")) {
            Picasso.get().load("http://192.168.1.4:8000/storage/" + url).into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.default_profile);
        }
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingActivity.this);

            }
        });
        final SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        final ApiService apiService = new ApiService(SettingActivity.this);
        Button uploadProfile = findViewById(R.id.upload_profile);
        uploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.uploadImageProfile(sharedPrefManager.getUserID(), bitmapImgProfile, new ApiService.OnRecieviedImg() {
                    @Override
                    public void onRecieved(boolean state) {
                        if (state) {
                            Toast.makeText(SettingActivity.this, "true", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingActivity.this, "false", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imgProfile.setImageURI(resultUri);
                try {
                    bitmapImgProfile = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
