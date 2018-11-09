package com.android.caesar.project_c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateCourse extends AppCompatActivity {

    private Button refeshBtn;

    private Button createClassName;
    private EditText inputClassNum;
    private EditText inputClassName;
    private Spinner spinnerClassNN;

    private WifiManager mWifiManager;
    ArrayList<String> newWifiList = new ArrayList<String>();
    List<ScanResult> wifiList = new ArrayList<ScanResult>();

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("創建課程");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        createClassName = (Button) findViewById(R.id.create_class);
        inputClassName = (EditText) findViewById(R.id.editText_class_name);
        inputClassNum = (EditText) findViewById(R.id.editText_class_num);
        spinnerClassNN= (Spinner)findViewById(R.id.spinner_class_name);

        refeshBtn = (Button)findViewById(R.id.refresh_Btn);

        createClassName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadTheClass();
                uploadWifiList();
            }
        });

        refeshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refeshData();
            }
        });

    }

/*
    public static String getNumLargeLetter(int size){
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for(int i=0; i<size;i++){
            if(random.nextInt(2) % 2 == 0){
                buffer.append((char) (random.nextInt(27) + 'A'));
                Log.v("getNumLargeLetter", "getNumLargeLetter: " +getNumLargeLetter(size));
            }else{
                buffer.append(random.nextInt(10));
            }
        }
        return buffer.toString();
    }
*/

    /**按鈕刷新spinner中的資料*/
    private void refeshData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Class name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> classGetNum = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String user = areaSnapshot.getValue(String.class);
                    classGetNum.add(user);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_spinner_item,classGetNum);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClassNN.setAdapter(areasAdapter);
                areasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

}

    private void uploadTheClass(){
        /**取得用戶資訊*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /**建立陣列*/
        final ArrayList<String> classNumber = new ArrayList<>();

        final String className = (String) inputClassName.getText().toString();
        final String classNum = (String) inputClassNum.getText().toString();

        /**判斷TextView資料是否為空*/
        if(TextUtils.isEmpty(className)){
            Toast.makeText(getApplication(),"Please enter className",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(classNum)){
            Toast.makeText(getApplication(),"Please enter classNum",Toast.LENGTH_LONG).show();
            return;
        }

        //Log.v("name","name" + className);

        /**把資料塞進陣列*/
        classNumber.add(className.toString());

        /**把資料放上Firebase的"Class name"*/
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        for(String classNumbera : classNumber) {
            databaseReference.child("Class name").child(classNum+"-"+className).setValue(classNum+"-"+className +" "+ user.getDisplayName());
        }


        /**Spinner 顯示老師建立課表*/

        databaseReference.child("Class name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> classGetNum = new ArrayList<String>();

                /**從Firebase中取得資料放進Spinner*/
                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String user = areaSnapshot.getValue(String.class);
                    classGetNum.add(user);
                }

                /**資料放進陣列後再用適配器放入Spinner*/
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_spinner_item,classGetNum);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClassNN.setAdapter(areasAdapter);
                /**資料變動更新spinner 內容*/
                areasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void uploadWifiList() {

        /**向WifiManager申請控制Wifi*/
        mWifiManager = (WifiManager) (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        /**接收WIFI*/
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver ( ) {
            @Override
            public void onReceive(Context context, Intent intent) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                /**取得WIFI列表*/
                wifiList = mWifiManager.getScanResults ();
                /**填入WIFI列表到Array*/
                for(int i = 0; i < 5; i++){ //wifiList.size ()
                    if(!wifiList.get ( i ).SSID.isEmpty()){
                        newWifiList.add ( wifiList.get ( i ).SSID);

                        if(wifiList.get(i).level < -70){
                            String name = "wifi"+ i ;
                            databaseReference.child("SSID").child(name).child("wifiname").setValue(wifiList.get(i).SSID.trim());
                            databaseReference.child("SSID").child(name).child("level").setValue(wifiList.get(i).level);
                        }
                    }
                }
            }
        };

        getApplication().registerReceiver ( wifiScanReceiver, new IntentFilter( mWifiManager.SCAN_RESULTS_AVAILABLE_ACTION ) );

        /**如果版本大於M，取得M版本以上的權限，小於M則直接掃瞄附近WIFI*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getApplication().checkSelfPermission( android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        } else{
            mWifiManager.startScan ();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
