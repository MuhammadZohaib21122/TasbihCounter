package com.example.tasbeeh;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView .ViewHolder{


    TextView tvwazaif;

    public CardView cardView;
    public MyViewHolder(@NonNull View itemView) {

        super(itemView);
        tvwazaif = itemView . findViewById(R.id.tvwazaif);
        cardView = itemView . findViewById(androidx.core.R.id.notification_main_column_container);


    }
}
