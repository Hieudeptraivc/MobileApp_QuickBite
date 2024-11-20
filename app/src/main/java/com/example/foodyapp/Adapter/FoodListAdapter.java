package com.example.foodyapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodyapp.Activity.DetailActivity;
import com.example.foodyapp.Domain.Foods;
import com.example.foodyapp.R;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.viewholder> {
    ArrayList<Foods> items;
    Context context;

    public FoodListAdapter(ArrayList<Foods> items){
        this.items = items;
    }

    @NonNull
    @Override
    public FoodListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_food,parent,false );
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.viewholder holder, int position) {
        holder.title_text.setText(items.get(position).getTitle());
        holder.price_text.setText(String.format("$%.2f", items.get(position).getPrice())); // Định dạng giá trị giá cả thành chuỗi
        holder.time_text.setText(String.format("%d min", items.get(position).getTimeValue())); // Đảm bảo thời gian là chuỗi
        holder.rate_text.setText(String.valueOf(items.get(position).getStar())); // Chuyển đổi giá trị star thành chuỗi

        Glide.with(context)
                .load(items.get(position).getImagePath())

                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object",items.get(position));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    //////
    public static class viewholder extends RecyclerView.ViewHolder{
        TextView title_text,price_text,rate_text,time_text;
        ImageView pic;
        public viewholder(@NonNull View itemView){
            super(itemView);
            title_text = itemView.findViewById(R.id.title_text);
            price_text = itemView.findViewById(R.id.price_text);
            rate_text = itemView.findViewById(R.id.rate_text);
            time_text = itemView.findViewById(R.id.time_text);
            pic = itemView.findViewById(R.id.img_foodFav);



        }
    }
}
