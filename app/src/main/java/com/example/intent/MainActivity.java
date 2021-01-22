package com.example.intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    Button mbtn_test;
    Button mbtn_people;
    Button mbtn_mask;
    Button mbtn_call;
    String abc;
    TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("COVID19 Info");
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView2);
        tv.setText("20160658 강상우\n");

        new JsoupAsyncTask().execute();
        task = new TimerTask() {
            @Override
            public void run(){
                new JsoupAsyncTask().execute();
            }
        };
        new Timer().scheduleAtFixedRate(task, 10000,1000);

        mbtn_test = findViewById(R.id.btn_test);
        mbtn_people = findViewById(R.id.btn_people);
        mbtn_mask = findViewById(R.id.btn_mask);
        mbtn_call = findViewById(R.id.btn_call);
        mbtn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NextActivity.class);
                startActivityForResult(intent, 200);
            }
        });
        mbtn_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://coronamap.site/"));
                startActivity(intent);
            }
        });
        mbtn_mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://00mask.com/#/map"));
                startActivity(intent);
            }
        });

        mbtn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1339"));
                startActivity(tt);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 201) {
            int Check_num = data.getIntExtra("result", -1);
            if (Check_num > 0)
                tv.setText("의심증상을 발견하였습니다!! 자세한 검진을 받아보세요!\n");
            else
                tv.setText("의심증상이 발견되지 않았습니다. 마스크를 항상 착용하여주세요!\n");
        }
    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        private int index = 0;
        private Elements links;
        private String parse = "";
        private String text = "";
        private ArrayList<String> parseArray = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("http://ncov.mohw.go.kr/").get();
                links = doc.select("ul.liveNum");

                for (Element link : links) {
                    Elements files = link.getElementsByTag("span");
                    for( Element elm : files ) {
                        if(!elm.text().equals(new String("(누적)"))) {
                            parseArray.add(elm.text());
                                //parseArray.set(index, elm.text() + "명");
                        }
                        index++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            int index = 0;
            tv.setText("\r\n20160658 강상우\r\n");
            for(String s : parseArray) {
                switch(index) {
                    case 0:
                        tv.append("\r\n");
                        tv.append("현재 확진자");
                        tv.append(s + "명 / ");
                        break;
                    case 1:
                        tv.append(s + "명\n");
                        break;
                    case 2:
                        tv.append("격리해제(누적)");
                        break;
                    case 3:
                        tv.append(s + "명 / ");
                        break;
                    case 4:
                        tv.append("전일대비 " + s + "명\n");
                        break;
                    case 5:
                        tv.append("격리 중(누적)");
                        break;
                    case 6:
                        tv.append(s + "명 / ");
                        break;
                    case 7:
                        tv.append("전일대비 " + s + "명\n");
                        break;
                    case 8:
                        tv.append("사망자(누적)" + s + "명 / 전일대비 ");
                        break;
                    case 9:
                        tv.append(s + "명\n");
                        break;
                    default:
                        tv.append(s + "\n");
                        break;
                }
                index++;
            }
        }
    }
}
