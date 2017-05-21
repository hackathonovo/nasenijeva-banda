<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class RescueActions extends Model
{
    use SoftDeletes;

    protected $table = 'rescue_actions';

    protected $guarded = ['id', 'created_at', 'updated_at', 'deleted_at'];

    public function actionType()
    {
        return $this->belongsTo('App\ActionTypes');
    }

    public function users()
    {
        return $this->belongsToMany('App\User', 'rescue_action_users', 'rescue_action_id', 'user_id')->withPivot('accepted');
    }

    public function station()
    {
        return $this->belongsTo('App\Stations');
    }

    public function leader()
    {
        return $this->belongsTo('App\User');
    }
}
