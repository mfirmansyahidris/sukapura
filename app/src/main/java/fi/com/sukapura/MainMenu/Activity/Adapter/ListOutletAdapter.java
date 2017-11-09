package fi.com.sukapura.MainMenu.Activity.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fi.com.sukapura.MainMenu.Activity.Konsinyasi;
import fi.com.sukapura.MainMenu.Activity.ProfilOD;
import fi.com.sukapura.MainMenu.Activity.Utils.SendMsisdnKonsinyasi;
import fi.com.sukapura.Model.GsonInterface.ListOutletInterface;
import fi.com.sukapura.R;

/**
 * Created by _fi on 11/1/2017.
 */

public class ListOutletAdapter extends RecyclerView.Adapter<ListOutletAdapter.ViewHolder> {
    private List<ListOutletInterface.OutletDetail> lisOutlet;
    public Context context;
    public String action;

    public String device_id;
    public ArrayList<String> msisdn;

    public Dialog dialog;

    public ListOutletAdapter(List<ListOutletInterface.OutletDetail> lisOutlet, String action, Context context){
        this.lisOutlet = lisOutlet;
        this.action = action;
        this.context = context;
    }


    //constuctor for dialog konsinyasi.
    public ListOutletAdapter(List<ListOutletInterface.OutletDetail> lisOutlet, String action, ArrayList<String> msisdn, String device_id, Context context, Dialog dialog){
        this.lisOutlet = lisOutlet;
        this.msisdn = msisdn;
        this.action = action;
        this.context = context;
        this.device_id = device_id;
        this.dialog = dialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_outlet, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ListOutletInterface.OutletDetail outlet = lisOutlet.get(position);
        holder.tv_outlet_nama.setText(outlet.getNama());
        holder.tv_outlet_id.setText(outlet.getId());
        holder.tv_outlet_alamat.setText(outlet.getAlamat());
        holder.tv_outlet_tdc.setText(outlet.getTdc());
        holder.ll_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(action.equals("profil_od")){
                    Intent i = new Intent(context, ProfilOD.class);
                    i.putExtra("id", outlet.getId());
                    i.putExtra("name", outlet.getNama());
                    view.getContext().startActivity(i);
                }else if(action.equals("konsinyasi_od")){
                    Intent i = new Intent(context, Konsinyasi.class);
                    i.putExtra("id", outlet.getId());
                    i.putExtra("name", outlet.getNama());
                    view.getContext().startActivity(i);
                }else if(action.equals("pindah_outlet")){
                    SendMsisdnKonsinyasi sendMsisdnKonsinyasi = new SendMsisdnKonsinyasi("2", msisdn, outlet.getId(), device_id, context, dialog);
                    sendMsisdnKonsinyasi.execute();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lisOutlet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_outlet_nama, tv_outlet_id, tv_outlet_alamat, tv_outlet_tdc;
        public LinearLayout ll_holder;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_outlet_nama = itemView.findViewById(R.id.tv_outlet_nama);
            tv_outlet_id = itemView.findViewById(R.id.tv_outlet_id);
            tv_outlet_alamat = itemView.findViewById(R.id.tv_outlet_alamat);
            tv_outlet_tdc = itemView.findViewById(R.id.tv_outlet_tdc);
            ll_holder = itemView.findViewById(R.id.ll_holder);
        }
    }
}
