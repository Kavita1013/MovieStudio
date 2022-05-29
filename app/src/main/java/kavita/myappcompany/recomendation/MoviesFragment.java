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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import kavita.myappcompany.recomendation.Adapters.HomeAdapter;
import kavita.myappcompany.recomendation.Adapters.MovieAdapter;
import kavita.myappcompany.recomendation.JSONPlaceholderAPI.JSONPlaceHolderAPI;
import kavita.myappcompany.recomendation.POJOClasses.MovieSimilar;
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


public class MoviesFragment extends Fragment {

    private RecyclerView MoviesRV;
    TextInputEditText MovieTitle;
    Button Search;
    private List<String> MoviesArrayList;

    BottomNavigationView bottomNavigation;

    private String idToken = "Bearer ";

    JSONPlaceHolderAPI jsonPlaceHolderAPI;
    MovieAdapter movieAdapter;


    int cacheSize = 10 * 1024 * 1024;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.HomeAppBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return v;
    }




    /*public void onStart() {
        super.onStart();
        idToken = MainActivity.getIdToken();

    }*/


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigation = getActivity().findViewById(R.id.bottom_nav_bar);


        MoviesArrayList = new ArrayList<>();

        MovieTitle = view.findViewById(R.id.textInputEditText);
        Search = view.findViewById(R.id.MoviesSearch);
        MoviesRV = view.findViewById(R.id.RVMovies);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        MoviesRV.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(getContext(), MoviesArrayList);
        MoviesRV.setAdapter(movieAdapter);


        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Here a logging interceptor is created
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                //The logging interceptor will be added to the http client
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(logging);
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("https://recommenderml.azurewebsites.net/")
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create(gson));
                Retrofit retrofit = builder.build();


                jsonPlaceHolderAPI = retrofit.create(JSONPlaceHolderAPI.class);
                getMovies();
            }
        });


        Log.i("callback problem", "error");

    }


    public void getMovies() {

        //homeAdapter.showProgress();
        /*Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://recommenderml.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        jsonPlaceHolderAPI = retrofit.create(JSONPlaceHolderAPI.class);*/

        Log.i("Movies", String.valueOf(MovieTitle.getText()));

        Call<HashMap<Integer, String>> call3 = jsonPlaceHolderAPI.getSimilarMovies(new MovieSimilar(String.valueOf(MovieTitle.getText())));

        call3.enqueue(new Callback<HashMap<Integer, String>>() {
            @Override
            public void onResponse(Call<HashMap<Integer, String>> call3, Response<HashMap<Integer, String>> response5) {
                if (!response5.isSuccessful()) {
                    Toast.makeText(getContext(), "Check Spelling And Capitalisation of each word", Toast.LENGTH_LONG).show();
                    Log.i("Movies", "This didnt work");
                    return;
                }
                HashMap<Integer, String> map = new HashMap<Integer, String>();
                map = response5.body();
                Log.i("hashmap", String.valueOf(map));
                List<String> list = new ArrayList<String>(map.values());

                movieAdapter.setMovieList(list);
                Log.i("hashmap", String.valueOf(list));
                MoviesArrayList = list;

                Log.i("response", String.valueOf(response5.body()));

            }

            @Override
            public void onFailure(Call<HashMap<Integer, String>> call3, Throwable t) {
                Toast.makeText(getContext(), "Sorry movie not found!!", Toast.LENGTH_LONG).show();
                Log.i("failed5", t.getMessage());

            }
        });
    }

}