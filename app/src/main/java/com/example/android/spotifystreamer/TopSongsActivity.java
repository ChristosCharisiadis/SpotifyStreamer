package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
Activity that finds the top 10 songs of a selected artist.
 */
public class TopSongsActivity extends ActionBarActivity {
    private final String LOG_TAG = TopSongsActivity.class.getSimpleName();
    private ArrayList<Song> songs;
    private ListView mListView;
    private SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_songs);
        Intent intent = this.getIntent();
        mListView = (ListView) findViewById(R.id.top_songs_list);
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String artistId = intent.getStringExtra(Intent.EXTRA_TEXT); //get the current artist's ID
            SearchSongsTask search = new SearchSongsTask();
            search.execute(artistId);
            songs = new ArrayList<>();
            try {
                songs = search.get();
            } catch (Exception e) {
                Log.d(LOG_TAG, e.toString());
            }
            if (songs.size() == 0) {
                Toast.makeText(getBaseContext(),"No Songs found for this artist.", Toast.LENGTH_LONG).show();
            }
            else {
                adapter = new SongAdapter(getBaseContext(), songs);
                mListView.setAdapter(adapter);
            }
        }
    }

    public class SearchSongsTask extends AsyncTask<String, Void, ArrayList<Song>> {
        @Override
        protected void onPostExecute(ArrayList<Song> songs) {
            super.onPostExecute(songs);
        }

        @Override
        protected ArrayList<Song> doInBackground(String... params) {
            if (params.length == 0)
            {
                return null;
            }
            String artistId = params[0];
            return getTopSongs(artistId);
        }

        private ArrayList<Song> getTopSongs(String artistId) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            final String BASE_URL = "https://api.spotify.com/v1/artists/";
            try {
                URL url = new URL(BASE_URL + artistId + "/top-tracks?country=SE");
                //example URL https://api.spotify.com/v1/search?q=firstname+lastname&type=artist
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            }
            catch (Exception e){
                Log.e(LOG_TAG, "Error ", e);
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            //parse Json String and collect data from it
            try
            {
                return getSongDataFromJson(forecastJsonStr);
            }
            catch (JSONException e)
            {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<Song> getSongDataFromJson(String forecastJsonStr) throws JSONException {
            ArrayList<Song> songsList = new ArrayList<>();
            JSONObject jsonObj = new JSONObject(forecastJsonStr);
            JSONArray songs = jsonObj.getJSONArray("tracks");
            for (int i = 0; i < songs.length(); i++) {
                JSONObject jsonSong = songs.getJSONObject(i);
                String trackName = jsonSong.getString("name");
                String url = jsonSong.getString("preview_url");
                JSONObject jsonAlbum = jsonSong.getJSONObject("album");
                String albumName = jsonAlbum.getString("name");
                String bigImage = null;
                String smallImage = null;
                JSONArray images = jsonAlbum.getJSONArray("images");
                if (images.length() > 0) bigImage = images.getJSONObject(0).getString("url");
                if (images.length() > 1) smallImage = images.getJSONObject(1).getString("url");
                songsList.add(new Song(trackName, albumName, bigImage, smallImage, url));
            }
            return songsList;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_songs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
