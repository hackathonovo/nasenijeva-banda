package com.example.hkozak.gss.RescueActions;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hkozak.gss.API.RescueActionsAPI;
import com.example.hkozak.gss.Constants;
import com.example.hkozak.gss.R;
import com.example.hkozak.gss.User.MembersAdapter;
import com.example.hkozak.gss.User.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class RescueActionActivity extends AppCompatActivity {

    public static final String EXTRA_ACTION_ID = "EXTRA_ACTION_ID";

    @BindView(R.id.users_container)
    RecyclerView membersContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.fab_clear)
    FloatingActionButton fabClear;

    private RecyclerView.LayoutManager membersLayoutManager;
    private ActionMembersAdapter membersAdapter;

    private List<User> members = new ArrayList<>();

    int actionID;
    String broadcastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_action);
        ButterKnife.bind(this);

        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        actionID = getIntent().getIntExtra(EXTRA_ACTION_ID, -1);

        Timber.e("Started Action activity with id " + actionID);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending SMS message to all participants.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showMessageDialog();
            }
        });

        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Closing Rescue Action!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showCloseActionDialog();
            }
        });

        loadDetails();
    }

    public void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send message to all members.");

        // Set up the input
        final EditText input = new EditText(this);
        input.setLines(4);
        input.setMinLines(4);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                broadcastMessage = input.getText().toString();
                sendBroadcastSMS(broadcastMessage);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void showCloseActionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Close this action?");

        // Set up the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeAction();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void loadDetails() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final String authToken = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.PREF_API_TOKEN, "");

        Timber.e(authToken);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + authToken).build();
                return chain.proceed(request);
            }
        }).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RescueActionsAPI rescueActionsAPI = retrofit.create(RescueActionsAPI.class);

        Call<RescueActionByIDResponse> actionsCall = rescueActionsAPI.getRescueActionById(Integer.toString(actionID));
        actionsCall.enqueue(new Callback<RescueActionByIDResponse>() {
            @Override
            public void onResponse(Call<RescueActionByIDResponse> call, retrofit2.Response<RescueActionByIDResponse> response) {
                Timber.e(response.message());
                if (response.isSuccessful()) {
                    Timber.e("Loaded Rescue Actions");
                    RescueActionByIDResponse actionsResponse = response.body();
                    Timber.e(actionsResponse.toString());

                    // populate list with data
                    initUI(actionsResponse.getRescueAction());
                }
            }

            @Override
            public void onFailure(Call<RescueActionByIDResponse> call, Throwable t) {
                Timber.e("Failed to load Rescue Actions");
            }
        });
    }


    public void initUI(RescueAction rescueAction) {

        getSupportActionBar().setTitle(rescueAction.getName());

        membersLayoutManager = new LinearLayoutManager(this);
        membersContainer.setLayoutManager(membersLayoutManager);

        membersAdapter = new ActionMembersAdapter(this, rescueAction.getUsers());
        membersContainer.setAdapter(membersAdapter);
    }

    public void sendBroadcastSMS(String message) {
        Timber.e("SENDING BROADCAST SMS");

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RescueActionsAPI rescueActionsAPI = retrofit.create(RescueActionsAPI.class);

        BroadcastSMSMessage broadcastSMSMessage = new BroadcastSMSMessage(message);

        Call<Void> actionsCall = rescueActionsAPI.sendBroadcastSMS(Integer.toString(actionID), broadcastSMSMessage);
        actionsCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Timber.e(response.message());
                Timber.e(response.toString());
                if (response.isSuccessful()) {
                    Timber.e("Accepted action!");
                    Toast.makeText(RescueActionActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Timber.e("Failed to send SMS!");
            }
        });
    }

    public void closeAction() {
        Timber.e("CLOSING ACTION");

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RescueActionsAPI rescueActionsAPI = retrofit.create(RescueActionsAPI.class);

        Call<Void> actionsCall = rescueActionsAPI.closeAction(Integer.toString(actionID));
        actionsCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Timber.e(response.message());
                if (response.isSuccessful()) {
                    Timber.e("Closing Done!!");
                    Toast.makeText(RescueActionActivity.this, "Action Closed!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Timber.e("Failed to send SMS!");
            }
        });
    }
}
