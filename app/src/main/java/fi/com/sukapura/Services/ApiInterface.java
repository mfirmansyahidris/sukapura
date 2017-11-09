package fi.com.sukapura.Services;

import fi.com.sukapura.Model.GsonInterface.AkuisisiNdudcDetail;
import fi.com.sukapura.Model.GsonInterface.ListMsisdnKonsinyasi;
import fi.com.sukapura.Model.GsonInterface.ListOutletInterface;
import fi.com.sukapura.Model.GsonInterface.ResponseHandler;
import fi.com.sukapura.Model.GsonInterface.UserInterface;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by _fi on 10/16/2017.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<UserInterface> getUser(
            @Field("id") String id,
            @Field("password") String password,
            @Field("imei") String imei);

    @FormUrlEncoded
    @POST("logout.php")
    Call<ResponseHandler> logout(
            @Field("id") String id,
            @Field("imei") String imei);

    @FormUrlEncoded
    @POST("get_number.php")
    Call<ResponseHandler> numberReport(
            @Field("id") String id,
            @Field("msisdn") String msisdn,
            @Field("type") String type,
            @Field("imei") String imei);

    @FormUrlEncoded
    @POST("list_outlet.php")
    Call<ListOutletInterface> listOutlet(
            @Field("imei") String imei,
            @Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("get_profil_od.php")
    Call<ResponseHandler> profil_od(
            @Field("imei") String imei,
            @Field("user") String user,
            @Field("id_outlet") String id_outlet,

            @Field("omset_samsung") String omset_samsung,
            @Field("omset_oppo") String omset_oppo,
            @Field("omset_vivo") String omset_vivo,
            @Field("omset_iphone") String omset_iphone,
            @Field("omset_huawei") String omset_huawei,
            @Field("omset_lain") String omset_lain,

            @Field("distribusi_simpati") String distribusi_simpati,
            @Field("distribusi_im3") String distribusi_im3,
            @Field("distribusi_xl") String distribusi_xl,
            @Field("distribusi_kartuas") String distribusi_kartuas,
            @Field("distribusi_mentari") String distribusi_mentari,
            @Field("distribusi_axis") String distribusi_axis,
            @Field("distribusi_loop") String distribusi_loop,
            @Field("distribusi_tri") String distribusi_tri,
            @Field("distribusi_smartfren") String distribusi_smartfren,

            @Field("sales_simpati") String sales_simpati,
            @Field("sales_im3") String sales_im3,
            @Field("sales_xl") String sales_xl,
            @Field("sales_kartuas") String sales_kartuas,
            @Field("sales_mentari") String sales_mentari,
            @Field("sales_axis") String sales_axis,
            @Field("sales_loop") String sales_loop,
            @Field("sales_tri") String sales_tri,
            @Field("sales_smartfren") String sales_smartfren,

            @Field("belanja_telkomsel") String belanja_telkomsel,
            @Field("belanja_xl") String belanja_xl,
            @Field("belanja_smartfren") String belanja_smartfren,
            @Field("belanja_indosat") String belanja_indosat,
            @Field("belanja_tri") String belanja_tri);

    @FormUrlEncoded
    @POST("detail_akuisisi_ndudc.php")
    Call<AkuisisiNdudcDetail> akuisisi_ndudc_detail(
            @Field("id") String id,
            @Field("imei") String imei);

    @FormUrlEncoded
    @POST("list_msisdn_konsinyasi.php")
    Call<ListMsisdnKonsinyasi> list_msisdn_konsinyasi(
            @Field("outlet_id") String outlet_id,
            @Field("imei") String imei);

    @FormUrlEncoded
    @POST("msisdn_konsinyasi.php")
    Call<ResponseHandler> msisdn_konsinyasi(
            @Field("msisdn") String msisdn,
            @Field("to") String to,
            @Field("type") String type,
            @Field("imei") String imei
    );

}
