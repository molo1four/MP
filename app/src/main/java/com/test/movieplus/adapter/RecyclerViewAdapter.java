package com.test.movieplus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.movieplus.model.Movies;
import com.test.movieplus.R;
import com.test.movieplus.utils.Utils;

import java.util.ArrayList;

//마지막으로 어갭터에 우리가 만든 뷰홀더를 연결합니다.
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    //멥버변수 셋팅
    Context context;
    ArrayList<Movies> moviesArrayList;

    //1. 생성자 만들기
    public RecyclerViewAdapter(Context context, ArrayList<Movies> moviesArrayList) {
        this.context = context;
        this.moviesArrayList = moviesArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mcard_row, parent, false);
        // 리턴에 위에서 생성한 뷰를 뷰홀더에 담아서 리턴한다.
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, final int position) {
        final Movies movies = moviesArrayList.get(position);
        int id = movies.getId();
        String title = movies.getTitle();
        String year = movies.getYear();
        String url = Utils.BASE_IMG_URL + movies.getPhoto_url();
        holder.txtTitle.setText(title);
        holder.txtYear.setText(year);
        Glide.with(context).load(url).into(holder.imgUrl);

        holder.switch1.setOnCheckedChangeListener(null);
        holder.switch1.setSelected(movies.isChecked());
        holder.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.switch1.setChecked(true);
                    movies.setChecked(true);
                    Log.i("ssibal",""+moviesArrayList.get(10).isChecked()+"      ///     "+position);
                }else{
                    holder.switch1.setChecked(false);
                    movies.setChecked(false);
                    Log.i("ssibal",""+isChecked+"      ///     "+position);
                }
            }
        });
        if(movies.isChecked()){
            holder.switch1.setChecked(true);
        } else {
            holder.switch1.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtYear;
        ImageView imgUrl;
        Switch switch1;
        public CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.textTitle);
            txtYear = itemView.findViewById(R.id.textYear);
            imgUrl = itemView.findViewById(R.id.imgUrl);
            switch1 = itemView.findViewById(R.id.switch1);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }

    public ArrayList<Movies> getMoviesArrayList() {
        return moviesArrayList;
    }
}
