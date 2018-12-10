package com.ubikasoftwares.marriageinvitation;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ubikasoftwares.marriageinvitation.Adapter.InviteesAdapter;
import com.ubikasoftwares.marriageinvitation.Model.InviteesDetails;
import com.ubikasoftwares.marriageinvitation.Model.Param;
import com.ubikasoftwares.marriageinvitation.Model.Parameters;
import com.ubikasoftwares.marriageinvitation.Model.ViewInvitees;
import com.ubikasoftwares.marriageinvitation.Network.RetrofitApiInterface;
import com.ubikasoftwares.marriageinvitation.Network.RetrofitSingleInstance;
import com.ubikasoftwares.marriageinvitation.Utils.ProgressDialogHelper;
import com.ubikasoftwares.marriageinvitation.bin.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewInviteesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    InviteesAdapter inviteesAdapter;
    Button submit;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    RetrofitApiInterface apiInterface;
    List<InviteesDetails> inviteesDetails;
    ProgressDialogHelper progressDialogHelper;
    EditText search;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invitees);

        progressDialogHelper = new ProgressDialogHelper();
        inviteesDetails = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.mrginv);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.CYAN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent back = new Intent(ViewInviteesActivity.this,ViewInviteesActivity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(back);
                swipeRefreshLayout.setRefreshing(true);
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ViewInviteesActivity.this,Main2Activity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(back);
            }
        });
        search = findViewById(R.id.search);


        recyclerView = findViewById(R.id.viewInvitees);
        submit = findViewById(R.id.submit);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        loadListDetails();
    }

    private void loadListDetails() {
        progressDialogHelper.ShowPdialog(this);
        apiInterface = RetrofitSingleInstance.getRetrofitInstance()
                .create(RetrofitApiInterface.class);


        Parameters params = new Parameters("details");
        ViewInvitees viewInvitees = new ViewInvitees("viewInvitees",params);


        Call<ResponseBody> call = apiInterface.getInviteesList("application/json",viewInvitees);

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
                                    JSONArray jsonArray = response1.getJSONArray("message");
                                    for(int i= 0 ; i < jsonArray.length();i++){
                                        JSONObject product = jsonArray.getJSONObject(i);

                                        inviteesDetails.add(new InviteesDetails(product.getString("name"),
                                                product.getString("phone"),product.getString("invitation_status"),product.getString("invitation_id")));

                                    }
                                    inviteesAdapter = new InviteesAdapter(ViewInviteesActivity.this,inviteesDetails, Config.ACTIVITY_1);
                                    recyclerView.setAdapter(inviteesAdapter);
                                    search.addTextChangedListener(new TextWatcher() {

                                        @Override
                                        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                            // When user changed the Text
                                            inviteesAdapter.getFilter().filter(cs);
                                        }

                                        @Override
                                        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                                      int arg3) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void afterTextChanged(Editable arg0) {
                                            // TODO Auto-generated method stub
                                        }
                                    });

//                                    attendanceAdapter = new AttendanceAdapter(ViewInviteesActivity.this, );
//                                    recyclerView.setAdapter(attendanceAdapter);
                                } else {
                                    Toast.makeText(ViewInviteesActivity.this, "No Students are there!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ViewInviteesActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ViewInviteesActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                        Toast.makeText(ViewInviteesActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                    }



            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialogHelper.HidePdialog();
                Log.e("error", t.toString()+call.toString());
                Toast.makeText(ViewInviteesActivity.this, "Internet Error!!", Toast.LENGTH_SHORT).show();

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

    protected void onPause()
    {
        super.onPause();
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }


}
