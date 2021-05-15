package com.example.lqasocialmedia.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.repository.NetworkState;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class PostRecyclerViewAdapter extends PagedListAdapter<Post, PostRecyclerViewAdapter.PostViewHolder> {
//    private List<Post> data;
    private Activity activity;
    private static final String TAG = "PostRecyclerViewAdapter";
    private NetworkState networkState;

    public PostRecyclerViewAdapter(Activity activity) {
        super(DIFF_CALLBACK);
        this.activity = activity;
    }

//    public void setData(List<Post> data) {
//        this.data = data;
//        notifyDataSetChanged();
//    }

//    public void appendData(List<Post> newData) {
//        this.data.addAll(newData);
//        notifyDataSetChanged();
////        submitList(newData);
//    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.post_list_item, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = getItem(position);
        if (post == null) {
            return;
        }

        Glide.with(this.activity).load(post.getThumbnailUrl()).into(holder.imgProfilePicture);
        holder.lblUserName.setText(post.getUserId());
        Glide.with(this.activity).load(post.getThumbnailUrl()).into(holder.imgThumbnail);
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

//    @Override
//    public int getItemCount() {
//        if (this.data == null) {
//            return 0;
//        }
//        return this.data.size();
//    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        this.networkState = newNetworkState;
    }

    public static final DiffUtil.ItemCallback<Post> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Post>() {
                @Override
                public boolean areItemsTheSame(Post oldItem, Post newItem) {
                    return oldItem.getId() == newItem.getId();
                }
                @Override
                public boolean areContentsTheSame(Post oldItem, Post newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public class PostViewHolder extends RecyclerView.ViewHolder {
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
}
