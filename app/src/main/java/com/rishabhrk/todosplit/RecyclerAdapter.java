package com.rishabhrk.todosplit;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    ArrayList<String> list;
    View view;
    OnItemClickListner listner;
    RecyclerAdapter(ArrayList<String> list) {
        this.list = list;
    }
    public void setOnItemClickListener(OnItemClickListner listener) {
        this.listner = listener;
    }
    public interface OnItemClickListner{
        void onDeleteClick(int pos);
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        view = layoutInflater.inflate(R.layout.todo_items,viewGroup,false);
        return new RecyclerViewHolder(view,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        String text = list.get(i);
        recyclerViewHolder.todo_text.setText(text);
    }

    @Override
    public int getItemCount() {
        return (list == null)?0:list.size();
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView todo_text;
        ImageView todo_delete;
       // int wd = getScreenWidth();

        public RecyclerViewHolder(@NonNull View itemView,final OnItemClickListner listner) {
            super(itemView);
            todo_text = itemView.findViewById(R.id.text_todo);
            todo_delete = itemView.findViewById(R.id.delete_btn);

            todo_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listner.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
