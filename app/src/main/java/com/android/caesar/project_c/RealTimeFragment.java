package com.android.caesar.project_c;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class RealTimeFragment extends Fragment implements View.OnClickListener{

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 2;
    private static final int WIFI_MODE_SCAN_ONLY =3;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private Button scanwifiBtn;
    private Button wifiSwitchBtn;
    private Button rollCallBtn;
    private TextView textViewRollCall;
    private ImageView imageViewWifi;

    private ListView rollCallListView;
    ArrayList<String> list = new ArrayList<>();

    Calendar calander;
    SimpleDateFormat simpleDateFormat;
    String Date;

    private WifiManager.WifiLock mWifiLock;
    private WifiManager mWifiManager;

    //IntentFilter intentFilter = null;

    ArrayList<String> newWifiList = new ArrayList<String>();
    List<ScanResult> wifiList = new ArrayList<ScanResult>();
    ArrayList<String> userName = new ArrayList<String>();


    ListView wifiListView;

    public RealTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("即時資訊");

        View v = inflater.inflate(R.layout.fragment_real_time,container,false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        textViewRollCall = (TextView) v.findViewById(R.id.text_con);
        imageViewWifi = (ImageView) v.findViewById(R.id.wifi_ig);

        rollCallListView = (ListView) v.findViewById(R.id.rollCallLv);

/*
        scanwifiBtn = (Button) v.findViewById(R.id.scan_btn);
        scanwifiBtn.setOnClickListener(this);

        wifiSwitchBtn = (Button) v.findViewById(R.id.wifi_btn);
        wifiSwitchBtn.setOnClickListener(this);

        rollCallBtn = (Button) v.findViewById(R.id.rollcall_btn);
        rollCallBtn.setOnClickListener(this);
*/

        rollCallListView = (ListView) v.findViewById(R.id.rollCallLv);


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item,list);
        rollCallListView.setAdapter(adapter);

        rollCallListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listView = (ListView) adapterView;
                //Toast.makeText(getContext(), "己點選"+ rollCallListView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();

                String name = rollCallListView.getItemAtPosition(i).toString();

                openOptionsDialog(name);

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Class name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                list.add(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v ;
    }

    public void openOptionsDialog(String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("是否進行"+name+"點名")
                .setPositiveButton("點名",new DialogInterface.OnClickListener(){
                    @Override
                    public  void onClick(DialogInterface dialogInterface,int i){
/*
                        if(wifiList.get(1).level>= -70){
                            String wname = wifiList.get(1).SSID.toString();
                            Toast.makeText(getContext(), wname , Toast.LENGTH_SHORT).show();
                        }
*/
                        uploadWifiList();
                    }
                })
                .setNeutralButton("查看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getActivity().getApplicationContext(), RollCallList.class));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
        }



    /**時間運行*/

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState){

        Thread t = new Thread(){
            @Override
            public void run(){
                try {
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        if(getActivity() == null)
                            return;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView dateTime = (TextView) getView().findViewById(R.id.dateTime);
                                calander = Calendar.getInstance();
                                long date = System.currentTimeMillis();
                                simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                Date = simpleDateFormat.format(calander.getTime());
                                String dateString = simpleDateFormat.format(date);
                                dateTime.setText(Date);
                            }
                        });
                    }
                }catch (InterruptedException e){
                }
            }
        };
        t.start();
    }

    /**              SCAN WIFI BUTTON                */

    private void uploadWifiList() {

        /**向WifiManager申請控制Wifi*/
        mWifiManager = (WifiManager) (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        //Toast.makeText(getContext(), "點名中", Toast.LENGTH_SHORT).show();

        /**接收WIFI*/
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver ( ) {
            @Override
            public void onReceive(Context context, Intent intent) {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


/*
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                Log.d("wifiInfo", wifiInfo.toString());
                Log.d("SSID",wifiInfo.getSSID());
*/

                //Log.d("ADD", "In broadcast receiver" );
                /**取得WIFI列表*/
                wifiList = mWifiManager.getScanResults ();
                //Log.d("ADD", "Scan result " + wifiList );
                /**填入WIFI列表到Array*/
                for(int i = 0; i < 5; i++){ //wifiList.size ()
                    if(!wifiList.get ( i ).SSID.isEmpty()){
                        newWifiList.add ( wifiList.get ( i ).SSID);
                        userName.add(user.getDisplayName());


                        if(wifiList.get(i).level < -60 ){

                            final String scanname = wifiList.get(i).SSID.toString();
                            final String name = "wifi" + i;

                            databaseReference.child("SSID")
                                    //.child(name)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String showname = (String) dataSnapshot.child("wifiname").getValue(String.class);
                                            //int showlevel = (int) dataSnapshot.child("level").getChildrenCount();

                                            //Toast toast = Toast.makeText(getContext(), showname , Toast.LENGTH_SHORT);
                                            Toast toast1 = Toast.makeText(getContext(), "SSID:" + dataSnapshot.child("wifi3").child("wifiname").getValue(String.class) +
                                                    "    Level:" + dataSnapshot.child("wifi4").child("level").getValue().toString(), Toast.LENGTH_SHORT);
                                            toast1.setGravity(Gravity.CENTER,0,0);
                                            toast1.show();

                                            Toast toast2 = Toast.makeText(getContext(), "SSID:" + dataSnapshot.child("wifi4").child("wifiname").getValue(String.class) +
                                                    "    Level:"  + dataSnapshot.child("wifi4").child("level").getValue().toString(), Toast.LENGTH_SHORT);
                                            toast2.setGravity(Gravity.CENTER,0,100);
                                            toast2.show();

                                            //toast.setGravity(Gravity.CENTER,0,0 );
                                            //toast.show();

                                            //Log.d("scanname",scanname);
                                            //Log.d("showname",dataSnapshot.child("SSID").child(name).child("wifiname").getValue(String.class));

                                            if(scanname != showname){
                                                Toast.makeText(getContext(), "配對成功" , Toast.LENGTH_LONG).show();
                                                textViewRollCall.setText("已點名");
                                                imageViewWifi.setImageDrawable(getResources().getDrawable(R.drawable.ic_wifi_g));

                                            }else {
                                                Toast.makeText(getContext(), "配對不成功" , Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }

                        databaseReference.child("USER").child(user.getDisplayName().trim()).setValue(user.getDisplayName().trim());

                    }
                }

                //Log.d("ASS", "CONNECTIONS: " + newWifiList);
            }
        };

        getActivity().registerReceiver ( wifiScanReceiver, new IntentFilter( mWifiManager.SCAN_RESULTS_AVAILABLE_ACTION ) );

        /**如果版本大於M，取得M版本以上的權限，小於M則直接掃瞄附近WIFI*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission( android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        } else{
            mWifiManager.startScan ();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
/*            case R.id.scan_btn:
                {
                    //uploadWifiList();
                    //uploadUesrList();

                    break;
                }

            case R.id.wifi_btn:
                {

                    break;
                }

            case R.id.rollcall_btn:
                {
                    //rollCallList();
                    startActivity(new Intent(getActivity().getApplicationContext(), RollCallList.class));
                    break;
                }
*/        }
    }












    /**              WIFI ON / OFF                */

    private void uploadUesrList(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ArrayList<String> userlist = new ArrayList<>();
        userlist.add(user.getDisplayName());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        for(String userlista : userlist) {
            databaseReference.child("userlist").child(userlista).setValue(true);


            if ( user.equals(user.getDisplayName())){
            }
        }
    }

    /**             STUDENT ROLL CALL LIST                */

    private void rollCallList(){

    }


}