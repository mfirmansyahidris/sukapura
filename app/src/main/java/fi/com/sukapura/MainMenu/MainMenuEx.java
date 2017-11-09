package fi.com.sukapura.MainMenu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import fi.com.sukapura.MainMenu.Activity.ListNumber;
import fi.com.sukapura.Model.Database.NumberBundling;
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

public class MainMenuEx extends Fragment {
    TextView tv_keluar;
    SessionManager sessionManager;
    ApiInterface apiInterface;

    ImageView bt_scan;

    private Realm realm;

    public MainMenuEx(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflateView = inflater.inflate(R.layout.fragment_main_menu_ex, container, false);

        sessionManager = new SessionManager(getActivity().getApplicationContext());

        this.realm = RealmController.with(getActivity()).getRealm();

        tv_keluar = inflateView.findViewById(R.id.bt_keluar);
        bt_scan = inflateView.findViewById(R.id.bt_scan);

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

        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ListNumber.class);
                i.putExtra("title", "Daftar Nomor");
                i.putExtra("type", "1");
                startActivity(i);
            }
        });

        return inflateView;
    }

    private void logout(){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Memuat Data...");
        progressDialog.show();

        HashMap<String, String> user = sessionManager.getUserDetails();
        String id = user.get(SessionManager.ID);
        String device_id = user.get(SessionManager.DEVICE_ID);
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
