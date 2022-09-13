package com.example.screentime.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTaskRunner {
    //Handler-ul este obiectul care 'vegheaza' asupra firului principal de executie (activitate/fragment)
    //Pastreaza o referinta catre acesta astfel incat sa putem trimite mesaje din alte thread-uri
    private final Handler handler = new Handler(Looper.getMainLooper());
    //Executor este responsabil cu gestionarea thread-urilor, decide momentul oportun de a porni un thread (apelare metoda start)
    private final Executor executor = Executors.newCachedThreadPool();

    public <R> void executeAsync(Callable<R> asyncOperation, Callback<R> mainThreadOperation) {
        try {
            //ii specificam Executor-ului ca dorim sa procesam un Thread
            executor.execute(runAsyncOperation(asyncOperation, mainThreadOperation));
        } catch (Exception e) {
            Log.e("AsyncTaskRunner", "failed call executeAsync " + e.getMessage());

        }
    }

    private <R> Runnable runAsyncOperation(Callable<R> asyncOperation, Callback<R> mainThreadOperation) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    //apelam declansarea operatiei asincrone, care se realizeaza pe acest thread.
                    //in exemplu din seminarul 8 call() realizeaza executia metodei cu acelasi nume din clasa HttpManager
                    R result = asyncOperation.call();
                    //rezultatul obtinut mai sus este trimis catre handler prin intermediul metodei runMainThreadOperation
                    //Scopul acestui apel de post este de a notifica Handler-ul
                    //ca dorim sa trimitem rezultatul R result in bucata de cod referita de mainThreadOperation (care este implementata in MainActivity)
                    handler.post(runMainThreadOperation(result, mainThreadOperation));
                } catch (Exception e) {
                    Log.e("AsyncTaskRunner", "failed call runAsyncOperation "+e.getMessage());
                }
            }
        };
    }

    private <R> Runnable runMainThreadOperation(R result, Callback<R> mainThreadOperation) {
        return new Runnable() {
            @Override
            public void run() {
                //se trimite rezultatul in activitate/fragment
                mainThreadOperation.runResultOnUiThread(result);
            }
        };
    }
}
