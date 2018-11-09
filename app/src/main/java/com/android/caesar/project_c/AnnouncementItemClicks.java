package com.android.caesar.project_c;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AnnouncementItemClicks extends AppCompatActivity {

    TextView iUrlTv;
    TextView iTitleTv;
    TextView iDateTv;
    TextView iDetialTv;
    ImageView iImageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_item_clicks);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("詳情");

        /**action bar的返回鍵*/
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        iTitleTv = (TextView) findViewById(R.id.iTitle_tv);
        iDateTv = (TextView) findViewById(R.id.iDate_tv);
        iUrlTv = (TextView) findViewById(R.id.iUrl_tv);
        iImageTv = (ImageView) findViewById(R.id.iImage_tv);
        iDetialTv = (TextView) findViewById(R.id.iDetial_tv);

        /**取得打包的東西*/
        byte[] bytes = getIntent().getByteArrayExtra("png");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String detial = getIntent().getStringExtra("detial");

        iImageTv.setImageBitmap(bmp);
        iTitleTv.setText(title);
        iDateTv.setText(date);
        iDetialTv.setText(detial);
    }

    /**action bar的返回鍵*/
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
