package com.example.homepharmacy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class cAdmin_Products_View extends AppCompatActivity implements cImageAdapter.onItemClickListener {

    private RecyclerView mRecyclerView;
    private cImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<cUpload> mUploads;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__products);


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

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mAdapter = new cImageAdapter(cAdmin_Products_View.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(cAdmin_Products_View.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    cUpload upload = postSnapshot.getValue(cUpload.class);
                    upload.setmKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(cAdmin_Products_View.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        //  Toast.makeText(this,"Normal click at position :"+ position , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateClick(int position) {
        //   Toast.makeText(this,"Update click at position :"+ position , Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,"Update click at position :"+ position , Toast.LENGTH_SHORT).show();
        cUpload selectItem = mUploads.get(position);
        final String selectedKey = selectItem.getmKey();

        Intent intent = new Intent(this, updateProduct.class);
        intent.putExtra("name", selectItem.getName());
        intent.putExtra("price", selectItem.getmPrice());
        intent.putExtra("quantity", selectItem.getmQuantity());
        intent.putExtra("imageUrl", selectItem.getImageUrl());
        intent.putExtra("key", selectedKey);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        final cUpload selectItem = mUploads.get(position);
        final String selectedKey = selectItem.getmKey();

        AlertDialog.Builder builder = new AlertDialog.Builder(cAdmin_Products_View.this);
        builder.setMessage("Delete this Item ? : ")
                .setTitle("Confirm Delete")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StorageReference imageRef = mStorage.getReferenceFromUrl(selectItem.getImageUrl());
                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mDatabaseRef.child(selectedKey).removeValue();
                                Toast.makeText(cAdmin_Products_View.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CANCEL
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}