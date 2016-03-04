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
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gekaer on 015 15.02.16.
 */


public class novosti extends razdeli {

    public static String inURL;
    public static String title11;
     PaukTask2 paukTask2;
    TextView tvInfo2;
    ProgressBar progressBar2;
    HashMap<String, String> novostmap;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title11);
        setContentView(R.layout.novosti);

        tvInfo2 = (TextView) findViewById(R.id.textView2);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        paukTask2 = new PaukTask2();
        paukTask2.execute();
    }




    class PaukTask2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo2.setText("Полез на сайт...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                novostmap = parseNovost();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (novostmap != null) {
                progressBar2.setVisibility(View.INVISIBLE);
                tvInfo2.setVisibility(View.INVISIBLE);

                try
                {
                    /// Тут строим наши кнопки с разделами

                    Map<String, Date> keystr = new HashMap<String, Date>();
                    ListView listView = (ListView) findViewById(R.id.lvTest);

                    for (Map.Entry<String, String> pair : novostmap.entrySet()) {
                        String str2 = pair.getKey();
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy hh.mm");
                        try {
                            Date date123 = myFormat.parse(str2);
                            keystr.put(str2, date123);
                        }
                        catch (Exception e)
                        {
                            Date date123 = myFormat.parse("01.01.2015 11.11");
                            keystr.put(str2, date123);
                        }
                    }

                    keystr = MapUtil.sortByValue( keystr );
                    List<String> sss2 = new LinkedList<String>(keystr.keySet());

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(novosti.this, R.layout.item_simple, sss2);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                                long id) {

                            /// Тк выполнение перехода к конкретной новости

                            if (isNetworkConnected()) {

                                Intent intent = new Intent(novosti.this, itemnovo.class);
                                String str = novostmap.get((String) ((TextView) itemClicked).getText());

                                itemnovo.inURL2 = str;
                                itemnovo.title22 = (String) ((TextView) itemClicked).getText();
                                startActivity(intent);
                                listView.clearFocus();
                                paukTask2.cancel(true);
                            } else {
                                noINTERNET();
                            }

                        }
                    });

                    listView.setAdapter(adapter);
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
            };
        }
    }

    ///-------------- парсинг

    public HashMap<String, String> parseNovost()
    {
        String site = inURL;

        try {

            Document doc = Jsoup.connect(site).get();
            HashMap<String, String> map = new HashMap<String, String>();

            Elements links = doc.getElementsByTag("a");

            for(Element link : links) {

                /// Проверяем кнопка ли это от раздела новостей, либо просто ссылка на что-то

                if (((link.attr("target")).equals("_blank"))&&(!((link.attr("rel")).equals("nofollow")))) {
                    // получаем текст на заголовка

                    String skey = link.text();

                    // получаем текст ссылки
                    String linkHref = link.attr("href");

                    // Заполняем карту ключ - надпись на кнопке, значение полная - ссылка на раздел новостей
                    if (!(skey.equals("На главную страницу")))
                        map.put(skey, "http://www.pauk-press.ru"+linkHref);

                }
            }



            return map;

        } catch (IOException e) {

            noINTERNET();
            return null;
        }
        }

    }

