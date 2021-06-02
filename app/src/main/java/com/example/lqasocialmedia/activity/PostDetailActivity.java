package com.example.lqasocialmedia.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.model.Account;
import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.util.CommonUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {
    private ShapeableImageView imgProfilePicture;
    private TextView lblUserName;
    private ImageView imgThumbnail;
    private ImageView btnLike;
    private ImageView btnComment;
    private TextView lblCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        initView();

        Intent intent = getIntent();
        String dataJson = intent.getStringExtra("post");
        Post post = new Post();
        Type type = new TypeToken<Post>() {
        }.getType();
        post = new Gson().fromJson(dataJson, type);

        if (post.getAccount().getProfilePictureUrl() == null || post.getAccount().getProfilePictureUrl().equals("")) {
            Glide.with(this).load(getResources().getString(R.string.default_profile_picture_url)).into(imgProfilePicture);
        } else {
            Glide.with(this).load(CommonUtils.getImageFullPath(post.getAccount().getProfilePictureUrl())).into(imgProfilePicture);
        }

        lblUserName.setText(post.getAccount().getUserName());
        Glide.with(this).load(CommonUtils.getImageFullPath(post.getThumbnailUrl())).into(imgThumbnail);
        lblCaption.setText(post.getCaption());
//        if (post.isLiked()) {
//            holder.btnLike.setColorFilter(activity.getResources().getColor(R.color.liked, activity.getTheme()));
//        }

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailActivity.this, "comment", Toast.LENGTH_LONG).show();
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailActivity.this, "like", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        imgProfilePicture = findViewById(R.id.imgProfilePicture);
        lblUserName = findViewById(R.id.lblUserName);
        imgThumbnail = findViewById(R.id.imgThumbnail);
        btnLike = findViewById(R.id.btnLike);
        btnComment = findViewById(R.id.btnComment);
        lblCaption = findViewById(R.id.lblCaption);
    }
}