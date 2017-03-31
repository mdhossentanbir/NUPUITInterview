package com.mdhossentanbir.nupuitinterview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Tanbir on 31-Mar-17.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    int oneTime = 10;

    private List<ContactList> contactLists;
    //boolean isMoreContactAvailable;

    public CustomAdapter(List<ContactList> contactLists) {
        this.contactLists = contactLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == R.layout.custom_list_view){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_list_view, parent, false);
        }else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (position == oneTime){
            holder.button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                   if (contactLists.size()>position){
                       System.out.println("More Available");
                       oneTime+=10;
                       if (oneTime>=contactLists.size()){
                           oneTime = contactLists.size();
                           notifyDataSetChanged();
                           holder.button.setText("No more results to show");
                       }else {
                           notifyDataSetChanged();
                       }
                   }
                }
            });
        }else {
            if (position<contactLists.size()) {
                ContactList current = contactLists.get(position);
                holder.name.setText(current.getName());
                holder.number.setText(current.getNumber());
            }
        }
    }

    @Override
    public int getItemCount() {
        return oneTime+1;
    }


    public boolean isMoreContactAvailable(int position){
        if (getItemCount()>position){
            return true;
        }
        return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, number;
        public Button button;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            number = (TextView) view.findViewById(R.id.number);
            button = (Button) view.findViewById(R.id.load_more);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == oneTime) ? R.layout.footer : R.layout.custom_list_view;
    }
}
