package org.tensorflow.lite.examples.classification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Repost extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance().getInstance(); // 실시간 데이터 베이스 (서버에 연동 시킬 수 있는)

    private EditText mTitle, mContents; //게시물 게시할 때 제목, 내용

    private String nickname;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repost);

        mTitle = findViewById(R.id.repost_title_edit);
        mContents = findViewById(R.id.repost_contents_edit);

        Intent getintent = getIntent();
        id = getIntent().getStringExtra(FirebaseID.documentId);
        Log.e("ITEM DOCUMENT ID:", id);

        findViewById(R.id.repost_save_button).setOnClickListener(this);

        mStore.collection(FirebaseID.post).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                if (task.getResult() != null) {
                                    Map<String, Object> snap = task.getResult().getData();
                                    String title = String.valueOf(snap.get(FirebaseID.title));
                                    String contents = String.valueOf(snap.get(FirebaseID.contents));
                                    String name = String.valueOf(snap.get(FirebaseID.nickname));

                                    mTitle.setText(title);
                                    mContents.setText(contents);
                                }
                            } else {
                                Toast.makeText(Repost.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (mAuth.getCurrentUser()!=null) {
            DocumentReference washingtonRef = mStore.collection(FirebaseID.post).document(id);

// Set the "isCapital" field of the city 'DC'
            washingtonRef
                    .update(FirebaseID.title, mTitle.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error updating document", e);
                        }
                    });
            washingtonRef
                    .update(FirebaseID.contents, mContents.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error updating document", e);
                        }
                    });
            finish();
        }
    }
}