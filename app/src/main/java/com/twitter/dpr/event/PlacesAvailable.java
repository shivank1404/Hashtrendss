package com.twitter.dpr.event;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class PlacesAvailable extends AppCompatActivity {

    ListView listview;
    String location;
    List<String> adapterString;
    ArrayAdapter arrayadapter;
    ProgressBar progressBar;
    public static final String Loc="";

    class getLocationAndUpdate extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String str = "";

            try {

                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey("sLG34b6Aw8GeVeleTLFfBTwEE")
                        .setOAuthConsumerSecret("CZ7tXwN9SVJLrNvqKdwENXsx7WsG7uqMviEwRy2OIYgRzdfAC0")
                        .setOAuthAccessToken("827466163135328256-RXKfcNa89JgSSzUxToKl1TqkSeSkfLR")
                        .setOAuthAccessTokenSecret("OtBMx3uD5H0gSWyP61FRtglaJFNIP6K4ZZ2fbmHN7oLYo");
                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();

                ResponseList<Location> locations = twitter.getAvailableTrends();

                for (Location location : locations) {
                        str+= location.getName() + " (" + location.getCountryName() + ") :::::";
                    }

            } catch (TwitterException te) {
                return "Error";
            }

            return str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("Error"))
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"No location available", Toast.LENGTH_SHORT).show();
            }
            else{
                String[] strList = s.split(":::::");
                progressBar.setVisibility(View.INVISIBLE);
                for(String str : strList){
                    adapterString.add(str);
                    arrayadapter.notifyDataSetChanged();
                }

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_available);

        adapterString = new ArrayList<String>();
        listview = (ListView) findViewById(R.id.listview2);
        arrayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapterString);
        listview.setAdapter(arrayadapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String hash = parent.getItemAtPosition(position).toString();
                Intent intent=new Intent(PlacesAvailable.this,Test1.class);
                intent.putExtra(Loc,hash);
                startActivity(intent);
            }

        });
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);

        new getLocationAndUpdate().execute();
    }
}
