<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Specialities extends Model
{
    use SoftDeletes;

    protected $guarded = ['id', 'created_at', 'updated_at', 'deleted_at'];

    public function users()
    {
        return $this->belongsToMany('App\User', 'specialities_user', 'speciality_id', 'user_id');
    }
}
