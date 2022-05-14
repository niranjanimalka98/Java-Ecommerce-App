package onlinestore.njn.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import onlinestore.njn.R;

public class SearchActivity extends AppCompatActivity {

    ImageView search_btn;
    EditText search_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));

        search_btn = findViewById(R.id.search_btn);
        search_text = findViewById(R.id.search_text);




        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this, SearchResultsActivity.class);
                i.putExtra("search_id", search_text.getText().toString());
                SearchActivity.this.startActivity(i);

            }
        });

    }
}