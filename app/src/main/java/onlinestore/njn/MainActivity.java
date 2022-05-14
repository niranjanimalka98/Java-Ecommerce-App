package onlinestore.njn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.activity.AddItemActivity;
import onlinestore.njn.activity.AdminOrdersActivity;
import onlinestore.njn.activity.AdminUsersActivity;
import onlinestore.njn.activity.CategoryActivity;
import onlinestore.njn.activity.CheckOutActivity;
import onlinestore.njn.activity.NotificationActivity;
import onlinestore.njn.activity.ProductActivity;
import onlinestore.njn.activity.SearchActivity;
import onlinestore.njn.activity.SignInActivity;
import onlinestore.njn.activity.SignUpActivity;
import onlinestore.njn.adapter.ProductAdapter;
import onlinestore.njn.model.ItemModel;
import onlinestore.njn.model.UserModel;
import onlinestore.njn.tools.Utils;

public class MainActivity extends AppCompatActivity {
    UserModel loggedInUser;

    private RecyclerView recyclerView;
    private LinearLayout home;
    private LinearLayout search;
    private LinearLayout shopping;
    private LinearLayout setting;
    private LinearLayout user;


    private ProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue_900)));
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue_900));
        getWindow().setNavigationBarColor((getResources().getColor(R.color.light_blue_900)));
        getWindow().clearFlags( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initToolbar();


        get_data();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.main_menu, menu);

        String user_type;
        String admin = "admin";


        try {
            user_type = Utils.get_logged_in_user().user_type;

            if (user_type.equals(admin)) {
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
                menu.getItem(4).setVisible(true);
                menu.getItem(5).setVisible(true);
                menu.getItem(6).setVisible(true);
                menu.getItem(7).setVisible(true);
                menu.getItem(8).setVisible(true);
            } else {
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(true);
                menu.getItem(3).setVisible(true);
                menu.getItem(4).setVisible(false);
                menu.getItem(5).setVisible(false);
                menu.getItem(6).setVisible(false);
                menu.getItem(7).setVisible(false);
                menu.getItem(8).setVisible(false);
            }

        } catch (Exception e) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(false);
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(false);
            menu.getItem(6).setVisible(false);
            menu.getItem(7).setVisible(false);
            menu.getItem(8).setVisible(false);

        }


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            Intent i = new Intent(MainActivity.this, CheckOutActivity.class);
            MainActivity.this.startActivity(i);
        } else if (item.getItemId() == R.id.action_add_product) {
            Intent i = new Intent(MainActivity.this, AddItemActivity.class);
            MainActivity.this.startActivity(i);
        } else if (item.getItemId() == R.id.action_orders) {
            Intent i = new Intent(MainActivity.this, AdminOrdersActivity.class);
            MainActivity.this.startActivity(i);
        } else if (item.getItemId() == R.id.action_login) {
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            MainActivity.this.startActivity(i);
        } else if (item.getItemId() == R.id.action_create_acc) {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            MainActivity.this.startActivity(i);
        } else if (item.getItemId() == R.id.action_customers) {
            Intent i = new Intent(MainActivity.this, AdminUsersActivity.class);
            MainActivity.this.startActivity(i);
        } else if (item.getItemId() == R.id.action_notification) {
            Intent i = new Intent(MainActivity.this, NotificationActivity.class);
            MainActivity.this.startActivity(i);
        } else if (item.getItemId() == R.id.action_logout) {
            Toast.makeText(this, "Logging you out....", Toast.LENGTH_SHORT).show();
            try {
                UserModel.deleteAll(UserModel.class);
                Toast.makeText(this, "Logged you out successfully!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(i);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to Log you out because " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    List<ItemModel> products = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void get_data() {
        //db.collection("PRODUCTS").orderBy("product_name").startAt("tv").endAt("tv"+'\uf8ff');
        db.collection("PRODUCTS").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                products = queryDocumentSnapshots.toObjects(ItemModel.class);
                initComponents();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                initComponents();
            }
        });
    }

    ProgressBar progressBar;


    private void initComponents() {

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter = new ProductAdapter(products, this, "0");
        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ItemModel obj, int pos) {
                Intent i = new Intent(MainActivity.this, ProductActivity.class);
                i.putExtra("id", obj.product_id);
                MainActivity.this.startActivity(i);
            }
        });
    }

    private void initToolbar() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        home = findViewById(R.id.home_icon);
        search = findViewById(R.id.search_icon);
        shopping = findViewById(R.id.shopping_icon);
        setting = findViewById(R.id.setting_icon);
        user = findViewById(R.id.user_icon);

        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "you clicked home", Toast.LENGTH_SHORT).show();
                home.setBackgroundResource(R.drawable.circle);
                search.setBackgroundResource(R.drawable.circle_no_bg);
                shopping.setBackgroundResource(R.drawable.circle_no_bg);
                setting.setBackgroundResource(R.drawable.circle_no_bg);
                user.setBackgroundResource(R.drawable.circle_no_bg);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(i);
                search.setBackgroundResource(R.drawable.circle);
                home.setBackgroundResource(R.drawable.circle_no_bg);
                shopping.setBackgroundResource(R.drawable.circle_no_bg);
                setting.setBackgroundResource(R.drawable.circle_no_bg);
                user.setBackgroundResource(R.drawable.circle_no_bg);
            }
        });
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                MainActivity.this.startActivity(i);
                shopping.setBackgroundResource(R.drawable.circle);
                home.setBackgroundResource(R.drawable.circle_no_bg);
                search.setBackgroundResource(R.drawable.circle_no_bg);
                setting.setBackgroundResource(R.drawable.circle_no_bg);
                user.setBackgroundResource(R.drawable.circle_no_bg);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "you clicked setting", Toast.LENGTH_SHORT).show();
                setting.setBackgroundResource(R.drawable.circle);
                home.setBackgroundResource(R.drawable.circle_no_bg);
                search.setBackgroundResource(R.drawable.circle_no_bg);
                shopping.setBackgroundResource(R.drawable.circle_no_bg);
                user.setBackgroundResource(R.drawable.circle_no_bg);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "you clicked user", Toast.LENGTH_SHORT).show();
                user.setBackgroundResource(R.drawable.circle);
                home.setBackgroundResource(R.drawable.circle_no_bg);
                search.setBackgroundResource(R.drawable.circle_no_bg);
                shopping.setBackgroundResource(R.drawable.circle_no_bg);
                setting.setBackgroundResource(R.drawable.circle_no_bg);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.view_comfy);
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