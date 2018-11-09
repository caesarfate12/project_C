package com.android.caesar.project_c;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;
    TextView titleView;
    TextView dateView;
    TextView urlView;
    TextView detialView;
    ImageView pngView;

    public ViewHolder(View itemView){
        super(itemView);

        mView = itemView;

        /**訂製RecycleView的點擊事件*/
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view , getAdapterPosition());
                return true;
            }
        });


    }

    public void setDetails(Context ctx,String title, String date,String url,String png,String detial){
        titleView = (TextView) mView.findViewById(R.id.title_View);
        dateView = (TextView) mView.findViewById(R.id.date_View);
        pngView = (ImageView) mView.findViewById(R.id.image_View);
        urlView = (TextView) mView.findViewById(R.id.url_View);
        detialView = (TextView) mView.findViewById(R.id.detial_View);

        titleView.setText(title);
        dateView.setText(date);
        urlView.setText(url);
        detialView.setText(detial);
        Picasso.get().load(png).error(R.mipmap.ic_cycu_logo);
        Picasso.get().load(png).into(pngView);

    }

    private ViewHolder.ClickListener mClickListener;

    public  interface ClickListener{
        void onItemClick(View view , int position);
        void onItemLongClick(View view , int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){

        mClickListener = clickListener;
    }

}
