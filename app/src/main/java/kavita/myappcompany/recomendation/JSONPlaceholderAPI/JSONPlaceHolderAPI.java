package kavita.myappcompany.recomendation.JSONPlaceholderAPI;

import java.util.HashMap;

import kavita.myappcompany.recomendation.POJOClasses.MovieSimilar;
import kavita.myappcompany.recomendation.POJOClasses.MoviesDetails.MoviesDetails;
import kavita.myappcompany.recomendation.POJOClasses.MoviesTOP;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONPlaceHolderAPI {


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("3/movie/top_rated")
    Call<MoviesTOP> getTopMovies(@Query("api_key") String api_key);

    @GET("movie/{movie_id}")
    Call<MoviesDetails> getParticularMovie(@Path("movie_id") String id, @Query("api_key") String api_key);

    @GET("discover/movie")
    Call<MoviesTOP> getModeMovies(@Query("api_key") String api_key,@Query("with_genre") int id);

    @POST("predict")
    Call<HashMap<Integer, String>> getSimilarMovies(@Body MovieSimilar body);

}
