package com.example.lqasocialmedia.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.Session;
import com.example.lqasocialmedia.dto.PostDTO;
import com.example.lqasocialmedia.dto.SignUpDTO;
import com.example.lqasocialmedia.model.Account;
import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.network.NetworkProvider;
import com.example.lqasocialmedia.network.SocialMediaService;
import com.example.lqasocialmedia.util.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private static final String LOG_TAG = "CreatePostActivity";

    private EditText txtCaption, txtThumbnail;
    private Button btnThumbnail, btnCreate, btnBack;
    private TextView lblCreateError;

    private Uri thumbnailUri;

    private Session session;
    private SocialMediaService socialMediaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initView();

        session = new Session(this);
        socialMediaService = NetworkProvider.getInstance().getRetrofit().create(SocialMediaService.class);

        btnThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermissionAndBrowseFile();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostDTO postDTO = new PostDTO();
                postDTO.setAccountId(session.getAccountId());
                postDTO.setCaption(txtCaption.getText().toString());
                postDTO.setThumbnail(txtThumbnail.getText().toString());

                boolean isValid = validate(postDTO);
                if (!isValid) {
                    return;
                }
                tryCreatePost(getPostRequestBody(postDTO));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        txtCaption = findViewById(R.id.txtCaption);
        txtThumbnail = findViewById(R.id.txtThumbnail);
        btnThumbnail = findViewById(R.id.btnThumbnail);
        btnCreate = findViewById(R.id.btnCreate);
        btnBack = findViewById(R.id.btnBack);
        lblCreateError = findViewById(R.id.lblCreateError);

        lblCreateError.setText("");
    }

    private void tryCreatePost(MultipartBody requestBody) {
        Log.i(LOG_TAG, "Start request");
        socialMediaService.createPost(requestBody).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    finish();
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
                    lblCreateError.setText(error);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.i(LOG_TAG, "Error: " + t.getMessage());
            }
        });
    }

    private MultipartBody getPostRequestBody(PostDTO postDTO) {
        final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (!postDTO.getThumbnail().equals("") && this.thumbnailUri != null) {
            File thumbnail = FileUtils.getFile(this, thumbnailUri);
            requestBodyBuilder.addFormDataPart(
                    "thumbnail",
                    thumbnail.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), thumbnail)
            );
            Log.i(LOG_TAG, "Size " + thumbnail.getName() + ": " + thumbnail.getTotalSpace());
        }

        requestBodyBuilder.addFormDataPart(
                "caption",
                null,
                RequestBody.create(MediaType.parse("text/plain"), postDTO.getCaption())
        );
        requestBodyBuilder.addFormDataPart(
                "accountId",
                null,
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postDTO.getAccountId()))
        );
        return requestBodyBuilder.build();
    }

    private void askPermissionAndBrowseFile()  {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23

            // Check if we have Call permission
            int permisson = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }
        }
        this.doBrowseFile();
    }

    private void doBrowseFile()  {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(LOG_TAG,"Permission granted!");
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();

                    this.doBrowseFile();
                }
                // Cancelled or denied.
                else {
                    Log.i(LOG_TAG,"Permission denied!");
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null)  {
                        Uri fileUri = data.getData();
                        this.thumbnailUri = fileUri;
                        Log.i(LOG_TAG, "Uri: " + fileUri);

                        String filePath = null;
                        try {
                            filePath = fileUri.getPath();
//                            filePath = getPath(fileUri); // FileUtils.getPath(this, fileUri);
                        } catch (Exception e) {
                            Log.e(LOG_TAG,"Error: " + e);
                            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.txtThumbnail.setText(filePath);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validate(PostDTO postDTO) {
        boolean isValid = true;
        txtCaption.setError(null);
        txtThumbnail.setError(null);

        if (postDTO.getCaption().equals("")) {
            txtCaption.setError("Caption is required");
            isValid = false;
        }

        if (postDTO.getThumbnail().equals("")) {
            txtThumbnail.setError("Thumbnail is required");
            isValid = false;
        }

        return isValid;
    }
}