package com.timeManager.check;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epicstudio.or3432.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class admin_user_entry extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView entrylistRecyclerView;
    ArrayList<user_entry_model> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_entry);

        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");

        db.collection("user_logins")
                .whereEqualTo("user_id", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("out").toString().equals("0")) {
                                    data.add(new user_entry_model(document.getDate("in").toString(), "null"));
                                } else {
                                    data.add(new user_entry_model(document.getDate("in").toString(), document.getDate("out").toString()));
                                }
                                entrylistRecyclerView = findViewById(R.id.user_entry_recycle);
                                user_entry_adapter adapter = new user_entry_adapter(data);
                                entrylistRecyclerView.setAdapter(adapter);
                                entrylistRecyclerView.addItemDecoration(new DividerItemDecoration(admin_user_entry.this, DividerItemDecoration.VERTICAL));
                                entrylistRecyclerView.setLayoutManager(new LinearLayoutManager(admin_user_entry.this, RecyclerView.VERTICAL, false));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}