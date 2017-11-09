package fi.com.sukapura;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import fi.com.sukapura.MainMenu.MainMenuEx;
import fi.com.sukapura.MainMenu.MainMenuIn;
import fi.com.sukapura.Services.SessionManager;

public class SukaPura extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukapura);

        session = new SessionManager(getApplicationContext());
        if(session.checkUserLogin()){
            finish();
            return;
        }

        HashMap<String, String> user = session.getUserDetails();
        String access = user.get(SessionManager.ACCESS);

        try{
            if(access.equals("ex")){
                //fragment
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.main_container, new MainMenuEx());
                fragmentTransaction.commit();
            }else if(access.equals("in")){
                //fragment
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.main_container, new MainMenuIn());
                fragmentTransaction.commit();
            }else{
                Intent i = new Intent(getApplicationContext(), Auth.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }catch(NullPointerException e){
            Intent i = new Intent(getApplicationContext(), Auth.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            e.printStackTrace();
        }
    }
}
