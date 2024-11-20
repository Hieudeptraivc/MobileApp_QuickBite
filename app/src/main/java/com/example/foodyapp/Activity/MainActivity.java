    package com.example.foodyapp.Activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.os.Handler;
    import android.view.KeyEvent;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.inputmethod.EditorInfo;
    import android.widget.ArrayAdapter;

    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.coordinatorlayout.widget.CoordinatorLayout;
    import androidx.core.widget.NestedScrollView;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.viewpager2.widget.CompositePageTransformer;
    import androidx.viewpager2.widget.MarginPageTransformer;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.load.resource.bitmap.CenterCrop;
    import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
    import com.example.foodyapp.Adapter.BannerAdapter;
    import com.example.foodyapp.Adapter.CategoryAdapter;
    import com.example.foodyapp.Adapter.PopularAdapter;
    import com.example.foodyapp.Domain.Banner;
    import com.example.foodyapp.Domain.Category;
    import com.example.foodyapp.Domain.Foods;
    import com.example.foodyapp.Domain.Location;
    import com.example.foodyapp.Domain.Price;
    import com.example.foodyapp.Domain.Time;
    import com.example.foodyapp.Helper.ActivityHelper;
    import com.example.foodyapp.Helper.BannerView;
    import com.example.foodyapp.R;
    import com.example.foodyapp.databinding.ActivityMainBinding;
    import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
    import com.google.android.material.bottomnavigation.BottomNavigationView;
    import com.google.android.material.navigation.NavigationBarView;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;


    import java.util.ArrayList;
    import java.util.List;


    public class MainActivity extends BaseActivity {
        private ActivityMainBinding binding;
        private BannerView viewModel;
        private Handler sliderHandler = new Handler();
        private BottomNavigationView nav;


        private List<Banner> bannerList = new ArrayList<>();

        private Runnable sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (binding.viewPager2.getCurrentItem() < bannerList.size() - 1) {
                    binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
                } else {
                    binding.viewPager2.setCurrentItem(0);
                }
                // Tiếp tục gọi lại sau 3 giây
                sliderHandler.postDelayed(this, 3000);
            }
        };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            viewModel = new ViewModelProvider(this).get(BannerView.class);
            nav = findViewById(R.id.navHome_menu);

            loadUserInfo();
            initBottomNavigate();
            initScrollScreen();
            initBanner();
            initLocation();
            initTime();
            initPrice();
            initPopular();
            initCategory();
            setVariable();

        }

        private void loadUserInfo() {
            binding.progressBarProfileImage.setVisibility(View.VISIBLE);
            DatabaseReference ref = database.getReference("Users");
            ref.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = ""+snapshot.child("name").getValue();
                    String profileImage = ""+snapshot.child("profileImage").getValue();
                    String uid = ""+snapshot.child("uid").getValue();


                    binding.userNameText.setText(name);
                    Glide.with(MainActivity.this)
                            .load(profileImage)
                            .transform(new CenterCrop(),new RoundedCorners(70))
                            .placeholder(R.drawable.avatar)
                            .into(binding.avatarImg);
                    binding.progressBarProfileImage.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void initBottomNavigate() {

            nav.setSelectedItemId(R.id.home_nav);

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



        private void initScrollScreen(){

            binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        binding.navHomeMenu.animate().translationY(binding.navHomeMenu.getHeight()+50).setDuration(100);
                    } else if (scrollY < oldScrollY) {
                        binding.navHomeMenu.animate().translationY(0).setDuration(100);
                    }
                }
            });
        }

        private void initBanner() {
            binding.progressBarBanner.setVisibility(View.VISIBLE);
            viewModel.getBanner().observe(this,banners -> {
                bannerList = banners;
                setupBanner(banners);
                binding.progressBarBanner.setVisibility(View.GONE);
            });
            viewModel.loadBanner();
        }
        private void setupBanner(List<Banner> images){
            binding.viewPager2.setAdapter(new BannerAdapter(images,binding.viewPager2));
            binding.viewPager2.setClipToPadding(false);
            binding.viewPager2.setClipChildren(false);
            binding.viewPager2.setOffscreenPageLimit(4);
            binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer transformer = new CompositePageTransformer();
            transformer.addTransformer(new MarginPageTransformer(40));
            binding.viewPager2.setPageTransformer(transformer);
            if (images.size() > 1){
                binding.dotIndicator.setVisibility(View.VISIBLE);
                binding.dotIndicator.attachTo(binding.viewPager2);
            }
        }
        @Override
        protected void onResume() {
            super.onResume();
            if (nav.getSelectedItemId() != R.id.home_nav) {
                nav.setSelectedItemId(R.id.home_nav);
            }
            sliderHandler.postDelayed(sliderRunnable, 3000);


        }

        @Override
        protected void onPause() {
            super.onPause();
            sliderHandler.removeCallbacks(sliderRunnable);
        }
        private void setVariable() {
            binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    ActivityHelper.startNextActivity(MainActivity.this, LoginActivity.class);
                }

            });


            binding.searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {


                        String text = binding.searchEdit.getText().toString();
                        String text_cap = ActivityHelper.capitalizeWords(text);

                        if (!text.isEmpty()) {
                            Intent intent = new Intent(MainActivity.this, ListFoodsActivity.class);
                            intent.putExtra("text", text_cap);
                            intent.putExtra("isSearch", true);
                            startActivity(intent);
                        }


                        return true;
                    }
                    return false;
                }
            });

        }

        private void initPopular() {
            DatabaseReference myRef = database.getReference("Foods");
            binding.progressBarPopular.setVisibility(View.VISIBLE);
            ArrayList<Foods> list = new ArrayList<>();
            Query query = myRef.orderByChild("BestFood").equalTo(true);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot issue:snapshot.getChildren()){
                            list.add(issue.getValue(Foods.class));

                    }
                        if(!list.isEmpty()){
                            binding.recyclerViewPopular.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false));
                            RecyclerView.Adapter adapter = new PopularAdapter(list);
                            binding.recyclerViewPopular.setAdapter(adapter);
                        }
                        binding.progressBarPopular.setVisibility(View.GONE);

                }}

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void initCategory() {
            DatabaseReference myRef = database.getReference("Category");
            binding.progressBarCategory.setVisibility(View.VISIBLE);
            ArrayList<Category> list = new ArrayList<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot issue:snapshot.getChildren()){
                            list.add(issue.getValue(Category.class));

                        }
                        if(!list.isEmpty()){
                            binding.recyclerViewCategory.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false));
                            RecyclerView.Adapter adapter = new CategoryAdapter(list);
                            binding.recyclerViewCategory.setAdapter(adapter);
                        }
                        binding.progressBarCategory.setVisibility(View.GONE);

                    }}

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void initLocation() {
            DatabaseReference myRef = database.getReference("Location");
            ArrayList<Location> list = new ArrayList<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot issue:snapshot.getChildren()){
                            list.add(issue.getValue(Location.class));
                        }
                        ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.locationSp.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void initTime() {
            DatabaseReference myRef = database.getReference("Time");
            ArrayList<Time> list = new ArrayList<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot issue:snapshot.getChildren()){
                            list.add(issue.getValue(Time.class));
                        }
                        ArrayAdapter<Time> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.timeSp.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void initPrice() {
            DatabaseReference myRef = database.getReference("Price");
            ArrayList<Price> list = new ArrayList<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot issue:snapshot.getChildren()){
                            list.add(issue.getValue(Price.class));
                        }
                        ArrayAdapter<Price> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.priceSp.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }