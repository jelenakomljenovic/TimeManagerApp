package com.timeManager.check;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epicstudio.or3432.R;

import java.util.ArrayList;

public class user_entry_adapter extends RecyclerView.Adapter<user_entry_adapter.ViewHolder> {
    ArrayList<user_entry_model> data = new ArrayList<>();

    public user_entry_adapter(ArrayList<user_entry_model> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public user_entry_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new user_entry_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull user_entry_adapter.ViewHolder holder, int position) {
        holder.in.setText(data.get(position).in);
        holder.out_time.setText(data.get(position).out_time);
        holder.date.setText(data.get(position).in_date);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView in, out_time, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            in = itemView.findViewById(R.id.in_time);
            out_time = itemView.findViewById(R.id.out_time);
            date = itemView.findViewById(R.id.date);
        }
    }
}
