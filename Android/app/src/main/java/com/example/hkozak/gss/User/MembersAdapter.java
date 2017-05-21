package com.example.hkozak.gss.User;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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

import com.example.hkozak.gss.Constants;
import com.example.hkozak.gss.R;
import com.example.hkozak.gss.RescueActions.RescueAction;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by HKozak on 5/20/2017.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    public static final int MODE_NORMAL = 1;
    public static final int MODE_SELECT_MULTIPLE = 2;

    private Context context;

    private int mode = MODE_NORMAL;

    private List<User> members;
    private List<MembersAdapter.ViewHolder> holders = new ArrayList<>();



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout parent;
        public ImageView avatar;
        public TextView name;
        public ImageView buttonInfo;
        public ViewGroup expanded;

        public TextView homeStation;
        public TextView availability;
        public TextView skills;
        public TextView title;

        public boolean userInfoVisible = false;

        public ViewHolder(View v) {
            super(v);
            parent = (LinearLayout) v;
            avatar = (ImageView) v.findViewById(R.id.member_avatar);
            name = (TextView) v.findViewById(R.id.member_name);
            buttonInfo = (ImageView) v.findViewById(R.id.member_button_more_info);
            expanded = (ViewGroup) v.findViewById(R.id.member_expanded);
            homeStation = (TextView) v.findViewById(R.id.member_home_station);
            availability = (TextView) v.findViewById(R.id.member_availability);
            skills = (TextView) v.findViewById(R.id.member_skills);
            title = (TextView) v.findViewById(R.id.member_title);
        }
    }

    public MembersAdapter(Context context, List<User> members) {
        this.context = context;
        this.members = members;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.member, parent, false);

            MembersAdapter.ViewHolder vh = new MembersAdapter.ViewHolder(v);

            // keep every ViewHolderLeader in a private list
            holders.add(vh);
            return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MembersAdapter.ViewHolder holder, final int position) {

        final User member = members.get(position);
        holder.name.setText(member.getName());
        if (member.station.getCity() != null && !member.station.getCity().isEmpty())
            holder.homeStation.setText(member.station.getCity());
        if (member.availability != null && !member.availability.isEmpty())
            holder.availability.setText(member.availability);

        StringBuilder sb = new StringBuilder();
        for (Speciality s : members.get(position).specialities)
        {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(s.getName());

        }

        holder.skills.setText(sb.toString());
        holder.title.setText(members.get(position).level.getName());

        if (member.selected) {
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Timber.e("LONG CLICK");
                member.selected = true;
                mode = MODE_SELECT_MULTIPLE;
                updateMemberBackgroundColor(member, holder.parent);

                ((MembersActivity) context).showCancelSelectionMenuButton();
                ((MembersActivity) context).showSnackbarMessage("Select multiple users.");

                return true;
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.e("CLICK");
                if (mode == MODE_SELECT_MULTIPLE) {
                    // toggle selected
                    member.selected = !member.selected;

                }
                updateMemberBackgroundColor(member, holder.parent);
            }
        });

        holder.buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.e("CLICKED ON INFO");

                expandInfo(holder);
            }
        });

    }

    public void expandInfo(ViewHolder holder) {


        if (holder.expanded.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.expanded.setVisibility(View.VISIBLE);
            holder.buttonInfo.setImageResource(R.drawable.ic_keyboard_arrow_up_black_36dp);
        } else {
            holder.expanded.setVisibility(View.GONE);
            holder.buttonInfo.setImageResource(R.drawable.ic_keyboard_arrow_down_black_36dp);
        }
    }

    public void updateMemberBackgroundColor(User member, View parent) {
        parent.setBackgroundColor(
                member.selected ? context.getResources().getColor(R.color.colorPrimaryLight) : 0x000000);
    }

    public void cancelSelectionMode() {
        mode = MODE_NORMAL;

        for (ViewHolder holder : holders) {
            holder.parent.setBackgroundColor(0x000000);
        }
    }

    public List<Integer> getSelectedMembers() {
        List<Integer> selected = new ArrayList<>();

        for (User user : members) {
            if (user.selected)
                selected.add(user.getId());
        }
        return selected;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return members.size();
    }

}
