<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class RescueActionUsers extends Model
{
    use SoftDeletes;

    protected $table = 'rescue_action_users';

    protected $fillable = [
        'user_id', 'rescue_action_id'
    ];
}
