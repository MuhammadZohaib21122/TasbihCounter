package com.example.tasbeeh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Item> items;

    private SelectListener listener;

    String message;


    public MyAdapter(Context context, List<Item> items, SelectListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvwazaif.setText(items.get(position).getWazaif());

        holder.tvwazaif.setOnClickListener(v -> {
            Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show();

            MyDialoge myDialog = new MyDialoge();

            myDialog.showDialogWithThreeButtons(
                    context,
                    "Item Clicked",
                    "You clicked on: " + items.get(position).getWazaif(), // Dialog message
                    listener,
                    items.get(position)
            );
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}
