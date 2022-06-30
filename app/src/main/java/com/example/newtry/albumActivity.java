package com.example.newtry;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class albumActivity extends AppCompatActivity {

    GridView gridView;

    String url="https://jsonplaceholder.typicode.com/photos";

    String result="";

    int AlbID;

    List<String> photosURLS=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Intent intent=getIntent();
        AlbID=Integer.parseInt(intent.getStringExtra("myID"));


        gridView=findViewById(R.id.myGrid);


        new getJson().execute();

    }

    class getJson extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL phurl=new URL(url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) phurl.openConnection();

                InputStreamReader streamReader=new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader phreader=new BufferedReader(streamReader);
                StringBuilder phBuilder=new StringBuilder();

                String line;
                while ((line=phreader.readLine())!=null){
                    phBuilder.append(line);
                }
                result=phBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray=new JSONArray(s);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    if(Integer.parseInt(jsonObject.getString("albumId"))==AlbID){
                        photosURLS.add(jsonObject.getString("url"));
                    }
                }
                Log.e("my urls", String.valueOf(photosURLS));

//                ArrayAdapter adapter=new ArrayAdapter(albumActivity.this, android.R.layout.simple_list_item_1,photosURLS);
                CustomAdapter adapter=new CustomAdapter(albumActivity.this);
                gridView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class CustomAdapter extends BaseAdapter{

        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(Context context) {
            this.context = context;
            this.layoutInflater=(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return photosURLS.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view=layoutInflater.inflate(R.layout.rowitem,viewGroup,false);
            }

            ImageView imageView=view.findViewById(R.id.imgView);
//            imageView.setImageURI(photosURLS.get(i));
//            try {
//                HttpURLConnection connection=(HttpURLConnection).openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                imageView.setImageBitmap(myBitmap);

                Picasso.get().load(photosURLS.get(i)).into(imageView);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            return view;
        }
    }
}