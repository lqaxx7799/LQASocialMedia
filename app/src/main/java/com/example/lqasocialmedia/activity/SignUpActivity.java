package com.example.lqasocialmedia.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.Session;
import com.example.lqasocialmedia.dto.SignUpDTO;
import com.example.lqasocialmedia.model.Account;
import com.example.lqasocialmedia.network.NetworkProvider;
import com.example.lqasocialmedia.network.SocialMediaService;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private static final String LOG_TAG = "SignUpActivity";

    private EditText txtEmail, txtPassword, txtReenterPassword, txtUsername, txtName, txtPhoneNumber, txtDateOfBirth, txtBio, txtProfilePicture;
    private Button btnDateOfBirth, btnSignUp, btnBack, btnProfilePicture;
    private Spinner spnGender;
    private TextView lblSignUpError;

    private Session session;
    private SocialMediaService socialMediaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        session = new Session(this);
        socialMediaService = NetworkProvider.getInstance().getRetrofit().create(SocialMediaService.class);

        initView();

        btnDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth);
                        txtDateOfBirth.setText(dateString);
                    }
                }, y, m, d).show();
            }
        });

        btnProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionAndBrowseFile();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpDTO signUpDTO = new SignUpDTO();
                signUpDTO.setBio(txtBio.getText().toString());
                signUpDTO.setDateOfBirth(stringToDate(txtDateOfBirth.getText().toString()));
                signUpDTO.setEmail(txtEmail.getText().toString());
                signUpDTO.setGender(spnGender.getSelectedItem().toString());
                signUpDTO.setName(txtName.getText().toString());
                signUpDTO.setPassword(txtPassword.getText().toString());
                signUpDTO.setPhoneNumber(txtPhoneNumber.getText().toString());
                signUpDTO.setReenterPassword(txtReenterPassword.getText().toString());
                signUpDTO.setUsername(txtUsername.getText().toString());
                signUpDTO.setProfilePicture(txtProfilePicture.getText().toString());

                trySignUp(getSignUpRequestBody(signUpDTO));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtReenterPassword = findViewById(R.id.txtReenterPassword);
        txtUsername = findViewById(R.id.txtUsername);
        txtName = findViewById(R.id.txtName);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtDateOfBirth = findViewById(R.id.txtDateOfBirth);
        txtBio = findViewById(R.id.txtBio);
        txtProfilePicture = findViewById(R.id.txtProfilePicture);
        btnDateOfBirth = findViewById(R.id.btnDateOfBirth);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnBack = findViewById(R.id.btnBack);
        btnProfilePicture = findViewById(R.id.btnProfilePicture);

        spnGender = findViewById(R.id.spnGender);
        lblSignUpError = findViewById(R.id.lblSignUpError);

        lblSignUpError.setText("");
    }

    private void trySignUp(MultipartBody requestBody) {
        Log.i(LOG_TAG, "Start request");
        socialMediaService.signUp(requestBody).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    session.setToken(response.body().getEmail());
                    session.setAccountId(Integer.parseInt(response.body().getId()));
                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
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
                    lblSignUpError.setText(error);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }

    private static MultipartBody getSignUpRequestBody(SignUpDTO signUpDTO) {
        final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        File profilePicture = new File(signUpDTO.getProfilePicture());
        requestBodyBuilder.addFormDataPart(
                "profilePicture",
                profilePicture.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), profilePicture)
        );

        Log.i(LOG_TAG, "Size " + profilePicture.getName() + ": " + profilePicture.getTotalSpace());

        requestBodyBuilder.addFormDataPart(
                "dateOfBirth",
                null,
                RequestBody.create(MediaType.parse("text/plain"), dateToISOString(signUpDTO.getDateOfBirth()))
        );
        requestBodyBuilder.addFormDataPart(
                "bio",
                null,
                RequestBody.create(MediaType.parse("text/plain"), signUpDTO.getBio())
        );
        requestBodyBuilder.addFormDataPart(
                "email",
                null,
                RequestBody.create(MediaType.parse("text/plain"), signUpDTO.getEmail())
        );
        requestBodyBuilder.addFormDataPart(
                "gender",
                null,
                RequestBody.create(MediaType.parse("text/plain"), signUpDTO.getGender())
        );
        requestBodyBuilder.addFormDataPart(
                "name",
                null,
                RequestBody.create(MediaType.parse("text/plain"), signUpDTO.getName())
        );
        requestBodyBuilder.addFormDataPart(
                "password",
                null,
                RequestBody.create(MediaType.parse("text/plain"), signUpDTO.getPassword())
        );
        requestBodyBuilder.addFormDataPart(
                "reenterPassword",
                null,
                RequestBody.create(MediaType.parse("text/plain"), signUpDTO.getReenterPassword())
        );
        requestBodyBuilder.addFormDataPart(
                "phoneNumber",
                null,
                RequestBody.create(MediaType.parse("text/plain"), signUpDTO.getPhoneNumber())
        );
        requestBodyBuilder.addFormDataPart(
                "username",
                null,
                RequestBody.create(MediaType.parse("text/plain"), signUpDTO.getUsername())
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
                        Log.i(LOG_TAG, "Uri: " + fileUri);

                        String filePath = null;
                        try {
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor = getContentResolver().query(fileUri, filePathColumn, null, null, null);
                            if(cursor.moveToFirst()){
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                filePath = cursor.getString(columnIndex);
                                Log.i(LOG_TAG, "Path: " + filePath);
                            } else {
                                //boooo, cursor doesn't have rows ...
                            }
                            cursor.close();
//                            filePath = fileUri.getPath(); // FileUtils.getPath(this, fileUri);
                        } catch (Exception e) {
                            Log.e(LOG_TAG,"Error: " + e);
                            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.txtProfilePicture.setText(filePath);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath()  {
        return this.txtProfilePicture.getText().toString();
    }

    private static Date stringToDate(String input) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return df.parse(input);
        } catch (Exception e) {
            return new Date();
        }
    }

    private static String dateToISOString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return df.format(date);
    }
}