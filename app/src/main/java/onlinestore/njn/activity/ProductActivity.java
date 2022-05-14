package onlinestore.njn.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.R;
import onlinestore.njn.model.CartModel;
import onlinestore.njn.model.ItemModel;
import onlinestore.njn.model.UserModel;
import onlinestore.njn.tools.Utils;

public class ProductActivity extends AppCompatActivity {
    UserModel loggedInUser;
    String id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initToolbar();
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue_900));
        getWindow().setNavigationBarColor((getResources().getColor(R.color.light_blue_900)));

        context = this;

        Intent i = getIntent();
        id = i.getStringExtra("id");

        if(id == null || id.length()<1){
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        bind_views();
        get_data();
        
    }
    ImageView product_image;
    TextView product_name, product_price, product_details, product_qty, quantity;
    Button add_to_cart;
    private void bind_views() {
        product_image = findViewById(R.id.product_image);
        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        product_details = findViewById(R.id.product_details);
        product_qty = findViewById(R.id.product_qty);
        add_to_cart = findViewById(R.id.add_to_cart);
        quantity = findViewById(R.id.quantity);

    }

    private void feed_data() {
        Glide.with(context)
                .load(product.photo)

                .into(product_image);

        product_name.setText(product.product_name);
        product_price.setText(product.price+"");
        product_details.setText(product.description+"");
        product_qty.setText(product.quantity+"");

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loggedInUser = Utils.get_logged_in_user();
                if(loggedInUser == null){
                    Toast.makeText(ProductActivity.this, "You are not logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ProductActivity.this, SignInActivity.class);
                    ProductActivity.this.startActivity(i);
                    finish();
                    return;
                }else{
                    add_product_to_cart();
                }

            }
        });
    }

    private void add_product_to_cart() {
        CartModel cartModel = new CartModel();
        cartModel.product_id = product.product_id;
        cartModel.product_name = product.product_name;
        cartModel.product_price = product.price*Integer.valueOf(quantity.getText().toString());
        Log.d("test", String.valueOf(Integer.valueOf(quantity.getText().toString())));
        cartModel.quantity = Integer.valueOf(quantity.getText().toString());
        cartModel.product_photo = product.photo;

        try {
            //cartModel.save();
            CartModel.save(cartModel);
            Toast.makeText(context, "Product added to the cart", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Failed to save " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    ItemModel product = new ItemModel();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;
    private void get_data() {
        db.collection("PRODUCTS").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(!documentSnapshot.exists()){
                    Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                product = documentSnapshot.toObject(ItemModel.class);
                feed_data();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });
    }
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSystemBarColor(this);
    }

    public static void setSystemBarColor(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    int minteger = 1;
    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);

    }public void decreaseInteger(View view) {
        minteger = minteger - 1;
        display(minteger);

    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.quantity);
        displayInteger.setText("" + number);
        if(number<1){
            minteger = 1;
            displayInteger.setText("" + 1);
        }

    }

}