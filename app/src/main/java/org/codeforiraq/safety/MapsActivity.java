package org.codeforiraq.safety;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.defaultValue;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 7000;
    private GoogleMap mMap;
    TextView text_view_add;
    double Latitude;
    double Longitude;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    Marker mk;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Connect To DataBase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Address");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Open DetailsActivity
        text_view_add = (TextView) findViewById(R.id.text_view_add);
        text_view_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check If Latitude Empty
                if (Latitude == 0) {
                    Toast.makeText(getApplicationContext(), "الرجاء تحديد موقع الحالة على الخريطة", Toast.LENGTH_SHORT).show();
                }
                //Check If Longitude Empty
                if (Longitude == 0) {
                    Toast.makeText(getApplicationContext(), "الرجاء تحديد موقع الحالة على الخريطة", Toast.LENGTH_SHORT).show();
                } else {
                    //Open DetailsActivity And Send Latitude & Longitude
                    Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
                    intent.putExtra("Latitude_Key", Latitude);
                    intent.putExtra("Longitude_Key", Longitude);
                    startActivity(intent);
                }
            }
        });
    }

    //Receiving Data From DataBase And Add Marker To Map
    private void addMarker() {
        FirebaseDatabase.getInstance().getReference().child("Address").child("Marker").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Loop To Receiving All Data From DataBase
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    FirebaseMarker marker = s.getValue(FirebaseMarker.class);
                    String title = marker.getTitle();
                    String numberPhone = marker.getNumberPhone();
                    double latitude = marker.getLatitude();
                    double longitude = marker.getLongitude();
                    LatLng location = new LatLng(latitude, longitude);
                    Marker mk = mMap.addMarker(new MarkerOptions().position(location).title("العنوان: " + title));
                    if (numberPhone != null){
                        mk.setSnippet(("الرقم: " + numberPhone));
                    }
                    //Check If Number Is Empty
                    if (numberPhone.equals("")){
                        mk.setSnippet(("الرقم: لا يوجد"));
                    }
                    mk.showInfoWindow();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Enabled GPS
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker and move the camera
        addMarker();
        enableMyLocation();

        LatLng Najaf = new LatLng(32.022189, 44.344260);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Najaf, 7));

        // Add animation
        CameraPosition cameraPosition = new CameraPosition.Builder().target(Najaf).zoom(6).bearing(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);


        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        final Context context = this;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //When You Click The Second Time It Will Be Deleted Marker
                if (mk != null) {
                    mk.remove();
                }
                mk = mMap.addMarker(new MarkerOptions().position(latLng));
                mk.showInfoWindow();
                //Toast.makeText(context, latLng.latitude + "," + latLng.longitude, Toast.LENGTH_LONG).show();
                Latitude = latLng.latitude;
                Longitude = latLng.longitude;

            }
        });
    }
}


