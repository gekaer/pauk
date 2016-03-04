package com.example.pauk;

import android.content.Intent;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gekaer on 016 16.02.16.
 */
public class stonovostei extends MyActivity {

    Pauk100 pauk100;
    TextView tvInfo100;
    ProgressBar progressBar100;
    HashMap<String, String> map100;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stonovosrtei);

        tvInfo100 = (TextView) findViewById(R.id.textView100);
        progressBar100 = (ProgressBar) findViewById(R.id.progressBar100);

        pauk100 = new Pauk100();
        pauk100.execute();

    }




    class Pauk100 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo100.setText("Полез на сайт...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                map100 = parse100();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (map100 != null) {
                progressBar100.setVisibility(View.INVISIBLE);
                tvInfo100.setVisibility(View.INVISIBLE);

                try
                {
                    /// Тут строим наши кнопки с разделами


                    Map<String, Date> keystr = new HashMap<String, Date>();
                    ListView listView = (ListView) findViewById(R.id.listView100);


                    for (Map.Entry<String, String> pair : map100.entrySet()) {
                        String str2 = pair.getKey();
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy hh.mm");
                        Date date123 = myFormat.parse(str2);
                        keystr.put(str2, date123);

                    }

                    keystr = MapUtil.sortByValue( keystr );

                    List<String> sss2 = new LinkedList<String>(keystr.keySet());


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(stonovostei.this, R.layout.item_simple, sss2);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                                long id) {


                            if (isNetworkConnected()) {

                                Intent intent = new Intent(stonovostei.this, itemnovo.class);
                                String str = map100.get((String) ((TextView) itemClicked).getText());

                                itemnovo.inURL2 = str;
                                itemnovo.title22 = (String) ((TextView) itemClicked).getText();
                                startActivity(intent);
                                listView.clearFocus();

                                pauk100.cancel(true);
                            } else {
                                noINTERNET();
                            }
                        }
                    });

                    listView.setAdapter(adapter);
                }
                catch (Exception e)
                {
                    // noINTERNET("Ошибка");
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
            };
        }
    }


    ///-------------- парсинг

    public HashMap<String, String> parse100()
    {
        String site = "http://www.pauk-press.ru";

        try {
            Document doc = Jsoup.connect(site).get();

            HashMap<String, String> map = new HashMap<String, String>();

            Elements links = doc.getElementsByTag("dd");

            for(Element link : links) {

                /// Проверяем кнопка ли это от раздела новостей, либо просто ссылка на что-то

                if ((link.getElementsByTag("dd").attr("class")).equals("list-group-item")) {

                    /// Проверяем кнопка ли это от раздела новостей, либо просто ссылка на что-то

                    ///      if ((element.attr("target")).equals("container")) {
                    // получаем текст на кнопке

                    String skey = link.text();

                    // получаем текст ссылки

                    Elements link2 = link.getElementsByTag("a");
                    String linkHref = link2.attr("href");


                    // Заполняем карту ключ - надпись на кнопке, значение полная - ссылка на раздел новостей
                    if (!(skey.equals("На главную страницу")))
                        map.put(skey, site+linkHref);

                }
            }



            return map;

        } catch (IOException e) {

            return null;
        }

    }






}
