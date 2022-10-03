package com.timeManager.check;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.epicstudio.or3432.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class admin_user_profile extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView user_qr;
    private TextView user_id_txt, user_name, user_email, user_phone;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_profile);
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_phone = findViewById(R.id.user_phone);
        user_id_txt = findViewById(R.id.user_id_txt);
        user_qr = findViewById(R.id.user_qr);

        DocumentReference docRef = db.collection("users").document(user_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    user_id_txt.setText("User ID: " + user_id);
                    user_name.setText("Name: " + document.getString("name"));
                    user_email.setText("Email: " + document.getString("email"));
                    user_phone.setText("Phone: " + document.getString("phone"));

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int dimen = width < height ? width : height;
                    dimen = dimen * 3 / 4;
                    qrgEncoder = new QRGEncoder(user_id.toString(), null, QRGContents.Type.TEXT, dimen);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        user_qr.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Toast.makeText(getApplicationContext(), "TAG: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(admin_user_profile.this, Admin_Home.class);
        startActivity(intent);
    }

}