package com.example.lqasocialmedia.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.util.CommonUtils;

import java.util.List;

public class AccountPostGridAdapter extends RecyclerView.Adapter<AccountPostGridAdapter.AccountPostViewHolder> {
    private List<Post> data;
    private Activity activity;
    
    public AccountPostGridAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public void setData(List<Post> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public AccountPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.account_post_grid_item, parent, false);
        return new AccountPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountPostViewHolder holder, int position) {
        Post post = this.data.get(position);
        Glide.with(this.activity).load(CommonUtils.getImageFullPath(post.getThumbnailUrl())).into(holder.imgGridThumbnail);
        holder.imgGridThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Clicked on post", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.data == null) {
            return 0;
        }
        return this.data.size();
    }

    public class AccountPostViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgGridThumbnail;
        public AccountPostViewHolder(@NonNull View itemView) {
            super(itemView);

            imgGridThumbnail = itemView.findViewById(R.id.imgGridThumbnail);
        }
    }
}
