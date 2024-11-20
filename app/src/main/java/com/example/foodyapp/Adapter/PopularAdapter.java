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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodyapp.Activity.DetailActivity;
import com.example.foodyapp.Domain.Foods;
import com.example.foodyapp.R;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.viewholder> {
    ArrayList<Foods> items;
    Context context;




    public PopularAdapter(ArrayList<Foods> items){
        this.items = items;
    }
    @NonNull
    @Override
    public PopularAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular_foods,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.viewholder holder, int position) {
        holder.title_text.setText(items.get(position).getTitle());
        holder.price_text.setText(String.format("$%.2f", items.get(position).getPrice())); // Định dạng giá trị giá cả thành chuỗi
        holder.time_text.setText(String.format("%d min", items.get(position).getTimeValue())); // Đảm bảo thời gian là chuỗi
        holder.star_text.setText(String.valueOf(items.get(position).getStar())); // Chuyển đổi giá trị star thành chuỗi

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
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

    public static class viewholder extends RecyclerView.ViewHolder{
        TextView title_text, price_text,star_text,time_text;
        ImageView pic;
        public viewholder(@NonNull View itemView){
            super(itemView);
            title_text= itemView.findViewById(R.id.title_text);
            price_text= itemView.findViewById(R.id.price_text);
            time_text= itemView.findViewById(R.id.time_text);
            star_text= itemView.findViewById(R.id.star_text);
            pic= itemView.findViewById(R.id.pic);


        }
    }
}
