package com.ubikasoftwares.marriageinvitation.Network;

import com.ubikasoftwares.marriageinvitation.Model.AddInvitees;
import com.ubikasoftwares.marriageinvitation.Model.InviteDetails;
import com.ubikasoftwares.marriageinvitation.Model.InviteesDetails;
import com.ubikasoftwares.marriageinvitation.Model.Param;
import com.ubikasoftwares.marriageinvitation.Model.Params;
import com.ubikasoftwares.marriageinvitation.Model.ViewInvitees;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitApiInterface {


    @POST("/marriage_invitation/REST/index.php")

    Call<ResponseBody> loginData(@Header("Content-Type") String content_type, @Body Params params);

    @POST("/marriage_invitation/REST/index.php")
    Call<ResponseBody> addInvitees(@Header("Content-Type") String content_type, @Body AddInvitees addInvitees);

    @POST("/marriage_invitation/REST/index.php")
    Call<ResponseBody> getInviteesList(@Header("Content-Type") String content_type, @Body ViewInvitees jsonObject);

    @POST("/marriage_invitation/REST/index.php")
    Call<ResponseBody> getInviteesDetails(@Header("Content-Type") String content_type, @Body ViewInvitees jsonObject);

    @POST("/marriage_invitation/REST/index.php")
    Call<ResponseBody> updateInvitedDetails(@Header("Content-Type") String content_type, @Body InviteDetails jsonObject);



}
