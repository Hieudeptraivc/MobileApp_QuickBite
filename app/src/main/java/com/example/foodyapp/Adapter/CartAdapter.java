package com.example.foodyapp.Adapter;

import android.content.Context;
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
import com.example.foodyapp.Domain.Foods;
import com.example.foodyapp.Helper.ChangeNumberItemsListener;
import com.example.foodyapp.Helper.ManagmentCart;
import com.example.foodyapp.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Foods> list;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Foods> list, Context context, ChangeNumberItemsListener changeNumberItemsListener){
        this.list = list;
        managmentCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }
    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.total.setText(String.format("$%.2f", (list.get(position).getNumberInCart() * list.get(position).getPrice())));
        holder.totalEachItem.setText(String.format("%d * $%.2f",
                list.get(position).getNumberInCart(),
                list.get(position).getPrice()
        ));
        holder.num.setText(String.valueOf(list.get(position).getNumberInCart()));


        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop())
                .into(holder.pic);

        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managmentCart.plusNumberItem(list, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });

        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managmentCart.minusNumberItem(list, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class viewholder extends RecyclerView.ViewHolder{
        TextView title,total,plusItem,minusItem;
        ImageView pic;
        TextView totalEachItem,num;
        public  viewholder(@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.title_text);
            total = itemView.findViewById(R.id.total_text);
            plusItem = itemView.findViewById(R.id.plusCart_btn);
            minusItem = itemView.findViewById(R.id.minusCart_btn);
            pic = itemView.findViewById(R.id.pic);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            num = itemView.findViewById(R.id.numberItem_btn);


        }
    }
}
