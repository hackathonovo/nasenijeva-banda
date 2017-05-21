package com.example.hkozak.gss.RescueActions;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hkozak.gss.API.RescueActionsAPI;
import com.example.hkozak.gss.Constants;
import com.example.hkozak.gss.MainActivity;
import com.example.hkozak.gss.R;
import com.example.hkozak.gss.User.MembersActivity;
import com.example.hkozak.gss.User.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

/**
 * Created by HKozak on 5/20/2017.
 */

public class RescueActionsAdapter extends RecyclerView.Adapter<RescueActionsAdapter.ViewHolder> {

    public static int ANIMATION_SPEED_LONG = 500;
    public static int ANIMATION_SPEED_SHORT = 200;


    private Context context;

    private List<RescueAction> rescueActions;
    private List<ViewHolder> holders = new ArrayList<>();



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View parent;
        public TextView title;
        public TextView subtitle;
        public TextView description;
        public Button coverage;

        public ViewHolder(View v) {
            super(v);
            parent = v;
            title = (TextView) v.findViewById(R.id.card_rescue_title);
            subtitle = (TextView) v.findViewById(R.id.card_rescue_subtitle);
            description = (TextView) v.findViewById(R.id.card_rescue_description);
            coverage = (Button) v.findViewById(R.id.button_coverage);
        }
    }

    public static class ViewHolderUser extends ViewHolder {

        public Button buttonAccept;
        public Button buttonPass;

        public ViewHolderUser(View v) {
            super(v);

            buttonAccept = (Button) v.findViewById(R.id.button_accept);
            buttonPass = (Button) v.findViewById(R.id.button_pass);
        }
    }

    public static class ViewHolderLeader extends ViewHolder {

        public Button buttonTeam;
        public Button buttonInfo;

        public ViewHolderLeader(View v) {
            super(v);
            buttonTeam = (Button) v.findViewById(R.id.button_team);
            buttonInfo = (Button) v.findViewById(R.id.button_info);
        }
    }


    public RescueActionsAdapter(Context context, List<RescueAction> rescueActions) {
        this.context = context;
        this.rescueActions = rescueActions;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        int userID = PreferenceManager.getDefaultSharedPreferences(context).getInt(Constants.PREF_USER_ID, -1);
        return rescueActions.get(position).getActionType(userID);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        if (viewType == RescueAction.TYPE_ACTION_FOR_USER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_rescue_item, parent, false);

            ViewHolderUser vh = new ViewHolderUser(v);

            // keep every ViewHolderLeader in a private list
            holders.add(vh);
            return vh;
        } else if (viewType == RescueAction.TYPE_ACTION_FOR_LEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_rescue_item_leader, parent, false);

            ViewHolderLeader vh = new ViewHolderLeader(v);

            holders.add(vh);
            return vh;
        }

        return null;
    }

    public int getNumberOfAcceptedInvites(RescueAction action) {
        int accepted = 0;
        for (User member : action.getUsers()) {
            if (member.pivot.getAccepted() == 1)
                accepted++;
        }
        return accepted;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.title.setText(rescueActions.get(position).getName());
        holder.subtitle.setText(rescueActions.get(position).getLocation());
        holder.description.setText(rescueActions.get(position).getDescription());

        RescueAction action = rescueActions.get(position);
        int accepted = getNumberOfAcceptedInvites(action);
        int invited = action.getUsers().size();

        String text = Integer.toString(accepted) + "/" + Integer.toString(invited);
        holder.coverage.setText(text);

        if (accepted <= invited / 2) {
            holder.coverage.setTextColor(context.getColor(R.color.colorStatusRed));
        } else  {
            holder.coverage.setTextColor(context.getColor(R.color.colorStatusGreen));
        }

        if (holder.getItemViewType() == RescueAction.TYPE_ACTION_FOR_USER)
            holder.coverage.setVisibility(View.INVISIBLE);

        // setup User buttons
        if (holder.getItemViewType() == RescueAction.TYPE_ACTION_FOR_USER) {
            ViewHolderUser userHolder = (ViewHolderUser) holder;
            userHolder.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptRescueAction(position);
                }
            });

            userHolder.buttonPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passRescueAction(position);
                }
            });
        }

        //setup Leader buttons
        if (holder.getItemViewType() == RescueAction.TYPE_ACTION_FOR_LEADER) {
            ViewHolderLeader leaderHolder = (ViewHolderLeader) holder;
            leaderHolder.buttonTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MembersActivity.class);
                    intent.putExtra(MembersActivity.EXTRA_ACTION_ID, rescueActions.get(position).getId());
                    context.startActivity(intent);
                }
            });

            leaderHolder.buttonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RescueActionActivity.class);
                    intent.putExtra(RescueActionActivity.EXTRA_ACTION_ID, rescueActions.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    public void acceptRescueAction(final int position) {

        Timber.e("ACCEPT ACTION");
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final String authToken = PreferenceManager.getDefaultSharedPreferences(context)
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

        int actionId = rescueActions.get(position).getId();
        Call<Void> actionsCall = rescueActionsAPI.acceptInvite(Integer.toString(actionId));
        actionsCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Timber.e(response.message());
                if (response.isSuccessful()) {
                    Timber.e("Accepted action!");
                    rescueActions.remove(position);
                    notifyItemRemoved(position);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Timber.e("Failed to accept action!");
            }
        });
    }

    public void passRescueAction(int position) {

        Timber.e("PASS ACTION");
        rescueActions.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rescueActions.size();
    }


}
