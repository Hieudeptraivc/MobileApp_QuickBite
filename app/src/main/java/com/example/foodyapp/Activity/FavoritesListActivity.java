package com.example.foodyapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyapp.Adapter.FavoriteListAdapter;
import com.example.foodyapp.Domain.Foods;
import com.example.foodyapp.R;
import com.example.foodyapp.databinding.ActivityFavoritesListBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoritesListActivity extends BaseActivity {
    private ActivityFavoritesListBinding binding;
    private RecyclerView.Adapter adapterFavList;
    private final ArrayList<Foods> foodsFavList = new ArrayList<>();
    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        nav = findViewById(R.id.navFav_menu);


        setVariable();
        loadFavFood();
        initScrollScreen();
        initBottomNavigate();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nav.getSelectedItemId() != R.id.favorites_nav) {
            nav.setSelectedItemId(R.id.favorites_nav);
        }
    }
    private void checkEmptyCart() {
        if (foodsFavList.isEmpty()) {
            binding.progressBarFavListFood.setVisibility(View.GONE);
            binding.emptyFavListText.setVisibility(View.VISIBLE);
            binding.scrollViewFavList.setVisibility(View.GONE);
        } else {
            binding.emptyFavListText.setVisibility(View.GONE);
            binding.progressBarFavListFood.setVisibility(View.GONE);
            binding.scrollViewFavList.setVisibility(View.VISIBLE);

        }
    }

    private void loadFavFood() {
        DatabaseReference ref = database.getReference("Users");
        binding.progressBarFavListFood.setVisibility(View.VISIBLE);

        ref.child(auth.getUid()).child("Favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        foodsFavList.clear();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String foodId = "" + ds.child("foodId").getValue();
                            Foods foods = new Foods();
                            foods.setId(Integer.parseInt(foodId));
                            foodsFavList.add(foods);
                        }

                        checkEmptyCart();

                        if (!foodsFavList.isEmpty()) {

                            binding.recycerViewFavList.setLayoutManager(new GridLayoutManager(FavoritesListActivity.this, 2));
                            adapterFavList = new FavoriteListAdapter(FavoritesListActivity.this, foodsFavList);
                            binding.recycerViewFavList.setAdapter(adapterFavList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.progressBarFavListFood.setVisibility(View.GONE);
                        binding.emptyFavListText.setVisibility(View.VISIBLE);
                        binding.scrollViewFavList.setVisibility(View.GONE);
                        Toast.makeText(FavoritesListActivity.this, "Lỗi khi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initScrollScreen(){

        binding.scrollViewFavList.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    binding.navFavMenu.animate().translationY(binding.navFavMenu.getHeight()+50).setDuration(100);
                } else if (scrollY < oldScrollY) {
                    binding.navFavMenu.animate().translationY(0).setDuration(100);
                }
            }
        });
    }
    private void initBottomNavigate() {
        nav.setSelectedItemId(R.id.favorites_nav);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.cart_nav) {
                    startActivity(new Intent(getApplicationContext(), CartActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    overridePendingTransition(0,0);
                    return true;
                } else if (itemId == R.id.home_nav) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    overridePendingTransition(0,0);
                    return true;
                }else if (itemId == R.id.profile_nav) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    overridePendingTransition(0,0);
                    return true;
                }else if(itemId == R.id.favorites_nav){
                    startActivity(new Intent(getApplicationContext(), FavoritesListActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    overridePendingTransition(0,0);
                    return  true;
                }
                return false;
            }
        });
    }
    private void setVariable() {
        binding.backCartBtn.setOnClickListener(view -> finish());
    }

}