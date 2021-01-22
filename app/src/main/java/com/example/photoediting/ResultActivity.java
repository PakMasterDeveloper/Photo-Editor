package com.example.photoediting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.photoediting.databinding.ActivityResultBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.android.BuildConfig;

public class ResultActivity extends AppCompatActivity {
    ActivityResultBinding activityResultBinding;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResultBinding=ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(activityResultBinding.getRoot());
        getSupportActionBar().hide();
        activityResultBinding.image.setImageURI(getIntent().getData());
        interstitialAd=new InterstitialAd(ResultActivity.this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544~3347511713");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (interstitialAd.isLoaded())
                {
                    interstitialAd.show();
                }
            }
        });
        activityResultBinding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, getIntent().getData());
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        activityResultBinding.whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Image for Whats App");
                whatsappIntent.putExtra(Intent.EXTRA_STREAM, getIntent().getData());
                whatsappIntent.setType("image/*");
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                   startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ResultActivity.this, "WhatsApp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        activityResultBinding.facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebook=new Intent(Intent.ACTION_SEND);
                facebook.setType("text/plain");
                facebook.setPackage("com.facebook");
                facebook.putExtra(Intent.EXTRA_TEXT,"Image for FaceBook");
                facebook.putExtra(Intent.EXTRA_TEXT,getIntent().getData());
                facebook.setType("image/*");
                facebook.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                try {
                   startActivity(facebook);
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(ResultActivity.this, "FaceBook have not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        activityResultBinding.instagrambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebook=new Intent(Intent.ACTION_SEND);
                facebook.setType("text/plain");
                facebook.setPackage("com.instagram");
                facebook.putExtra(Intent.EXTRA_TEXT,"Image for Instagram");
                facebook.putExtra(Intent.EXTRA_TEXT,getIntent().getData());
                facebook.setType("image/*");
                facebook.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                try {
                    startActivity(facebook);
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(ResultActivity.this, "Instagram have not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}