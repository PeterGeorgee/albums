package com.example.newtry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class albumActivity extends AppCompatActivity {

    GridView gridView;

    String url="https://jsonplaceholder.typicode.com/photos";

    String result="";

    int AlbID;

    List<String> photosURLS=new ArrayList<String>();

    List<photoObj> myphotos=new ArrayList<photoObj>();

    CustomAdapter adapter;

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
                        photoObj photo=new photoObj(jsonObject.getString("title"),jsonObject.getString("url"));
                        myphotos.add(photo);
//                        photosURLS.add(jsonObject.getString("url"));
                    }
                }
                Log.e("my urls", String.valueOf(myphotos.get(1).getUrl()));

//                ArrayAdapter adapter=new ArrayAdapter(albumActivity.this, android.R.layout.simple_list_item_1,photosURLS);
                adapter=new CustomAdapter(albumActivity.this,myphotos);
                gridView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search_view);
        SearchView searchView= (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.search_view){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class CustomAdapter extends BaseAdapter implements Filterable {

        private Context context;
        private LayoutInflater layoutInflater;

        private List<photoObj> photosList;
        private List<photoObj> photosListFiltered;


        public CustomAdapter(Context context,List<photoObj> photosList) {
            this.context = context;
            this.layoutInflater=(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.photosList=photosList;
            this.photosListFiltered=photosList;

        }

        @Override
        public int getCount() {
            return photosListFiltered.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view=layoutInflater.inflate(R.layout.rowitem,viewGroup,false);
            }

            ImageView imageView=view.findViewById(R.id.imgView);

            Log.e("i'm here",myphotos.get(i).getUrl());
            Picasso.get().load(photosListFiltered.get(i).getUrl()).into(imageView);


            return view;
        }

        @Override
        public Filter getFilter() {
            Filter filter=new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults=new FilterResults();

                    if(charSequence==null || charSequence.length()==0){
                        filterResults.count=photosList.size();
                        filterResults.values=photosList;
                    }else{
                        String searchStr=charSequence.toString().toLowerCase();
                        List<photoObj> resultData=new ArrayList<photoObj>();

                        for (photoObj photo:photosList){
                            if(photo.getTitle().contains(searchStr)){
                                resultData.add(photo);
                            }
                            filterResults.count=resultData.size();
                            filterResults.values=resultData;
                        }

                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    photosListFiltered= (List<photoObj>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }
}