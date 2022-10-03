package com.timeManager.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Admin_Home extends AppCompatActivity {
    Button btn_new_user_activity;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView mRecyclerView;
    ArrayList<users_list_model> data = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btn_new_user_activity = findViewById(R.id.btn_new_user_activity);

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data.add(new users_list_model(document.getString("name"), document.getId()));
                                mRecyclerView = findViewById(R.id.users_recycle);
                                users_list_adapter adapter = new users_list_adapter(data);
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.addItemDecoration(new DividerItemDecoration(Admin_Home.this, DividerItemDecoration.VERTICAL));
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(Admin_Home.this, RecyclerView.VERTICAL, false));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        btn_new_user_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), new_user_admin.class);
                startActivity(intent);
            }
        });
    }
}
