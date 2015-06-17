package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


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

//TODO rotation
public class MainActivity extends ActionBarActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText mEditText;
    private ListView mListView;
    private ArtistAdapter adapter;
    private ArrayList<Artist> artists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.artist_search);
        mListView = (ListView) findViewById(R.id.artist_list);
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    SearchArtistTask search = new SearchArtistTask();
                    search.execute(mEditText.getText().toString());
                    artists = new ArrayList<>();
                    try {
                        artists = search.get();
                    } catch (Exception e) {
                        Log.d(LOG_TAG, e.toString());
                    }
                    adapter = new ArtistAdapter(getBaseContext(), artists);
                    mListView.setAdapter(adapter);
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist selectedArtist = (Artist) adapter.getItem(position);
                Intent intent = new Intent(getBaseContext(), TopSongsActivity.class).putExtra(Intent.EXTRA_TEXT, selectedArtist.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public class SearchArtistTask extends AsyncTask<String, Void, ArrayList<Artist>> {
        @Override
        protected void onPostExecute(ArrayList<Artist> artists) {

            //add stuff there
            super.onPostExecute(artists);
        }

        @Override
        protected ArrayList<Artist> doInBackground(String... params) {
            if (params.length == 0)
            {
                return null;
            }
            String name = params[0].replace(" ", "+");  //replace empty spaces with "+" so we can add the name to the url
            return getArtists(name);
        }

        private ArrayList<Artist> getArtists(String name) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            final String BASE_URL = "https://api.spotify.com/v1/search?";
            try {
                URL url = new URL(BASE_URL + "q=" + name + "&type=artist");
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
                return getArtistDataFromJson(forecastJsonStr);
            }
            catch (JSONException e)
            {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        }
        private ArrayList<Artist> getArtistDataFromJson (String forecastJsonStr) throws JSONException{
            ArrayList<Artist> artistsList = new ArrayList<>();
            JSONObject jsonObj = new JSONObject(forecastJsonStr);
            JSONObject temp = jsonObj.getJSONObject("artists");
            JSONArray artistsArray = temp.getJSONArray("items");
            for (int i = 0; i < artistsArray.length(); i++) {
                JSONObject jsonArtist = artistsArray.getJSONObject(i);
                JSONArray images = jsonArtist.getJSONArray("images");
                String name = jsonArtist.getString("name");
                String url;
                if (images.length() == 0) {
                    url = null;
                }
                else {
                    url = images.getJSONObject(0).getString("url");
                }
                String id = jsonArtist.getString("id");
                artistsList.add(new Artist(name, id, url));
            }
            return artistsList;
        }
    }
}
