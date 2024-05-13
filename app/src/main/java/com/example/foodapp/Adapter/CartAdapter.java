package com.example.foodapp.Adapter;

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
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Helper.ChangeNumberItemsListener;
import com.example.foodapp.Helper.ManagmentCart;
import com.example.foodapp.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<Foods> itemList;
    private ManagmentCart managmentCart;
    private ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Foods> itemList, ManagmentCart managmentCart, ChangeNumberItemsListener changeNumberItemsListener) {
        this.itemList = itemList;
        this.managmentCart = managmentCart;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods currentItem = itemList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(currentItem.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.title.setText(currentItem.getTitle());
        holder.feeEachItem.setText("$" + (currentItem.getNumberInCart() * currentItem.getPrice()));
        holder.totalEachItem.setText(currentItem.getNumberInCart() + " * $" + currentItem.getPrice());
        holder.num.setText(String.valueOf(currentItem.getNumberInCart()));

        holder.plusItem.setOnClickListener(v ->
                managmentCart.plusNumberItem(itemList, position, () -> {
                    changeNumberItemsListener.change();
                    notifyDataSetChanged();
                }));

        holder.minusItem.setOnClickListener(v ->
                managmentCart.minusNumberItem(itemList, position, () -> {
                    changeNumberItemsListener.change();
                    notifyDataSetChanged();
                }));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem, totalEachItem, num;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            plusItem = itemView.findViewById(R.id.plusBtn);
            minusItem = itemView.findViewById(R.id.minusBtn);
            num = itemView.findViewById(R.id.numTxt);
        }
    }
}
