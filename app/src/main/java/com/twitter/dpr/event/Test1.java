package com.twitter.dpr.event;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Location;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;



public class Test1 extends AppCompatActivity {

    ListView listview;
    SearchView searchView;
    String location;
    String llocation="india";
    String p="india";

    List<String> adapterString;
    List<String> sportstring;

    String trendd="";
    String sports="sports";
    Integer flag;

    ArrayAdapter arrayadapter;
    ProgressBar progressBar;
    public static final String hashh="key";
    public  static String hashh1="";
    String tag="";
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "http:shivamgupta1012.pythonanywhere.com/api/trends?hash=";
    public String gethash;
    public static String gethash1;

    DatabaseHandler database;
    String tableName ;


    // @SuppressLint("StaticFieldLeak")
    @SuppressLint("StaticFieldLeak")
    class searchAndUpdate extends AsyncTask<String, Void, String>{

        private String location;

        @Override
        protected String doInBackground(String... strings) {
            this.location = strings[0];
            String str = "";
            try{
                QueryResult result = null;

                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey("sLG34b6Aw8GeVeleTLFfBTwEE")
                        .setOAuthConsumerSecret("CZ7tXwN9SVJLrNvqKdwENXsx7WsG7uqMviEwRy2OIYgRzdfAC0")
                        .setOAuthAccessToken("827466163135328256-RXKfcNa89JgSSzUxToKl1TqkSeSkfLR")
                        .setOAuthAccessTokenSecret("OtBMx3uD5H0gSWyP61FRtglaJFNIP6K4ZZ2fbmHN7oLYo");
                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();

                Integer idTrendLocation = getTrendLocationId(Uri.encode(strings[0]));

                if (idTrendLocation == null) {
                    return "Error";
                }

                Trends trends = twitter.getPlaceTrends(idTrendLocation);
                for (int i = 0; i < trends.getTrends().length; i++) {
                    str += trends.getTrends()[i].getName() + ":::::";

                }

//

            } catch (Exception e) {
                e.printStackTrace();
            }
//            String str = "";
//            if (result != null) {
//                for (twitter4j.Status status : result.getTweets()) {
//
////                       System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
//                    str = str + status.getUser().getScreenName().toString() + ":" + status.getText().toString();
//
//                }
//
//            }
//
            return str;
        }

        private Integer getTrendLocationId(String locationName) {

            int idTrendLocation = 0;

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
                    if (location.getName().toLowerCase().equals(locationName.toLowerCase())) {
                        idTrendLocation = location.getWoeid();
                        break;
                    }
                }

                if (idTrendLocation > 0) {
                    return idTrendLocation;
                }

                return null;

            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to get trends: " + te.getMessage());
                return null;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapterString.clear();
            arrayadapter.notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("Error"))
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Error finding the location.", Toast.LENGTH_SHORT).show();
            }
            else{
                String[] strList = s.split(":::::");

                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                Date date = new Date();
                String dateString = df.format(date);

                progressBar.setVisibility(View.INVISIBLE);
                for(String str : strList){


                    Tweet tweet = new Tweet(location.toLowerCase(), dateString, str);
                    database.addEntry(tweet,tableName);
                    adapterString.add(str);
                    arrayadapter.notifyDataSetChanged();


                }

            }

        }

    }
    @SuppressLint("StaticFieldLeak")
    private class GetJson extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Test1.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            //String jsonStr = sh.makeServiceCall(url+hashh1);
            String jsonStr = sh.makeServiceCall(url+hashh1);


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    gethash = jsonObj.getString("classified");
                    gethash1=gethash;
                    if(gethash1.equals("sports"))
                    {
                        flag=1;
                    }




                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();

                        }
                    });
                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                        gethash="";
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


            //Toast.makeText(getApplicationContext(),gethash,Toast.LENGTH_LONG).show();





        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        adapterString = new ArrayList<String>();
        searchView = (SearchView) findViewById(R.id.srch1);
        listview = (ListView) findViewById(R.id.listview);
        arrayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapterString);
        listview.setAdapter(arrayadapter);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        database = new DatabaseHandler(this, "TweetsDatabase", 1 );
        tableName = "TweetTable";
        database.createTable(tableName);
        Intent intent = getIntent();

        // Receiving User Email Send By MainActivity.
         //p = intent.getStringExtra(PlacesAvailable.Loc);
        if(intent.getStringExtra(PlacesAvailable.Loc)==null)
        {
            llocation="india";
        }
        else{
            String p=intent.getStringExtra(PlacesAvailable.Loc);
            String[] pp=p.split("\\ \\(");
            String g=pp[0];

            llocation=g.toLowerCase();

        }
        new searchAndUpdate().execute(llocation.toLowerCase());

        Toast.makeText(getApplicationContext(), llocation, Toast.LENGTH_SHORT).show();
        arrayadapter.notifyDataSetChanged();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String hash = parent.getItemAtPosition(position).toString();
                hashh1 = hash;
                String s="#";
                if(hashh1.charAt(0) == s.charAt(0))
                {
                    hashh1=hashh1.substring(1,hashh1.length());

                }
                new GetJson().execute();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(Test1.this, SplashSports.class);
                        // intent.putExtra(hashh, hashh1);
                        startActivity(intent);

                    }
                }, 3500);



            }

        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {


                location = query;
                new searchAndUpdate().execute(location.toLowerCase());

                Toast.makeText(getApplicationContext(), location, Toast.LENGTH_SHORT).show();
                arrayadapter.notifyDataSetChanged();

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String hash = parent.getItemAtPosition(position).toString();
                        hashh1 = hash;
                        String s="#";
                        if(hashh1.charAt(0) == s.charAt(0))
                        {
                            hashh1=hashh1.substring(1,hashh1.length());

                        }
                        new GetJson().execute();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(Test1.this, SplashSports.class);
                               // intent.putExtra(hashh, hashh1);
                                startActivity(intent);

                            }
                        }, 3500);


                    }

                });



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected


            case R.id.recent_search:
                Intent intent = new Intent(this,RecentSearch.class);
                startActivity(intent);
                break;

            case R.id.places:
                intent = new Intent(this, PlacesAvailable.class);
                startActivity(intent);
                break;


            default:
                break;
        }

        return true;
    }

}


