package com.example.eduardo.musicsearchapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LyricsActivity extends Activity {

    TextView track, artist, album, lyrics;
    ImageView image;

    //This activity doesn't do a whole lot except display some of the information that is already
    //known about the song the user clicked on in the previous activity. Beyond setting the content
    //onCreate gets the lyrics from lyrics.wikia.com.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        MusicItem item = getIntent().getParcelableExtra("musicItem");

        track = (TextView) findViewById(R.id.track);
        artist = (TextView) findViewById(R.id.artist);
        album = (TextView) findViewById(R.id.album);
        lyrics = (TextView) findViewById(R.id.lyrics);
        image = (ImageView) findViewById(R.id.image);

        track.setText(item.track);
        artist.setText(item.artist);
        album.setText(item.album);

        //Again use Picasso to load album cover

        Picasso.with(this).load(item.imageUrl).into(image);

        //Execute a task to get the lyrics

        GetLyricsTask task = new GetLyricsTask();
        task.execute("http://lyrics.wikia.com/api.php?func=getSong" +
                "&artist=" + item.artist.replace(" ", "+") +
                "&song=" + item.track.replace(" ", "+") +
                "&fmt=json");


    }

    //The task executed above to get the lyrics is defined here.

    public class GetLyricsTask extends AsyncTask<String, Void, Void> {

        String someLyrics;

        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection conn = null;
            URL u;
            InputStream iStream = null;

            try {

                // Convert String to URL
                u = new URL(params[0]);

                // Created a connection using that url
                conn = (HttpURLConnection) u.openConnection();
                iStream = conn.getInputStream();
                InputStreamReader iReader = new InputStreamReader(iStream);

                BufferedReader br = new BufferedReader(iReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                String text = sb.toString();

                //Instead of parsing JSON, the data appears to be given by lyrics.wikia.com
                //in a form that requires string parsing in addition to JSON parsing to get to
                //the lyrics.

                JSONObject jo = new JSONObject(text.replace("song = ",""));
                someLyrics = jo.getString("lyrics");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (iStream != null) {
                    try {
                        iStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            lyrics.setText(someLyrics);
        }


    }
}
