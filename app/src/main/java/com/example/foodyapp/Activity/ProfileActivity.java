package com.example.foodyapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.foodyapp.R;
import com.example.foodyapp.databinding.ActivityProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends BaseActivity {
    private ActivityProfileBinding binding;
    private String TAG = "PROFILE_TAG";
    BottomNavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        nav = findViewById(R.id.navProfile_menu);

        loadUserInfo();
        initBottomNavigate();
        setVariable();
    }
    private void initBottomNavigate() {
        nav.setSelectedItemId(R.id.profile_nav);

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
            }else if (itemId == R.id.favorites_nav) {
                    startActivity(new Intent(getApplicationContext(), FavoritesListActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
    }
    private void loadUserInfo() {
        binding.progressBarProfileImageEdit.setVisibility(View.VISIBLE);
        DatabaseReference ref = database.getReference("Users");
        ref.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = ""+snapshot.child("email").getValue();
                String name = ""+snapshot.child("name").getValue();
                String password = ""+snapshot.child("password").getValue();
                String profileImage = ""+snapshot.child("profileImage").getValue();
                String uid = ""+snapshot.child("uid").getValue();

                binding.emailText.setText(email);
                binding.nameProfileText.setText(name);
                binding.profileNameText.setText(name);
                binding.passText.setText(password);
                binding.emailhText.setText(email);

                Glide.with(ProfileActivity.this)
                        .load(profileImage)
                        .placeholder(R.drawable.avatar)
                        .into(binding.profileImg);
                binding.progressBarProfileImageEdit.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nav.getSelectedItemId() != R.id.profile_nav) {
            nav.setSelectedItemId(R.id.profile_nav);
        }
    }
    private void setVariable() {
        binding.btnback.setOnClickListener(view -> finish());
        binding.profileEditbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }
}