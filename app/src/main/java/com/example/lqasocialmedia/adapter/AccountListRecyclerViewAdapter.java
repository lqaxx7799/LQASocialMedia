package com.example.lqasocialmedia.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.model.Account;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class AccountListRecyclerViewAdapter extends RecyclerView.Adapter<AccountListRecyclerViewAdapter.AccountListItemViewHolder> {
    private Activity activity;
    private List<Account> data;
    
    public AccountListRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public void setData(List<Account> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public AccountListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.account_list_item, parent, false);
        return new AccountListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListItemViewHolder holder, int position) {
        Account account = this.data.get(position);
        if (account == null) {
            return;
        }
        if (account.getProfilePictureUrl() == null || account.getProfilePictureUrl().equals("")) {
            Glide.with(this.activity).load(activity.getResources().getString(R.string.default_profile_picture_url)).into(holder.accountImgProfilePicture);
        } else {
            Glide.with(this.activity).load(account.getProfilePictureUrl()).into(holder.accountImgProfilePicture);
        }
        holder.accountLblUsername.setText(account.getUserName());
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Clicked on account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data == null ? 0 : this.data.size();
    }

    public class AccountListItemViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView accountImgProfilePicture;
        private TextView accountLblUsername;
        
        public AccountListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            accountImgProfilePicture = itemView.findViewById(R.id.accountImgProfilePicture);
            accountLblUsername = itemView.findViewById(R.id.accountLblUsername);
        }
    }
}
