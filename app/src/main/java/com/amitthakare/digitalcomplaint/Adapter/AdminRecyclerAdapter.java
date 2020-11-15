package com.amitthakare.digitalcomplaint.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import  com.amitthakare.digitalcomplaint.AdminViewComplaint;
import  com.amitthakare.digitalcomplaint.Model.AdminRecyclerInfo;
import  com.amitthakare.digitalcomplaint.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdminRecyclerAdapter extends RecyclerView.Adapter<AdminRecyclerAdapter.MyViewHolder> {

    Context mContext;
    List<AdminRecyclerInfo> mData;

    public AdminRecyclerAdapter(Context mContext, List<AdminRecyclerInfo> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.complaint_format,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        String Name = "Full Name : "+mData.get(position).getComplaintFullName();
        String Mobile = "Mobile No : "+mData.get(position).getComplaintMobileNo();
        String Location = "Location : "+mData.get(position).getComplaintLocation();
        String City = "City : "+mData.get(position).getComplaintCity();
        String Description = "Description : "+mData.get(position).getComplaintDescription();
        String Status = "Status : "+mData.get(position).getComplaintStatus();

        Glide.with(mContext).load(mData.get(position).getComplaintImage()).into(holder.complaintImg);
        holder.name.setText(Name);
        holder.mobile.setText(Mobile);
        holder.location.setText(Location);
        holder.city.setText(City);
        holder.description.setText(Description);
        holder.status.setText(Status);

        holder.recyclerViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AdminViewComplaint.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("image",mData.get(position).getComplaintImage());
                intent.putExtra("name",mData.get(position).getComplaintFullName());
                intent.putExtra("mobile",mData.get(position).getComplaintMobileNo());
                intent.putExtra("location",mData.get(position).getComplaintLocation());
                intent.putExtra("city",mData.get(position).getComplaintCity());
                intent.putExtra("description",mData.get(position).getComplaintDescription());
                intent.putExtra("status",mData.get(position).getComplaintStatus());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView complaintImg;
        TextView name,mobile,location,description,city,status;
        LinearLayout recyclerViewLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            complaintImg = itemView.findViewById(R.id.complaintImgView);
            name = itemView.findViewById(R.id.complaintNameView);
            mobile =itemView.findViewById(R.id.complaintMobileNoView);
            location = itemView.findViewById(R.id.complaintLocationView);
            city = itemView.findViewById(R.id.complaintCityView);
            description = itemView.findViewById(R.id.complaintDescriptionView);
            recyclerViewLayout = itemView.findViewById(R.id.recyclerViewMainLayout);
            status = itemView.findViewById(R.id.complaintStatusView);

        }
    }
}
