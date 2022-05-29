package kavita.myappcompany.recomendation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import kavita.myappcompany.recomendation.JSONPlaceholderAPI.JSONPlaceHolderAPI;
import kavita.myappcompany.recomendation.POJOClasses.MoviesDetails.Genre;
import kavita.myappcompany.recomendation.POJOClasses.MoviesDetails.MoviesDetails;
import kavita.myappcompany.recomendation.POJOClasses.MoviesDetails.SpokenLanguage;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParticularMovieFragment extends Fragment {

    private TextView movieName, movieNameEng,genre,SpokenLang,status,voteAvg,popularity,ReleaseDate,description;

    private String website, imageString;
    private ImageView imageView;
    private String movieID,movieNaming;
    JSONPlaceHolderAPI jsonPlaceHolderAPI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_particular_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieName = view.findViewById(R.id.movieTitle);
        movieNameEng = view.findViewById(R.id.movieTitleEng);
        genre = view.findViewById(R.id.dateStart);
        SpokenLang = view.findViewById(R.id.dateEnd);
        popularity = view.findViewById(R.id.venue);
        ReleaseDate = view.findViewById(R.id.Date);
        description = view.findViewById(R.id.aboutHack);
        status = view.findViewById(R.id.minMem);
        voteAvg = view.findViewById(R.id.maxMem);
        imageView = view.findViewById(R.id.hackImage);
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
                .baseUrl("https://api.themoviedb.org/3/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();

        jsonPlaceHolderAPI = retrofit.create(JSONPlaceHolderAPI.class);
        Bundle bundle = this.getArguments();
        movieID = bundle.getString("ID", null);
fetchData();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottom_nav_bar);
        bottomNavigation.setVisibility(View.VISIBLE);
    }



    public void fetchData()
    {
        Call<MoviesDetails> call = jsonPlaceHolderAPI.getParticularMovie(movieID,"8492690b2520941df875011c9c0a9f8a");
        call.enqueue(new Callback<MoviesDetails>() {
            @Override
            public void onResponse(Call<MoviesDetails> call, Response<MoviesDetails> response) {
                if(!response.isSuccessful())
                {

                    Toast.makeText(getActivity(), "Error in retrieving the Data !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.body() != null) {

                    getView().findViewById(R.id.hackProfileScroll).setVisibility(View.VISIBLE);

                    Glide.with(getContext()).
                            load("https://image.tmdb.org/t/p/w300/"+response.body().getPosterPath()).
                            placeholder(R.drawable.placeholder).
                            into(imageView);
                    //imageView.setImageDrawable(Drawable.createFromPath(response.body().getPoster()));
                    imageString = response.body().getPosterPath();
                    movieNaming = response.body().getTitle();
                   movieName.setText(response.body().getOriginalTitle());
                    movieNameEng.setText(response.body().getTitle());
                    /*List<Genre> genreList= new ArrayList<>();
                    genreList= response.body().getGenres();
                    genre.setText((CharSequence) genreList.get(1));
                    List<SpokenLanguage> LangList= new ArrayList<>();
                   LangList= response.body().getSpokenLanguages();

                    SpokenLang.setText((CharSequence) LangList.get(1));*/
                    voteAvg.setText(String.valueOf(response.body().getVoteAverage()));
                    status.setText(response.body().getStatus());
                    popularity.setText(String.valueOf(response.body().getPopularity()));
                   ReleaseDate.setText(response.body().getReleaseDate());
                    description.setText(response.body().getOverview());

                }
            }

            @Override
            public void onFailure(Call<MoviesDetails> call, Throwable t) {

                Log.i("apple5",t.getMessage());
            }
        });
    }




}