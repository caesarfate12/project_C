package com.android.caesar.project_c;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Advertising extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);

        /**-------------hide action bar-------------*/

        getSupportActionBar().hide();

        /**-------------skip button-------------*/

        Button button = (Button)findViewById(R.id.skip_btn);
        button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Advertising.this, Login.class);
                startActivity(intent);
                Advertising.this.finish();
            }
        });


        /**-------------Picasso套件-------------*/

        Picasso.get().load("http://cdn1.data-service.cloud/site/9b994e58-bb6c-4385-8433-ec2f1bb6e317/uploadimage/59bdc571-3cf4-4372-8f3c-479c3b5fedc0.jpg")
                .placeholder(R.mipmap.ic_launcher)
                //.fit()
                .error(R.mipmap.ic_launcher)
                .into((ImageView) findViewById(R.id.aImage_View));

        Picasso.get().load("http://cdn1.data-service.cloud/site/9b994e58-bb6c-4385-8433-ec2f1bb6e317/uploadimage/b34a9993-9eb0-4a95-be89-bf41863f8c3d.jpg")
                .placeholder(R.mipmap.ic_launcher)
                //.fit()
                .error(R.mipmap.ic_launcher)
                .into((ImageView) findViewById(R.id.aImage_View1));


        Picasso.get().load("http://cdn1.data-service.cloud/site/9b994e58-bb6c-4385-8433-ec2f1bb6e317/uploadimage/1e46e4ce-8fed-438b-9721-e07db3a71e9c.jpg")
                .placeholder(R.mipmap.ic_launcher)
                //.fit()
                .error(R.mipmap.ic_launcher)
                .into((ImageView) findViewById(R.id.aImage_View2));

        Picasso.get().load("http://cdn1.data-service.cloud/site/9b994e58-bb6c-4385-8433-ec2f1bb6e317/uploadimage/9991dd7f-ff02-4b91-9c7f-72b0bab02a8b.jpg")
                .placeholder(R.mipmap.ic_launcher)
                //.fit()
                .error(R.mipmap.ic_launcher)
                .into((ImageView) findViewById(R.id.aImage_View3));


    }
}
