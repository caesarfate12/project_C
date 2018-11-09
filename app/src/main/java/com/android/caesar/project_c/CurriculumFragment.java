package com.android.caesar.project_c;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurriculumFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public CurriculumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_curriculum,container,false);
        // Inflate the layout for this fragment

        ((MainActivity)getActivity()).setActionBarTitle("課表");

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CoursesList");

        return v;

    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Curriculums,ViewHolderC> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Curriculums, ViewHolderC>(
                Curriculums.class,
                R.layout.curriculum_item,
                ViewHolderC.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(ViewHolderC viewHolderc, Curriculums curriculums, int position) {
                viewHolderc.setCurriculumDetails(getContext(),curriculums.getTime(),curriculums.getCourses(),curriculums.getRoom(),curriculums.getCodes(),curriculums.getHour(),curriculums.getWeek());
            }

        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}
