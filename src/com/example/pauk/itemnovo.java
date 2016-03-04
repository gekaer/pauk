package com.example.pauk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by gekaer on 016 16.02.16.
 */


public class itemnovo  extends novosti {

    public static String inURL2;
    public static String title22;
    PaukTask3 paukTask3;
    TextView tvInfo3;
    ProgressBar progressBar3;
    ArrayList<String> textnovo;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title22);

        setContentView(R.layout.itemnovo);


        textnovo = null;

        tvInfo3 = (TextView) findViewById(R.id.textView3);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);

        paukTask3 = new PaukTask3();
        paukTask3.execute();
    }


    class PaukTask3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo3.setText("Полез на сайт...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                textnovo = parseNovo();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (textnovo != null) {
                progressBar3.setVisibility(View.INVISIBLE);
                tvInfo3.setVisibility(View.INVISIBLE);

                try
                {
                    /// Тут строим наши кнопки с разделами
                    TextView tvInfo4 = (TextView) findViewById(R.id.textView4);
                    tvInfo4.setText(textnovo.get(0));

                }
                catch (Exception e)
                {

                }

            }
            else {

                Toast toast2 = Toast.makeText(getApplicationContext(),
                        "Соединение с интернетом потеряно",
                        Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.BOTTOM, 0, 0);

                LinearLayout toastContainer = (LinearLayout) toast2.getView();
                ImageView catImageView = new ImageView(getApplicationContext());
                catImageView.setImageResource(R.drawable.manquestion);
                toastContainer.addView(catImageView, 0);

                toast2.show();
            }
        }
    }

    ///-------------- парсинг

    public ArrayList<String> parseNovo()
    {
        String site = inURL2;
        ArrayList<String> skey;
        skey = new ArrayList<String>();

        try {

            Document doc = Jsoup.connect(site).get();
            Elements div = doc.getElementsByTag("div");

            for(Element element : div) {

                /// Проверяем кнопка ли это от раздела новостей, либо просто ссылка на что-то

                if ((element.attr("class")).equals("container")) {
                    // получаем текст на кнопке
                    skey.add(element.text().toString());
                }
            }


            return skey;

        } catch (IOException e) {


            noINTERNET();
            return null;
        }

    }








}