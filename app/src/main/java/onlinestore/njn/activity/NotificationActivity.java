package onlinestore.njn.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import onlinestore.njn.MainActivity;
import onlinestore.njn.R;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    EditText notification_title;
    EditText notification_body;
    Button btn_notification;

    String CHANNEL_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue_900));
        if (getIntent().hasExtra("category")){
            Intent intent = new Intent(NotificationActivity.this, MainActivity.class);

            startActivity(intent);
        }

        notification_title = findViewById(R.id.notification_title);
        notification_body = findViewById(R.id.notification_body);
        btn_notification = findViewById(R.id.btn_notification);
        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });


    }

    private void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", notification_title.getText().toString());
            notificationObj.put("body", notification_body.getText().toString());

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("NJN", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("NJN", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAyMrhS60:APA91bHyMVIXNpXEjs-oCqzBCJV5zZWv8YaIDlv82rK_QrTkuDjqR-PuwzNd6LJE4IiODmhGWQp55ROcsHLFKZ8NcYY7mL2E95700ae6iBvDy2MfUJoHRHN-q-cjPqcu4Y5iqIVN6tIG");
                    return header;
                }
            };
            mRequestQue.add(request);
            Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
            notification_title.setText("");
            notification_body.setText("");
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }


}