package kavita.myappcompany.recomendation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kavita.myappcompany.recomendation.Adapters.HomeAdapter;
import kavita.myappcompany.recomendation.Adapters.MoodAdapter;
import kavita.myappcompany.recomendation.JSONPlaceholderAPI.JSONPlaceHolderAPI;
import kavita.myappcompany.recomendation.POJOClasses.MoviesTOP;
import kavita.myappcompany.recomendation.POJOClasses.Result;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DramaFragment extends Fragment {

    private RecyclerView MoodRV;

    private List<Result> MoodArrayList;

    BottomNavigationView bottomNavigation;
    public String mood;
    int mood_id;
    private Button hackInfo;
    JSONPlaceHolderAPI jsonPlaceHolderAPI;
    MoodAdapter moodAdapter;


    private boolean isLoading = false, isLastPage = false;
    private int page = 1;

    int cacheSize = 10 * 1024 * 1024;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_drama, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.HomeAppBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigation = getActivity().findViewById(R.id.bottom_nav_bar);
        MoodArrayList = new ArrayList<Result>();

//get the spinner from the xml.
        Spinner dropdown = view.findViewById(R.id.spinner1);
//create a list of items for the spinner.
        String[] items = new String[]{"Sad", "Love", "Fear", "Anticipation", "Happy"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mood = dropdown.getSelectedItem().toString();
                MoodArrayList.clear();
                moodAdapter.notifyDataSetChanged();
                caching();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        MoodRV = view.findViewById(R.id.RVMood);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        MoodRV.setLayoutManager(layoutManager);
        moodAdapter = new MoodAdapter(getContext(), MoodArrayList);
        MoodRV.setAdapter(moodAdapter);


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void caching() {

        Interceptor onlineInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                int maxAge = 60; // read from cache for 60 seconds even if there is internet connection
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            }
        };
        Cache cache = new Cache(getContext().getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Interceptor.Chain chain)
                            throws IOException {
                        Request request = chain.request();
                        if (!isNetworkAvailable()) {
                            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                            request = request
                                    .newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                })
                .addNetworkInterceptor(onlineInterceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        jsonPlaceHolderAPI = retrofit.create(JSONPlaceHolderAPI.class);
        getMoviesMode();

    }


    public void getMoviesMode() {
        //homeAdapter.showProgress();

        switch (mood) {
            case "Sad":
                mood_id = 10751;
                break;
            case "Love":
                mood_id = 10749;
                break;
            case "Fear":
                mood_id = 27;
                break;
            case "Anticipation":
                mood_id = 80;
                break;
            case "Happy":
                mood_id = 35;
        }


        Call<MoviesTOP> call5 = jsonPlaceHolderAPI.getModeMovies("8492690b2520941df875011c9c0a9f8a", mood_id);

        call5.enqueue(new Callback<MoviesTOP>() {
            @Override
            public void onResponse(Call<MoviesTOP> call5, Response<MoviesTOP> response5) {


                if (!response5.isSuccessful()) {
                    Log.i("not sucess5", "code: " + response5.code());

                    return;
                }

                MoviesTOP moviesTOP = (MoviesTOP) response5.body();

                List<Result> final_objs = moviesTOP.getResults();


                moodAdapter.setMoodList(final_objs);


            }

            @Override
            public void onFailure(Call<MoviesTOP> call5, Throwable t) {
                Log.i("failed5", t.getMessage());


            }
        });
    }
}