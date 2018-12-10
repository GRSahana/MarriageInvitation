package com.ubikasoftwares.marriageinvitation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.ubikasoftwares.marriageinvitation.Model.AddInvitees;
import com.ubikasoftwares.marriageinvitation.Model.Login;
import com.ubikasoftwares.marriageinvitation.Model.Param;
import com.ubikasoftwares.marriageinvitation.Model.Params;
import com.ubikasoftwares.marriageinvitation.Network.RetrofitApiInterface;
import com.ubikasoftwares.marriageinvitation.Network.RetrofitSingleInstance;
import com.ubikasoftwares.marriageinvitation.Session.SessionManagement;
import com.ubikasoftwares.marriageinvitation.Utils.ProgressDialogHelper;
import com.ubikasoftwares.marriageinvitation.bin.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInviteesActivity extends AppCompatActivity {
    EditText edName, edAddress, edPhone;
    Spinner spinnerCategory;
    String name, address, phone, category;
    Button btnSubmit;
    RetrofitApiInterface retrofitApiInterface;
    ProgressDialogHelper progressDialogHelper;
    SessionManagement sessionManagement;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitees);

        progressDialogHelper = new ProgressDialogHelper();
        sessionManagement = new SessionManagement(this);
        edAddress = findViewById(R.id.address);
        edName = findViewById(R.id.name);
        edPhone = findViewById(R.id.phone);
        btnSubmit = findViewById(R.id.btn_submit);

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
                Intent back = new Intent(AddInviteesActivity.this,Main2Activity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(back);
            }
        });

        spinnerCategory = findViewById(R.id.CategorySpinner);

        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(this, R.array.Category, android.R.layout.simple_spinner_item);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerCategory.setAdapter(a);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddInviteesActivity.this, "Select a city.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edName.getText().toString();
                address = edAddress.getText().toString();
                phone = edPhone.getText().toString();

                Boolean status = true;

                if(name.isEmpty() || !Validation.isValidName(name) || name.length()>50){
                    status = false;
                    edName.setError("Enter a valid name!!");
                }
                if(address.isEmpty() || address.length()>200){
                    status = false;
                    edAddress.setError("Enter a valid address!!");
                }
                if(phone.isEmpty() || !Validation.isValidPhone(phone)){
                    status = false;
                    edPhone.setError("Enter a valid Phone Number!!");
                }
                if(status){
                    submitForm();
                }
            }
        });

    }

    private void submitForm() {
        progressDialogHelper.ShowPdialog(this);
        retrofitApiInterface = RetrofitSingleInstance
                .getRetrofitInstance()
                .create(RetrofitApiInterface.class);
        Param login = new Param(sessionManagement.getData(),name,address,phone,category);
        AddInvitees params = new AddInvitees("addInvitees",login);

        Call<ResponseBody> call = retrofitApiInterface.addInvitees("application/json", params);

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
                                        //JSONObject message = response1.getJSONObject("message");
                                        Toast.makeText(AddInviteesActivity.this,"Invitees are successfully added.",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(AddInviteesActivity.this,AddInviteesActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(AddInviteesActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(AddInviteesActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(AddInviteesActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddInviteesActivity.this, "Internet Error!!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
