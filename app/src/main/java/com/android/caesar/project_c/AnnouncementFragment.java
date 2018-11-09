package com.android.caesar.project_c;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public AnnouncementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_announcement,container,false);
        // Inflate the layout for this fragment

        ((MainActivity)getActivity()).getSupportActionBar();
        ((MainActivity)getActivity()).setActionBarTitle("公告");

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView1);

        /**RecyclerView 的Item size 不改變*/
        recyclerView.setHasFixedSize(true);

        /**LinearLayout垂直報局*/
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /**連接Firebase 獲取Data目錄的東西*/
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Data");

        /**Fragment通過onCreateOptionsMenu() 方法來參與填充選項菜單。*/
        setHasOptionsMenu(true);

        return v;
    }

    private void firebaseSearch(String searchText){

        /**
         * orderByChild( ) 按路徑下子節點的值(title)做排序
         * startAt(searchText) -> endAt(searchText + "\uf8ff")
         * */

        Query firebaseSearchQuery = databaseReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Announcements,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Announcements, ViewHolder>(
                Announcements.class,
                R.layout.announcement_item,
                ViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            /**populateViewHolder 填充getter setter中的物件*/
            protected void populateViewHolder(ViewHolder viewHolder, Announcements announcements, int position) {
                viewHolder.setDetails(getContext(),announcements.getTitle(),announcements.getDate(),announcements.getUrl(),announcements.getPng(),announcements.getDetial());
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ViewHolder viewHolder = super.onCreateViewHolder(parent,viewType);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView titleView = (TextView) view.findViewById(R.id.title_View);
                        TextView dateView = (TextView) view.findViewById(R.id.date_View);
                        TextView urlView = (TextView) view.findViewById(R.id.url_View);
                        TextView detialView = (TextView) view.findViewById(R.id.detial_View);
                        ImageView pngView = (ImageView) view.findViewById(R.id.image_View);

                        String titleTv = titleView.getText().toString();
                        String dateTv = dateView.getText().toString();
                        String detialTv = detialView.getText().toString();
                        Drawable drawable = pngView.getDrawable();

                        /**Bitmap 獲取圖像並壓縮圖像*/
                        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

                        Intent intent = new Intent(view.getContext(),AnnouncementFragment.class);
                        /**ByteArrayOutputStream 創建一個新的字節數組輸出流。緩衝區容量最初為32字節，但必要時其大小會增加*/
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("png",bytes);

                        intent.putExtra("title",titleTv);
                        intent.putExtra("date",dateTv);
                        intent.putExtra("detial",detialTv);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder;
            }
        };
        /**recyclerView填入適配器firebaseRecyclerAdapter*/
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Announcements,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Announcements, ViewHolder>(
                Announcements.class,
                R.layout.announcement_item,
                ViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Announcements announcements, int position) {
                viewHolder.setDetails(getContext(),announcements.getTitle(),announcements.getDate(),announcements.getUrl(),announcements.getPng(),announcements.getDetial());
            }



            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ViewHolder viewHolder = super.onCreateViewHolder(parent,viewType);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView titleView = (TextView) view.findViewById(R.id.title_View);
                        TextView dateView = (TextView) view.findViewById(R.id.date_View);
                        TextView urlView = (TextView) view.findViewById(R.id.url_View);
                        TextView detialView = (TextView) view.findViewById(R.id.detial_View);
                        ImageView pngView = (ImageView) view.findViewById(R.id.image_View);

                        String titleTv = titleView.getText().toString();
                        String dateTv = dateView.getText().toString();
                        String detialTv = detialView.getText().toString();
                        Drawable drawable = pngView.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

                        Intent intent = new Intent(view.getContext(),AnnouncementItemClicks.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        /**100->壓縮質量最高*/
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("png",bytes);

                        intent.putExtra("title",titleTv);
                        intent.putExtra("date",dateTv);
                        intent.putExtra("detial",detialTv);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder;
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /**Fragment 需加上getActivity()才能調用inflate*/
        getActivity().getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            /**USER進行查詢*/
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            /**USER更改查詢字*/
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }


/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
}

