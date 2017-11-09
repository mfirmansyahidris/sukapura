package fi.com.sukapura.MainMenu.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fi.com.sukapura.MainMenu.Activity.Adapter.KonsinyasiAdapter;
import fi.com.sukapura.MainMenu.Activity.Adapter.ListOutletAdapter;
import fi.com.sukapura.MainMenu.Activity.Utils.SendMsisdnKonsinyasi;
import fi.com.sukapura.Model.GsonInterface.ListMsisdnKonsinyasi;
import fi.com.sukapura.Model.GsonInterface.ListOutletInterface;
import fi.com.sukapura.R;
import fi.com.sukapura.Services.ApiClient;
import fi.com.sukapura.Services.ApiInterface;
import fi.com.sukapura.Services.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Konsinyasi extends AppCompatActivity {

    private TextView tv_title, tv_msisdn_result;
    private String title;
    private String outlet_id, outlet_name;
    private String device_id;
    private TextView bt_cek_sisa, bt_pindah_stok;
    public ProgressBar pb_msisdn;

    private List<ListMsisdnKonsinyasi.ListMsisdn> listMsisdn = new ArrayList<>();
    private RecyclerView recyclerView;
    private KonsinyasiAdapter konsinyasiAdapter;
    ApiInterface apiInterface;

    ArrayList<String> msisdns = new ArrayList<>();

    Bundle bundle;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsinyasi);

        bundle = getIntent().getExtras();
        outlet_id = bundle.getString("id");
        outlet_name = bundle.getString("name");
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        device_id = user.get(SessionManager.DEVICE_ID);

        tv_title = findViewById(R.id.tv_title);
        tv_msisdn_result = findViewById(R.id.tv_msisdn_result);
        bt_cek_sisa = findViewById(R.id.bt_cek_sisa);
        bt_pindah_stok = findViewById(R.id.bt_pindah_stokl);
        recyclerView = findViewById(R.id.rv_msisdn_konsinyasi);
        pb_msisdn = findViewById(R.id.pb_msisdn);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        title = outlet_name + "(" + outlet_id + ")";
        tv_title.setText(title);

        loadMsisdn();

        bt_cek_sisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Konsinyasi.this).create();
                if(listMsisdn.size() < 1){
                    alertDialog.setMessage("Tidak ada nomor ditemukan.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
                }else{
                    int sold = 0;
                    int not_sold = 0;

                    for (int i = 0; i < listMsisdn.size(); i++) {
                        if (listMsisdn.get(i).isChecked()) {
                            not_sold++;
                        } else {
                            sold++;
                        }
                    }
                    alertDialog.setMessage("Nomor yang tersisa: " + not_sold + " Nomor, dan yang terjual: " + sold + " Nomor. Lanjutkan?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Lanjutkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (i = 0; i < listMsisdn.size(); i++) {
                                if (!listMsisdn.get(i).isChecked()) {
                                    msisdns.add(listMsisdn.get(i).getMsisdn());
                                }
                            }
                            SendMsisdnKonsinyasi sendMsisdnKonsinyasi = new SendMsisdnKonsinyasi("1", msisdns, outlet_id, device_id, Konsinyasi.this);
                            sendMsisdnKonsinyasi.execute();
                        }
                    });
                }

                alertDialog.show();
            }
        });

        bt_pindah_stok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listMsisdn.size() < 1){
                    final AlertDialog alertDialog = new AlertDialog.Builder(Konsinyasi.this).create();
                    alertDialog.setMessage("Tidak ada nomor ditemukan.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }else{
                    int onChecked = 0;
                    for(int i = 0; i < listMsisdn.size(); i++){
                        if(listMsisdn.get(i).isChecked()){
                            onChecked++;
                        }
                    }
                    if(onChecked < 1){
                        final AlertDialog alertDialog = new AlertDialog.Builder(Konsinyasi.this).create();
                        alertDialog.setMessage("Silahkan Pilih Nomor yang ingin dipindahkan.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }else{
                        final Dialog dialog = new Dialog(Konsinyasi.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_opsi_konsinyasi_od);

                        TextView bt_pindah_outlet = dialog.findViewById(R.id.bt_pindah_outlet);
                        TextView bt_redirect_sales = dialog.findViewById(R.id.bt_redirect_sales);
                        TextView bt_dialog_tutup = dialog.findViewById(R.id.bt_dialog_tutup);

                        bt_dialog_tutup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        bt_pindah_outlet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                showListOutlet();
                            }
                        });

                        bt_redirect_sales.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();

                                final Dialog dialog = new Dialog(Konsinyasi.this);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_input_redirect_sales);
                                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                                layoutParams.gravity = Gravity.CENTER;

                                dialog.getWindow().setAttributes(layoutParams);

                                final EditText et_redirect_sales_input = dialog.findViewById(R.id.et_redirect_sales_input);
                                TextView bt_cancel = dialog.findViewById(R.id.bt_redirect_sales_cancel);
                                TextView bt_ok = dialog.findViewById(R.id.bt_redirect_sales_oke);

                                bt_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                                bt_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ArrayList<String> msisdn_send = new ArrayList<>();
                                        for (int i = 0; i < listMsisdn.size(); i++) {
                                            if (listMsisdn.get(i).isChecked()) {
                                                msisdn_send.add(listMsisdn.get(i).getMsisdn());
                                            }
                                        }
                                        String to = et_redirect_sales_input.getText().toString();
                                        SendMsisdnKonsinyasi sendMsisdnKonsinyasi = new SendMsisdnKonsinyasi("3", msisdn_send, to, device_id, Konsinyasi.this);
                                        sendMsisdnKonsinyasi.execute();
                                    }
                                });

                                dialog.show();

                            }
                        });
                        dialog.show();
                    }
                }
            }
        });

    }

    public void showListOutlet() {
        final Dialog dialog = new Dialog(Konsinyasi.this);
        final ArrayList<String> msisdn_send = new ArrayList<>();
        for (int i = 0; i < listMsisdn.size(); i++) {
            if (listMsisdn.get(i).isChecked()) {
                msisdn_send.add(listMsisdn.get(i).getMsisdn());
            }
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list_outlet);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(layoutParams);

        TextView bt_dialog_tutup = dialog.findViewById(R.id.bt_dialog_tutup);
        final EditText et_search = dialog.findViewById(R.id.et_search);
        ImageView bt_search = dialog.findViewById(R.id.iv_search);
        final ProgressBar pb_list_outlet = dialog.findViewById(R.id.pb_dialog_list_outlet);
        final TextView tv_hasil = dialog.findViewById(R.id.tv_no_result);

        final RecyclerView dialog_recycler_view = dialog.findViewById(R.id.rv_list_outlet);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        dialog_recycler_view.setLayoutManager(layoutManager);
        dialog_recycler_view.setItemAnimator(new DefaultItemAnimator());

        bt_dialog_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog_recycler_view.setVisibility(View.GONE);
        pb_list_outlet.setVisibility(View.GONE);
        tv_hasil.setVisibility(View.GONE);

        dialog.show();

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               LoadOutlet loadOutlet = new LoadOutlet(msisdn_send, pb_list_outlet, dialog_recycler_view, tv_hasil, et_search, dialog);
               loadOutlet.execute();
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                LoadOutlet loadOutlet = new LoadOutlet(msisdn_send,pb_list_outlet, dialog_recycler_view, tv_hasil, et_search, dialog);
                loadOutlet.execute();
            }
        });
    }


    public void loadMsisdn() {
        pb_msisdn.setVisibility(View.VISIBLE);
        tv_msisdn_result.setVisibility(View.GONE);
        recyclerView.setVisibility(View.INVISIBLE);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ListMsisdnKonsinyasi> call = apiInterface.list_msisdn_konsinyasi(outlet_id, device_id);
        call.enqueue(new Callback<ListMsisdnKonsinyasi>() {
            @Override
            public void onResponse(Call<ListMsisdnKonsinyasi> call, Response<ListMsisdnKonsinyasi> response) {
                Log.d("Konsinyasi", "res:" + response.code());
                String res = response.body().getResponse();
                if (res.equals("true")) {
                    listMsisdn = response.body().getList_msisdn();
                    if (listMsisdn.size() > 0) {
                        konsinyasiAdapter = new KonsinyasiAdapter(listMsisdn);
                        recyclerView.setAdapter(konsinyasiAdapter);
                        konsinyasiAdapter.notifyDataSetChanged();

                        pb_msisdn.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        tv_msisdn_result.setVisibility(View.GONE);
                    } else {
                        pb_msisdn.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        tv_msisdn_result.setText("Tidak ada nomor ditemukan");
                        tv_msisdn_result.setVisibility(View.VISIBLE);
                    }
                } else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(Konsinyasi.this).create();
                    alertDialog.setMessage("Sesi anda telah berakhir, silahkan login kembali untuk memulai sesi.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                    finish();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<ListMsisdnKonsinyasi> call, Throwable t) {
                pb_msisdn.setVisibility(View.GONE);
                tv_msisdn_result.setText("Gagal dalam sambungan");
                tv_msisdn_result.setVisibility(View.VISIBLE);
            }
        });
    }

    public class LoadOutlet extends AsyncTask<Void, Void, Void>{
        ArrayList<String> listMsisdn = new ArrayList<>();
        ProgressBar pb_list_outlet;
        RecyclerView dialog_recycler_view;
        TextView tv_hasil;
        EditText et_search;
        String keyword;
        Dialog dialog;

        public LoadOutlet(ArrayList<String> listMsisdn, ProgressBar pb_list_outlet, RecyclerView dialog_recycler_view, TextView tv_hasil, EditText et_search, Dialog dialog){
            this.listMsisdn = listMsisdn;
            this.pb_list_outlet = pb_list_outlet;
            this.dialog_recycler_view = dialog_recycler_view;
            this.tv_hasil = tv_hasil;
            this.et_search = et_search;
            this.dialog = dialog;
        }

        List<ListOutletInterface.OutletDetail> listOutlet = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_list_outlet.setVisibility(View.VISIBLE);
            dialog_recycler_view.setVisibility(View.GONE);
            tv_hasil.setVisibility(View.GONE);

            keyword = et_search.getText().toString();
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            listOutlet.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Call<ListOutletInterface> call = apiInterface.listOutlet(device_id, keyword);
            call.enqueue(new Callback<ListOutletInterface>() {
                @Override
                public void onResponse(Call<ListOutletInterface> call, Response<ListOutletInterface> response) {
                    String res = response.body().getResponse();
                    if (res.equals("true")) {
                        listOutlet = response.body().getOutletDetails();
                        if (listOutlet.size() > 0) {
                            ListOutletAdapter listOutletAdapter = new ListOutletAdapter(listOutlet, "pindah_outlet",  listMsisdn, device_id, Konsinyasi.this, dialog);
                            dialog_recycler_view.setAdapter(listOutletAdapter);
                            listOutletAdapter.notifyDataSetChanged();
                            pb_list_outlet.setVisibility(View.GONE);
                            tv_hasil.setVisibility(View.GONE);
                            dialog_recycler_view.setVisibility(View.VISIBLE);
                        } else {
                            pb_list_outlet.setVisibility(View.GONE);
                            tv_hasil.setVisibility(View.VISIBLE);
                            dialog_recycler_view.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListOutletInterface> call, Throwable t) {

                }
            });
            return null;
        }
    }
}
