package com.example.foodyapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.foodyapp.Domain.Foods;
import com.example.foodyapp.Helper.ManagmentCart;
import com.example.foodyapp.R;
import com.example.foodyapp.databinding.ActivityDetailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DetailActivity extends BaseActivity {
    private ActivityDetailBinding binding;
    private final Context context = this;
    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;
    private String foodId;
    private boolean isInMyFavoriteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        getIntentExtra();
        checkIsFavorite();
        setVariable();


    }
    private  void checkIsFavorite(){

        DatabaseReference ref = database.getReference("Users");
        ref.child(auth.getUid()).child("Favorites").child(foodId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isInMyFavoriteList = snapshot.exists();
                        if (isInMyFavoriteList){
                            binding.btnLike.setBackgroundResource(R.drawable.favorite_red);
                        }else{
                            binding.btnLike.setBackgroundResource(R.drawable.favorite_white);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void addToFavoriteList() {
        if (auth.getCurrentUser()==null){
            Toast.makeText(context, "You are not logged in", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("foodId", foodId);

            DatabaseReference ref = database.getReference("Users");
            ref.child(auth.getUid()).child("Favorites").child(foodId)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Added to your favorites list...", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to add to favorites due to"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void removeFromFavoriteList(){
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
    private void setVariable() {
        managmentCart = new ManagmentCart(this);
        binding.backBtn.setOnClickListener(view -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        binding.priceText.setText(String.format("$%.2f", object.getPrice()));
        binding.titleText.setText(object.getTitle());
        binding.descriptionText.setText(object.getDescription());
        binding.rateText.setText(String.format("%.1f Rating", object.getStar()));
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalText.setText(String.format("$%.2f", num * object.getPrice()));


        binding.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num +=1;
                binding.numText.setText(String.format("%d", num));
                binding.totalText.setText(String.format("$%.2f", num * object.getPrice()));
            }
        });
        binding.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num-=1;
                binding.numText.setText(String.format("%d", num));
                binding.totalText.setText(String.format("$%.2f", num * object.getPrice()));
            }
        });
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.setNumberInCart(num);
                managmentCart.insertFood(object);
            }
        });
        binding.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(auth.getCurrentUser() == null){
                    Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
                }else{
                    if(isInMyFavoriteList){
                        removeFromFavoriteList();
                    }else {
                        addToFavoriteList();
                    }
                }
            }
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
        foodId = String.valueOf(object.getId());
        if (object == null) {
            Toast.makeText(this, "Dữ liệu món ăn bị thiếu!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}