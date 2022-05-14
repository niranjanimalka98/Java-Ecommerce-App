package onlinestore.njn.activity;

import static onlinestore.njn.activity.SignUpActivity.USERS_TABLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import onlinestore.njn.MainActivity;
import onlinestore.njn.R;
import onlinestore.njn.model.UserModel;
import onlinestore.njn.tools.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue_900));
        context = this;

        bind_views();

        UserModel loggedInUser = Utils.get_logged_in_user();
        if(loggedInUser != null){
            Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    EditText email, password;
    TextView register;
    Button sign_in;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void bind_views() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        sign_in = findViewById(R.id.sign_in_button);
        register = findViewById(R.id.register);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_user();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignUpActivity.class);
                context.startActivity(intent);
            }
        });

    }
    String email_value = "";
    String password_value = "";
    ProgressDialog progressDialog;
    Context context;
    private void sign_user() {
        email_value = email.getText().toString().trim();
        password_value = password.getText().toString().trim();

        if (email_value.isEmpty() || password_value.isEmpty()){
            Toast.makeText(this, "Fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        db.collection(USERS_TABLE).whereEqualTo("email", email_value)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots == null){
                            progressDialog.hide();
                            progressDialog.dismiss();
                            Toast.makeText(context, "Email not found on our database", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (queryDocumentSnapshots.isEmpty()){
                            progressDialog.hide();
                            progressDialog.dismiss();
                            Toast.makeText(context, "Email not found on our database", Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<UserModel> users = queryDocumentSnapshots.toObjects(UserModel.class);

                        if (!users.get(0).password.equals(password_value)){
                            progressDialog.hide();
                            progressDialog.dismiss();
                            Toast.makeText(context, "Wrong password", Toast.LENGTH_LONG).show();
                            return;
                        }

                        progressDialog.hide();
                        progressDialog.dismiss();
                        if(login_user(users.get(0))){
                            Toast.makeText(context, "Your login successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            return;
                        }else{
                            Toast.makeText(context, "Failed to login", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, "Failed to create an account", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                progressDialog.dismiss();
            }
        });
    }

    private boolean login_user(UserModel u) {

        try {
            UserModel.save(u);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}