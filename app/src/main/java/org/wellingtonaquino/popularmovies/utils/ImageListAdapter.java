package org.wellingtonaquino.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import org.wellingtonaquino.popularmovies.BuildConfig;
import org.wellingtonaquino.popularmovies.R;
import org.wellingtonaquino.popularmovies.dataobjects.MovieDO;

import java.util.List;


/**
 * Created by wellingtonaquino on 7/3/16.
 */
public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<MovieDO> movieDOs;
    private String[] imageUrls;

    public ImageListAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.list_item_movie, imageUrls);
        this.context = context;
        this.imageUrls = imageUrls;
        inflater = LayoutInflater.from(context);
    }
    public ImageListAdapter(Context context, List<MovieDO> movieDOs) {
        super(context, R.layout.list_item_movie, movieDOs);
        this.context = context;
        this.movieDOs = movieDOs;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.list_item_movie, parent, false);
        }

            Picasso
                    .with(context)
                    //.load(imageUrls[position])
                    .load(buildImageUrl("300", movieDOs.get(position).getPosterPath()))
                    .fit()
                    .placeholder(R.drawable.ic_sync_black_24dp)
                    .into((ImageView) convertView);

        return convertView;
    }

    /*
    * the Width of the images could change to other than 300
    * confirm with /configuration api on the movie app db
    * */
    public String buildImageUrl(String width, String imagePath){
        final String MOVIES_BASE_IMAGE_URL= "http://image.tmdb.org/t/p/";
        final String APPID_PARAM = "api_key";
        Uri builtUri = Uri.parse(MOVIES_BASE_IMAGE_URL).buildUpon()
                .appendPath("w"+width)
                .appendEncodedPath(imagePath)
                .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
        return builtUri.toString();
    }

    public void updateData(List<MovieDO> movies){
        this.movieDOs.clear();
        this.movieDOs.addAll(movies);
        this.notifyDataSetChanged();
    }
}