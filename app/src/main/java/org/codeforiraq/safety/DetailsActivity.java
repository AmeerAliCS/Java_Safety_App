package org.codeforiraq.safety;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.defaultValue;

public class DetailsActivity extends AppCompatActivity {

    EditText title_edit_text , number_phone_edit_text;
    String title , numberPhone;
    Button submit_details;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Connect To DataBase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Address");

        //Receiving Data From MapsActivity
        Intent intent = this.getIntent();
        final double Latitude = intent.getDoubleExtra("Latitude_Key", defaultValue);
        final double Longitude = intent.getDoubleExtra("Longitude_Key", defaultValue);

        title_edit_text = (EditText) findViewById(R.id.title_edit_text);
        //description_edit_text = (EditText) findViewById(R.id.description_edit_text);
        number_phone_edit_text = (EditText) findViewById(R.id.number_phone_edit_text);

        submit_details = (Button) findViewById(R.id.submit_details);
        //Push Data To DataBase
        submit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check If Name Field Empty
                if (TextUtils.isEmpty(title_edit_text.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "يرجى تحديد اسم للحالة", Toast.LENGTH_SHORT).show();
                }

                //Push Data To DataBase
                else {
                    title = title_edit_text.getText().toString().trim();
                    //description = description_edit_text.getText().toString().trim();
                    numberPhone = number_phone_edit_text.getText().toString().trim();
                    FirebaseMarker marker = new FirebaseMarker();
                    marker.setTitle(title);
                    //marker.setDescription(description);
                    marker.setNumberPhone(numberPhone);
                    marker.setLatitude(Latitude);
                    marker.setLongitude(Longitude);
                    DatabaseReference newMarker = users.child("Marker").push();
                    newMarker.setValue(marker);
                    Toast.makeText(getApplicationContext(), "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}
