package com.example.engab.movie_app.fragment;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import static com.example.engab.movie_app.Keys.KEY_AUTH;
import static com.example.engab.movie_app.Keys.KEY_CONTENT;
import static com.example.engab.movie_app.Keys.KEY_DUR;
import static com.example.engab.movie_app.Keys.KEY_NAME;
import static com.example.engab.movie_app.Keys.KEY_TUBE;
import static com.example.engab.movie_app.Url.BASE_URL;
import static com.example.engab.movie_app.Url.KEY;
import static com.example.engab.movie_app.Url.PARAM_MOV;
import static com.example.engab.movie_app.Url.PARAM_REVS;
import static com.example.engab.movie_app.Url.PARAM_TRAIL;
import static com.example.engab.movie_app.Url.PARAM_TR_API;
import static com.example.engab.movie_app.fragment.MoviesFragment.EX;
import static com.example.engab.movie_app.fragment.MoviesFragment.TAG;
import static com.example.engab.movie_app.fragment.MoviesFragment.adapter;
import static com.example.engab.movie_app.fragment.MoviesFragment.fav;

import com.example.engab.movie_app.adapter.CoreAdapter;
import com.example.engab.movie_app.DBHelper;
import com.example.engab.movie_app.Duration;
import com.example.engab.movie_app.HttpManager;
import com.example.engab.movie_app.Movie;
import com.example.engab.movie_app.R;
import com.example.engab.movie_app.Review;
import com.example.engab.movie_app.Trailer;
import com.example.engab.movie_app.Url;
import com.example.engab.movie_app.adapter.ReviewsAdapter;
import com.example.engab.movie_app.adapter.TrailersAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by engab on 22-Apr-16.
 */
