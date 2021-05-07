package com.amitthakare.digitalcomplaint.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import  com.amitthakare.digitalcomplaint.AdminViewComplaint;
import  com.amitthakare.digitalcomplaint.Model.RecyclerInfo;
import  com.amitthakare.digitalcomplaint.R;
import com.bumptech.glide.Glide;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    Context mContext;
    List<RecyclerInfo> mData;

    public RecyclerAdapter(Context context, List<RecyclerInfo> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.complaint_format,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, final int position) {

        Glide.with(mContext).load(mData.get(position).getComplaintImage()).into(holder.complaintImg);
        holder.name.setText(mData.get(position).getComplaintFullName());
        holder.mobile.setText(mData.get(position).getComplaintMobileNo());
        holder.location.setText(mData.get(position).getComplaintLocation());
        holder.city.setText(mData.get(position).getComplaintCity());
        holder.description.setText(mData.get(position).getComplaintDescription());
        holder.status.setText(mData.get(position).getComplaintStatus());


        /*holder.recyclerViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AdminViewComplaint.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("image",mData.get(position).getComplaintImage());
                intent.putExtra("name",mData.get(position).getComplaintFullName());
                intent.putExtra("mobile",mData.get(position).getComplaintMobileNo());
                intent.putExtra("location",mData.get(position).getComplaintLocation());
                intent.putExtra("city",mData.get(position).getComplaintCity());
                intent.putExtra("description",mData.get(position).getComplaintImage());
                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView complaintImg;
        TextView name,mobile,location,description,city,status;
        //LinearLayout recyclerViewLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

           complaintImg = itemView.findViewById(R.id.complaintImgView);
           name = itemView.findViewById(R.id.complaintNameView);
           mobile =itemView.findViewById(R.id.complaintMobileNoView);
           location = itemView.findViewById(R.id.complaintLocationView);
           city = itemView.findViewById(R.id.complaintCityView);
           description = itemView.findViewById(R.id.complaintDescriptionView);
         //  recyclerViewLayout = itemView.findViewById(R.id.recyclerViewMainLayout);
           status = itemView.findViewById(R.id.complaintStatusView);

        }
    }
}
