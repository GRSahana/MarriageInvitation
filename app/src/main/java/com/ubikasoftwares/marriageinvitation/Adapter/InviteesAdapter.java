package com.ubikasoftwares.marriageinvitation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubikasoftwares.marriageinvitation.InviteesDetailsActivity;
import com.ubikasoftwares.marriageinvitation.Model.InviteesDetails;
import com.ubikasoftwares.marriageinvitation.R;

import java.util.ArrayList;
import java.util.List;

public class InviteesAdapter extends RecyclerView.Adapter<InviteesAdapter.MyViewHolder> implements Filterable {

    public Context context;
    public static List<InviteesDetails> inviteesDetails;
    public static List<InviteesDetails> inviteesDetailsFilter;
    public String callingActivity;

    public InviteesAdapter(Context context, List<InviteesDetails> inviteesDetails, String callingActivity){
        this.context = context;
        this.inviteesDetails = inviteesDetails;
        this.inviteesDetailsFilter = inviteesDetails;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public InviteesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutInflater = LayoutInflater.from(context).inflate(R.layout.activity_invitees_list, parent, false);

        return new MyViewHolder(layoutInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteesAdapter.MyViewHolder holder, int position) {
        holder.tvStudentName.setText(inviteesDetails.get(position).getName());
        holder.tvStudentId.setText(inviteesDetails.get(position).getPhone());
        if(inviteesDetails.get(position).getStatus().equals("1")){
            holder.checkBoxStudent.setVisibility(View.VISIBLE);
        }else{
            holder.checkBoxStudent.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if (inviteesDetails == null) {
            return 0;
        }
        return inviteesDetails.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    inviteesDetails = inviteesDetailsFilter;
                } else {
                    List<InviteesDetails> filteredList = new ArrayList<>();
                    for (InviteesDetails row : inviteesDetailsFilter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())||row.getPhone().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    inviteesDetails = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = inviteesDetails;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inviteesDetails = (ArrayList<InviteesDetails>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentId;
        ImageView checkBoxStudent;

        public MyViewHolder(final View itemView) {
            super(itemView);

            tvStudentName = itemView.findViewById(R.id.name);
            tvStudentId = itemView.findViewById(R.id.phone);
            checkBoxStudent = itemView.findViewById(R.id.check);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        InviteesDetails checkEventSelected = inviteesDetails.get(pos);
                        //eventid = checkEventSelected.getId();

                        Intent intent = new Intent(context,InviteesDetailsActivity.class);
                        intent.putExtra("EventId",checkEventSelected.getId());
                        intent.putExtra("CallingActivity",callingActivity);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}
