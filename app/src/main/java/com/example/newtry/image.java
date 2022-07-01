package com.example.newtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.squareup.picasso.Picasso;

public class image extends AppCompatActivity {

    ImageView myImg;

    photoObj myPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        myImg=findViewById(R.id.myImage);

        Intent intent=getIntent();

        if(intent.getExtras() != null){
            myPhoto= (photoObj) intent.getSerializableExtra("pho");
            Picasso.get().load(myPhoto.getUrl()).into(myImg);
        }

        Zoomy.Builder builder=new Zoomy.Builder(image.this).target(myImg);

        builder.register();
    }
}