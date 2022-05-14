package onlinestore.njn.activity;

import static onlinestore.njn.activity.CheckOutActivity.CUSTOMER_ORDERS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import onlinestore.njn.R;
import onlinestore.njn.model.OrderModel;
import onlinestore.njn.model.UserModel;

public class AdminUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        context = this;
        user_id = getIntent().getStringExtra("user_id");
        if (user_id==null){
            Toast.makeText(context, "User id not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initToolbar();
        get_data();
    }
    String user_id;
    Context context;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserModel userModel;
    TextView user_name, user_ID, user_email, user_address, user_phone, user_reg;
    ImageView user_image;
    private void get_data() {
        db.collection("users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                userModel = documentSnapshot.toObject(UserModel.class);
                //feed_data();
                user_image = findViewById(R.id.user_image);
                user_name = findViewById(R.id.user_name);
                user_ID = findViewById(R.id.users_id);
                user_email = findViewById(R.id.user_email);
                user_address = findViewById(R.id.user_address);
                user_phone = findViewById(R.id.user_phone);

                user_reg = findViewById(R.id.user_reg);

                user_name.setText(userModel.first_name+" "+userModel.last_name);
                Glide.with(context)
                        .load(userModel.profile_photo)

                        .into(user_image);

                user_ID.setText(userModel.user_id);
                user_email.setText(userModel.email);
                user_address.setText(userModel.address);
                user_phone.setText(userModel.phone_number);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String reg_date = simpleDateFormat.format(Long.valueOf(userModel.reg_date));

                user_reg.setText(String.valueOf(reg_date));

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

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customer Info");
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