//package com.example.splashscreen;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class TaskSettingsAdapter extends RecyclerView.Adapter<TaskSettingsAdapter.ViewHolder> {
//
//    ArrayList taskSettingImg, taskSettingName, taskSettingDesc;
//    Context context;
//
//    LinearLayout taskSettingsLayout;
//
//    public TaskSettingsAdapter(Context context, ArrayList taskSettingImg, ArrayList taskSettingName, ArrayList taskSettingDesc){
//        this.context = context;
////        this.taskSettingImg = taskSettingImg;
//        this.taskSettingName = taskSettingName;
//        this.taskSettingDesc = taskSettingDesc;
//    }
//
//
//
//    @NonNull
//    @Override
//    public TaskSettingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_settings_items, parent, false);
//        TaskSettingsAdapter.ViewHolder viewHolder = new TaskSettingsAdapter.ViewHolder(view);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        int res = (int) taskSettingImg.get(position);
//        holder.images.setImageResource(res);
//        holder.text.setText((String) taskSettingName.get(position));
//        holder.desc.setText((String) taskSettingDesc.get(position));
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return taskSettingImg.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        ImageView images;
//        TextView text, desc;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            images = (ImageView) itemView.findViewById(R.id.taskSettingImg);
//            text = (TextView) itemView.findViewById(R.id.taskSettingName);
//            desc = (TextView) itemView.findViewById(R.id.taskSettingDesc);
//            taskSettingsLayout = (LinearLayout) itemView.findViewById(R.id.taskSettingsLayout);
//        }
//
//        @Override
//        public void onClick(View view) {
//            Toast.makeText(view.getContext(), "position = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
//        }
//    }
//}
