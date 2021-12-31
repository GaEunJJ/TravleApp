package org.tensorflow.lite.examples.classification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class Post2Activity extends AppCompatActivity {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private TextView mTitleText, mContentsText, mNameText;

    private String id;

    ImageView load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);

        mTitleText = findViewById(R.id.post2_title);
        mContentsText = findViewById(R.id.post2_contents);
        mNameText = findViewById(R.id.post2_name);

        Intent getintent = getIntent();
        id = getIntent().getStringExtra(FirebaseID.documentId);
        Log.e("ITEM DOCUMENT ID:", id);

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

                                    mTitleText.setText(title);
                                    mContentsText.setText(contents);
                                    mNameText.setText(name);

                                    load=(ImageView)findViewById(R.id.postimg2);
                                    FirebaseStorage storage=FirebaseStorage.getInstance();
                                    StorageReference storageReference = storage.getReferenceFromUrl("gs://travelproject-ca1fd.appspot.com");
                                    StorageReference pathReference = storageReference.child("photo");
                                    if (pathReference == null) {
                                        Toast.makeText(Post2Activity.this, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        StorageReference submitProfile = storageReference.child("photo/" + snap.get(FirebaseID.documentId));
                                        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(Post2Activity.this).load(uri).into(load);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                    }
                                }
                            } else {
                                Toast.makeText(Post2Activity.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}