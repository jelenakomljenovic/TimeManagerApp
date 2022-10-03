package com.timeManager.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.epicstudio.or3432.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class new_user_admin extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn_new_user;
    EditText new_user_name, new_user_email, new_user_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_admin);

        btn_new_user = findViewById(R.id.btn_new_user);
        new_user_name = findViewById(R.id.new_user_name);
        new_user_email = findViewById(R.id.new_user_email);
        new_user_phone = findViewById(R.id.new_user_phone);

        btn_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> data = new HashMap<>();
                data.put("name", new_user_name.getText().toString());
                data.put("email", new_user_email.getText().toString());
                data.put("phone", new_user_phone.getText().toString());
                data.put("last_session_id", null);
                db.collection("users").add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), admin_user_profile.class);
                                intent.putExtra("user_id", documentReference.getId());
                                startActivity(intent);
                            }
                        });

            }
        });
    }
}