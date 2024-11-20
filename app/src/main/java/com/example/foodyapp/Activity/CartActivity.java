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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodyapp.Adapter.CartAdapter;
import com.example.foodyapp.Adapter.CategoryAdapter;
import com.example.foodyapp.Domain.Foods;
import com.example.foodyapp.Helper.ChangeNumberItemsListener;
import com.example.foodyapp.Helper.ManagmentCart;
import com.example.foodyapp.R;
import com.example.foodyapp.databinding.ActivityCartBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        nav = findViewById(R.id.navCart_menu);
        initBottomNavigate();
        initScrollScreen();
        initList();
        calculateCart();
        setVariable();
        checkEmptyCart();
        saveOrder();
    }

    private void saveOrder() {
        binding.placeOrderBtn.setOnClickListener(v -> {
            String address = binding.addressText.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter your delivery address!", Toast.LENGTH_SHORT).show();
                return;
            }
            saveOrderToFirebase(address);
        });
    }
    private void saveOrderToFirebase(String address) {
        // Tạo ID cho đơn hàng
        String orderId = database.getReference().child("Orders").push().getKey();

        // Lấy danh sách món ăn từ ManagmentCart
        ArrayList<Foods> cartItems = managmentCart.getListCart();

        // Tính toán chi phí
        double percentTax = 0.02;
        double deliveryFee = 10;
        double subtotal = managmentCart.getTotalFee();
        double tax = (double) Math.round(subtotal * percentTax * 100.0) / 100;
        double total = (double) Math.round((subtotal + tax + deliveryFee) * 100.0) / 100;
        SimpleDateFormat currentTime = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss", Locale.getDefault());
        String currentDateandTime = currentTime.format(new Date());

        // Chuẩn bị dữ liệu để lưu
        HashMap<String, Object> orderData = new HashMap<>();
        orderData.put("userID", auth.getUid());
        orderData.put("deliveryAddress", address);
        orderData.put("subtotal", subtotal);
        orderData.put("tax", tax);
        orderData.put("deliveryFee", deliveryFee);
        orderData.put("total", total);
        orderData.put("timeStamp",currentDateandTime);

        // Lưu danh sách món ăn
        ArrayList<HashMap<String, Object>> itemsData = new ArrayList<>();
        for (Foods item : cartItems) {
            HashMap<String, Object> itemData = new HashMap<>();
            itemData.put("foodName", item.getTitle());
            itemData.put("foodID", item.getId()); // Lưu foodID
            itemData.put("price", item.getPrice());
            itemData.put("quantity", item.getNumberInCart());
            itemsData.add(itemData);
        }
        orderData.put("cartItems", itemsData);

        // Ghi dữ liệu vào Firebase
        assert orderId != null;
        database.getReference().child("Orders").child(orderId).setValue(orderData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    managmentCart.clearCart();
                    finish(); // Quay lại màn hình trước đó
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void initScrollScreen(){

        binding.scrollviewCart.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    binding.navCartMenu.animate().translationY(binding.navCartMenu.getHeight()+50).setDuration(100);
                } else if (scrollY < oldScrollY) {
                    binding.navCartMenu.animate().translationY(0).setDuration(100);
                }
            }
        });
    }

    private void checkEmptyCart() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyText.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        } else {
            binding.emptyText.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }
    }

    private void initBottomNavigate() {

        nav.setSelectedItemId(R.id.cart_nav);

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
                    startActivity(new Intent(getApplicationContext(), FavoritesListActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)  );
                    overridePendingTransition(0,0);
                    return  true;
                }
                return false;
            }
        });
    }

    private void initList() {
        checkEmptyCart();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(),this, () -> {calculateCart();checkEmptyCart();});

        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = (double) Math.round(managmentCart.getTotalFee() * percentTax * 100.0) /100;
        double total = (double) Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) /100;
        double itemTotal = (double) Math.round(managmentCart.getTotalFee() * 100) /100;

        binding.deliveryText.setText(String.format("$%.2f", delivery));
        binding.subTotalText.setText(String.format("$%.2f", itemTotal));
        binding.taxText.setText(String.format("$%.2f", tax));
        binding.totalText.setText(String.format("$%.2f",total));


    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nav.getSelectedItemId() != R.id.cart_nav) {
            nav.setSelectedItemId(R.id.cart_nav);
        }
        initList();

    }
    private void setVariable() {
        binding.backCartBtn.setOnClickListener(view -> finish());

    }

}