package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.CartAdapter;
import com.example.foodapp.Helper.ManagmentCart;
import com.example.foodapp.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        setVariable();
        initList();
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollview.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(managmentCart.getListCart(), managmentCart, () -> {
        });
        binding.cartView.setAdapter(adapter);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v ->{
            startActivity(new Intent(CartActivity.this,MainActivity.class));
        });
        binding.paymentBtn.setOnClickListener(v -> {
            if (!managmentCart.getListCart().isEmpty()) {
                startActivity(new Intent(CartActivity.this, PaymentActivity.class));
            } else {
                Toast.makeText(this, "Your cart is empty. Please add some items before proceeding to payment.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollview.setVisibility(View.VISIBLE);
        }
    }
}
