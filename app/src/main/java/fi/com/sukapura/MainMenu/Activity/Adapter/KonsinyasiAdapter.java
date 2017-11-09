package fi.com.sukapura.MainMenu.Activity.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fi.com.sukapura.Model.GsonInterface.ListMsisdnKonsinyasi;
import fi.com.sukapura.R;

/**
 * Created by _fi on 11/8/2017.
 */

public class KonsinyasiAdapter extends  RecyclerView.Adapter<KonsinyasiAdapter.ViewHolder> {
    private List<ListMsisdnKonsinyasi.ListMsisdn> listMsisdns;

    public KonsinyasiAdapter(List<ListMsisdnKonsinyasi.ListMsisdn> listMsisdns){
        this.listMsisdns = listMsisdns;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_konsinyasi, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ListMsisdnKonsinyasi.ListMsisdn listMsisdn = listMsisdns.get(position);
        holder.tv_msisdn.setText(listMsisdn.getMsisdn());
        holder.ll_msisdn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.chk_msisdn.isChecked()){
                    holder.chk_msisdn.setChecked(false);
                }else {
                    holder.chk_msisdn.setChecked(true);
                }
            }
        });
        holder.chk_msisdn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    listMsisdns.get(position).setChecked(true);
                }else {
                    listMsisdns.get(position).setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return  listMsisdns.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_msisdn;
        public CheckBox chk_msisdn;
        public LinearLayout ll_msisdn;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_msisdn = itemView.findViewById(R.id.tv_msisdn);
            chk_msisdn = itemView.findViewById(R.id.chk_msisdn);
            ll_msisdn = itemView.findViewById(R.id.ll_msisdn);
        }
    }
}
