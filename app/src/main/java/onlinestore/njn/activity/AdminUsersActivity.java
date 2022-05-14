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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.R;
import onlinestore.njn.adapter.OrdersAdapter;
import onlinestore.njn.adapter.UserAdapter;
import onlinestore.njn.model.OrderModel;
import onlinestore.njn.model.UserModel;

public class AdminUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);
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
        getSupportActionBar().setTitle("Customers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSystemBarColor(this);
    }
    List<UserModel> users = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void get_data() {
        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                users = queryDocumentSnapshots.toObjects(UserModel.class);
                initComponents();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                initComponents();
            }
        });
    }
    private UserAdapter mAdapter;
    private void initComponents() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter = new UserAdapter(users, this, "0");
        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, UserModel obj, int pos) {
                Intent i = new Intent(AdminUsersActivity.this, AdminUserActivity.class);
                i.putExtra("user_id", obj.user_id);
                AdminUsersActivity.this.startActivity(i);
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