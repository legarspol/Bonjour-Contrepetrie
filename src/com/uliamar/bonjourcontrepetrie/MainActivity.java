package com.uliamar.bonjourcontrepetrie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import uk.co.senab.photoview.PhotoViewAttacher;


public class MainActivity extends Activity {
    GridView gridview;
    ImageAdapter adaptateur;
    ImageView image;
    FrameLayout imageContainer;
    Boolean hasRefreshed = false;
    final String SAVED_STATE_KEY_REFRESH = "hasRefresh";
    final String IMAGE_DISPLAYED_FULLSCREEN_KEY_REFRESH = "imageToDisplayFullSize";
    private PullToRefreshLayout mPullToRefreshLayout;
    String imageDisplayedFullSize = "";
    PhotoViewAttacher mAttacher;


    public final static String BASE_URL = "http://uliamar.com/h/contre/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setEmptyView(findViewById(R.id.emptyList));
        adaptateur = new ImageAdapter(this, new ArrayList<Contre>());
        gridview.setAdapter(adaptateur);
        gridview.setOnItemClickListener(myClickListener);

        imageContainer = (FrameLayout) findViewById(R.id.pictureContainer);
        image = (ImageView) findViewById(R.id.image);
        image.setImageResource(R.drawable.l);
        mAttacher = new PhotoViewAttacher(image);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                hidePicure(null);
            }
        });



        setDataFromDB();


        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);

        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(this)
                // Mark All Children as pullable
                .allChildrenArePullable()
                        // Set a OnRefreshListener
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        refresh();
                    }

                })
                        // Finally commit the setup to our PullToRefreshLayout
                .setup(mPullToRefreshLayout);

        if (savedInstanceState != null) {
            hasRefreshed = savedInstanceState
                    .getBoolean(SAVED_STATE_KEY_REFRESH);
            imageDisplayedFullSize = savedInstanceState.getString(IMAGE_DISPLAYED_FULLSCREEN_KEY_REFRESH);

        }
        if (hasRefreshed == false) {
            refresh();
            mPullToRefreshLayout.setRefreshing(true);
        }
        if (!imageDisplayedFullSize.equals("")) {
            displayImageFullScreen();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_STATE_KEY_REFRESH, hasRefreshed);
        outState.putString(IMAGE_DISPLAYED_FULLSCREEN_KEY_REFRESH, imageDisplayedFullSize);
    }

    @Override
    public void onBackPressed() {
        if (imageDisplayedFullSize.equals("")) {
            super.onBackPressed();
        } else {
            hidePicure(null);
        }
    }

    public void hidePicure(View v) {

        imageContainer.setVisibility(FrameLayout.GONE);
        this.imageDisplayedFullSize = "";
    }

    private void setDataFromDB() {
        new DBLoader().execute();

    }

    private class DBLoader extends AsyncTask<Void, Void, List<Contre>> {

        @Override
        protected List<Contre> doInBackground(Void... arg0) {
            return Contre.find(Contre.class, "", new String[0], "",
                    "date DESC", "");

        }

        protected void onPostExecute(List<Contre> contres) {
            adaptateur.update(contres);
        }

    }

    public void displayImageFullScreen() {
        String url = imageDisplayedFullSize;
        Log.v("Picasso", "on load l'image: " + url);
        Picasso.with(MainActivity.this).load(url).into(image);
        mAttacher.update();
        imageContainer.setVisibility(FrameLayout.VISIBLE);
    }


    private OnItemClickListener myClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            String url = BASE_URL + Contre.findById(Contre.class, id).getImg();
            imageDisplayedFullSize = url;
            displayImageFullScreen();
        }

    };

    private void refresh() {
        Log.v("", "Find last date in DB");
        List<Contre> contres = Contre.find(Contre.class, "", new String[0], "",
                "date DESC", "1");
        if (contres.size() != 0) {
            Log.v("", "last date found in DB: " + contres.get(0).getDate());

            getRecentContre(contres.get(0).getDate());
        } else {
            Log.v("", "No date found retrieve everything");

            getAllContre();
        }
    }

    public void getRecentContre(int date) {
        ContreRestClient.getRecentContre(date, MyHandler);
    }

    private void getAllContre() {
        ContreRestClient.getAllContre(MyHandler);
    }

    private JsonHttpResponseHandler MyHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(JSONArray userJson) {
            Log.v("sdf", "il y a " + userJson.length()
                    + " obj dans l'array lors de la recup");
            Log.v("sdfsdf", userJson.toString());
            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT)
                    .show();
            for (int i = 0; i < userJson.length(); ++i) {
                try {
                    JSONObject jsonContre = ((JSONObject) userJson.get(i));
                    Contre contre = new Contre(MainActivity.this,
                            jsonContre.getString("title"),
                            jsonContre.getString("link"),
                            jsonContre.getString("img"),
                            jsonContre.getInt("date"));

                    contre.save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            hasRefreshed = true;
            if (userJson.length() != 0) {
                setDataFromDB();
            }
            mPullToRefreshLayout.setRefreshComplete();

        }

        @Override
        public void onFailure(Throwable e, JSONObject errorResponse) {
            super.onFailure(e, errorResponse);

            Toast.makeText(MainActivity.this, "Unable to update",
                    Toast.LENGTH_SHORT).show();
            mPullToRefreshLayout.setRefreshComplete();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
