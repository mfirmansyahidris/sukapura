package fi.com.sukapura.MainMenu;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

import fi.com.sukapura.MainMenu.Activity.ListNumber;
import fi.com.sukapura.MainMenu.Activity.ListOutlet;
import fi.com.sukapura.Model.Database.NumberBundling;
import fi.com.sukapura.Model.GsonInterface.AkuisisiNdudcDetail;
import fi.com.sukapura.Model.GsonInterface.ResponseHandler;
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

public class MainMenuIn extends Fragment {
    TextView tv_keluar;
    TextView tv_profil_od, tv_konsinyasi, tv_migrasi_usim, tv_akuisisi_ndudc;
    SessionManager sessionManager;
    ApiInterface apiInterface;

    String id, device_id;

    private Realm realm;

    public MainMenuIn(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView =  inflater.inflate(R.layout.fragment_main_menu_in, container, false);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        id = user.get(SessionManager.ID);
        device_id = user.get(SessionManager.DEVICE_ID);

        this.realm = RealmController.with(getActivity()).getRealm();

        tv_profil_od = inflatedView.findViewById(R.id.tv_profil_od);
        tv_konsinyasi = inflatedView.findViewById(R.id.tv_konsinyasi);
        tv_migrasi_usim = inflatedView.findViewById(R.id.tv_migrasi_usim);
        tv_akuisisi_ndudc = inflatedView.findViewById(R.id.tv_akuisisi_ndudc);

        tv_keluar = inflatedView.findViewById(R.id.bt_keluar);

        tv_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setMessage("Anda yakin ingin keluar?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Iya",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                logout();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        tv_migrasi_usim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ListNumber.class);
                i.putExtra("title", "Migrasi USIM");
                i.putExtra("type", "2");
                startActivity(i);
            }
        });

        tv_akuisisi_ndudc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_sub_menu_akuisis_ndudc);
                TextView bt_dialog_lapor_ndudc = dialog.findViewById(R.id.bt_dialog_lapor_ndudc);
                TextView bt_dialog_hasil_ndudc = dialog.findViewById(R.id.bt_dialog_hasil_ndudc);
                TextView bt_dialog_tutup = dialog.findViewById(R.id.bt_dialog_tutup);

                bt_dialog_tutup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                bt_dialog_lapor_ndudc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), ListNumber.class);
                        i.putExtra("title", "Akuisisi NDUDC");
                        i.putExtra("type", "3");
                        startActivity(i);
                    }
                });

                bt_dialog_hasil_ndudc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAkuisisiDetail();
                    }
                });
            }
        });

        tv_profil_od.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ListOutlet.class);
                i.putExtra("title", "Profil OD");
                i.putExtra("action", "profil_od");
                startActivity(i);
            }
        });

        tv_konsinyasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ListOutlet.class);
                i.putExtra("title", "Konsinyasi OD");
                i.putExtra("action", "konsinyasi_od");
                startActivity(i);
            }
        });

        return inflatedView;
    }

    public void showAkuisisiDetail(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_akuisisi_ndudc_detail);
        final ProgressBar pb_dialog_detail_ndudc = dialog.findViewById(R.id.pb_dialog_detail_ndudc);
        final TextView tv_dialog_detail_ndudc = dialog.findViewById(R.id.tv_dialog_detail_ndudc);
        TextView bt_dialog_close = dialog.findViewById(R.id.bt_dialog_tutup);

        tv_dialog_detail_ndudc.setVisibility(View.GONE);
        pb_dialog_detail_ndudc.setVisibility(View.VISIBLE);

        dialog.show();

        final AkuisisiNdudcDetail.Detail[] detail = new AkuisisiNdudcDetail.Detail[1];
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AkuisisiNdudcDetail> call = apiInterface.akuisisi_ndudc_detail(id, device_id);
        call.enqueue(new Callback<AkuisisiNdudcDetail>() {
            @Override
            public void onResponse(Call<AkuisisiNdudcDetail> call, Response<AkuisisiNdudcDetail> response) {
                String res = response.body().getResponse();
                if(res.equals("true")){
                    String detail_text = "";
                    detail[0] = response.body().getDetail();

                    detail_text += "ID " + detail[0].getId();
                    detail_text += ", atas nama " + detail[0].getName() ;
                    detail_text += ", cluster " + detail[0].getCluster() ;
                    detail_text += ", telah melaporkan " + detail[0].getValid()  + " MSISDN";
                    detail_text += ", dan akuisisi new data user " + detail[0].getValid_ndudc() + " MSISDN" ;
                    tv_dialog_detail_ndudc.setText(detail_text);
                }else{
                    tv_dialog_detail_ndudc.setText("Sesi telah berakhir, silahkan login kembali.");
                }
                pb_dialog_detail_ndudc.setVisibility(View.GONE);
                tv_dialog_detail_ndudc.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<AkuisisiNdudcDetail> call, Throwable t) {
                tv_dialog_detail_ndudc.setText("Terjadi kesalahan dalam koneksi");
                tv_dialog_detail_ndudc.setVisibility(View.VISIBLE);
                pb_dialog_detail_ndudc.setVisibility(View.GONE);
            }
        });

        bt_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void logout(){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Memuat Data...");
        progressDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseHandler> call = apiInterface.logout(id, device_id);
        call.enqueue(new Callback<ResponseHandler>() {
            @Override
            public void onResponse(Call<ResponseHandler> call, Response<ResponseHandler> response) {
                String res = response.body().getResponse();
                if(res.equals("true")){
                    progressDialog.dismiss();
                    sessionManager.logoutUser();

                    RealmResults<NumberBundling> results = realm.where(NumberBundling.class).findAll();
                    realm.beginTransaction();
                    results.deleteAllFromRealm();
                    realm.commitTransaction();

                    getActivity().finish();
                }else{
                    progressDialog.dismiss();

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setMessage("Gagal dalam sambungan");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Oke",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseHandler> call, Throwable t) {
                progressDialog.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
