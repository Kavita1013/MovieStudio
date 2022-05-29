package kavita.myappcompany.recomendation.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.jetbrains.annotations.NotNull;

import java.util.List;



import kavita.myappcompany.recomendation.MainActivity;
import kavita.myappcompany.recomendation.POJOClasses.Result;
import kavita.myappcompany.recomendation.ParticularMovieFragment;
import kavita.myappcompany.recomendation.R;


public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Result> HomeArrayList;
    private int id;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    public  ImageView Profilephoto_home;

    public HomeAdapter(Context context, List<Result> homeArrayList) {
        this.context = context;
        HomeArrayList = homeArrayList;
    }


    /*public int getItemViewType(int position) {
        if(HomeArrayList.get(position) == null)
            return VIEW_TYPE_LOADING;
        return VIEW_TYPE_ITEM;

    }*/

    public void setHackList(List<Result> final_objs) {

        int position = this.HomeArrayList.size();
        this.HomeArrayList.addAll(final_objs);
        notifyItemRangeInserted(position, final_objs.size());
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       // if(viewType == VIEW_TYPE_ITEM)
            return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_rv_layout, parent, false));
       // else
       //     return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Viewholder)
            populateItem((Viewholder) holder,position);

    }

    private void populateItem(Viewholder holder, int position) {
        Result result= HomeArrayList.get(position);


        holder.HackName.setText(HomeArrayList.get(position).getTitle());

        holder.teamSizeMin.setText(String.valueOf(HomeArrayList.get(position).getOriginalLanguage()));
        holder.startDate.setText(HomeArrayList.get(position).getReleaseDate());
        holder.endDate.setText("1");

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/"+HomeArrayList.get(position).getPosterPath())
                .placeholder(R.drawable.placeholder)
                .into(Profilephoto_home);


        holder.itemView.setOnClickListener(v -> {
            id = HomeArrayList.get(position).getId();
            ParticularMovieFragment frag = new ParticularMovieFragment();
            Log.i("hackId", String.valueOf(id));
            Bundle bundle = new Bundle();
            bundle.putString("ID", String.valueOf(id));
            frag.setArguments(bundle);

            MainActivity activity = (MainActivity) v.getContext();

            BottomNavigationView bottomNavigation = activity.findViewById(R.id.bottom_nav_bar);;
            bottomNavigation.setVisibility(View.GONE);

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null)
                    .commit();

        });
    }

    @NonNull
    @Override
    public int getItemCount() {

        return HomeArrayList.size();
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
        HomeArrayList.clear();
        HomeArrayList.add(null);
    }


    public class Viewholder extends RecyclerView.ViewHolder  {



        private TextView HackName, teamSizeMin,teamSizeMax, startDate,endDate;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Profilephoto_home=itemView.findViewById(R.id.HackImage);
            HackName=itemView.findViewById(R.id.MovieName);
            teamSizeMin=itemView.findViewById(R.id.teamSize__min_input);

            startDate=itemView.findViewById(R.id.startDate_input);
            endDate=itemView.findViewById(R.id.endDate_input);


        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NotNull View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }




}