package com.example.foodyapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodyapp.Activity.DetailActivity;
import com.example.foodyapp.Domain.Foods;
import com.example.foodyapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.viewholder> {

    private Context context;
    private ArrayList<Foods> listFavFood;
    private  static final String TAG = "FAV_BOOK_TAG";
    public FavoriteListAdapter(Context context, ArrayList<Foods> listFavFood) {
        this.context = context;
        this.listFavFood = listFavFood;
    }

    @NonNull
    @Override
    public FavoriteListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_listfavorite_food,parent,false);
        return new FavoriteListAdapter.viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteListAdapter.viewholder holder, int position) {
            Foods foods = listFavFood.get(position);
            LoadFoodDetail(foods,holder);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("object",listFavFood.get(position));
                    context.startActivity(intent);
                }
            });

            holder.btn_likeFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://quickbite-9284c-default-rtdb.asia-southeast1.firebasedatabase.app");
                    String foodId = String.valueOf(foods.getId());
                    if (auth.getCurrentUser()==null){
                        Toast.makeText(context, "You are not logged in", Toast.LENGTH_SHORT).show();
                    }else {
                        DatabaseReference ref = database.getReference("Users");
                        ref.child(auth.getUid()).child("Favorites").child(foodId)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Remove from your favorites list...", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to remove to favorites due to"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
    }

    private void LoadFoodDetail(Foods foods, viewholder holder ) {
        String foodId = String.valueOf(foods.getId());
        Log.d(TAG,"LoadFoodDetail: Food detail of Food Id: "+foodId);
        DatabaseReference ref = FirebaseDatabase.getInstance("https://quickbite-9284c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Foods");
        ref.child(foodId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         String foodTitle = ""+snapshot.child("Title").getValue();
                         String foodPrice = ""+snapshot.child("Price").getValue();
                         String foodRate = ""+snapshot.child("Star").getValue();
                         String foodTime = ""+snapshot.child("TimeValue").getValue();
                         String foodDescription = ""+snapshot.child("Description").getValue();
                         String foodImage = ""+snapshot.child("ImagePath").getValue();

                         ///

                        foods.setFavorite(true);
                        foods.setDescription(foodDescription);
                        foods.setStar(Double.parseDouble(foodRate));
                        foods.setTimeValue(Integer.parseInt(foodTime));
                        foods.setPrice(Double.parseDouble(foodPrice));
                        foods.setTitle(foodTitle);
                        foods.setImagePath(foodImage);


                        ////

                        holder.titleFav_text.setText(foodTitle);
                        holder.priceFav_text.setText(foodPrice);
                        holder.timeFav_text.setText(foodTime+" min");
                        holder.rateFav_text.setText(foodRate);
//                        holder.descriptionFav_text.setText(foodDescription);

                        Glide.with(context)
                                .load(foodImage)
                                .into(holder.picFav);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return listFavFood.size();
    }
    public static class viewholder extends RecyclerView.ViewHolder {
        ImageView picFav;
        TextView titleFav_text,priceFav_text,rateFav_text,timeFav_text,descriptionFav_text;
        ImageButton btn_likeFav;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleFav_text = itemView.findViewById(R.id.titleFav_text);
            priceFav_text = itemView.findViewById(R.id.priceFav_text);
            rateFav_text = itemView.findViewById(R.id.rateFav_text);
            timeFav_text = itemView.findViewById(R.id.timeFav_text);
            picFav = itemView.findViewById(R.id.img_foodFav);
            btn_likeFav = itemView.findViewById(R.id.btn_likeFav);
//            descriptionFav_text = itemView.findViewById(R.id.descriptionFav_text);

        }
    }
}
