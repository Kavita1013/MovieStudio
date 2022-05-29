package kavita.myappcompany.recomendation.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kavita.myappcompany.recomendation.MainActivity;
import kavita.myappcompany.recomendation.ParticularMovieFragment;
import kavita.myappcompany.recomendation.R;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> MovieArrayList;
    private int id;


    public MovieAdapter(Context context, List<String> movieArrayList) {
        this.context = context;
        MovieArrayList = movieArrayList;
    }
    public void setMovieList(List<String> list) {
        int position = this.MovieArrayList.size();
        this.MovieArrayList.addAll(list);
        notifyItemRangeInserted(position, list.size());

    }

    @NonNull

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new MovieAdapter.Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_rv_layout, parent, false));
    }


    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieAdapter.Viewholder)
            populateItem((MovieAdapter.Viewholder) holder, position);

    }

    private void populateItem(MovieAdapter.Viewholder holder, int position) {

        String list = MovieArrayList.get(position);


        holder.MovieName.setText(list);




        /*holder.itemView.setOnClickListener(v -> {
            id = MovieArrayList.get(position).;
            ParticularMovieFragment frag = new ParticularMovieFragment();
            Log.i("hackId", String.valueOf(id));
            Bundle bundle = new Bundle();
            bundle.putString("ID", String.valueOf(id));
            frag.setArguments(bundle);

            MainActivity activity = (MainActivity) v.getContext();

            BottomNavigationView bottomNavigation = activity.findViewById(R.id.bottom_nav_bar);
            ;
            bottomNavigation.setVisibility(View.GONE);

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null)
                    .commit();

        });*/
    }

    @NonNull

    public int getItemCount() {

        return MovieArrayList.size();
    }

    /* public void showProgress() {
         if(!HomeArrayList.contains(null)) {
             HomeArrayList.add(null);
             notifyItemInserted(HomeArrayList.size() - 1);
         }
     }

     public void removeProgress() {
         int position = HomeArrayList.indexOf(null);
         notifyItemRemoved(position);
         HomeArrayList.remove(null);
     }
 */
    public void clearList() {
        MovieArrayList.clear();
        MovieArrayList.add(null);
    }




    public class Viewholder extends RecyclerView.ViewHolder {


        private TextView MovieName, knowMore_similar;


        public Viewholder(@NonNull View itemView) {
            super(itemView);


            MovieName = itemView.findViewById(R.id.MovieName);
            knowMore_similar = itemView.findViewById(R.id.knowMore_similar);

        }
    }


}
