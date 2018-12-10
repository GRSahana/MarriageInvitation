package com.ubikasoftwares.marriageinvitation;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ubikasoftwares.marriageinvitation.Adapter.InviteesAdapter;
import com.ubikasoftwares.marriageinvitation.Model.InviteDetails;
import com.ubikasoftwares.marriageinvitation.Model.InviteDetailsParam;
import com.ubikasoftwares.marriageinvitation.Model.InviteesDetails;
import com.ubikasoftwares.marriageinvitation.Model.Parameters;
import com.ubikasoftwares.marriageinvitation.Model.ViewInvitees;
import com.ubikasoftwares.marriageinvitation.Network.RetrofitApiInterface;
import com.ubikasoftwares.marriageinvitation.Network.RetrofitSingleInstance;
import com.ubikasoftwares.marriageinvitation.Session.SessionManagement;
import com.ubikasoftwares.marriageinvitation.Utils.ProgressDialogHelper;
import com.ubikasoftwares.marriageinvitation.bin.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteesDetailsActivity extends AppCompatActivity {
    
    Button btn_invite, btn_uninvite, btn_location; 
    TextView tvName, tvAddress, tvPhone, tvInvitedBy, tvDate, tvCategory;
    ProgressDialogHelper progressDialogHelper;
    RetrofitApiInterface apiInterface;
    String invitationId,callingActivity;
    ImageView imageview;
    Parameters params;
    SessionManagement sessionManagement;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitees_details);

        progressDialogHelper = new ProgressDialogHelper();
        sessionManagement = new SessionManagement(this);

        invitationId = getIntent().getStringExtra("EventId");
        callingActivity = getIntent().getStringExtra("CallingActivity");
        Log.d("invitationId",invitationId);

        toolbar = findViewById(R.id.toolbar3);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.mrginv);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              callActivity();
            }
        });
        
        tvName = findViewById(R.id.name);
        tvAddress = findViewById(R.id.address);
        tvPhone = findViewById(R.id.phone);
        tvInvitedBy = findViewById(R.id.invitedBy);
        tvDate = findViewById(R.id.date);
        tvCategory = findViewById(R.id.cat);
        imageview = findViewById(R.id.imageView);
        
        btn_invite = findViewById(R.id.invite);
        btn_uninvite = findViewById(R.id.uninvite);
        btn_location = findViewById(R.id.tagloc);

        if(!invitationId.isEmpty()&&!callingActivity.isEmpty()){
            loadInviteesDetails();
        }else{
            Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show();
        }
        


    }

    private void loadInviteesDetails() {

        progressDialogHelper.ShowPdialog(this);
        apiInterface = RetrofitSingleInstance.getRetrofitInstance()
                .create(RetrofitApiInterface.class);


            String encoded = Base64.encodeToString(invitationId.getBytes(),Base64.URL_SAFE);
            encoded = encoded.replaceAll("[\\t\\n\\r]+","");
        encoded = encoded.replaceAll("[=]+","");
            params = new Parameters(encoded);


        ViewInvitees viewInvitees = new ViewInvitees("viewInviteesById",params);


        Call<ResponseBody> call = apiInterface.getInviteesDetails("application/json",viewInvitees);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("response", response.toString());
                progressDialogHelper.HidePdialog();
                //AttendanceAdapter.itemStateArray.clear();
                if (response.body() != null && response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.has("response")) {

                            JSONObject response1 = jsonObject.getJSONObject("response");
                            //JSONObject error = jsonObject.getJSONObject("error");
                            Boolean success = response1.getBoolean("success");
                            String Status = response1.getString("status");

                            if (success) {
                                if (Status.equals("200")) {
                                    JSONObject message = response1.getJSONObject("message");
                                    tvName.setText(message.getString("name"));
                                    final String address = message.getString("address").trim();
                                    tvAddress.setText(address);
                                    tvCategory.setText(message.getString("category"));

                                    tvPhone.setText(message.getString("phone"));

                                    if(message.getString("invitation_status").equals("1")){
                                        btn_invite.setText("Invited");
                                        btn_invite.setVisibility(View.INVISIBLE);
                                        btn_location.setVisibility(View.INVISIBLE);
                                        tvInvitedBy.setText(message.getString("invited_by"));
                                        tvDate.setText(message.getString("created_on"));

                                    }else {
                                        imageview.setVisibility(View.INVISIBLE);
                                        btn_uninvite.setVisibility(View.INVISIBLE);
                                    }

                                    btn_invite.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            updateInvited();
                                        }
                                    });
                                    btn_uninvite.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            updateUnInvited();
                                        }
                                    });

                                    btn_location.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String url = "http://maps.google.com/maps?daddr="+address;
                                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
                                            intent.setPackage("com.google.android.apps.maps");
                                            try
                                            {
                                                startActivity(intent);
                                            }
                                            catch(ActivityNotFoundException ex)
                                            {
                                                try
                                                {
                                                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                    startActivity(unrestrictedIntent);
                                                }
                                                catch(ActivityNotFoundException innerEx)
                                                {
                                                    Toast.makeText(InviteesDetailsActivity.this, "Please install a maps application", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialogHelper.HidePdialog();
                Log.e("error", t.toString()+call.toString());
                Toast.makeText(InviteesDetailsActivity.this, "Internet Error!!", Toast.LENGTH_SHORT).show();

            }


        });
    }

    private void updateUnInvited() {

        progressDialogHelper.ShowPdialog(this);
        apiInterface = RetrofitSingleInstance.getRetrofitInstance()
                .create(RetrofitApiInterface.class);

        String encoded = Base64.encodeToString(invitationId.getBytes(),Base64.URL_SAFE);
        encoded = encoded.replaceAll("[\\t\\n\\r]+","");
        encoded = encoded.replaceAll("[=]+","");
        InviteDetailsParam params = new InviteDetailsParam(encoded,sessionManagement.getUSERNAME());


        InviteDetails viewInvitees = new InviteDetails("changeInvitationStatus",params);


        Call<ResponseBody> call = apiInterface.updateInvitedDetails("application/json",viewInvitees);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("response", response.toString());
                progressDialogHelper.HidePdialog();
                //AttendanceAdapter.itemStateArray.clear();
                if (response.body() != null && response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.has("response")) {

                            JSONObject response1 = jsonObject.getJSONObject("response");
                            //JSONObject error = jsonObject.getJSONObject("error");
                            Boolean success = response1.getBoolean("success");
                            String Status = response1.getString("status");

                            if (success) {
                                if (Status.equals("200")) {
                                    Toast.makeText(InviteesDetailsActivity.this, "Uninvited Successfully.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(InviteesDetailsActivity.this,InviteesDetailsActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("EventId",invitationId);
                                    intent.putExtra("CallingActivity",callingActivity);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialogHelper.HidePdialog();
                Log.e("error", t.toString()+call.toString());
                Toast.makeText(InviteesDetailsActivity.this, "Internet Error!!", Toast.LENGTH_SHORT).show();

            }


        });



    }

    private void updateInvited() {

        progressDialogHelper.ShowPdialog(this);
        apiInterface = RetrofitSingleInstance.getRetrofitInstance()
                .create(RetrofitApiInterface.class);

        String encoded = Base64.encodeToString(invitationId.getBytes(),Base64.URL_SAFE);
        encoded = encoded.replaceAll("[\\t\\n\\r]+","");
        encoded = encoded.replaceAll("[=]+","");
        InviteDetailsParam params = new InviteDetailsParam(encoded,sessionManagement.getUSERNAME());


        InviteDetails viewInvitees = new InviteDetails("updateInvitationStatus",params);


        Call<ResponseBody> call = apiInterface.updateInvitedDetails("application/json",viewInvitees);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("response", response.toString());
                progressDialogHelper.HidePdialog();
                //AttendanceAdapter.itemStateArray.clear();
                if (response.body() != null && response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.has("response")) {

                            JSONObject response1 = jsonObject.getJSONObject("response");
                            //JSONObject error = jsonObject.getJSONObject("error");
                            Boolean success = response1.getBoolean("success");
                            String Status = response1.getString("status");

                            if (success) {
                                if (Status.equals("200")) {
                                    Toast.makeText(InviteesDetailsActivity.this, "Invited Successfully!!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(InviteesDetailsActivity.this,InviteesDetailsActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("EventId",invitationId);
                                    intent.putExtra("CallingActivity",callingActivity);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(InviteesDetailsActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialogHelper.HidePdialog();
                Log.e("error", t.toString()+call.toString());
                Toast.makeText(InviteesDetailsActivity.this, "Internet Error!!", Toast.LENGTH_SHORT).show();

            }


        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       callActivity();

    }

    private void callActivity() {

            Intent intent = new Intent(this,ViewInviteesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

    }
}
