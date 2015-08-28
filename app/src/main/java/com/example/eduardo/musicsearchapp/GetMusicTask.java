package com.example.eduardo.musicsearchapp;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Eduardo on 8/27/2015.
 *
 * This method handles the retrieval of the data resulting from querying the user and utilizing
 * the iTunes API.
 */

public class GetMusicTask extends AsyncTask <String, Void, Void> {
    ArrayList<MusicItem> musicList;
    TaskInterface tf;

    public GetMusicTask(TaskInterface mainActivity) {
        musicList = new ArrayList<MusicItem>();
        tf = mainActivity;
    }

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

            String downloadedTextData = sb.toString();

            JSONObject jObject = new JSONObject(downloadedTextData);
            JSONArray musicArray = jObject.getJSONArray("results");

            for (int i = 0; i < musicArray.length(); i++) {

                JSONObject jo = musicArray.getJSONObject(i);

                String track = jo.getString("trackName");
                String artist = jo.getString("artistName");
                String album = jo.getString("collectionName");
                String imageUrl = jo.getString("artworkUrl100");


                MusicItem item = new MusicItem(track, artist, album, imageUrl);
                musicList.add(item);

            }

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

        tf.drawListItems(musicList);

    }
}
