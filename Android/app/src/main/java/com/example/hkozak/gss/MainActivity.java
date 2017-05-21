package com.example.hkozak.gss;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hkozak.gss.API.RescueActionsAPI;
import com.example.hkozak.gss.RescueActions.RescueAction;
import com.example.hkozak.gss.RescueActions.RescueActionsAdapter;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_recycler_view)
    RecyclerView rescueCardsList;

    @BindView(R.id.empty_view)
    TextView emptyView;

    private RecyclerView.LayoutManager rescueCardsLayoutManager;
    private RecyclerView.Adapter rescueCardsAdapter;

    private List<RescueAction> rescueActions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Rescue Actions");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateUI();

        // fetch actions data from backend
        loadRescueActions();
    }

    public void initRescueActions(List<RescueAction> actions) {
        rescueCardsLayoutManager = new LinearLayoutManager(this);
        rescueCardsList.setLayoutManager(rescueCardsLayoutManager);

        rescueActions = actions;
        rescueCardsAdapter = new RescueActionsAdapter(getBaseContext(), rescueActions);
        rescueCardsList.setAdapter(rescueCardsAdapter);

        updateUI();
    }

    public void loadRescueActions() {
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

        Call<RescueActionsResponse> actionsCall = rescueActionsAPI.getActiveActions();
        actionsCall.enqueue(new Callback<RescueActionsResponse>() {
            @Override
            public void onResponse(Call<RescueActionsResponse> call, retrofit2.Response<RescueActionsResponse> response) {
                Timber.e(response.message());
                if (response.isSuccessful()) {
                    Timber.e("Loaded Rescue Actions");
                    RescueActionsResponse actionsResponse = response.body();
                    Timber.e(actionsResponse.toString());

                    // populate list with data
                    initRescueActions(actionsResponse.getRescueActions());

                    // save user ID
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit()
                            .putInt(Constants.PREF_USER_ID, actionsResponse.getUserId()).apply();
                }
            }

            @Override
            public void onFailure(Call<RescueActionsResponse> call, Throwable t) {
                Timber.e("Failed to load Rescue Actions");
            }
        });
    }


    @Override
    protected void onResume() {
        if (rescueCardsAdapter != null) {
            rescueCardsAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void updateUI() {
        if (rescueActions.isEmpty()) {
            rescueCardsList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            rescueCardsList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
