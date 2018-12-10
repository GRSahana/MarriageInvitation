package com.ubikasoftwares.marriageinvitation;

import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ubikasoftwares.marriageinvitation.Model.AddInvitees;
import com.ubikasoftwares.marriageinvitation.Model.Login;
import com.ubikasoftwares.marriageinvitation.Model.Params;
import com.ubikasoftwares.marriageinvitation.Network.RetrofitApiInterface;
import com.ubikasoftwares.marriageinvitation.Network.RetrofitSingleInstance;
import com.ubikasoftwares.marriageinvitation.Session.SessionManagement;
import com.ubikasoftwares.marriageinvitation.Utils.ProgressDialogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText edUser, edEmpid;
    boolean status =  true;
    String Username, EmpId;
    RetrofitApiInterface retrofitApiInterface;
    ProgressDialogHelper progressDialogHelper;
    SessionManagement session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialogHelper = new ProgressDialogHelper();
        session = new SessionManagement(this);

      edUser = findViewById(R.id.username);
      edEmpid = findViewById(R.id.password);

      Button btnLoin = findViewById(R.id.submit);

        btnLoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username = edUser.getText().toString();
                EmpId = edEmpid.getText().toString();

                if (Username.isEmpty()) {
                    edUser.setError("Enter a valid username");
                    status = false;
                }

                if (EmpId.isEmpty()) {
                    edEmpid.setError("Enter a valid Password");
                    status = false;
                }

                if (status) {
                    login();
                }
            }
        });
    }

    public void login() {
        progressDialogHelper.ShowPdialog(this);
        retrofitApiInterface = RetrofitSingleInstance
                .getRetrofitInstance()
                .create(RetrofitApiInterface.class);
        Login login = new Login(Username,EmpId);
        Params params = new Params("generatetoken",login);

        Call<ResponseBody> call = retrofitApiInterface.loginData("application/json", params);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("response",response.toString());
                progressDialogHelper.HidePdialog();

                if(response.body()!=null){

                    if(response.isSuccessful()) {
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
                                        String userid = message.getString("userid");
                                        session.createLoginSession(userid, "user", Username);
                                        SignUpSuccess();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "Invalid Username Or Password!!", Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e){
                                e.printStackTrace();
                            } catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialogHelper.HidePdialog();
                Log.e("error", t.toString()+call.toString());
                Toast.makeText(MainActivity.this, "Internet Error!!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public  void SignUpSuccess(){

        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
