package onlinestore.njn.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import onlinestore.njn.MainActivity;
import onlinestore.njn.R;

public class CategoryActivity extends AppCompatActivity {
    ImageView electronic,men,women,smart_phones,school_items,kids,grocery,pharmacy,watches,beauty,shoes,kitchen,sport,furniture,jewelry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));


        onImageClick();
    }

    private void onImageClick(){
        electronic = findViewById(R.id.electronic);
        men = findViewById(R.id.men);
        women = findViewById(R.id.women);
        smart_phones = findViewById(R.id.smart_phones);
        school_items = findViewById(R.id.school_items);
        kids = findViewById(R.id.kids);
        grocery = findViewById(R.id.grocery);
        pharmacy = findViewById(R.id.pharmacy);
        watches = findViewById(R.id.watches);
        beauty = findViewById(R.id.beauty);
        shoes = findViewById(R.id.shoes);
        kitchen = findViewById(R.id.kitchen);
        sport = findViewById(R.id.sport);
        furniture = findViewById(R.id.furniture);
        jewelry = findViewById(R.id.jewelry);

        //image on click

        electronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Electronic");
                CategoryActivity.this.startActivity(i);
            }
        });

        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Men");
                CategoryActivity.this.startActivity(i);
            }
        });

        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Women");
                CategoryActivity.this.startActivity(i);
            }
        });

        smart_phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Smart phones");
                CategoryActivity.this.startActivity(i);
            }
        });

        school_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "School items");
                CategoryActivity.this.startActivity(i);
            }
        });

        kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Kids");
                CategoryActivity.this.startActivity(i);
            }
        });

        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Grocery");
                CategoryActivity.this.startActivity(i);
            }
        });

        pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Pharmacy");
                CategoryActivity.this.startActivity(i);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Watches");
                CategoryActivity.this.startActivity(i);
            }
        });

        beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Beauty");
                CategoryActivity.this.startActivity(i);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Shoes");
                CategoryActivity.this.startActivity(i);
            }
        });

        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Kitchen item");
                CategoryActivity.this.startActivity(i);
            }
        });

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Sport item");
                CategoryActivity.this.startActivity(i);
            }
        });

        furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Furniture");
                CategoryActivity.this.startActivity(i);
            }
        });

        jewelry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CategoryResultsActivity.class);
                i.putExtra("category", "Jewelry");
                CategoryActivity.this.startActivity(i);
            }
        });


    }
}