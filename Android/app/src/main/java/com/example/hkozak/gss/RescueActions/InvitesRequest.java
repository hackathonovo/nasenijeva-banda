package com.example.hkozak.gss.RescueActions;

import java.util.List;

/**
 * Created by HKozak on 5/21/2017.
 */

public class InvitesRequest {

    List<Integer> user_ids;
    Integer rescue_action_id;

    public InvitesRequest(List<Integer> ids, int actionId) {
        this.user_ids = ids;
        this.rescue_action_id = actionId;
    }
}
