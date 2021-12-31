package org.tensorflow.lite.examples.classification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance(); //파이어스토어 연결
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스 (서버에 연동 시킬 수 있는)
    private EditText mNickname, mEtEmail, mEtPwd, mEtRePwd;      // 회원가입 입력필드
    private Button mBtnRegister;            // 회원가입 입력버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("travel_project");

        mNickname = findViewById(R.id.sign_nickname);
        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtRePwd = findViewById(R.id.et_repwd);
        mBtnRegister = findViewById(R.id.btn_register);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                           // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();    // 사용자가 입력한 값 문자열로 변환해 가져옴.
                String strPwd = mEtPwd.getText().toString();
                String strRePwd = mEtRePwd.getText().toString();


                //Firebase Auth 진행
                if(strPwd.equals(strRePwd)==true) {
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put(FirebaseID.documentId, firebaseUser.getUid());
                                userMap.put(FirebaseID.nickname, mNickname.getText().toString());
                                userMap.put(FirebaseID.email, mEtEmail.getText().toString());
                                userMap.put(FirebaseID.password, mEtPwd.getText().toString());
                                mStore.collection(FirebaseID.user).document(firebaseUser.getUid()).set(userMap, SetOptions.merge());
                                UserAccount account = new UserAccount();
                                account.setIdToken(firebaseUser.getUid());
                                account.setEmailId(firebaseUser.getEmail());
                                account.setPassword(strPwd);

                                // setValue : database에 insert(삽입)하는 행위
                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다!", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        finish();
                                        startActivity(intent);
                                    }
                                }, 600);// 0.6초 정도 딜레이를 준 후 시작

                            } else {
                                Toast.makeText(RegisterActivity.this, "회원가입에 실패 하셨습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패 하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}