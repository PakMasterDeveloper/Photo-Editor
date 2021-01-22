package com.example.photoediting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.photoediting.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.ByteArrayOutputStream;
import java.io.InterruptedIOException;
import java.net.URI;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    private final static int IMAGE_REQUEST_CODE=100;
    private final static int CAMERA_REQUEST_CODE=14;
    private final static int Edit_REQUEST_CODE=200;
    private Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest=new AdRequest.Builder().build();
        activityMainBinding.adView.loadAd(adRequest);
        getSupportActionBar().hide();
        activityMainBinding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_REQUEST_CODE);
            }
        });
        activityMainBinding.cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},32);
                }
                else
                {
                    Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera,CAMERA_REQUEST_CODE);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST_CODE)
        {
            if (resultCode==RESULT_OK)
            {
                if (data!=null)
                {
                    path=data.getData();
                    EditPhoto(path);
                }
                else
                {
                    Toast.makeText(this, "Data Null", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "Result code unmatch", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==CAMERA_REQUEST_CODE)
        {
            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
            path=GetImageUri(bitmap);
            EditPhoto(path);
        }
        if(requestCode==Edit_REQUEST_CODE)
        {
            Intent intent=new Intent(MainActivity.this,ResultActivity.class);
            intent.setData(path);
            startActivity(intent);
        }
    }
    private Uri GetImageUri(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        String path=MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Title","Convert");
        return Uri.parse(path);
    }
    private  void EditPhoto(Uri uri)
    {
        Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
        dsPhotoEditorIntent.setData(uri);
        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Photo_Editor");
        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
        startActivityForResult(dsPhotoEditorIntent, Edit_REQUEST_CODE);
    }
}