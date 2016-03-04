package com.example.pauk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private LinearLayout layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.main);
        layout = (LinearLayout) findViewById(R.id.Layor1);
        buildmain();
    }

    private void buildmain() {
        WebView myWeb = (WebView) layout.findViewById(R.id.webView);
        myWeb.loadUrl("file:///android_asset/spider_move.gif");
        myWeb.setInitialScale(50);
        myWeb.getSettings().setBuiltInZoomControls(false);
        myWeb.getSettings().setSupportZoom(false);
        myWeb.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);

    }

    public void onClickButton1(View view) {
        /// Тут создаем новое окно в которое парсим
        if (isNetworkConnected())
        {
        Intent intent = new Intent(MyActivity.this, razdeli.class);
        startActivity(intent);
         }
        else {
            noINTERNET();
        }
    }


    public void noINTERNET()
    {

        Toast toast = Toast.makeText(getApplicationContext(),
                "Соединение с интернетом не найдено",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);

        LinearLayout toastContainer = (LinearLayout) toast.getView();
        ImageView catImageView = new ImageView(getApplicationContext());
        catImageView.setImageResource(R.drawable.manquestion);
        toastContainer.addView(catImageView, 0);

        toast.show();
    }

    public void noINTERNET(String sss)
    {

        Toast toast = Toast.makeText(getApplicationContext(),
                sss,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);

        LinearLayout toastContainer = (LinearLayout) toast.getView();
        ImageView catImageView = new ImageView(getApplicationContext());
        catImageView.setImageResource(R.drawable.manquestion);
        toastContainer.addView(catImageView, 0);

        toast.show();
    }

    public void onClickButton2(View view) {
        //создаем и отображаем текстовое уведомление
        Toast toast2 = Toast.makeText(getApplicationContext(),
                "Пока что не реализовано!",
                Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.BOTTOM, 0, 0);

        LinearLayout toastContainer = (LinearLayout) toast2.getView();
        ImageView catImageView = new ImageView(getApplicationContext());
        catImageView.setImageResource(R.drawable.kot);
        toastContainer.addView(catImageView, 0);

        toast2.show();
    }

    public void onClickButton3(View view) {
        /// Тут создадим окно и будем парсить новости.

        if (isNetworkConnected())
        {
            Intent intent = new Intent(MyActivity.this, stonovostei.class);
            startActivity(intent);
        }
        else {
            noINTERNET();
        }
    }

    boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
