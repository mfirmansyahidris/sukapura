package fi.com.sukapura.MainMenu.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fi.com.sukapura.MainMenu.Activity.Adapter.ListOutletAdapter;
import fi.com.sukapura.Model.GsonInterface.ListOutletInterface;
import fi.com.sukapura.R;
import fi.com.sukapura.Services.ApiClient;
import fi.com.sukapura.Services.ApiInterface;
import fi.com.sukapura.Services.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOutlet extends AppCompatActivity {
    private List<ListOutletInterface.OutletDetail> listOutlet = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListOutletAdapter outletAdapter;

    private EditText et_search;
    private ImageView iv_search;
    private ProgressBar pb_load_list;
    private TextView tv_title, tv_no_result;
    ApiInterface apiInterface;

    SessionManager sessionManager;

    String device_id;
    String action;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_outlet);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        device_id = user.get(SessionManager.DEVICE_ID);

        bundle = getIntent().getExtras();
        action = bundle.getString("action");

        recyclerView = findViewById(R.id.rv_list_outlet);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        et_search = findViewById(R.id.et_search);
        iv_search = findViewById(R.id.iv_search);
        pb_load_list = findViewById(R.id.pb_load_list);
        tv_title = findViewById(R.id.tv_title);
        tv_no_result = findViewById(R.id.tv_no_result);
        tv_title = findViewById(R.id.tv_title);

        tv_title.setText(bundle.getString("title"));

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_search.getText().toString().length() > 0){
                    loadOutlet(et_search.getText().toString());
                }else{
                    recyclerView.setVisibility(View.GONE);
                    tv_no_result.setVisibility(View.GONE);
                }
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et_search.getText().toString().length() > 0){
                    loadOutlet(et_search.getText().toString());
                }else{
                    recyclerView.setVisibility(View.GONE);
                    tv_no_result.setVisibility(View.GONE);
                }
            }
        });
    }

    public void loadOutlet(String keyword){
        recyclerView.setVisibility(View.GONE);
        tv_no_result.setVisibility(View.GONE);
        pb_load_list.setVisibility(View.VISIBLE);

        listOutlet.clear();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ListOutletInterface> call = apiInterface.listOutlet(device_id, keyword);
        call.enqueue(new Callback<ListOutletInterface>() {
            @Override
            public void onResponse(Call<ListOutletInterface> call, Response<ListOutletInterface> response) {
                String res = response.body().getResponse();
                if(res.equals("true")){
                    listOutlet = response.body().getOutletDetails();
                    pb_load_list.setVisibility(View.GONE);
                    if(listOutlet.size() > 0){
                        outletAdapter = new ListOutletAdapter(listOutlet, action, getApplicationContext());
                        recyclerView.setAdapter(outletAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        tv_no_result.setVisibility(View.GONE);
                        outletAdapter.notifyDataSetChanged();
                    }else{
                        tv_no_result.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListOutletInterface> call, Throwable t) {

            }
        });
    }
}
