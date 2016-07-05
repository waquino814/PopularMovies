package org.wellingtonaquino.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wellingtonaquino.popularmovies.dataobjects.MovieDO;
import org.wellingtonaquino.popularmovies.utils.ImageListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    ImageListAdapter mMoviesAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public void updateMovies(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        FetchMoviesTaks fetchWeatherTask = new FetchMoviesTaks();
        fetchWeatherTask.execute(sortOrder);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMoviesAdapter = ImageListAdapter.getInstance(getContext(), new ArrayList<MovieDO>());
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMoviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DetailActivity.class)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT,position);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getContext(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FetchMoviesTaks extends AsyncTask<String,Void,List<MovieDO>>{
        private final String LOG_TAG = FetchMoviesTaks.class.getSimpleName();

        @Override
        protected void onPostExecute(List<MovieDO> movieDOs) {
           // super.onPostExecute(movieDOs);
            mMoviesAdapter.updateData(movieDOs);
        }

        @Override
        protected List<MovieDO> doInBackground(String... params) {
            List<MovieDO> movies = new ArrayList<>();
            if(params.length ==0){
                return null;
            }
            String sortOrder = params[0];
            String sortPath ="popular";
            if(sortOrder.equals(getString(R.string.pref_sort_popular))){
                sortPath = "popular";
            }else if (sortOrder.equals(getString(R.string.pref_sort_top))){
                sortPath = "top_rated";
            }

            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&appid=430bceae5878ac5e838cc8042829325a");
                final String MOVIES_BASE_URL= "http://api.themoviedb.org/3/movie/";
                final String APPID_PARAM = "api_key";


                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(sortPath)
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
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
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return  null;
            } finally{
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
            try {
                return getListMoviesFromJson(moviesJsonStr);
               // return getWeatherDataFromJson(moviesJsonStr, numDays);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
    }

    public List<MovieDO> getListMoviesFromJson(String jsonString) throws JSONException{
        List<MovieDO> movies = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray result = jsonObject.getJSONArray("results");
        for (int i=0; i < result.length();i++) {
            JSONObject temp = result.getJSONObject(i);
            MovieDO movieTemp = new MovieDO();
            movieTemp.setTitle(temp.getString("title"))
                    .setId(temp.getLong("id"))
                    .setOverview(temp.getString("overview"))
                    .setPosterPath(temp.getString("poster_path"));
            movies.add(movieTemp);
        }
        return movies;
    }
}
