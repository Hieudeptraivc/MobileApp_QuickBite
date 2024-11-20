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
import com.example.foodyapp.Activity.CartActivity;
import com.example.foodyapp.Activity.ListFoodsActivity;
import com.example.foodyapp.Domain.Category;
import com.example.foodyapp.Domain.Foods;
import com.example.foodyapp.Helper.ChangeNumberItemsListener;
import com.example.foodyapp.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    ArrayList<Category> items;
    Context context;




    public CategoryAdapter(ArrayList<Category> items){
        this.items = items;
    }


    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, int position) {
        holder.title_text.setText(items.get(position).getName());
        holder.pic.setBackgroundResource(R.drawable.cat1_background);
        switch (position){
            case 0: {
                holder.pic.setBackgroundResource(R.drawable.cat1_background);
                break;
            }
            case 1: {
                holder.pic.setBackgroundResource(R.drawable.cat2_background);
                break;
            }
            case 2: {
                holder.pic.setBackgroundResource(R.drawable.cat3_background);
                break;

            }
            case 3: {
                holder.pic.setBackgroundResource(R.drawable.cat4_background);
                break;

            }
            case 4: {
                holder.pic.setBackgroundResource(R.drawable.cat5_background);
                break;

            }
            case 5: {
                holder.pic.setBackgroundResource(R.drawable.cat6_background);
                break;

            }case 6: {
                holder.pic.setBackgroundResource(R.drawable.cat7_background);
                break;

            }case 7: {
                holder.pic.setBackgroundResource(R.drawable.cat8_background);
                break;

            }


        }
        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListFoodsActivity.class);
                intent.putExtra("CategoryId",items.get(position).getId());
                intent.putExtra("CategoryName",items.get(position).getName());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder{
        TextView title_text;
        ImageView pic;
        public viewholder(@NonNull View itemView){
            super(itemView);
            title_text= itemView.findViewById(R.id.catName_text);
            pic= itemView.findViewById(R.id.imgCat);


        }
    }
}
