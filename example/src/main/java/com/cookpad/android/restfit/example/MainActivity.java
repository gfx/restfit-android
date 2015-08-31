package com.cookpad.android.restfit.example;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitHurlStack;
import com.cookpad.android.restfit.RestfitResponse;
import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import rx.SingleSubscriber;

public class MainActivity extends AppCompatActivity {

    RestfitClient client;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.content);

        client = new RestfitClient.Builder()
                .httpStack(new RestfitHurlStack())
                .userAgent("RestfitExample/1.0")
                .endpoint("https://api.github.com/")
                .debug(BuildConfig.DEBUG)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        client.requestBuilder()
                .method("GET")
                .path("/emojis")
                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<RestfitResponse>() {
                    @Override
                    public void onSuccess(RestfitResponse value) {
                        textView.setText(value.toString());
                    }

                    @Override
                    public void onError(Throwable error) {
                        textView.setText(error.toString());
                    }
                });
    }
}
