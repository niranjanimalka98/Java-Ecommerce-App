package onlinestore.njn.activity;

import static onlinestore.njn.activity.CheckOutActivity.CUSTOMER_ORDERS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.MainActivity;
import onlinestore.njn.R;
import onlinestore.njn.adapter.OrdersAdapter;
import onlinestore.njn.adapter.ProductAdapter;
import onlinestore.njn.model.ItemModel;
import onlinestore.njn.model.OrderModel;

public class AdminOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        initToolbar();
        get_data();
    }
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private void initToolbar() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Online Store");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSystemBarColor(this);
    }
    List<OrderModel> orders = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void get_data() {
        db.collection(CUSTOMER_ORDERS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                orders = queryDocumentSnapshots.toObjects(OrderModel.class);
                initComponents();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                initComponents();
            }
        });
    }
    private OrdersAdapter mAdapter;
    private void initComponents() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter = new OrdersAdapter(orders, this, "0");
        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new OrdersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, OrderModel obj, int pos) {
                Intent i = new Intent(AdminOrdersActivity.this, AdminOrderActivity.class);
                i.putExtra("order_id", obj.order_id);
                AdminOrdersActivity.this.startActivity(i);
            }
        });
    }

    public static void setSystemBarColor(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}