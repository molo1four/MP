package com.test.movieplus.adapter;

import android.content.Context;
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
import com.test.movieplus.R;
import com.test.movieplus.model.Movies;
import com.test.movieplus.utils.Utils;

import java.util.ArrayList;

//마지막으로 어갭터에 우리가 만든 뷰홀더를 연결합니다.
public class Recom_RecyclerViewAdapter  extends RecyclerView.Adapter<Recom_RecyclerViewAdapter.ViewHolder> {
    //멥버변수 셋팅
    Context context;
    ArrayList<Movies> moviesArrayList;

    public void setMoviesArrayList(ArrayList<Movies> moviesArrayList) {
        this.moviesArrayList = moviesArrayList;
    }

    //1. 생성자 만들기
    public Recom_RecyclerViewAdapter(Context context, ArrayList<Movies> moviesArrayList) {
        this.context = context;
        this.moviesArrayList = moviesArrayList;
    }

    @NonNull
    @Override
    public Recom_RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mcard_recom_row, parent, false);
        // 리턴에 위에서 생성한 뷰를 뷰홀더에 담아서 리턴한다.
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final Recom_RecyclerViewAdapter.ViewHolder holder, final int position) {
        final Movies movies = moviesArrayList.get(position);
        int id = movies.getId();
        String title = movies.getTitle();
        String year = movies.getYear();
        String url = Utils.BASE_IMG_URL + movies.getPhoto_url();
        holder.txtTitle.setText(title);
        holder.txtYear.setText(year);
        Glide.with(context).load(url).into(holder.imgUrl);

    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtYear;
        ImageView imgUrl;
        public CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.textTitle);
            txtYear = itemView.findViewById(R.id.textYear);
            imgUrl = itemView.findViewById(R.id.imgUrl);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }

    public ArrayList<Movies> getMoviesArrayList() {
        return moviesArrayList;
    }
}
