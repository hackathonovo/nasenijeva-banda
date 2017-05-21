<?php

namespace App;

use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Notifications\Notifiable;
use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable
{
    use Notifiable, SoftDeletes;

    protected $fillable = [
        'name', 'email', 'password',
    ];

    protected $hidden = [
        'password', 'remember_token',
    ];

    protected $with = [
        'phones', 'specialities', 'station', 'user_level'
    ];

    public function station()
    {
        return $this->belongsTo('App\Stations');
    }

    public function user_level()
    {
        return $this->belongsTo('App\UserLevel');
    }

    public function phones()
    {
        return $this->hasMany('App\Phones');
    }

    public function specialities()
    {
        return $this->belongsToMany('App\Specialities', 'specialities_user', 'user_id', 'speciality_id');
    }

    public function rescue_actions()
    {
        return $this->belongsToMany('App\RescueActions', 'rescue_action_users', 'user_id', 'rescue_action_id');
    }
}
