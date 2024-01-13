package com.example.androidlabproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.NewViewHolder> {
    String emptyText;
    public NewAdapter(String emptyText){
        this.emptyText=emptyText;
    }


    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_adapter_view, parent, false);
        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewAdapter.NewViewHolder holder, int position) {
        holder.emptyTextView.setText(emptyText);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class NewViewHolder extends RecyclerView.ViewHolder{
        TextView emptyTextView;
        public NewViewHolder(@NonNull View itemView){
            super(itemView);
            emptyTextView=itemView.findViewById(R.id.emptyTextView);
        }
    }
}
