package onlinestore.njn.activity;

import static onlinestore.njn.activity.CheckOutActivity.CUSTOMER_ORDERS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.R;
import onlinestore.njn.adapter.ProductAdapter;
import onlinestore.njn.model.CartModel;
import onlinestore.njn.model.ItemModel;
import onlinestore.njn.model.OrderModel;


public class AdminOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);
        context = this;
        order_id = getIntent().getStringExtra("order_id");
        if (order_id==null){
            Toast.makeText(context, "Order id not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initToolbar();
        get_data();
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    OrderModel orderModel;
    private void get_data() {
        db.collection(CUSTOMER_ORDERS).document(order_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    Toast.makeText(context, "Orders not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                orderModel = documentSnapshot.toObject(OrderModel.class);
                feed_data();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to get orders " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });
    }
    RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    TextView order_id_view;
    EditText customer_name, customer_address, customer_contact;
    Button delete_order;
    private void feed_data() {
        cartModels = orderModel.cart;
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


        }

        recyclerView = findViewById(R.id.cart_products);
        order_id_view = findViewById(R.id.order_id);
        customer_name= findViewById(R.id.customer_name);
        customer_address= findViewById(R.id.customer_address);
        customer_contact= findViewById(R.id.customer_contact);
        delete_order = findViewById(R.id.delete_order);

        order_id_view.setText("Order #"+ orderModel.order_id);
        customer_name.setText(orderModel.customer.first_name+" "+orderModel.customer.last_name);
        customer_address.setText(orderModel.customer.address);
        customer_contact.setText(orderModel.customer.phone_number);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        mAdapter = new ProductAdapter(products, this, "1");
        recyclerView.setAdapter(mAdapter);


        delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection(CUSTOMER_ORDERS).document(order_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Order Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to delete order" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });
    }

    String order_id = null;
    Context context;
    List<ItemModel> products = new ArrayList<>();
    List<CartModel> cartModels = null;

    private void initToolbar() {
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Online Store");
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
