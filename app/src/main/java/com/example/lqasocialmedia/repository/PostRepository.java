package com.example.lqasocialmedia.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ItemKeyedDataSource;

import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    private FirebaseFirestore db;

    public PostRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAll(final String startPost, final int size, @NonNull final ItemKeyedDataSource.LoadCallback<Post> callback) {
        db.collection("posts")
            .whereGreaterThan("id", startPost)
            .limit(size)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.w("", "exception in fetching from firestore", e);
                        return;
                    }
                    List<Post> postList = new ArrayList<>();
                    for(DocumentSnapshot doc : snapshots.getDocuments()){
                        postList.add(doc.toObject(Post.class));
                    }

                    if(postList.size() == 0){
                        return;
                    }
                    if(callback instanceof ItemKeyedDataSource.LoadInitialCallback){
                        //initial load
                        ((ItemKeyedDataSource.LoadInitialCallback)callback)
                                .onResult(postList, 0, postList.size());
                    }else{
                        //next pages load
                        callback.onResult(postList);
                    }
                }
            });
    }
}
