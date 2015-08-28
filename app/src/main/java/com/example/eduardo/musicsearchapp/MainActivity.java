package com.example.eduardo.musicsearchapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity implements TaskInterface, AdapterView.OnItemClickListener {

    //The url that returns a JSON file of search results, with the search term to be determined
    String url = "https://itunes.apple.com/search?term=";

    //Elements of the UI
    EditText et;
    Button b;
    ListView lv;

    //The list of song items that the user gets from their query
    ArrayList<MusicItem> musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.editText);
        b = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.listView);
    }

    //Button on first screen is wired to execute this method once clicked
    public void getSearchResults(View view){

        //Change the search query the user inputted into a proper format for combining with the
        //base url, and then launching task with the complete url

        String query = et.getText().toString();
        query = query.replace(" ", "+");

        GetMusicTask task = new GetMusicTask(this);
        task.execute(url+query);
    }

    //This method is implemented from the TaskInterface
    //Executes when retrieving the music list from the iTunes API completes

    @Override
    public void drawListItems(ArrayList<MusicItem> list) {
        this.musicList = new ArrayList<MusicItem>();
        this.musicList = list;
        MusicListAdapter adapter = new MusicListAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, LyricsActivity.class);
        intent.putExtra("musicItem", musicList.get(position));
        startActivity(intent);

    }
}
