package com.example.ant_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.ant_test.notification.EmNotification;
import com.example.ant_test.view.NewsRefreshListener;
import com.example.ant_test.view.PullListView;
import com.example.ant_test.view.PullListView.IPullListView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity implements IPullListView, OnItemClickListener{
	private PullListView plv;
	private List<Map<String, String>> data;
	private SimpleAdapter adapter;
	private int maxSave = 30;
	private int requestCount = 15;
	private int first = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plv = (PullListView)findViewById(R.id.plv1);
        data = new ArrayList<Map<String, String>>();
        for (int i = first; i < 15; i++) {
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("test1", i + "");
	       	map.put("test2", i * 10 + "");
	       	map.put("test3", i * 100 + "");
	       	map.put("test4", i * 1000 + "");
        	data.add(map);
        }
        adapter = new SimpleAdapter(this, data, R.layout.common_stocktable_view, 
        		new String[] {"test1", "test2", "test3", "test4"}, 
        		new int[] {R.id.val1, R.id.val2, R.id.val3, R.id.val4});
        plv.setAdapter(adapter);
        plv.setPullListViewImpl(this);
        plv.setOnItemClickListener(this);
//        EmNotification ntf = new EmNotification(this);
//        ntf.notify(R.drawable.ic_launcher, "test", "com.example.ant_test.MainActivity");
//        new Thread() {
//        	public void run() {
//		        try {
//			        HttpGet request = new HttpGet("http://gubaapi.eastmoney.com/v1/UserGubaArticleList.aspx?gt=1&uid=4538113647636652&ps=20&p=1");
//			        request.addHeader("Accept-Encoding", "gzip");
//			        HttpResponse response = new DefaultHttpClient().execute(request);
//			        HttpEntity entity = response.getEntity();
//			        InputStream stream = entity.getContent();
//			        
//			        InputStream zippedStream = new GZIPInputStream(stream);
//			        InputStreamReader reader = new InputStreamReader(zippedStream);
//			        BufferedReader buffer = new BufferedReader(reader);
//			        String star = buffer.readLine();
//			        Log.d("", "gzip>>>>" + star);
//		        } catch (Exception e) {
//		        	e.printStackTrace();
//		        	Log.d("", "gzip>>>>" + e);
//		        }
//        	}
//        }.start();
    }  

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onTopRefresh() {
		plv.postDelayed(new Runnable() {
			@Override
			public void run() {
				plv.resetHeader();
				plv.isRefreshable = true;
			}				
		}, 1000);
	}

	@Override
	public void onFootRefresh() {
		plv.postDelayed(new Runnable() {
			@Override
			public void run() {
				plv.resetFooter();
				plv.isRefreshable = true;
				data.clear();
				for (int i = first; i < first + 30; i++) {
					Map<String, String> map = new HashMap<String, String>();
			       	map.put("test1", i + "");
			       	map.put("test2", i * 10 + "");
			       	map.put("test3", i * 100 + "");
			       	map.put("test4", i * 1000 + "");
			       	data.add(map);
				}
				first += 15;
				adapter.notifyDataSetChanged();
			}				
		}, 1000);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		startActivity(new Intent(this, FragmentTestActivity.class));
		
	}
}
