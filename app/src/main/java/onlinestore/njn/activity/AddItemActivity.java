package onlinestore.njn.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import onlinestore.njn.MainActivity;
import onlinestore.njn.R;
import onlinestore.njn.model.ItemModel;

public class AddItemActivity extends AppCompatActivity {

    ImageButton btn_done;
    ImageView product_photo;
    TextInputEditText product_name, product_details, product_price, quantity;
    private final int PICK_IMAGE_REQUEST = 1;
    public final String Product_Table = "PRODUCTS";
    TextInputEditText Category_view;
    ItemModel itemModel = new ItemModel();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue_900)));
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue_900));
        bind_Views();
    }

    private void bind_Views() {
        itemModel.product_id = db.collection(Product_Table).document().getId();
        btn_done = findViewById(R.id.btn_done);
        product_name = findViewById(R.id.product_name);
        product_details = findViewById(R.id.product_details);
        product_price = findViewById(R.id.product_price);
        quantity = findViewById(R.id.product_quantity);
        product_photo = findViewById(R.id.product_photo);
        progressDialog = new ProgressDialog(this);
        Category_view = findViewById(R.id.Category_view);

        Category_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory();
            }
        });

        product_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_product();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Product Image"),PICK_IMAGE_REQUEST);
    }

    public static final String[] categories = new String[]{
            "None","Electronic","Food","School items","Women","Men","Kids", "Smart phones", "Grocery", "Pharmacy", "Watches", "Beauty", "Shoes", "Kitchen item", "Sport item", "Furniture", "Jewelry"
    };

    int selected_Category = 0;
    private void selectCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Category");
        builder.setSingleChoiceItems(categories, selected_Category, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Category_view.setText(categories[i]);
                selected_Category = i;
            }
        });

//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Category_view.setText(categories[i]);
//            }
//        });
        builder.setNegativeButton("Ok", null);
        builder.show();
    }

    private Uri imagePath = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                product_photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ProgressDialog progressDialog;
    StorageReference ref_main;

    private void send_to_firestore(){
        Toast.makeText(this, "Submitting...", Toast.LENGTH_SHORT).show();
        db.collection(Product_Table).document(itemModel.product_id).set(itemModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.hide();
                progressDialog.dismiss();
                Toast.makeText(AddItemActivity.this, "Uploading Successful", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.hide();
                Toast.makeText(AddItemActivity.this, "Uploading Failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
    private void submit_product() {

        itemModel.product_name = product_name.getText().toString();
        if(itemModel.product_name.isEmpty()){
            Toast.makeText(this, "Product Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        itemModel.category = Category_view.getText().toString();
        if(itemModel.category.isEmpty()){
            Toast.makeText(this, "Product Category cannot be empty", Toast.LENGTH_SHORT).show();
            selectCategory();
            return;
        }



        if(product_price.getText().toString().isEmpty()){
            Toast.makeText(this, "Product Price cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            itemModel.price = Integer.valueOf(product_price.getText().toString());
        }catch (Exception e){

        }

        if(itemModel.price<0){
            Toast.makeText(this, "Product Price should be grater than zero", Toast.LENGTH_SHORT).show();
            return;
        }
        // Start Get and Set Quantity
        if(quantity.getText().toString().isEmpty()){
            Toast.makeText(this, "Product Quantity cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            itemModel.quantity = Integer.valueOf(quantity.getText().toString());
        }catch (Exception e){

        }

        if(itemModel.quantity<0){
            Toast.makeText(this, "Product Quantity should be grater than zero", Toast.LENGTH_SHORT).show();
            return;
        }
        // End Get and Set Quantity
        itemModel.description = product_details.getText().toString();

        if(imagePath == null){
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        ref_main = FirebaseStorage.getInstance().getReference();
        ref_main.child("products/"+itemModel.product_id).putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddItemActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();

            ref_main.child("products/"+itemModel.product_id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    itemModel.photo  = uri.toString();
                    send_to_firestore();
                    Intent i = new Intent(AddItemActivity.this, MainActivity.class);
                    AddItemActivity.this.startActivity(i);
                    return;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    itemModel.photo  = "https://images.unsplash.com/photo-1578328819058-b69f3a3b0f6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80";
                    send_to_firestore();
                    return;
                }
            });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddItemActivity.this, "Failed to uplod photo"+e.getMessage(), Toast.LENGTH_SHORT).show();
                itemModel.photo  = "https://images.unsplash.com/photo-1578328819058-b69f3a3b0f6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80";
                send_to_firestore();
                return;
            }
        });


    }

    public void back(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}