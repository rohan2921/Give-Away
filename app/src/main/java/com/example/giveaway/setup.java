package com.example.giveaway;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class setup extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView image;
    Button submit;
    FirebaseUser currentUser;
    StorageReference storageReference;
    FirebaseFirestore db ;
    Uri resultUri;
    boolean im = false;
    EditText name;
    String url,n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        submit = (Button) findViewById(R.id.submit);
        name = (EditText) findViewById(R.id.name);
        image = (CircleImageView) findViewById(R.id.profile_image);
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Users").child(currentUser.getUid());
        setTitle("Settings");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            assert  getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        n = task.getResult().getString("name");
                        url = task.getResult().getString("image");
                        name.setText(n);
                        Picasso.get().load(url).placeholder(R.drawable.profile).into(image);

                    }
                }
            }
        });
    }

    private void p() {
        Intent gintent = new Intent();
        gintent.setType("image/*");
        gintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gintent.createChooser(gintent,"Select image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                storageReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url =  uri.toString();
                                Picasso.get().load(url).into(image);
                    }
                });
            }});
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this,error+" ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void submit(View view) {
        n = name.getText().toString();
        if(n.isEmpty() || url.isEmpty()) {
            Toast.makeText(this,"Add image and name",Toast.LENGTH_SHORT).show();
        }
        else {
            Map<String, Object> user = new HashMap<>();
            user.put("email", currentUser.getEmail());
            //user.put("phone", ph);
            user.put("image", url);
            user.put("name", n);
            db.collection("Users").document(currentUser.getUid().toString()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(setup.this,"Sucess",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(setup.this,MainActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
