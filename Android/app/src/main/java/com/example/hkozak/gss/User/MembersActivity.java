package com.example.hkozak.gss.User;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.hkozak.gss.API.RescueActionsAPI;
import com.example.hkozak.gss.Constants;
import com.example.hkozak.gss.R;
import com.example.hkozak.gss.RescueActions.InvitesRequest;
import com.example.hkozak.gss.RescueActions.RescueActionActivity;
import com.example.hkozak.gss.RescueActions.RescueActionByIDResponse;
import com.example.hkozak.gss.RescueActions.RescueActionsResponse;

import java.io.IOException;
import java.util.ArrayList;
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

public class MembersActivity extends AppCompatActivity {

    public static final String EXTRA_ACTION_ID = "EXTRA_ACTION_ID";

    @BindView(R.id.recycler_members_container)
    RecyclerView membersContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    MenuItem cancelSelectionMenuItem;

    private int actionID;

    private RecyclerView.LayoutManager membersLayoutManager;
    private MembersAdapter membersAdapter;

    private List<User> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        ButterKnife.bind(this);

        actionID = getIntent().getIntExtra(EXTRA_ACTION_ID, -1);

        toolbar.setTitle("Invite members");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBroadcastSMS();
                showSnackbarMessage("Sent message to all recipients");
            }
        });

        loadMembers();
    }

    public void sendBroadcastSMS() {
        Timber.e("BROADCAST");
        InvitesRequest invitesRequest = new InvitesRequest(membersAdapter.getSelectedMembers(), actionID);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder().build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RescueActionsAPI rescueActionsAPI = retrofit.create(RescueActionsAPI.class);

        Call<Void> actionsCall = rescueActionsAPI.sendInvites(invitesRequest);
        actionsCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Timber.e(response.message());
                if (response.isSuccessful()) {
                    Timber.e("Sent invites!!");

                    // start details activity
                    Intent intent = new Intent(MembersActivity.this, RescueActionActivity.class);
                    intent.putExtra(RescueActionActivity.EXTRA_ACTION_ID, actionID);
                    MembersActivity.this.startActivity(intent);

                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Timber.e("Failed to send invites!");
            }
        });

    }

    public void loadMembers() {
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
                    initMembers(actionsResponse.getUsersNotInvited());
                }
            }

            @Override
            public void onFailure(Call<RescueActionByIDResponse> call, Throwable t) {
                Timber.e("Failed to load Rescue Actions");
            }
        });
    }

    public void initMembers(List<User> members) {

        if (members == null) {
            members = new ArrayList<>();

            User u1 = new User();
            u1.setName("Hrvoje Kozak");
            User u2 = new User();
            u2.setName("Nino Uzelac");
            User u3 = new User();
            u3.setName("Zekoslav Mrkva");
            User u4 = new User();
            u4.setName("Marko Cupic");
            members.add(u1);
            members.add(u2);
            members.add(u3);
            members.add(u4);
        }
        membersLayoutManager = new LinearLayoutManager(this);
        membersContainer.setLayoutManager(membersLayoutManager);

        membersAdapter = new MembersAdapter(this, members);
        membersContainer.setAdapter(membersAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_members, menu);

        cancelSelectionMenuItem = menu.findItem(R.id.cancel_selection);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cancel_selection) {
            membersAdapter.cancelSelectionMode();
            cancelSelectionMenuItem.setVisible(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSnackbarMessage(String message) {
        Snackbar.make(fab, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void showCancelSelectionMenuButton() {
        cancelSelectionMenuItem.setVisible(true);
    }

}
