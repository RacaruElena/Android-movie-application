package com.example.screentime.network;

public interface Callback<R> {
    void runResultOnUiThread(R result);
}
