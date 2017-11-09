package fi.com.sukapura.MainMenu.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fi.com.sukapura.MainMenu.Activity.Adapter.ListNumberAdapter;
import fi.com.sukapura.Model.Database.NumberBundling;
import fi.com.sukapura.Model.GsonInterface.ResponseHandler;
import fi.com.sukapura.Model.Msisdn;
import fi.com.sukapura.R;
import fi.com.sukapura.Services.ApiClient;
import fi.com.sukapura.Services.ApiInterface;
import fi.com.sukapura.Services.RealmController;
import fi.com.sukapura.Services.SessionManager;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNumber extends AppCompatActivity {
    private List<Msisdn> listMsisdn = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListNumberAdapter numberAdapter;
    private Realm realm;
    private AlertDialog alertDialog;
    private SessionManager sessionManager;

    ImageView bt_scan, bt_clean, bt_send;
    ImageView bt_clean_in, bt_send_in, bt_add_num;
    EditText et_add_num;
    TextView tv_title, tv_empty_list;

    Bundle bundle;

    int countMsisdn;
    String type;

    String device_id, user_id;

    LinearLayout ll_bar_in, ll_bar_ex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_number);

        tv_title = findViewById(R.id.tv_title);
        tv_empty_list = findViewById(R.id.tv_empty_list);
        bt_scan = findViewById(R.id.bt_scan);
        bt_clean = findViewById(R.id.bt_clean);
        bt_send = findViewById(R.id.bt_send);

        et_add_num = findViewById(R.id.et_add_num);
        bt_add_num = findViewById(R.id.bt_add);
        bt_clean_in = findViewById(R.id.bt_clean_in);
        bt_send_in = findViewById(R.id.bt_send_in);

        ll_bar_ex = findViewById(R.id.ll_bar_ex);
        ll_bar_in = findViewById(R.id.ll_bar_in);

        bundle = getIntent().getExtras();
        type = bundle.getString("type");
        tv_title.setText(bundle.getString("title"));

        if (type.equals("1")){
            ll_bar_in.setVisibility(View.GONE);
            ll_bar_ex.setVisibility(View.VISIBLE);
        }else{
            ll_bar_ex.setVisibility(View.GONE);
            ll_bar_in.setVisibility(View.VISIBLE);
        }

        this.realm = RealmController.with(this).getRealm();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id = user.get(SessionManager.ID);
        device_id = user.get(SessionManager.DEVICE_ID);

        recyclerView = findViewById(R.id.rv_list_number);
        numberAdapter = new ListNumberAdapter(listMsisdn, this, type);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(numberAdapter);
        loadMsisdn();


        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Scanner.class);
                startActivityForResult(i, 1);
            }
        });

        bt_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clean();
            }
        });

        bt_clean_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clean();
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        bt_send_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        bt_add_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msisdn = et_add_num.getText().toString();
                RealmResults<NumberBundling> results = realm.where(NumberBundling.class)
                        .equalTo("number", msisdn)
                        .equalTo("type", type)
                        .findAll();
                if (results.size() < 1) {
                    NumberBundling numberBundling = new NumberBundling();
                    numberBundling.setId((int) (RealmController.getInstance().getNumber().size() + System.currentTimeMillis()));
                    numberBundling.setNumber(msisdn);
                    numberBundling.setChecked(false);
                    numberBundling.setType(type);
                    if (checkPhoneNumber(msisdn)) {
                        numberBundling.setValid(true);
                    } else {
                        numberBundling.setValid(false);
                    }
                    realm.beginTransaction();
                    realm.copyToRealm(numberBundling);
                    realm.commitTransaction();
                } else {
                    Toast.makeText(getApplicationContext(), "Nomor telah ada", Toast.LENGTH_LONG).show();
                }
                loadMsisdn();
                et_add_num.setText("");
            }
        });


    }

    public void clean(){
        if (countMsisdn < 1) {
            alertDialog = new AlertDialog.Builder(ListNumber.this).create();
            alertDialog.setMessage("Nomor masih kosong!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            final RealmResults<NumberBundling> results = realm.where(NumberBundling.class)
                    .equalTo("checked", true)
                    .equalTo("type", type)
                    .findAll();
            alertDialog = new AlertDialog.Builder(ListNumber.this).create();
            if (results.size() > 0) {
                alertDialog.setMessage("Yakin ingin menghapus " + results.size() + " nomor?");
            } else {
                alertDialog = new AlertDialog.Builder(ListNumber.this).create();
                alertDialog.setMessage("Silahkan pilih nomor yang ingin dihapus!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    realm.beginTransaction();
                    results.deleteAllFromRealm();
                    realm.commitTransaction();
                    alertDialog.dismiss();
                    countMsisdn = 0;
                    loadMsisdn();
                }
            });
            alertDialog.show();
        }
    }

    public void send(){
        if (countMsisdn < 1) {
            alertDialog = new AlertDialog.Builder(ListNumber.this).create();
            alertDialog.setMessage("Nomor masih kosong!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            RealmResults<NumberBundling> results = realm.where(NumberBundling.class)
                    .equalTo("checked", true)
                    .equalTo("type", type)
                    .findAll();
            alertDialog = new AlertDialog.Builder(ListNumber.this).create();
            if (results.size() > 0) {
                alertDialog.setMessage("Anda akan mengirim " + results.size() + " nomor, lanjutkan?");
            } else {
                alertDialog = new AlertDialog.Builder(ListNumber.this).create();
                alertDialog.setMessage("Silahkan pilih nomor yang ingin dikirim!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(isNetworkConnected()){
                        RealmResults<NumberBundling> results = realm.where(NumberBundling.class)
                                .equalTo("checked", true)
                                .equalTo("valid", true)
                                .equalTo("type", type)
                                .findAll();
                        ArrayList<String> listMsisdn = new ArrayList<String>();
                        for (i = 0; i < results.size(); i++) {
                            listMsisdn.add(results.get(i).getNumber());
                        }
                        SendNumber sendNumber = new SendNumber(listMsisdn, user_id, type);
                        sendNumber.execute();
                    }else{
                        alertDialog = new AlertDialog.Builder(ListNumber.this).create();
                        alertDialog.setMessage("Tidak dapat menghubungkan ke server.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        loadMsisdn();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            });
            alertDialog.show();
        }
    }

    public void loadMsisdn() {
        listMsisdn.clear();
        RealmResults<NumberBundling> results = realm.where(NumberBundling.class)
                .equalTo("type", type)
                .findAll();
        for (int i = 0; i < results.size(); i++) {
            Msisdn msisdn = new Msisdn(results.get(i).getNumber(), results.get(i).getValid(), results.get(i).getChecked());
            listMsisdn.add(msisdn);
            countMsisdn++;
        }
        if (countMsisdn > 0) {
            emptyMsisdn(false);
        } else {
            emptyMsisdn(true);
        }
        numberAdapter.notifyDataSetChanged();
    }

    private void emptyMsisdn(boolean empty) {
        if (empty) {
            this.countMsisdn = 0;
            tv_empty_list.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tv_title.setVisibility(View.INVISIBLE);
        } else {
            tv_empty_list.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            tv_title.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String msisdn = data.getStringExtra("scan_value");
                RealmResults<NumberBundling> results = realm.where(NumberBundling.class)
                        .equalTo("number", msisdn)
                        .equalTo("type", type)
                        .findAll();
                if (results.size() < 1) {
                    NumberBundling numberBundling = new NumberBundling();
                    numberBundling.setId((int) (RealmController.getInstance().getNumber().size() + System.currentTimeMillis()));
                    numberBundling.setNumber(msisdn);
                    numberBundling.setChecked(false);
                    numberBundling.setType(type);
                    if (checkPhoneNumber(msisdn)) {
                        numberBundling.setValid(true);
                    } else {
                        numberBundling.setValid(false);
                    }
                    realm.beginTransaction();
                    realm.copyToRealm(numberBundling);
                    realm.commitTransaction();
                } else {
                    Toast.makeText(getApplicationContext(), "Nomor telah ada", Toast.LENGTH_LONG).show();
                }
            }
        }
        loadMsisdn();
    }

    private boolean checkPhoneNumber(String result) {
        if (isNumber(result)) {
            if (validLength(result)) {
                if (isPhoneNumber(result)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isNumber(String result) {
        return result.matches("\\d+(?:\\.\\d+)?");
    }

    private boolean validLength(String result) {
        if ((result.length() < 20) && (result.length() > 6)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneNumber(String result) {
        String phoneCode = result.substring(0, 2);
        String phoneCodeFull = result.substring(0, 3);
        if (phoneCode.equals("08")) {
            return true;
        } else {
            if (phoneCodeFull.equals("628")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public class SendNumber extends AsyncTask<Void, Void, Void> {
        String type; // 1 = outlet bundling, 2 = migrasi ussim, 3 = akuisis ndudc
        String id;
        ArrayList msisdn;
        ApiInterface apiInterface;
        Realm realm;
        ProgressDialog progressDialog;
        AlertDialog alertDialog;
        int failed = 0;

        public SendNumber(ArrayList msisdn, String id, String type) {
            this.msisdn = msisdn;
            this.id = id;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            realm = RealmController.with(getApplication()).getRealm();
            progressDialog = new ProgressDialog(ListNumber.this);
            progressDialog.setMessage("Mengirim Nomor...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < msisdn.size(); i++) {
                Log.d("ListNumber", "id: " + id + ", msisdn: " + msisdn.get(i).toString() + ", type: " + type + ", device_id: " + device_id );
                Call<ResponseHandler> call = apiInterface.numberReport(id, msisdn.get(i).toString(), type, device_id);
                final int finalI = i;
                call.enqueue(new Callback<ResponseHandler>() {
                    @Override
                    public void onResponse(Call<ResponseHandler> call, Response<ResponseHandler> response) {
                        String success = response.body().getResponse();
                        if(success.equals("true")){
                            RealmResults<NumberBundling> results = realm.where(NumberBundling.class)
                                    .equalTo("number", msisdn.get(finalI).toString())
                                    .equalTo("type", type)
                                    .findAll();
                            realm.beginTransaction();
                            results.deleteAllFromRealm();
                            realm.commitTransaction();
                        }else{
                            Log.d("ListNumber", "success: " + success);
                            failed++;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseHandler> call, Throwable t) {
                        failed++;
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            alertDialog = new AlertDialog.Builder(ListNumber.this).create();
            alertDialog.setMessage("Selesai");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            countMsisdn = 0;
                            loadMsisdn();
                        }
                    });
            alertDialog.show();
        }
    }
}
