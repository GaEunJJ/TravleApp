package org.tensorflow.lite.examples.classification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.tensorflow.lite.examples.classification.adapters.PostAdapter;
import org.tensorflow.lite.examples.classification.models.Post;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증

    private RecyclerView mPostRecyclerView;

    private PostAdapter mAdapter;
    private List<Post> mDatas;
    String nickname, nickname_here;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        mPostRecyclerView = findViewById(R.id.main_recyclerview);

        findViewById(R.id.main_post_edit).setOnClickListener(this);

        mPostRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, mPostRecyclerView, this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mStore.collection(FirebaseID.post)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                String nickname = String.valueOf(shot.get(FirebaseID.nickname));
                                String title = String.valueOf(snap.get(FirebaseID.title));
                                String contents;
                                if (String.valueOf(shot.get(FirebaseID.contents)).length()>11) {
                                    contents = (String.valueOf(shot.get(FirebaseID.contents))).substring(0,10) + "...";
                                } else {
                                    contents = String.valueOf(shot.get(FirebaseID.contents));
                                }
                                Post data = new Post(documentId, nickname, title, contents);
                                mDatas.add(data);
                            }

                            mAdapter = new PostAdapter(mDatas);
                            mPostRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, PostActivity.class));

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, Post2Activity.class);
        intent.putExtra(FirebaseID.documentId, mDatas.get(position).getDocumentId());
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(View view, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("수정 또는 삭제하시겠습니까?");
        dialog.setNegativeButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mStore.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult() != null) {
                                    nickname = (String) task.getResult().getData().get(FirebaseID.nickname);
                                    DocumentReference nickname_come = mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentId());
                                    nickname_come.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    nickname_here = (String) document.get(FirebaseID.nickname);
                                                    if (nickname.equals(nickname_here)) {
                                                        Intent intent = new Intent(getApplicationContext(), Repost.class);
                                                        intent.putExtra(FirebaseID.documentId, mDatas.get(position).getDocumentId());
                                                        startActivity(intent);
                                                    }
                                                    else
                                                        Toast.makeText(CommunityActivity.this, "타인의 글을 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    //Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                //Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mStore.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult() != null) {
                                    nickname = (String) task.getResult().getData().get(FirebaseID.nickname);
                                    DocumentReference nickname_come = mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentId());
                                    nickname_come.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    nickname_here = (String) document.get(FirebaseID.nickname);
                                                    if (nickname.equals(nickname_here)) {
                                                        mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentId()).delete();
                                                        Toast.makeText(CommunityActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                        Toast.makeText(CommunityActivity.this, "타인의 글을 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    //Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                //Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });
        dialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CommunityActivity.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setTitle("게시글");
        dialog.show();
    }
}