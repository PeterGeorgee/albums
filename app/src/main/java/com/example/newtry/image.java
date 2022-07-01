package com.example.newtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class image extends AppCompatActivity {

    ImageView myImg;
    Button mybtn;

    photoObj myPhoto;

    BitmapDrawable drawable;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        myImg=findViewById(R.id.myImage);
        mybtn=findViewById(R.id.mybutton);

        Intent intent=getIntent();

        if(intent.getExtras() != null){
            myPhoto= (photoObj) intent.getSerializableExtra("pho");
            Picasso.get().load(myPhoto.getUrl()).into(myImg);
        }

        Zoomy.Builder builder=new Zoomy.Builder(image.this).target(myImg);

        builder.register();

        mybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });
    }

    private void shareImage() {
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        drawable=(BitmapDrawable) myImg.getDrawable();
        bitmap=drawable.getBitmap();
        File file=new File(getExternalCacheDir()+"/"+"myPhoto"+".png");
        Intent intent;
        try{
            FileOutputStream outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            intent=new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        startActivity(Intent.createChooser(intent,"Share Image Via:"));
    }
}