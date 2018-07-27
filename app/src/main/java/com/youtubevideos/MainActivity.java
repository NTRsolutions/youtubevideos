package com.youtubevideos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.youtubevideos.api.APIService;
import com.youtubevideos.api.Constants;
import com.youtubevideos.api.model.Item;
import com.youtubevideos.api.model.YoutubeResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    Context context;
    RecyclerView rvYoutubeVideos;
    LinearLayoutManager linearLayoutManager;
    YouTubeListAdapter adapter;
    private String lastToken = "";
    ArrayList<Item> itemArrayList;
    SwipeRefreshLayout swipeRefreshLayout;

    RoundedImageView ivProfilePic;
    TextView tvUserName, tvAge, tvGender, tvLogout;
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context             = this;
        rvYoutubeVideos     = findViewById(R.id.rvYoutubeVideos);
        swipeRefreshLayout  = findViewById(R.id.swipeRefreshLayout);
        ivProfilePic        = findViewById(R.id.ivProfilePic);
        tvUserName          = findViewById(R.id.tvUserName);
        tvAge               = findViewById(R.id.tvAge);
        tvGender            = findViewById(R.id.tvGender);
        tvLogout            = findViewById(R.id.tvLogout);
        itemArrayList       = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        rvYoutubeVideos.setLayoutManager(linearLayoutManager);

        adapter             = new YouTubeListAdapter(context,itemArrayList);
        rvYoutubeVideos.setAdapter(adapter);

        //load data from api.
        search(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                search(true);
            }
        });

        Log.e("username",Singleton.getPref(Constants.USER_NAME,context));
        if (Singleton.getPref(Constants.USER_NAME,context) != null)
            tvUserName.setText(Singleton.getPref(Constants.USER_NAME,context));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar dob = Calendar.getInstance();
        try {
            dob.setTime(sdf.parse(Singleton.getPref(Constants.USER_DOB,context)));
            age = Singleton.getAge(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvAge.setText(String.valueOf(age));

        Boolean gender = Singleton.getPrefBoolean(Constants.USER_GENDER,context);
        if (gender)
            tvGender.setText("Male");
        else
            tvGender.setText("Female");


        if (Singleton.getPref(Constants.USER_PROFILE_PIC,context) != null && !Singleton.getPref(Constants.USER_PROFILE_PIC,context).equals("")
                && !Singleton.getPref(Constants.USER_PROFILE_PIC,context).isEmpty()) {
            ivProfilePic.setImageBitmap(StringToBitMap(Singleton.getPref(Constants.USER_PROFILE_PIC, context)));
        }

        tvLogout.setOnClickListener(this);

    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    private void search(final boolean more) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Loading ...", true, false);

        if (!more) {
            lastToken = "";
        }

        Call<YoutubeResponse> youtubeResponseCall = APIService.youtubeApi.searchVideo("video","23", Constants.YOUTUBE_API_KEY, "snippet,id", "date","10", lastToken);
        youtubeResponseCall.enqueue(new Callback<YoutubeResponse>() {
            @Override
            public void onResponse(@NonNull Call<YoutubeResponse> call, @NonNull Response<YoutubeResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                YoutubeResponse body = response.body();
                Log.e("response", String.valueOf(response.body()));
                if (body != null) {
                    List<Item> items = body.getItems();
                    Log.e("items", String.valueOf(items));
                    lastToken = body.getNextPageToken();

                    Log.e("published",items.get(0).getSnippet().getPublishedAt());
                    Log.e("item size", String.valueOf(items.size()));

                    adapter.updateList(items);
//                    if (more) {
//                        adapter.addAll(items);
//                    } else {
//                        adapter.replaceWith(items);
//                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<YoutubeResponse> call, @NonNull Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvLogout:
                Singleton.setPrefBoolean(Constants.USER_SESSION,false,context);
                startActivity(new Intent(context,LoginActivity.class));
                break;
        }
    }
}
