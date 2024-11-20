package com.example.foodyapp.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyapp.Adapter.FoodListAdapter;
import com.example.foodyapp.Domain.Category;
import com.example.foodyapp.Domain.Foods;
import com.example.foodyapp.R;
import com.example.foodyapp.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity {
    private ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getIntentExtra();
        initList();
        setVariable();
    }

    private void setVariable() {

    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBarListFood.setVisibility(View.VISIBLE);
        ArrayList<Foods> list =new ArrayList<>();
        Query query;
        if (isSearch){
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText+'\uf8ff');

        } else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);

        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Foods.class));
                    }
                    if (!list.isEmpty()){
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this,2));
                        adapterListFood = new FoodListAdapter(list);
                        binding.foodListView.setAdapter(adapterListFood);
                    }
                    binding.progressBarListFood.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId",0);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch",false);
        binding.titleText.setText(categoryName);
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}