package com.example.foodyapp.Helper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodyapp.Domain.Banner;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BannerView extends ViewModel {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://quickbite-9284c-default-rtdb.asia-southeast1.firebasedatabase.app");
    private MutableLiveData<List<Banner>> _banner = new MutableLiveData<>();
    public LiveData<List<Banner>> getBanner(){
        return _banner;

    }
    public void loadBanner(){
        DatabaseReference myRef = firebaseDatabase.getReference("Banner");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Banner> lists = new ArrayList<>();
                for (DataSnapshot childSnapshot: snapshot.getChildren()){
                    Banner list = childSnapshot.getValue(Banner.class);
                    if (list != null){
                        lists.add(list);
                    }
                    _banner.setValue(lists);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
