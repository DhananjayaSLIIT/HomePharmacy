package com.example.homepharmacy;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class updateProduct extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private String name, price, quantity, imageUrl, key, myimage2;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private Button mTextViewShowUploads;
    private EditText mEditTextFileName;
    private EditText etPrice;
    private EditText etQuantity;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri, downloadUrl;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef, dref;
    private StorageTask mUploadTask;
    private Context mContext;
    private cUpload up;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        //Bottom Navigation----------------------------------------------------------------
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Aadd_Items);

        //Bottom Navigation Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Apre_orders:
                        startActivity(new Intent(getApplicationContext(),PresOrders.class));
                        return true;
                    case R.id.Alab_report:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        return true;
                    case R.id.Aadd_Items:
                        startActivity(new Intent(getApplicationContext(),cProduct_Upload.class));
                        return true;
                    case R.id.Alog_out:
                        startActivity(new Intent(getApplicationContext(),cLogin.class));
                        return true;
                }
                return false;
            }
        });
        //-----------------------------------

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        etPrice = findViewById(R.id.edit_text_file_price);
        etQuantity = findViewById(R.id.edit_text_file_quantity);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        // Upload up = new Upload();
        up = new cUpload();
        getData();
        setData();

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                uploadFile();
            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageActivity();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
            //mImageView.setImageURI(mImageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (!check1() || !check2() || !check3()) {
            //signUp.setEnabled(false);
            return;
        }
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //    mProgressBar.setProgress(0);
                                }
                            }, 500);


                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            downloadUrl = urlTask.getResult();


                            DatabaseReference upref = FirebaseDatabase.getInstance().getReference().child("uploads").child(key);
                            upref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // if(dataSnapshot.hasChild(key)){

                                    up.setName(mEditTextFileName.getText().toString().trim());
                                    up.setmPrice(etPrice.getText().toString().trim());
                                    up.setmQuantity(etQuantity.getText().toString().trim());
                                    up.setImageUrl(downloadUrl.toString());


                                    dref = FirebaseDatabase.getInstance().getReference().child("uploads").child(key);
                                    dref.setValue(up);
                                    clearData();

                                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Not Updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });


        } else {

            DatabaseReference upref = FirebaseDatabase.getInstance().getReference().child("uploads").child(key);
            upref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // if(dataSnapshot.hasChild(key)){

                    up.setName(mEditTextFileName.getText().toString().trim());
                    up.setmPrice(etPrice.getText().toString().trim());
                    up.setmQuantity(etQuantity.getText().toString().trim());
                    up.setImageUrl(downloadUrl.toString());

                    dref = FirebaseDatabase.getInstance().getReference().child("uploads").child(key);
                    dref.setValue(up);
                    clearData();

                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Not Updated successfully", Toast.LENGTH_SHORT).show();
                }
            });


        }


    }

    private void getData() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("imageUrl")) {
            name = getIntent().getStringExtra("name");
            price = getIntent().getStringExtra("price");
            quantity = getIntent().getStringExtra("quantity");
            imageUrl = getIntent().getStringExtra("imageUrl");
            key = getIntent().getStringExtra("key");
        } else {
            Toast.makeText(this, "No Data...", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        mEditTextFileName.setText(name);
        etPrice.setText(price);
        etQuantity.setText(quantity);
        Picasso.get().load(imageUrl)
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(mImageView);
    }

    private void openImageActivity() {
        Intent intent = new Intent(this, cAdmin_Products_View.class);
        startActivity(intent);
    }

    private void clearData() {
        mEditTextFileName.setText("");
        etPrice.setText("");
        etQuantity.setText("");
        mImageView.setImageDrawable(null);
    }

    private Boolean check1() {
        String val = mEditTextFileName.getText().toString();
        if (val.isEmpty()) {
            Toast.makeText(this, "Product Name cannot be Empty", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private Boolean check2() {
        String val = etPrice.getText().toString();
        if (val.isEmpty()) {
            Toast.makeText(this, "Price cannot be Empty", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private Boolean check3() {
        String val = etQuantity.getText().toString();
        if (val.isEmpty()) {
            Toast.makeText(this, "Quantity cannot be Empty", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
}