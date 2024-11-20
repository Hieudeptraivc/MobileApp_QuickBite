package com.example.foodyapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodyapp.Domain.Banner;
import com.example.foodyapp.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.viewholder> {
    private List<Banner> bannerModel;
    private ViewPager2 viewPager2;
    private Context context;

    public BannerAdapter(List<Banner> bannerModel, ViewPager2 viewPager2) {
        this.bannerModel = bannerModel;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public BannerAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.banner_container,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.viewholder holder, int position) {
        holder.setImage(bannerModel.get(position),context);
//        if (position == bannerModel.size() - 1 ){
//            viewPager2.post(()->notifyDataSetChanged());
//        }
    }

    @Override
    public int getItemCount() {
        return bannerModel.size();
    }
    public static class viewholder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageBanner);
        }
        public void setImage(Banner bannerModel, Context context){
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new CenterInside(),new RoundedCorners(30));
            Glide.with(context)
                    .load(bannerModel.getUrl())
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
}
