package com.example.giveaway;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

public class addpost extends AppCompatActivity {


    Toolbar toolbar;
    ImageView imageView;
    Button post,lo;
    EditText text;
    Spinner type;
    FirebaseUser currentUser;
    StorageReference mStorageRef;
    FirebaseFirestore db, firestore;
    Uri resultUri;
    String item, uid, n, pi,lat = "",lon = "";
    boolean im = false;
    LocationManager locationManager;
    LocationListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpost);
        imageView = (ImageView) findViewById(R.id.image);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        post = (Button) findViewById(R.id.post);
        lo = (Button) findViewById(R.id.location);
        text = (EditText) findViewById(R.id.text);
        db = FirebaseFirestore.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid().toString();
        mStorageRef = FirebaseStorage.getInstance().getReference("Posts").child(uid);
        type = (Spinner) findViewById(R.id.type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pick();

            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("CONTRIBUTE");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location Coordinates", location.getLatitude() + " , " + location.getLongitude());
                lo.setText(location.getLatitude() + " , " + location.getLongitude());
                lat = location.getLatitude() + " ";
                lon = location.getLongitude() + " ";
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            Log.d("Location Manager","Triggered");
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }
            catch (Exception e) {
                Log.d("EXception", e.getMessage().toString());
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }
        }
    }

    private void pick() {
        Intent gintent = new Intent();
        gintent.setType("image/*");
        gintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gintent.createChooser(gintent, "Select image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                imageView.setImageURI(resultUri);
                im = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error + " ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void p(View view) {

        final String d, t;
        d = text.getText().toString();
        t = item;
        if (d.isEmpty() || t.isEmpty() || !im) {
            Toast.makeText(this, "Fill the dis and type", Toast.LENGTH_SHORT).show();
        } else {
            mStorageRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            final String url = uri.toString();
                            firestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            n = task.getResult().getString("name");
                                            pi = task.getResult().getString("image");

                                            Map<String, Object> post = new HashMap<>();
                                            post.put("image", url);
                                            post.put("pimage", pi);
                                            post.put("name", n);
                                            post.put("post", d);
                                            post.put("type", t);
                                            post.put("uid", uid);
                                            db.collection("Posts").document(uid).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(addpost.this, "Posted", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(addpost.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public void lo(View view) {
        if (lat.isEmpty() || lon.isEmpty()) {

        }
        else {
            Intent intent = new Intent(addpost.this,MapsActivity.class);
            intent.putExtra("lon",lon);
            intent.putExtra("lat",lat);
            startActivity(intent);
        }
    }
}
