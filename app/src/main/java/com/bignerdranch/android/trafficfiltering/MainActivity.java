package com.bignerdranch.android.trafficfiltering;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity1";
    public Button btn;
    Disposable disposable;

    int currentState = TrafficFilterStateChangeEmitter.getInstance().getCurrentStatus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.change_btn);

        btn.setText(Integer.toString(currentState));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrafficFilterStateChangeEmitter emitter = TrafficFilterStateChangeEmitter.getInstance();

                currentState = (currentState + 1) % 2;

                emitter.putStatus(currentState);
            }
        });

        disposable = data()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        Log.d(TAG, "log: " + integer);
                        btn.setText(integer.toString());
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();

    }

    private Observable<Integer> data() {
        return Observable.create(TrafficFilterStateChangeEmitter.getInstance());
    }
}
