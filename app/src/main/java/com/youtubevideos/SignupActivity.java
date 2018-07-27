package com.youtubevideos;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youtubevideos.api.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    Context context;
    RoundedImageView iv_user_profile;
    ImageView ivCamera, ivMale, ivFemale;
    TextInputLayout inputName, inputDOB, inputEmail, inputPassword;
    TextInputEditText etName, etDOB, etEmail, etPassword;
    TextView tvMale, tvFemale, tvSignup, tvGenderError, tvLogin;
    private static final String REGEX_EMAIL = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int PERMISSION_REQUEST_CODE = 101;
    String encoded_image="";
    Boolean gender = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        context             = this;
        iv_user_profile     = findViewById(R.id.iv_user_profile);
        ivCamera            = findViewById(R.id.iv_camera);
        ivMale              = findViewById(R.id.ivMale);
        ivFemale            = findViewById(R.id.ivFemale);
        inputName           = findViewById(R.id.inputName);
        inputDOB            = findViewById(R.id.inputDOB);
        inputEmail          = findViewById(R.id.inputEmail);
        inputPassword       = findViewById(R.id.inputPassword);
        etName              = findViewById(R.id.etName);
        etDOB               = findViewById(R.id.etDOB);
        etEmail             = findViewById(R.id.etEmail);
        etPassword          = findViewById(R.id.etPassword);
        tvMale              = findViewById(R.id.tvMale);
        tvFemale            = findViewById(R.id.tvFemale);
        tvSignup            = findViewById(R.id.tvSignup);
        tvGenderError       = findViewById(R.id.tvGenderError);
        tvLogin             = findViewById(R.id.tvLogin);
        myCalendar          = Calendar.getInstance();


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etDOB.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivMale.setOnClickListener(this);
        ivFemale.setOnClickListener(this);
        tvLogin.setOnClickListener(this);


    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDOB.setError(null);
        etDOB.setText(sdf.format(myCalendar.getTime()));
    }


    public Boolean checkValidation(){
        if (etName.getText().toString().trim().length() == 0){
            inputName.setError("Enter name");
            return false;
        }
        if (etDOB.getText().toString().trim().length() == 0){
            inputDOB.setError("Enter name");
            return false;
        }
        if (!ivMale.isSelected() && !ivFemale.isSelected()){
            tvGenderError.setVisibility(View.VISIBLE);
        }
        if (etEmail.getText().toString().trim().length() == 0){
            inputEmail.setError("Enter email");
            return false;
        }
        if (!etEmail.getText().toString().trim().matches(REGEX_EMAIL)){
            inputEmail.setError("Enter valid email");
            return false;
        }
        if (etPassword.getText().toString().trim().length() == 0){
            inputPassword.setError("Enter password");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.etDOB:
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,date,myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                Date today = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(today);
                c.add( Calendar.YEAR, -18);
                long minDate = c.getTime().getTime();

                datePickerDialog.getDatePicker().setMaxDate(minDate);
                datePickerDialog.show();
                break;

            case R.id.tvSignup:
                if (checkValidation()) {
                    if (tvGenderError.getVisibility() == View.GONE) {
                        Singleton.setPref(Constants.USER_NAME,etName.getText().toString(),context);
                        Singleton.setPref(Constants.USER_DOB,etDOB.getText().toString(),context);
                        Singleton.setPref(Constants.USER_EMAIL,etEmail.getText().toString(),context);
                        Singleton.setPref(Constants.USER_PASSWORD,etPassword.getText().toString(),context);
                        if (ivMale.isSelected()) {
                            gender = true;
                        } else if (ivFemale.isSelected()) {
                            gender = false;
                        }
                        Singleton.setPrefBoolean(Constants.USER_GENDER,gender,context);
                        Singleton.setPrefBoolean(Constants.USER_SESSION,true,context);
                        Singleton.setPref(Constants.USER_PROFILE_PIC,encoded_image,context);
                        startActivity(new Intent(context,MainActivity.class));
                    }
                }
                break;

            case R.id.ivMale:
                if (ivMale.isSelected()) {
                    ivMale.setSelected(true);
                } else {
                    ivMale.setSelected(true);
                    ivFemale.setSelected(false);
                }
                break;

            case R.id.ivFemale:
                if (ivFemale.isSelected()) {
                    ivFemale.setSelected(true);
                } else {
                    ivFemale.setSelected(true);
                    ivMale.setSelected(false);

                }
                break;

            case R.id.iv_camera:
                if (!checkPermission())
                    requestPermission();
                else
                    selectImage();
                break;

            case R.id.tvLogin:
                startActivity(new Intent(context,LoginActivity.class));
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // boolean result=Utility.checkPermission(MainActivity.this);

                if (items[item].equals("Take Photo")) {
                    cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, bytes);

        encoded_image = getStringImage(thumbnail);
        iv_user_profile.setImageBitmap(thumbnail);
    }
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {

                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        encoded_image = getStringImage(bm);
        iv_user_profile.setImageBitmap(bm);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {

        ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted) {
                        Log.e("accpted","camera");
                        selectImage();
                    }
                    else {
                        Toast.makeText(context, "Please give the permission to set the profile image", Toast.LENGTH_SHORT).show();


                    }
                }


                break;
        }
    }
}
