package com.example.pauk;

/**
 * Created by gekaer on 014 14.02.16.
 */

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
import java.util.*;



public class razdeli extends MyActivity{

    PaukTask paukTask;
    TextView tvInfo;
    ProgressBar progressBar;
    HashMap<String, String> razdmap;
  //  RelativeLayout.LayoutParams layoutParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.razdeli);

        tvInfo = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        paukTask = new PaukTask();
        paukTask.execute();

    }

    class PaukTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Полез на сайт...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                razdmap = parseRazd();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


          if (razdmap != null) {
              progressBar.setVisibility(View.INVISIBLE);
              tvInfo.setVisibility(View.INVISIBLE);

            try
            {
              /// Тут строим наши кнопки с разделами

              List<String> keystr = new ArrayList<String>();
              ListView listView = (ListView) findViewById(R.id.listView1);

              for (Map.Entry<String, String> pair : razdmap.entrySet()) {
                  String str2 = pair.getKey();
                  keystr.add(str2);

              }

                Collections.sort(keystr, new Comparator<String>() {
                  public int compare(String o1, String o2) {
                      return o1.toString().compareTo(o2.toString());
                  }
              });


              ArrayAdapter<String> adapter = new ArrayAdapter<String>(razdeli.this, R.layout.item_simple, keystr);

              listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                          long id) {


                      if (isNetworkConnected()) {
                          Intent intent = new Intent(razdeli.this, novosti.class);
                          String str = razdmap.get((String) ((TextView) itemClicked).getText());
                          novosti.inURL = str;
                          novosti.title11 = (String) ((TextView) itemClicked).getText() + ":";
                          startActivity(intent);
                          listView.clearFocus();
                          paukTask.cancel(true);
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

    public HashMap<String, String> parseRazd()
    {
        String site = "http://www.pauk-press.ru";

        try {
            Document doc = Jsoup.connect(site).get();

            HashMap<String, String> map = new HashMap<String, String>();


            Elements links = doc.getElementsByTag("a");

            for(Element link : links) {

                /// Проверяем кнопка ли это от раздела новостей, либо просто ссылка на что-то

                if ((link.getElementsByTag("button").attr("class")).equals("btn btn-primary")) {
                    // получаем текст на кнопке

                    String skey = link.text().replace("\u00a0", "").trim();

                    // получаем текст ссылки
                    String linkHref = link.attr("href");

                    //  System.out.println(linkHref);
                    // Заполняем карту ключ - надпись на кнопке, значение полная - ссылка на раздел новостей
                    if (!(skey.equals("На главную страницу")))
                    map.put(skey, site+linkHref);

                }
            }



            return map;

        } catch (IOException e) {


            noINTERNET();
            return null;
        }

    }

}
