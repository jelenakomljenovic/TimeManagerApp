package com.timeManager.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.epicstudio.or3432.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private Button btn_qrscan, btn_adminarea;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_qrscan = findViewById(R.id.qr_scan_button);
        btn_adminarea = findViewById(R.id.btn_adminarea);

        btn_qrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        login.this
                );
                intentIntegrator.setPrompt("For flash use volume up key");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();

            }
        });

        btn_adminarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, admin_login.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        if (intentResult.getContents() != null) {
            if (DetectConnection.isInternetAvailable(login.this)) {
                DocumentReference docRef = db.collection("users").document(intentResult.getContents());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String last_session_id_val = document.getString("last_session_id");
                                /*DB user_logins*/
                                if (last_session_id_val == null) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("user_id", intentResult.getContents());
                                    data.put("in", FieldValue.serverTimestamp());
                                    data.put("out", "0");
                                    db.collection("user_logins").add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    db.collection("users").document(intentResult.getContents()).update("last_session_id", documentReference.getId());
                                                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    intent.putExtra("user_id", intentResult.getContents());
                                                    startActivity(intent);
                                                }
                                            });
                                } else {
                                    db.collection("user_logins").document(last_session_id_val).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot user_logins_document = task.getResult();
                                                if (user_logins_document.exists()) {
                                                    if (user_logins_document.get("out").toString().equals("0")) {
                                                        db.collection("user_logins").document(last_session_id_val)
                                                                .update("out", FieldValue.serverTimestamp());
                                                        Toast.makeText(getApplicationContext(), "Success, You Have Been Exited", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        intent.putExtra("user_id", intentResult.getContents());
                                                        startActivity(intent);
                                                    } else {
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("user_id", intentResult.getContents());
                                                        data.put("in", FieldValue.serverTimestamp());
                                                        data.put("out", "0");
                                                        db.collection("user_logins").add(data)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        db.collection("users").document(intentResult.getContents()).update("last_session_id", documentReference.getId());
                                                                        Toast.makeText(getApplicationContext(), "Success, You Have Been Entered", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                        intent.putExtra("user_id", intentResult.getContents());
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid QR", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection!", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Can't Scan QR", Toast.LENGTH_SHORT)
                    .show();
        }

    }
}
