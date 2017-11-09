package fi.com.sukapura.Services;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import fi.com.sukapura.Model.Database.NumberBundling;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by _fi on 10/19/2017.
 */

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment){
        if(instance == null){
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {
        realm.beginTransaction();
//        realm.clear(NumberBundling.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<NumberBundling> getNumber() {
        return realm.where(NumberBundling.class).findAll();
    }

    //query a single item with the given id
    public NumberBundling getNumber(String number) {
        return realm.where(NumberBundling.class).equalTo("number", number).findFirst();
    }
}
