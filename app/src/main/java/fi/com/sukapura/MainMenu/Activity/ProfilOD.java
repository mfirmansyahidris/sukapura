package fi.com.sukapura.MainMenu.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import fi.com.sukapura.Model.GsonInterface.ResponseHandler;
import fi.com.sukapura.R;
import fi.com.sukapura.Services.ApiClient;
import fi.com.sukapura.Services.ApiInterface;
import fi.com.sukapura.Services.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilOD extends AppCompatActivity {
    private TextView tv_title;
    private EditText et_omset_samsung, et_omset_oppo, et_omset_vivo, et_omset_iphone, et_omset_huawei, et_omset_lain;
    private EditText et_distribusi_simpati, et_distribusi_im3, et_distribusi_xl, et_distribusi_kartuas, et_distribusi_mentari,
                et_distribusi_axis, et_distribusi_loop, et_distribusi_tri, et_distribusi_smartfren;
    private EditText et_sales_simpati, et_sales_im3, et_sales_xl, et_sales_kartuas, et_sales_mentari,
            et_sales_axis, et_sales_loop, et_sales_tri, et_sales_smartfren;
    private EditText et_belanja_telkomsel, et_belanja_xl, et_belanja_smartfren, et_belanja_indosat, et_belanja_tri;
    private String omset_samsung, omset_oppo, omset_vivo, omset_iphone, omset_huawei, omset_lain;
    private String distribusi_simpati, distribusi_im3, distribusi_xl, distribusi_kartuas, distribusi_mentari,
                distribusi_axis, distribusi_loop, distribusi_tri, distribusi_smartfren;
    private String sales_simpati, sales_im3, sales_xl, sales_kartuas, sales_mentari, sales_axis, sales_loop, sales_tri, sales_smartfren;
    private String belanja_telkomsel, belanja_xl, belanja_smartfren, belanja_indosat, belanja_tri;
    private SessionManager sessionManager;
    private String user_id, device_id, outlet_id, outlet_name;
    private ImageView bt_clear, bt_send;
    ApiInterface apiInterface;

    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_od);

        tv_title = findViewById(R.id.tv_title);

        bt_clear = findViewById(R.id.bt_clean);
        bt_send = findViewById(R.id.bt_send);

        et_omset_samsung = findViewById(R.id.et_omset_samsung);
        et_omset_oppo = findViewById(R.id.et_omset_oppo);
        et_omset_vivo = findViewById(R.id.et_omset_vivo);
        et_omset_iphone = findViewById(R.id.et_omset_iphone);
        et_omset_huawei = findViewById(R.id.et_omset_huawei);
        et_omset_lain = findViewById(R.id.et_omset_lain);

        et_distribusi_simpati = findViewById(R.id.et_distribusi_simpati);
        et_distribusi_im3 = findViewById(R.id.et_distribusi_im3);
        et_distribusi_xl = findViewById(R.id.et_distribusi_xl);
        et_distribusi_kartuas = findViewById(R.id.et_distribusi_as);
        et_distribusi_mentari = findViewById(R.id.et_distribusi_mentari);
        et_distribusi_axis = findViewById(R.id.et_distribusi_axis);
        et_distribusi_loop = findViewById(R.id.et_distribusi_loop);
        et_distribusi_tri = findViewById(R.id.et_distribusi_tri);
        et_distribusi_smartfren = findViewById(R.id.et_distribusi_smartfren);

        et_sales_simpati = findViewById(R.id.et_sales_simpati);
        et_sales_im3 = findViewById(R.id.et_sales_im3);
        et_sales_xl = findViewById(R.id.et_sales_xl);
        et_sales_kartuas = findViewById(R.id.et_sales_as);
        et_sales_mentari = findViewById(R.id.et_sales_mentari);
        et_sales_axis = findViewById(R.id.et_sales_axis);
        et_sales_loop = findViewById(R.id.et_sales_loop);
        et_sales_tri = findViewById(R.id.et_sales_tri);
        et_sales_smartfren = findViewById(R.id.et_sales_smartfren);

        et_belanja_telkomsel = findViewById(R.id.et_belanja_telkomsel);
        et_belanja_xl = findViewById(R.id.et_belanja_xl);
        et_belanja_smartfren = findViewById(R.id.et_belanja_smartfren);
        et_belanja_indosat = findViewById(R.id.et_belanja_indosat);
        et_belanja_tri = findViewById(R.id.et_belanja_tri);

        bundle = getIntent().getExtras();
        outlet_id = bundle.getString("id");
        outlet_name = bundle.getString("name");
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id = user.get(SessionManager.ID);
        device_id = user.get(SessionManager.DEVICE_ID);

        tv_title.setText(outlet_name  + "(" + outlet_id + ")");

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_omset_samsung.setText("");
                et_omset_oppo.setText("");
                et_omset_vivo.setText("");
                et_omset_iphone.setText("");
                et_omset_huawei.setText("");
                et_omset_lain.setText("");

                et_distribusi_simpati.setText("");
                et_distribusi_im3.setText("");
                et_distribusi_xl.setText("");
                et_distribusi_kartuas.setText("");
                et_distribusi_mentari.setText("");
                et_distribusi_axis.setText("");
                et_distribusi_loop.setText("");
                et_distribusi_tri.setText("");
                et_distribusi_smartfren.setText("");

                et_sales_simpati.setText("");
                et_sales_im3.setText("");
                et_sales_xl.setText("");
                et_sales_kartuas.setText("");
                et_sales_mentari.setText("");
                et_sales_axis.setText("");
                et_sales_loop.setText("");
                et_sales_tri.setText("");
                et_sales_smartfren.setText("");

                et_belanja_telkomsel.setText("");
                et_belanja_xl.setText("");
                et_belanja_smartfren.setText("");
                et_belanja_indosat.setText("");
                et_belanja_tri.setText("");
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                omset_samsung = et_omset_samsung.getText().toString();
                omset_oppo = et_omset_oppo.getText().toString();
                omset_vivo = et_omset_vivo.getText().toString();
                omset_iphone = et_omset_iphone.getText().toString();
                omset_huawei = et_omset_huawei.getText().toString();
                omset_lain = et_omset_lain.getText().toString();

                distribusi_simpati = et_distribusi_simpati.getText().toString();
                distribusi_im3 = et_distribusi_im3.getText().toString();
                distribusi_xl = et_distribusi_xl.getText().toString();
                distribusi_kartuas = et_distribusi_kartuas.getText().toString();
                distribusi_mentari = et_distribusi_mentari.getText().toString();
                distribusi_axis = et_distribusi_axis.getText().toString();
                distribusi_loop = et_distribusi_loop.getText().toString();
                distribusi_tri = et_distribusi_tri.getText().toString();
                distribusi_smartfren = et_distribusi_smartfren.getText().toString();

                sales_simpati = et_sales_simpati.getText().toString();
                sales_im3 = et_sales_im3.getText().toString();
                sales_xl = et_sales_xl.getText().toString();
                sales_kartuas = et_sales_kartuas.getText().toString();
                sales_mentari = et_sales_mentari.getText().toString();
                sales_axis = et_sales_axis.getText().toString();
                sales_loop = et_sales_loop.getText().toString();
                sales_tri = et_sales_tri.getText().toString();
                sales_smartfren = et_sales_smartfren.getText().toString();

                belanja_telkomsel = et_belanja_telkomsel.getText().toString();
                belanja_xl = et_belanja_xl.getText().toString();
                belanja_smartfren = et_belanja_smartfren.getText().toString();
                belanja_indosat = et_belanja_indosat.getText().toString();
                belanja_tri = et_belanja_tri.getText().toString();
                send();
            }
        });

    }

    private void send(){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ProfilOD.this);
        progressDialog.setMessage("Memperbaharui Data...");
        progressDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseHandler> call = apiInterface
                .profil_od(
                        device_id, user_id, outlet_id,
                        omset_samsung, omset_oppo, omset_vivo, omset_iphone, omset_huawei, omset_lain,
                        distribusi_simpati, distribusi_im3, distribusi_xl, distribusi_kartuas, distribusi_mentari, distribusi_axis,
                        distribusi_loop, distribusi_tri, distribusi_smartfren,
                        sales_simpati, sales_im3, sales_xl, sales_kartuas, sales_mentari, sales_axis, sales_loop, sales_tri, sales_smartfren,
                        belanja_telkomsel, belanja_xl, belanja_smartfren, belanja_indosat, belanja_tri);
        call.enqueue(new Callback<ResponseHandler>() {
            @Override
            public void onResponse(Call<ResponseHandler> call, Response<ResponseHandler> response) {
                String res = response.body().getResponse();
                if(res.equals("true")){
                    String success = response.body().getSuccess();
                    if(success.equals("true")){
                        progressDialog.dismiss();
                        final AlertDialog alertDialog = new AlertDialog.Builder(ProfilOD.this).create();
                        alertDialog.setMessage("Profil Outlet Berhasil Diperbaharui.");
                        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }else{
                        progressDialog.dismiss();
                        final AlertDialog alertDialog = new AlertDialog.Builder(ProfilOD.this).create();
                        alertDialog.setMessage("Profil Outlet Gagal Diperbaharui, untuk Bantuan Silahkan Hubungi Administrator!");
                        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }

                }else{
                    progressDialog.dismiss();
                    final AlertDialog alertDialog = new AlertDialog.Builder(ProfilOD.this).create();
                    alertDialog.setMessage("Sesi anda telah berakhir, silahkan login kembali untuk memulai sesi.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseHandler> call, Throwable t) {
                progressDialog.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(ProfilOD.this).create();
                alertDialog.setMessage("Gagal dalam sambungan");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }
}
