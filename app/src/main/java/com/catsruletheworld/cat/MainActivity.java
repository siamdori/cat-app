package com.catsruletheworld.cat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kakao.sdk.user.UserApi;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="사용자";
    private ImageButton btn_login;
    Account user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this,(oAuthToken, error) -> {
                        if (error != null) {
                            Log.e(TAG, "로그인 실패", error);
                        } else if (oAuthToken != null) {
                            Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());

                            UserApiClient.getInstance().me((user, meError) -> {
                                if (meError != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패", meError);
                                } else {
                                    System.out.println("로그인 완료");
                                    Log.i(TAG, user.toString());
                                    {
                                        Log.i(TAG, "사용자 정보 요청 성공"+
                                                "\n회원번호: "+user.getId()+
                                                "\n이메일: "+user.getKakaoAccount().getEmail()+
                                                "\n이름: "+user.getKakaoAccount().getProfile().getNickname()+
                                                "\n사진: "+user.getKakaoAccount().getProfile().getProfileImageUrl());
                                    }
                                    user_info = user.getKakaoAccount();

                                    Intent login_intent = new Intent(getApplicationContext(), LoginSuccess.class);
                                    startActivity(login_intent);
                                }
                                return null;
                            });
                        }
                        return null;
                    });
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this,(oAuthToken, error) -> {
                        if (error != null) {
                            Log.e(TAG, "로그인 실패", error);
                        } else if (oAuthToken != null) {
                            Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());

                            UserApiClient.getInstance().me((user, meError) -> {
                                if (meError != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패", meError);
                                } else {
                                    System.out.println("로그인 완료");
                                    Log.i(TAG, user.toString());
                                    {
                                        Log.i(TAG, "사용자 정보 요청 성공" +
                                                "\n회원번호: "+user.getId() +
                                                "\n이메일: "+user.getKakaoAccount().getEmail());
                                    }
                                    user_info = user.getKakaoAccount();

                                    Intent login_intent = new Intent(getApplicationContext(), LoginSuccess.class);
                                    startActivity(login_intent);
                                }
                                return null;
                            });
                        }
                        return null;
                    });
                }
            }
        });

        /*
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else if (user != null){
                Log.i(TAG, user.toString());
                {

                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: " + user.getId() +
                            "\n이메일: " + user.getKakaoAccount().getEmail() +
                            "\n닉네임: " + user.getKakaoAccount().getProfile().getNickname() +
                            "\n프로필사진: " + user.getKakaoAccount().getProfile().getThumbnailImageUrl());
                }

            }
            return null;
        });
         */
    }
}