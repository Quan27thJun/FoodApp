package com.example.foodapp.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.CartAdapter;
import com.example.foodapp.Helper.ManagmentCart;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityPaymentBinding;

import eightbitlab.com.blurview.RenderScriptBlur;

public class PaymentActivity extends BaseActivity {
    private EditText addressEditText;
    private EditText phoneNumberEditText;

    private ActivityPaymentBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        addressEditText = findViewById(R.id.address);
        phoneNumberEditText = findViewById(R.id.phone);
        setVariable();
        calculateCart();
        initList();
        setBlurEffect();
        placeOrder();
    }

    private void placeOrder() {
        String address = addressEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        if (!address.isEmpty() && !phoneNumber.isEmpty()) {
            // Nếu đã nhập đủ, hiển thị thông báo đặt hàng thành công
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu chưa nhập đủ, hiển thị thông báo yêu cầu người dùng nhập địa chỉ và số điện thoại
            Toast.makeText(this, "Please enter your address and phone number", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBlurEffect() {
        float radius = 10f;
        View decorView = (this).getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        binding.blurView.setupWith(rootView, new RenderScriptBlur(this)) // or RenderEffectBlur
                .setFrameClearDrawable(windowBackground) // Optional
                .setBlurRadius(radius);
        binding.blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        binding.blurView.setClipToOutline(true);

        binding.blurView2.setupWith(rootView, new RenderScriptBlur(this)) // or RenderEffectBlur
                .setFrameClearDrawable(windowBackground) // Optional
                .setBlurRadius(radius);
        binding.blurView2.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        binding.blurView2.setClipToOutline(true);
    }

    private void initList() {
        if(managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollview.setVisibility(View.GONE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollview.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter=new CartAdapter(managmentCart.getListCart(),managmentCart, () -> calculateCart());
        binding.cartView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = (managmentCart.getTotalFee() * percentTax * 100.0) / 100.0;
        double total = ((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = (managmentCart.getTotalFee() * 100) / 100;
        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$"+tax);
        binding.deliveryTxt.setText("$"+delivery);
        binding.totalTxt.setText("$"+total);
    }

    private void setVariable() {
        binding.applyBtn.setOnClickListener(v -> applyCoupon());
        binding.backBtn.setOnClickListener(v -> finish());
        binding.orderBtn.setOnClickListener(v -> placeOrder());
    }

    private void applyCoupon() {
        String couponCode = binding.code.getText().toString().trim();
        // Kiểm tra nếu mã coupon không rỗng
        if (!couponCode.isEmpty()) {
            // Kiểm tra mã coupon và áp dụng giảm giá tương ứng
            // Ví dụ: Nếu mã là "ABC123", giảm giá 10%
            if (couponCode.equals("ABC123")) {
                // Áp dụng giảm giá vào giỏ hàng
                managmentCart.applyCoupon(0.1); // Giảm giá 10%
                // Cập nhật lại tổng tiền sau khi áp dụng mã giảm giá
                calculateCart();
            }
        } else {
            // Kiểm tra xem có đang áp dụng mã giảm giá không
            if (managmentCart.isCouponApplied()) {
                // Nếu đang áp dụng mã giảm giá, hủy áp dụng và cập nhật lại giá
                managmentCart.cancelCoupon(0.1); // Hủy áp dụng mã giảm giá
                // Cập nhật lại tổng tiền sau khi hủy áp dụng mã giảm giá
                calculateCart();
            }

        }
    }
}