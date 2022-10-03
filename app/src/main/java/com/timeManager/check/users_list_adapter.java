package com.timeManager.check;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epicstudio.or3432.R;

import java.util.ArrayList;

public class users_list_adapter extends RecyclerView.Adapter<users_list_adapter.ViewHolder> {
    ArrayList<users_list_model> data = new ArrayList<>();

    public users_list_adapter(ArrayList<users_list_model> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public users_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull users_list_adapter.ViewHolder holder, int position) {
        holder.name.setText(data.get(position).name);
        holder.pd_btn.setTag(data.get(position).user_id);
        holder.entry_dbtn.setTag(data.get(position).user_id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private Button pd_btn, entry_dbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name_u);
            pd_btn = itemView.findViewById(R.id.personal_details);
            entry_dbtn = itemView.findViewById(R.id.login_details);

            pd_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), admin_user_profile.class);
                    intent.putExtra("user_id", pd_btn.getTag().toString());
                    view.getContext().startActivity(intent);
                }
            });
            entry_dbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), admin_user_entry.class);
                    intent.putExtra("user_id", entry_dbtn.getTag().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
