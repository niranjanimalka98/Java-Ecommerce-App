package onlinestore.njn.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.MainActivity;
import onlinestore.njn.R;
import onlinestore.njn.adapter.ProductAdapter;
import onlinestore.njn.model.CartModel;
import onlinestore.njn.model.ItemModel;
import onlinestore.njn.model.OrderModel;
import onlinestore.njn.model.UserModel;
import onlinestore.njn.tools.Utils;

public class CheckOutActivity extends AppCompatActivity {
    TextView cart_total;


    UserModel loggedInUser;
    public int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        loggedInUser = Utils.get_logged_in_user();
        if(loggedInUser == null){
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, SignInActivity.class);
            this.startActivity(i);
            finish();
            return;
        }

        initToolbar();
        get_cart_data();
    }
    List<CartModel> cartModels = null;
    List<ItemModel> products = new ArrayList<>();


    private void get_cart_data() {
        try {
            cartModels = (List<CartModel>) CartModel.listAll(CartModel.class);
        }catch (Exception e){
            Toast.makeText(this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (cartModels == null){
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        total = 0;
        for (CartModel c : cartModels){
            ItemModel p = new ItemModel();
            p.product_name = c.product_name;
            p.category = "";
            p.description = "";
            p.price = c.product_price;
            p.quantity = c.quantity;
            p.product_id = c.product_id;
            p.photo = c.product_photo;
            products.add(p);
            total+=c.product_price;


        }
        cart_total = findViewById(R.id.cart_total);
        cart_total.setText(String.valueOf("Total: Rs. "+total));
        Log.d("test", String.valueOf(total));
        feed_cart_data();
    }

    RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    Button submit_order;

    private void feed_cart_data() {
        recyclerView = findViewById(R.id.cart_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        mAdapter = new ProductAdapter(products, this, "1");
        recyclerView.setAdapter(mAdapter);
        submit_order = findViewById(R.id.submit_order);


        submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit_order();
            }
        });
    }
    public static String CUSTOMER_ORDERS = "CUSTOMER_ORDERS";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    private void submit_order() {
        OrderModel orderModel = new OrderModel();
        orderModel.order_id = db.collection(CUSTOMER_ORDERS).document().getId();
        orderModel.customer = loggedInUser;
        orderModel.cart = cartModels;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Placing");
        progressDialog.setCancelable(false);
        progressDialog.show();

        db.collection(CUSTOMER_ORDERS).document(orderModel.order_id).set(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CheckOutActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                progressDialog.dismiss();
                try {
                    CartModel.deleteAll(CartModel.class);
                }catch (Exception e){
                    Toast.makeText(CheckOutActivity.this, "Failed to clean cart", Toast.LENGTH_SHORT).show();
                }
                finish();
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.hide();
                progressDialog.dismiss();
                Toast.makeText(CheckOutActivity.this, "Failed to place order" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
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

}