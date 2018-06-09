package com.twitter.dpr.event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecentSearch extends AppCompatActivity {

    String tableName;
    ListView listView;
    SearchView searchView;

    List<String> tweetList;
    ArrayAdapter<String> arrayAdapter;
    DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_search);

//        final DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext(),"TweetsDatabase",1);
        database = new DatabaseHandler(this, "TweetsDatabase",1);
        tableName = "TweetTable";
        searchView = (SearchView)findViewById(R.id.srch3);
        listView = (ListView)findViewById(R.id.listview3);
        Tweet t = new Tweet("delhi","11/10/1996","#AksSwag");
        tweetList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tweetList);
        listView.setAdapter(arrayAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                tweetList.clear();
                arrayAdapter.notifyDataSetChanged();
                database.getAllTweets(tableName, query.toLowerCase(),tweetList);

                if(tweetList.isEmpty())
                    Toast.makeText(getApplicationContext(),"Location not present in the database.",Toast.LENGTH_SHORT).show();
                arrayAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
}