public class DetailFragment extends Fragment
{
    public static ListView trailerList;
    public static ImageView detailFav, detailUnFav;
    private DBHelper helper;
    private List<Trailer> trailers = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private CardView detailCard;
    private ImageView detailPoster, tabBackdrop;
    private TextView detailDate, detailVoteAvr, detailOverview,
            noTrails, noRevs, detailDur, detailTitle;
    private TrailersAdapter trailsAdapter;
    private ReviewsAdapter revsAdapter;
    private LinearLayout reviewList;
    private String titleEndPoint, idEndpoint, posterEndPoint,
            backdropEndPoint, overviewEndPoint, dateEndPoint,
            voteEndPoint, year;
    private Movie movie;
    private int position;
    public DetailFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);
        detailCard = (CardView) v.findViewById(R.id.detailCard);
        detailPoster = (ImageView) v.findViewById(R.id.detailPoster);
        tabBackdrop = (ImageView) v.findViewById(R.id.tabBackdrop);
        detailFav = (ImageView) v.findViewById(R.id.detailFav);
        detailUnFav = (ImageView) v.findViewById(R.id.detailUnFav);
        detailDate = (TextView) v.findViewById(R.id.detailDate);
        detailVoteAvr = (TextView) v.findViewById(R.id.detailVoteAvr);
        detailOverview = (TextView) v.findViewById(R.id.detailOverview);
        detailTitle = (TextView) v.findViewById(R.id.detailTitle);
        trailerList = (ListView) v.findViewById(R.id.detail_trailers);
        noTrails = (TextView) v.findViewById(R.id.noTrails);
        noRevs = (TextView) v.findViewById(R.id.noRevs);
        reviewList = (LinearLayout) v.findViewById(R.id.revsList);
        detailDur = (TextView) v.findViewById(R.id.detailDur);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        helper = new DBHelper(getActivity());
        Configuration conf = getActivity().getResources().getConfiguration();
        if (conf.smallestScreenWidthDp >= 600)
        {
            if (bundle != null) {
                movie = bundle.getParcelable("movie");
                position = bundle.getInt("pos");
                if (movie != null)
                {
                    posterEndPoint = movie.getPoster();
                    backdropEndPoint = movie.getBackdrop();
                    overviewEndPoint = movie.getOverview();
                    dateEndPoint = movie.getDate();
                    voteEndPoint = movie.getVoteAvr();
                    titleEndPoint = movie.getTitle();
                    idEndpoint = movie.getId();
                    if (dateEndPoint.equals(""))
                    {
                        year = "Unknown";
                    }
                    else
                    {
                        year = dateEndPoint.substring(0, 4);
                    }
                    if (titleEndPoint.equals(""))
                    {
                        titleEndPoint = "Not available.";
                    }
                    else
                    {
                        titleEndPoint = movie.getTitle();
                    }
                    if (overviewEndPoint.equals(""))
                    {
                        overviewEndPoint = "No overview found.";
                    }
                    else
                    {
                        overviewEndPoint = movie.getOverview();
                    }
                }
            }
        }
        else
        {
            posterEndPoint = getActivity().getIntent().getExtras().getString("poster");
            backdropEndPoint = getActivity().getIntent().getExtras().getString("backdrop");
            overviewEndPoint = getActivity().getIntent().getExtras().getString("overview");
            dateEndPoint = getActivity().getIntent().getExtras().getString("date");
            voteEndPoint = getActivity().getIntent().getExtras().getString("vote");
            titleEndPoint = getActivity().getIntent().getExtras().getString("title");
            idEndpoint = getActivity().getIntent().getExtras().getString("id");
            position = getActivity().getIntent().getExtras().getInt("position");
        }
        boolean ifExist = helper.ifExist(idEndpoint);
        if (ifExist)
        {
            detailUnFav.setVisibility(VISIBLE);
            detailFav.setVisibility(GONE);
        }
        else
        {
            detailUnFav.setVisibility(GONE);
            detailFav.setVisibility(VISIBLE);
        }
        String posterUrl = Url.POSTERS_URL + "w342" + posterEndPoint;
        DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .displayer(new FadeInBitmapDisplayer(1500))
                .showImageOnFail(R.drawable.no_poster)
                .showImageForEmptyUri(R.drawable.no_poster)
                .build();
        ImageLoader.getInstance().displayImage(
                posterUrl,
                detailPoster,
                mOptions,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        Palette p = Palette.from(loadedImage).generate();
                        detailCard.setCardBackgroundColor(p.getVibrantColor(0));
                        if (p.getVibrantColor(0) == Color.TRANSPARENT)
                        {
                            detailCard.setCardBackgroundColor(p.getMutedColor(0));
                        }
                    }
                });

        String backdropUrl = Url.POSTERS_URL + "w780" + backdropEndPoint;
        if (conf.smallestScreenWidthDp >= 600)
        {
            ImageLoader.getInstance().displayImage(
                    backdropUrl,
                    tabBackdrop,
                    mOptions,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            Palette p = Palette.from(loadedImage).generate();
                            tabBackdrop.setBackgroundColor(p.getVibrantColor(0));
                            if (p.getVibrantColor(0) == Color.TRANSPARENT)
                            {
                                tabBackdrop.setBackgroundColor(p.getMutedColor(0));
                            }
                        }
                    });
            detailDate.setText(year);
            detailTitle.setText(titleEndPoint);
        }
        else
        {
            detailDate.setText(dateEndPoint);
        }
        detailVoteAvr.setText(voteEndPoint);
        detailOverview.setText(overviewEndPoint);

        final ScaleAnimation animation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(165);

        detailFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                detailFav.setVisibility(GONE);
                detailUnFav.startAnimation(animation);
                detailUnFav.setVisibility(VISIBLE);
                helper.insertRow(posterEndPoint, backdropEndPoint, idEndpoint,
                        titleEndPoint, dateEndPoint, voteEndPoint,
                        overviewEndPoint);
            }
        });

        detailUnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                detailUnFav.setVisibility(GONE);
                detailFav.startAnimation(animation);
                detailFav.setVisibility(VISIBLE);
                helper.deleteRow(idEndpoint);
                if (fav.isChecked())
                {
                    CoreAdapter.movies.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        new DurationTask().execute();

        trailsAdapter = new TrailersAdapter(trailers, getActivity());
        new TrailersTask().execute();

        revsAdapter = new ReviewsAdapter(reviews, getActivity());
        new ReviewsTask().execute();
    }

    public static void fix(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(
                        desiredWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private class TrailersTask extends AsyncTask<String, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(String... params)
        {
            String mainUrl = BASE_URL + PARAM_MOV + "/" + idEndpoint
                    + PARAM_TRAIL + PARAM_TR_API + KEY;
            String response = HttpManager.getJSON(mainUrl);
            try {
                if (response != null) {
                    JSONObject object = new JSONObject(response);
                    JSONArray arr = object.getJSONArray("results");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jObj = arr.getJSONObject(i);
                        Trailer trailer = new Trailer();
                        trailer.setName(jObj.getString(KEY_NAME));
                        trailer.setKey(jObj.getString(KEY_TUBE));
                        trailers.add(trailer);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG + " Trail" + EX, e.getMessage());
            }
            return trailers;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            trailsAdapter.notifyDataSetChanged();
            trailerList.setAdapter(trailsAdapter);
            fix(trailerList);
            if (trailers.isEmpty()) {
                noTrails.setVisibility(VISIBLE);
            }
        }
    }

    private class ReviewsTask extends AsyncTask<String, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(String... params) {
            String mainUrl = BASE_URL + PARAM_MOV + "/" + idEndpoint
                    + PARAM_REVS + PARAM_TR_API + KEY;
            String response = HttpManager.getJSON(mainUrl);
            try {
                if (response != null) {
                    JSONObject object = new JSONObject(response);
                    JSONArray arr = object.getJSONArray("results");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jObj = arr.getJSONObject(i);
                        Review review = new Review();
                        review.setName(jObj.getString(KEY_AUTH));
                        review.setComment(jObj.getString(KEY_CONTENT));
                        reviews.add(review);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG + " Revs" + EX, e.getMessage());
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            for (int i = 0; i < revsAdapter.getCount(); i++) {
                View view = revsAdapter.getView(i, null, null);
                reviewList.addView(view);
            }
            revsAdapter.notifyDataSetChanged();
            if (reviews.isEmpty()) {
                noRevs.setVisibility(VISIBLE);
            }
        }
    }

    private class DurationTask extends AsyncTask<Void, Void, Void> {
        StringBuilder builder;

        @Override
        protected Void doInBackground(Void... params) {
            String mainUrl = BASE_URL + PARAM_MOV + "/" + idEndpoint + PARAM_TR_API + KEY;
            String response = HttpManager.getJSON(mainUrl);
            try {
                if (response != null) {
                    JSONObject object = new JSONObject(response);
                    Duration duration = new Duration();
                    duration.setDuration(object.getString(KEY_DUR));
                    builder = new StringBuilder();
                    String dur = duration.getDuration();
                    boolean con = dur.equals("null") || dur.equals("0");
                    if (con) {
                        builder.append("Not available.");
                    } else {
                        builder.append(duration.getDuration()).append(" Min.");
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG + " Dur" + EX, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            detailDur.setText(builder);
        }
    }
}
