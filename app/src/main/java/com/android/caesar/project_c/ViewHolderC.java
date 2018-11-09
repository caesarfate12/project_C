package com.android.caesar.project_c;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolderC extends RecyclerView.ViewHolder {

    View mView;

    TextView timeTextView;
    TextView coursesTextView;
    TextView roomTextView;
    TextView codesTextView;
    TextView hourTextView;
    TextView weekTextView;

    public ViewHolderC(View itemView){
        super(itemView);

        mView = itemView;

    }


    public void setCurriculumDetails(Context context,String time,String courses,String room,String codes,String hour,String week){
        timeTextView = (TextView) mView.findViewById(R.id.time_Tv);
        coursesTextView = (TextView) mView.findViewById(R.id.courses_Tv);
        roomTextView = (TextView) mView.findViewById(R.id.room_Tv);
        codesTextView = (TextView) mView.findViewById(R.id.codes_Tv);
        hourTextView = (TextView) mView.findViewById(R.id.hour_Tv);
        weekTextView = (TextView) mView.findViewById(R.id.week_Tv);

        timeTextView.setText(time);
        coursesTextView.setText(courses);
        roomTextView.setText(room);
        codesTextView.setText(codes);
        hourTextView.setText(hour);
        weekTextView.setText(week);
    }


}
