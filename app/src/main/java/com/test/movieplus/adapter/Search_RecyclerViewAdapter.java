package com.test.movieplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.movieplus.MovieDetails;
import com.test.movieplus.R;
import com.test.movieplus.model.Movies;
import com.test.movieplus.utils.Utils;

import java.util.ArrayList;

public class Search_RecyclerViewAdapter extends RecyclerView.Adapter<Search_RecyclerViewAdapter.ViewHolder> {
    //멥버변수 셋팅
    Context context;
    ArrayList<Movies> moviesArrayList;
    String str ;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getI() {
        return str;
    }

    public void setI(String i) {
        this.str = i;
    }

    public void setMoviesArrayList(ArrayList<Movies> moviesArrayList) {
        this.moviesArrayList = moviesArrayList;
    }

    //1. 생성자 만들기
    public Search_RecyclerViewAdapter(Context context, ArrayList<Movies> moviesArrayList) {
        this.context = context;
        this.moviesArrayList = moviesArrayList;
    }

    @NonNull
    @Override
    public Search_RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mcard_search_row, parent, false);
        // 리턴에 위에서 생성한 뷰를 뷰홀더에 담아서 리턴한다.
        return new Search_RecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Search_RecyclerViewAdapter.ViewHolder holder, final int position) {
        final Movies movies = moviesArrayList.get(position);
        int id = movies.getId();
        String title = movies.getTitle();
        String year = movies.getYear();
        String overview = movies.getOverview();
        String url = Utils.BASE_IMG_URL + movies.getPhoto_url();
        String back_url = Utils.BASE_URL + movies.getBackdrop_url();
        holder.txtTitle.setText(title);
        holder.txtYear.setText(year);
        holder.txtOverview.setText(overview);
        Glide.with(context).load(url).into(holder.imgUrl);

    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtYear;
        TextView txtOverview;
        ImageView imgUrl;
        public CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.textTitle);
            txtYear = itemView.findViewById(R.id.textYear);
            txtOverview = itemView.findViewById(R.id.txtOverview);
            imgUrl = itemView.findViewById(R.id.imgUrl);
            cardView = itemView.findViewById(R.id.cardView);
            imgUrl.setAdjustViewBounds(true);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movies movies = moviesArrayList.get(getAdapterPosition());
                    Intent i = new Intent(context, MovieDetails.class);
                    i.putExtra("Movies", movies);
                    context.startActivity(i);
                }
            });

        }
    }

    public ArrayList<Movies> getMoviesArrayList() {
        return moviesArrayList;
    }
}
