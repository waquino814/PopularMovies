package org.wellingtonaquino.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wellingtonaquino.popularmovies.dataobjects.MovieDO;
import org.wellingtonaquino.popularmovies.utils.ImageListAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private ImageListAdapter imageListAdapter = null;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imageListAdapter = ImageListAdapter.getInstance();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            int position = intent.getIntExtra(Intent.EXTRA_TEXT,-1);
            MovieDO movie = imageListAdapter.getMovieByPosition(position);
            ((TextView) rootView.findViewById(R.id.txtTitle)).setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.textOverView)).setText(movie.getOverview());
            ImageView imgPoster = (ImageView) rootView.findViewById(R.id.imgPoster);
            ImageListAdapter.loadImageIntoImageView(getContext(),imgPoster,ImageListAdapter.buildImageUrl("300",movie.getPosterPath()));
        }
        return rootView;
    }
}
