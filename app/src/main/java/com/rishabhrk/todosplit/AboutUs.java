package com.rishabhrk.todosplit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUs extends Fragment {

    Button contact;
    TextView details;
    public AboutUs() {
        // Required empty public constructor
    }
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_about_us, container, false);
        contact = view.findViewById(R.id.btn_contact);
        details = view.findViewById(R.id.details);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "Mobile: 9634083118          Email: rishabhrk9634@gmail.com";
                details.setText(str);
                details.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

}
