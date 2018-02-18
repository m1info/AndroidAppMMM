package com.example.youssef.myapplication.firebase;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.youssef.myapplication.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by youssef on 18/02/18.
 */

public class Sender {
    Dialog push_dialog;
    Button push_button;
    String text;
    TextView push_text;
    private  final String key = "AAAA0VwG5e8:APA91bFRNSIowRIYsCEVSL-7JIYhbewb-h-lCRNj0tZpX4tIIP4GlsyxM-Ki2iVZl3PP2LluVwPC33WN94oVi2S1NERoMCeKIExd5MEmKcpvMPkkiEe5stfzv8Ad8IFIv42jIQeQrgQI";

    public Sender(Activity activity) {
        push_dialog = new Dialog(activity);
        push_dialog.setCancelable(false);
        push_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        push_dialog.setContentView(R.layout.notify_dialog);
        push_text = (TextView) push_dialog.findViewById(R.id.push_text);
        push_button = (Button) push_dialog.findViewById(R.id.push_button);

    }

    public void show(String event_title){

        push_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push(event_title, String.valueOf(push_text.getText()));
                push_dialog.cancel();
            }
        });

        push_dialog.show();
    }

    public void push(String title, String body){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "key="+key); // <-- this is the important line
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        httpClient.addInterceptor(logging);
        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com")//url of FCM message server
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())//use for convert JSON file into object
                .build();

        // prepare call in Retrofit 2.0
        FirebaseAPI firebaseAPI = retrofit.create(FirebaseAPI.class);

        //for messaging server
        NotifyData notifydata = new NotifyData(title,body);

        Call<Message> call2 = firebaseAPI.sendMessage(new Message("/topics/events", notifydata));

        call2.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                Log.d("Response ", "onResponse");

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("Response ", "onFailure");
            }


        });
    }
}