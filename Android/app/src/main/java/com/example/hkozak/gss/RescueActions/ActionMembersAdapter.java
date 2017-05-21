package com.example.hkozak.gss.RescueActions;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hkozak.gss.Constants;
import com.example.hkozak.gss.R;
import com.example.hkozak.gss.RescueActions.RescueAction;
import com.example.hkozak.gss.User.Speciality;
import com.example.hkozak.gss.User.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;

/**
 * Created by HKozak on 5/20/2017.
 */

public class ActionMembersAdapter extends RecyclerView.Adapter<ActionMembersAdapter.ViewHolder> {

    public static final int MODE_NORMAL = 1;
    public static final int MODE_SELECT_MULTIPLE = 2;

    private Context context;

    private int mode = MODE_NORMAL;

    private List<User> members;
    private List<ActionMembersAdapter.ViewHolder> holders = new ArrayList<>();



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout parent;
        public ImageView avatar;
        public TextView name;
        public ImageView phone;
        public ImageView message;
//        public ViewGroup expanded;

        public TextView status;
//        public TextView homeStation;
//        public TextView availability;
//        public TextView skills;
//        public TextView title;

        public boolean userInfoVisible = false;

        public ViewHolder(View v) {
            super(v);
            parent = (LinearLayout) v;
            avatar = (ImageView) v.findViewById(R.id.member_avatar);
            name = (TextView) v.findViewById(R.id.member_name);
            phone = (ImageView) v.findViewById(R.id.image_phone_call);
            message= (ImageView) v.findViewById(R.id.image_send_message);
//            expanded = (ViewGroup) v.findViewById(R.id.member_expanded);
//            homeStation = (TextView) v.findViewById(R.id.member_home_station);
//            availability = (TextView) v.findViewById(R.id.member_availability);
//            skills = (TextView) v.findViewById(R.id.member_skills);
//            title = (TextView) v.findViewById(R.id.member_title);
            status = (TextView) v.findViewById(R.id.member_status);
        }
    }

    public ActionMembersAdapter(Context context, List<User> members) {
        this.context = context;
        this.members = members;
        Collections.sort(this.members, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.pivot.getAccepted() - o1.pivot.getAccepted();
            }
        });
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ActionMembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.action_member, parent, false);

        ActionMembersAdapter.ViewHolder vh = new ActionMembersAdapter.ViewHolder(v);

        // keep every ViewHolderLeader in a private list
        holders.add(vh);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ActionMembersAdapter.ViewHolder holder, final int position) {

        final User member = members.get(position);
        holder.name.setText(member.getName());
//        if (member.station.getCity() != null && !member.station.getCity().isEmpty())
//            holder.homeStation.setText(member.station.getCity());
//        if (member.availability != null && !member.availability.isEmpty())
//            holder.availability.setText(member.availability);

        StringBuilder sb = new StringBuilder();
        for (Speciality s : members.get(position).specialities)
        {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(s.getName());
        }

        Timber.e(" " + member.pivot.getAccepted());

        if (member.pivot.getAccepted() == 1) {
            holder.status.setText("Accepted");
            holder.status.setTextColor(context.getColor(R.color.colorStatusGreen));
        } else {
            holder.status.setText("Pending...");
            holder.status.setTextColor(context.getColor(R.color.colorStatusRed));
        }

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (member.phones != null && !member.phones.isEmpty()) {

                    String number = member.phones.get(0).getNumber();
                    Uri uri = Uri.parse("tel:" + number);

                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(Intent.createChooser(i, "Dial phone..."));
                } else {
                    Toast.makeText(context, "This user has no phone set up.", Toast.LENGTH_SHORT).show();
                    Timber.e("This user has no phone");
                }
            }
        });

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (member.phones != null && !member.phones.isEmpty()) {
                    String number = member.phones.get(0).getNumber();
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(Intent.createChooser(i, "Send message..."));
                } else {
                    Toast.makeText(context, "This user has no phone set up.", Toast.LENGTH_SHORT).show();
                    Timber.e("This user has no phone");
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return members.size();
    }

}
