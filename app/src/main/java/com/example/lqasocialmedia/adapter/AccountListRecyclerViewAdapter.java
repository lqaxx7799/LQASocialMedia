package com.example.lqasocialmedia.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.Session;
import com.example.lqasocialmedia.activity.HomeActivity;
import com.example.lqasocialmedia.activity.SignUpActivity;
import com.example.lqasocialmedia.model.Account;
import com.example.lqasocialmedia.model.UserFollowing;
import com.example.lqasocialmedia.network.NetworkProvider;
import com.example.lqasocialmedia.network.SocialMediaService;
import com.example.lqasocialmedia.util.CommonUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountListRecyclerViewAdapter extends RecyclerView.Adapter<AccountListRecyclerViewAdapter.AccountListItemViewHolder> {
    private final static String LOG_TAG = "AccountListRecyclerView";
    private Activity activity;
    private List<Account> data;
    private Session session;
    private SocialMediaService socialMediaService;
    
    public AccountListRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
        this.session = new Session(activity);
        this.socialMediaService = NetworkProvider.getInstance().getRetrofit().create(SocialMediaService.class);
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
            Glide.with(this.activity).load(CommonUtils.getImageFullPath(account.getProfilePictureUrl())).into(holder.accountImgProfilePicture);
        }
        holder.accountLblUsername.setText(account.getUserName());

        if (account.getId() == session.getAccountId()) {
            holder.accountBtnFollow.setText("Follow");
            holder.accountBtnFollow.setEnabled(false);
        } else {
            UserFollowing foundFollowed = account.getUserFollowings2()
                    .stream()
                    .filter(c -> c.getId().getAccountId() == session.getAccountId())
                    .findFirst()
                    .orElse(null);
//        UserFollowing foundFollowed = null;
            if (foundFollowed != null) {
                holder.accountBtnFollow.setText("Unfollow");
                holder.accountBtnFollow.setBackgroundColor(activity.getResources().getColor(R.color.liked, activity.getTheme()));
            } else {
                holder.accountBtnFollow.setText("Follow");
                holder.accountBtnFollow.setBackgroundColor(activity.getResources().getColor(R.color.purple_500, activity.getTheme()));
            }

            holder.accountBtnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (foundFollowed != null) {
                        tryUnfollow(position, session.getAccountId(), account.getId());
                    } else {
                        tryFollow(position, session.getAccountId(), account.getId());
                    }
                }
            });
        }

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

    private void tryFollow(int position, int accountId, int targetId) {
        Log.i(LOG_TAG, "Start request");
        socialMediaService.followAccount(accountId, targetId).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    data.set(position, response.body());
                    notifyDataSetChanged();
                    Toast.makeText(activity, "Followed", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder error = new StringBuilder();
                    try {
                        BufferedReader bufferedReader = null;
                        if (response.errorBody() != null) {
                            bufferedReader = new BufferedReader(new InputStreamReader(
                                    response.errorBody().byteStream()));

                            String eLine = null;
                            while ((eLine = bufferedReader.readLine()) != null) {
                                error.append(eLine);
                            }
                            bufferedReader.close();
                        }
                    } catch (Exception e) {
                        error.append(e.getMessage());
                    }
                    Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.i(LOG_TAG, "Error: " + t.getMessage());
            }
        });
    }

    private void tryUnfollow(int position, int accountId, int targetId) {
        Log.i(LOG_TAG, "Start request");
        socialMediaService.unfollowAccount(accountId, targetId).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    data.set(position, response.body());
                    notifyDataSetChanged();
                    Toast.makeText(activity, "Unfollowed", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder error = new StringBuilder();
                    try {
                        BufferedReader bufferedReader = null;
                        if (response.errorBody() != null) {
                            bufferedReader = new BufferedReader(new InputStreamReader(
                                    response.errorBody().byteStream()));

                            String eLine = null;
                            while ((eLine = bufferedReader.readLine()) != null) {
                                error.append(eLine);
                            }
                            bufferedReader.close();
                        }
                    } catch (Exception e) {
                        error.append(e.getMessage());
                    }
                    Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.i(LOG_TAG, "Error: " + t.getMessage());
            }
        });
    }

    public class AccountListItemViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView accountImgProfilePicture;
        private TextView accountLblUsername;
        private Button accountBtnFollow;
        
        public AccountListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            accountImgProfilePicture = itemView.findViewById(R.id.accountImgProfilePicture);
            accountLblUsername = itemView.findViewById(R.id.accountLblUsername);
            accountBtnFollow = itemView.findViewById(R.id.accountBtnFollow);
        }
    }
}
