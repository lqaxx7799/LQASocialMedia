package com.example.lqasocialmedia.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ItemKeyedDataSource;

import com.example.lqasocialmedia.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private FirebaseFirestore db;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAll(@NonNull final ItemKeyedDataSource.LoadCallback<User> callback) {
        db.collection("users")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.w("", "exception in fetching from firestore", e);
                        return;
                    }
                    List<User> userList = new ArrayList<>();
                    for(DocumentSnapshot doc : snapshots.getDocuments()){
                        userList.add(doc.toObject(User.class));
                    }

                    if(userList.size() == 0){
                        return;
                    }
                    if(callback instanceof ItemKeyedDataSource.LoadInitialCallback){
                        //initial load
                        ((ItemKeyedDataSource.LoadInitialCallback)callback)
                                .onResult(userList, 0, userList.size());
                    }else{
                        //next pages load
                        callback.onResult(userList);
                    }
                }
            });
    }
}
