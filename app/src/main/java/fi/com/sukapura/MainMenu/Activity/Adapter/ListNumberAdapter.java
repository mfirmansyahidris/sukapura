package fi.com.sukapura.MainMenu.Activity.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fi.com.sukapura.Model.Msisdn;
import fi.com.sukapura.Model.Database.NumberBundling;
import fi.com.sukapura.R;
import fi.com.sukapura.Services.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by _fi on 10/18/2017.
 */

public class ListNumberAdapter extends  RecyclerView.Adapter<ListNumberAdapter.ViewHolder>{
    private List<Msisdn> listmsisdn;
    Context context;
    private Realm realm;
    String type;

    public ListNumberAdapter(List<Msisdn> listmsisdn, Context context, String type){
        this.listmsisdn = listmsisdn;
        this.context = context;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_number, parent, false);
        this.realm = RealmController.with((Activity) context).getRealm();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Msisdn msisdn = listmsisdn.get(position);
        holder.msisdn.setText(msisdn.getMsisdn());
        final RealmResults<NumberBundling> results = realm.where(NumberBundling.class)
                .equalTo("type", type)
                .findAll();
        holder.chk_number.setChecked(results.get(position).getChecked());
        if(results.get(position).getValid()){
            holder.valid.setText("valid");
            holder.valid.setTextColor(Color.parseColor("#1F9E1F"));
        }else{
            holder.valid.setText("invalid");
            holder.valid.setTextColor(Color.parseColor("#FF1A1a"));
        }
        holder.chk_number.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                RealmResults<NumberBundling> realmResults = realm.where(NumberBundling.class)
                        .equalTo("number", listmsisdn.get(position).getMsisdn())
                        .equalTo("type", type)
                        .findAll();
                realm.beginTransaction();
                if(b){
                    realmResults.get(0).setChecked(true);
                }else
                {
                    realmResults.get(0).setChecked(false);
                }
                realm.commitTransaction();
            }
        });

        holder.ll_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.chk_number.isChecked()){
                    holder.chk_number.setChecked(false);
                }else{
                    holder.chk_number.setChecked(true);
                }
            }
        });
}

    @Override
    public int getItemCount() {
        return listmsisdn.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView msisdn, valid;
        public CheckBox chk_number;
        public LinearLayout ll_holder;
        public ViewHolder(View itemView) {
            super(itemView);
            msisdn = itemView.findViewById(R.id.tv_msisdn);
            chk_number = itemView.findViewById(R.id.chk_number);
            valid = itemView.findViewById(R.id.tv_valid);
            ll_holder = itemView.findViewById(R.id.ll_holder);
        }
    }
}