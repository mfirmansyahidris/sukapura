package fi.com.sukapura.MainMenu.Activity.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import fi.com.sukapura.MainMenu.Activity.Konsinyasi;
import fi.com.sukapura.Model.GsonInterface.ResponseHandler;
import fi.com.sukapura.Services.ApiClient;
import fi.com.sukapura.Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by _fi on 11/9/2017.
 */

public class SendMsisdnKonsinyasi extends AsyncTask<Void, Void, Void> {
    Context context;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    String type, outlet_id, device_id;

    ApiInterface apiInterface;
    ArrayList<String> msisdns = new ArrayList<>();

    Dialog dialog;

    public SendMsisdnKonsinyasi(String type, ArrayList<String> msisdns, String outlet_id, String device_id, Context context) {
        this.type = type;
        this.msisdns = msisdns;
        this.outlet_id = outlet_id;
        this.device_id = device_id;
        this.context = context;
    }

    public SendMsisdnKonsinyasi(String type, ArrayList<String> msisdns, String outlet_id, String device_id, Context context, Dialog dialog) {
        this.type = type;
        this.msisdns = msisdns;
        this.outlet_id = outlet_id;
        this.device_id = device_id;
        this.context = context;
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(type.equals("2")){
            dialog.dismiss();
        }

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Mengirim Nomor...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < msisdns.size(); i++) {
            Log.e("sendMsisdn", "msisdn: " + msisdns.get(i).toString() + " outlet_id: " + outlet_id + " type: " + type + " device_id: " + device_id);
            Call<ResponseHandler> call = apiInterface.msisdn_konsinyasi(msisdns.get(i).toString(), outlet_id, type, device_id);
            call.enqueue(new Callback<ResponseHandler>() {
                @Override
                public void onResponse(Call<ResponseHandler> call, Response<ResponseHandler> response) {

                }

                @Override
                public void onFailure(Call<ResponseHandler> call, Throwable t) {

                }
            });
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage("Selesai");
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "Oke",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Konsinyasi konsinyasi = (Konsinyasi) context;
                        konsinyasi.loadMsisdn();
                    }
                });
        alertDialog.show();
    }
}
