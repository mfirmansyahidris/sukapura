package fi.com.sukapura;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import fi.com.sukapura.Model.GsonInterface.UserInterface;
import fi.com.sukapura.Services.ApiClient;
import fi.com.sukapura.Services.ApiInterface;
import fi.com.sukapura.Services.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth extends AppCompatActivity {

    TextView tv_login;
    EditText et_id, et_password;

    SessionManager session;
    String id, password, imei;

    ApiInterface apiInterface;

    UserInterface.UserDetail user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                Toast.makeText(getApplicationContext() , "Akses Aplikasi Diizinkan", Toast.LENGTH_SHORT).show();
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        session = new SessionManager(getApplicationContext());
        tv_login = findViewById(R.id.tv_login);
        et_id = findViewById(R.id.et_id);
        et_password = findViewById(R.id.et_password);

        id = et_id.getText().toString();
        password = et_password.getText().toString();

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = et_id.getText().toString();
                password = et_password.getText().toString();
                TelephonyManager mngr = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                imei = mngr.getDeviceId();

                Log.d("Auth", "ID: " + id + " | Password: " + password + " | imei: " + imei);

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(Auth.this);
                progressDialog.setMessage("Memuat Data...");
                progressDialog.show();

                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<UserInterface> call = apiInterface.getUser(id, password, imei);
                call.enqueue(new Callback<UserInterface>() {
                    @Override
                    public void onResponse(Call<UserInterface> call, Response<UserInterface> response) {
                        String success = response.body().getSuccess();
                        if(success.equals("true")){
                            user = response.body().getUserDetail();
                            session.createUserLoginSeason(user.getId(), user.getAccess(), imei);
                            progressDialog.dismiss();

                            Intent i = new Intent(getApplicationContext(), SukaPura.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }else{
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(Auth.this).create();
                            alertDialog.setMessage("Id atau Sandi Salah!");
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
                    public void onFailure(Call<UserInterface> call, Throwable t) {

                    }
                });
            }
        });
    }

}
