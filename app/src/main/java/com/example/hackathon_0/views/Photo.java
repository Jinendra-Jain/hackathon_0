package com.example.hackathon_0.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hackathon_0.Model;
import com.example.hackathon_0.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class Photo extends AppCompatActivity {

    private Button uploadBtn,showAllBtn;
    private ProgressBar progressBar;
    private ImageView vImage;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        getSupportActionBar().hide();

        progressBar = findViewById(R.id.progressBar2);
        vImage = findViewById(R.id.imageView);
        uploadBtn = findViewById(R.id.upload_button);
        showAllBtn = findViewById(R.id.gallery_button);

        progressBar.setVisibility(View.INVISIBLE);
        String imageUrl = getIntent().getStringExtra("imageUri");
        mUri = Uri.parse(imageUrl);
        vImage.setImageURI(mUri);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mUri != null){

                    uploadToFirebase(mUri);

                }else{

                    Toast.makeText(Photo.this, "Please Capture Image", Toast.LENGTH_SHORT).show();

                }

            }
        });

        showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(Photo.this, ShowActivity.class));
            }
        });

    }

    private void uploadToFirebase(Uri uri) {

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Model model = new Model(uri.toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(Photo.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        vImage.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {

                progressBar.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Photo.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

}