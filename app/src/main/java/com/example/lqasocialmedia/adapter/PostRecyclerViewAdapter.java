package com.example.lqasocialmedia.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.util.CommonUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<Post> data;
    private Activity activity;
    private static final String TAG = "PostRecyclerViewAdapter";
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public PostRecyclerViewAdapter(Activity activity, List<Post> data) {
        this.activity = activity;
        this.data = data;
    }

//    public void setData(List<Post> data) {
//        this.data = data;
//        notifyDataSetChanged();
//    }
//
//    public void appendData(List<Post> newData) {
//        if (this.data == null) {
//            setData(newData);
//        } else {
//            this.data.addAll(newData);
//            notifyDataSetChanged();
//        }
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.post_list_item, parent, false);
            return new PostViewHolder(v);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.post_list_loading, parent, false);
            return new LoadingViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostViewHolder) {
            bindPostViewHolder((PostViewHolder) holder, position);
        } else if (holder instanceof  LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (this.data == null) {
            return 0;
        }
        return this.data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void bindPostViewHolder(PostViewHolder holder, int position) {
        Post post = this.data.get(position);
        if (post == null) {
            return;
        }
        if (post.getAccount().getProfilePictureUrl() == null || post.getAccount().getProfilePictureUrl().equals("")) {
            Glide.with(this.activity).load(activity.getResources().getString(R.string.default_profile_picture_url)).into(holder.imgProfilePicture);
        } else {
            Glide.with(this.activity).load(CommonUtils.getImageFullPath(post.getAccount().getProfilePictureUrl())).into(holder.imgProfilePicture);
        }

        holder.lblUserName.setText(post.getAccount().getUserName());
        Glide.with(this.activity).load(CommonUtils.getImageFullPath(post.getThumbnailUrl())).into(holder.imgThumbnail);
        holder.lblCaption.setText(post.getCaption());
//        if (post.isLiked()) {
//            holder.btnLike.setColorFilter(activity.getResources().getColor(R.color.liked, activity.getTheme()));
//        }

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "comment", Toast.LENGTH_LONG).show();
            }
        });

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "like", Toast.LENGTH_LONG).show();
            }
        });

        holder.lblUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "go to profile page", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoadingView(LoadingViewHolder holder, int position) {
        //ProgressBar would be displayed
//        holder.progressBar.setVisibility(View.VISIBLE);
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView imgProfilePicture;
        private TextView lblUserName;
        private ImageView imgThumbnail;
        private ImageView btnLike;
        private ImageView btnComment;
        private TextView lblCaption;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfilePicture = itemView.findViewById(R.id.imgProfilePicture);
            lblUserName = itemView.findViewById(R.id.lblUserName);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            lblCaption = itemView.findViewById(R.id.lblCaption);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.postListProgressBar);
        }
    }
}
