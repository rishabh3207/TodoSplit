package com.rishabhrk.todosplit;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Todos extends Fragment {
    private View view;
    Button save_btn;
    TextView todos;
    EditText todo_text;
    RecyclerView recyclerView;
    ArrayList<String> todo_list,key_list;
    FloatingActionButton fbtn;
    public Todos() {
        // Required empty public constructor
    }

    private DatabaseReference dbref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_todos, container, false);
        dbref = FirebaseDatabase.getInstance().getReference();
        todo_list = new ArrayList<>();
        key_list = new ArrayList<>();
        final RecyclerAdapter adapter =new  RecyclerAdapter(todo_list);
        recyclerView = view.findViewById(R.id.recycler_view);
        todos = view.findViewById(R.id.text_todo);
        fbtn = view.findViewById(R.id.add_btn);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        dbref.child("TODO").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String data = dataSnapshot.getValue(String.class);
                String keys = dataSnapshot.getKey();
                if(data != null) {
                    todo_list.add(data);
                    adapter.notifyDataSetChanged();
                }
                if(keys != null){
                    key_list.add(keys);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                adapter.notifyDataSetChanged();
            }
        });
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListner() {
            @Override
            public void onDeleteClick(int pos) {
                todo_list.remove(pos);
                delete_todos(pos);
                key_list.remove(pos);
                adapter.notifyItemRemoved(pos);
            }
        });
        return view;
    }

    public void openDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mview = inflater.inflate(R.layout.open_dialog,null);
        todo_text = mview.findViewById(R.id.add_data);
        builder.setView(mview)
                .setTitle("Add Todo")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String text = todo_text.getText().toString();
                        if(text != null && text.length() > 0)
                            dbref.child("TODO").push().setValue(text);

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void delete_todos(int pos){

        DatabaseReference databaseReference =dbref.child("TODO").child(key_list.get(pos));
        databaseReference.removeValue();
    }
}
