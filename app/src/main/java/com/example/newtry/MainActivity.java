package com.example.newtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    String result="";
    String albumsResult="";
    String url="https://jsonplaceholder.typicode.com/users";
    String urlAlbum="https://jsonplaceholder.typicode.com/albums";

    TextView nameTxtView;
    TextView addTxtView;
    ListView myLv;

    Random rnd=new Random();
    int index=rnd.nextInt((9-0)+1)+0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTxtView=(TextView) findViewById(R.id.nameTxt);
        addTxtView=(TextView) findViewById(R.id.addTxt);
        myLv=(ListView) findViewById(R.id.myListView);

        new jsonTask().execute();
    }

    class jsonTask extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL myurl=new URL(url);
                URL myalburl=new URL(urlAlbum);

                HttpURLConnection urlConnection=(HttpURLConnection) myurl.openConnection();
                HttpURLConnection urlConnectionAlb=(HttpURLConnection) myalburl.openConnection();

                InputStreamReader streamReader=new InputStreamReader(urlConnection.getInputStream());
                BufferedReader reader=new BufferedReader(streamReader);
                StringBuilder builder=new StringBuilder();

                InputStreamReader streamReader1=new InputStreamReader(urlConnectionAlb.getInputStream());
                BufferedReader albreader=new BufferedReader(streamReader1);
                StringBuilder albBuilder=new StringBuilder();

                String line;
                while ((line=reader.readLine())!=null){
                    builder.append(line);
                }
                result=builder.toString();


                String albLine;
                while ((albLine=albreader.readLine())!=null){
                    albBuilder.append(albLine);
                }
                albumsResult= albBuilder.toString();
//                Log.e("My alb result",albBuilder.toString());
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
                JSONObject myobj=jsonArray.getJSONObject(index);
                nameTxtView.setText(myobj.getString("name"));

                JSONObject addObj= myobj.getJSONObject("address");

                addTxtView.setText(addObj.getString("street")+","+addObj.getString("suite")+","+addObj.getString("city")+","+addObj.getString("zipcode"));


                List<String> myAlbums = new ArrayList<String>();
                Map<String,Integer> myMap=new HashMap<String,Integer>();

                JSONArray albjsonArray=new JSONArray(albumsResult);
                for (int i=0;i<albjsonArray.length();i++){
                    JSONObject albObj=albjsonArray.getJSONObject(i);
                    if(Integer.parseInt(albObj.getString("userId"))==index+1){
                        myAlbums.add(albObj.getString("title"));
                        myMap.put(albObj.getString("title"),Integer.parseInt(albObj.getString("id")));
                    }
                }

                ArrayAdapter adapter=new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,myAlbums);
                myLv.setAdapter(adapter);

                myLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Toast.makeText(MainActivity.this,(String)myMap.get(adapterView.getAdapter().getItem(i)),Toast.LENGTH_SHORT).show();
//                        Log.e("indexxx", String.valueOf(myMap.get(adapterView.getAdapter().getItem(i))));
                        String albID=String.valueOf(myMap.get(adapterView.getAdapter().getItem(i)));
//                        Log.e("my idddd",albID);
                        Intent intent=new Intent(MainActivity.this,albumActivity.class);
                        intent.putExtra("myID",albID);
                        startActivity(intent);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}