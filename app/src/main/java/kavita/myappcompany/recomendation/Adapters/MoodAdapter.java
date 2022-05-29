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

public class MoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Result> MoodArrayList;
    private int id;

    public ImageView Profilephoto_home;

    public MoodAdapter(Context context, List<Result> moodArrayList) {
        this.context = context;
        MoodArrayList = moodArrayList;
    }


    public void setMoodList(List<Result> final_objs) {
        notifyDataSetChanged();
        int position = this.MoodArrayList.size();
        this.MoodArrayList.addAll(final_objs);
        notifyItemRangeInserted(position, final_objs.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // if(viewType == VIEW_TYPE_ITEM)
        return new MoodAdapter.Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_rv_layout, parent, false));
        // else
        //     return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MoodAdapter.Viewholder)
            populateItem((MoodAdapter.Viewholder) holder, position);

    }

    private void populateItem(MoodAdapter.Viewholder holder, int position) {
        Result result = MoodArrayList.get(position);


        holder.HackName.setText(MoodArrayList.get(position).getTitle());

        holder.teamSizeMin.setText(String.valueOf(MoodArrayList.get(position).getOriginalLanguage()));
        holder.startDate.setText(MoodArrayList.get(position).getReleaseDate());
        holder.endDate.setText("1");

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/" + MoodArrayList.get(position).getPosterPath())
                .placeholder(R.drawable.placeholder)
                .into(Profilephoto_home);


        holder.itemView.setOnClickListener(v -> {
            id = MoodArrayList.get(position).getId();
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

        });
    }

    @NonNull
    @Override
    public int getItemCount() {

        return MoodArrayList.size();
    }


    public void clearList() {
        MoodArrayList.clear();
        MoodArrayList.add(null);
    }


    public class Viewholder extends RecyclerView.ViewHolder {


        private TextView HackName, teamSizeMin, teamSizeMax, startDate, endDate;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Profilephoto_home = itemView.findViewById(R.id.HackImage);
            HackName = itemView.findViewById(R.id.MovieName);
            teamSizeMin = itemView.findViewById(R.id.teamSize__min_input);

            startDate = itemView.findViewById(R.id.startDate_input);
            endDate = itemView.findViewById(R.id.endDate_input);


        }
    }


}
