package onlinestore.njn.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import onlinestore.njn.MainActivity;
import onlinestore.njn.R;
import onlinestore.njn.model.UserModel;
import onlinestore.njn.tools.Utils;

import static onlinestore.njn.tools.Utils.get_logged_in_user;
public class SignUpActivity extends AppCompatActivity {
    UserModel loggedInUser = null;
    ImageView profile_photo;
    private final int PICK_IMAGE_REQUEST = 1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue_900));
        bindViews();

        loggedInUser = Utils.get_logged_in_user();
        if(loggedInUser != null){
            Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    EditText first_name, last_name,email,password,phone_number,address;
    Button sign_up_btn;
    private void bindViews() {
        userModel.user_id = db.collection(USERS_TABLE).document().getId();
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone_number = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);
        profile_photo = findViewById(R.id.profile_photo);
        sign_up_btn = findViewById(R.id.sign_up_button);

        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_data();
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile Image"),PICK_IMAGE_REQUEST);
    }

    private Uri imagePath = null;
    StorageReference ref_main;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profile_photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    UserModel userModel = new UserModel();
    private void validate_data() { // sign up activity validation part
        userModel.first_name = first_name.getText().toString();
        if(userModel.first_name.isEmpty()){
            Toast.makeText(this, "First Name cannot be empty", Toast.LENGTH_SHORT).show();
            first_name.requestFocus();
            return;
        }
        if(userModel.first_name.length()<2){
            Toast.makeText(this, "First Name too short", Toast.LENGTH_SHORT).show();
            first_name.requestFocus();
            return;
        }

        userModel.last_name = last_name.getText().toString();
        if(userModel.last_name.isEmpty()){
            Toast.makeText(this, "Last Name cannot be empty", Toast.LENGTH_SHORT).show();
            last_name.requestFocus();
            return;
        }
        if(userModel.last_name.length()<2){
            Toast.makeText(this, "Last Name too short", Toast.LENGTH_SHORT).show();
            last_name.requestFocus();
            return;
        }

        userModel.email = email.getText().toString();
        if(userModel.email.isEmpty()){
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if(userModel.email.length()<5){
            Toast.makeText(this, "Email too short", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }

        userModel.password = password.getText().toString();
        if(userModel.password.isEmpty()){
            Toast.makeText(this, "Last Name cannot be empty", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        if(userModel.password.length()<8){
            Toast.makeText(this, "Password too weak", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        userModel.address = address.getText().toString();
        userModel.phone_number = phone_number.getText().toString();
        userModel.user_type = "customer";
        userModel.profile_photo = "";
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ref_main = FirebaseStorage.getInstance().getReference();
        ref_main.child("customers/"+userModel.user_id).putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SignUpActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                ref_main.child("customers/"+userModel.user_id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        userModel.profile_photo  = uri.toString();
                        progressDialog.hide();
                        progressDialog.dismiss();
                        submit_data();
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userModel.profile_photo  = "https://images.unsplash.com/photo-1578328819058-b69f3a3b0f6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80";
                        progressDialog.hide();
                        progressDialog.dismiss();
                        submit_data();
                        return;
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Failed to uplod photo"+e.getMessage(), Toast.LENGTH_SHORT).show();
                userModel.profile_photo  = "https://images.unsplash.com/photo-1578328819058-b69f3a3b0f6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80";
                progressDialog.hide();
                progressDialog.dismiss();
                submit_data();
                return;
            }
        });


    }

    ProgressDialog progressDialog;

    public static final String USERS_TABLE = "users";
    private void submit_data() {


    progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Please Wait");
    progressDialog.setCancelable(false);
    progressDialog.show();
    db.collection(USERS_TABLE).whereEqualTo("email", userModel.email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            if(!queryDocumentSnapshots.isEmpty()){
                Toast.makeText(SignUpActivity.this, "Email already exist", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                progressDialog.dismiss();
                return;
            }

            userModel.user_id = db.collection(USERS_TABLE).document().getId();
            userModel.reg_date = String.valueOf(Calendar.getInstance().getTimeInMillis() +"");

            db.collection(USERS_TABLE).document(userModel.user_id).set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(SignUpActivity.this, "User Account Created Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                    progressDialog.dismiss();
                    if(login_user()){
                        Toast.makeText(SignUpActivity.this, "Your login successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SignUpActivity.this.startActivity(intent);
                        return;
                    }else{
                        Toast.makeText(SignUpActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this, "Failed to create an account", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                    progressDialog.dismiss();
                }
            });


        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(SignUpActivity.this, "Failed to create an account", Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            progressDialog.dismiss();
        }
    });

    }
    private static final String TAG = "SignUp_Activity";

    private boolean login_user() {

        try {
            UserModel.save(userModel);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}