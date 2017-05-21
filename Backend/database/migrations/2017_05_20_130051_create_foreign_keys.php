<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateForeignKeys extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('users', function (Blueprint $table) {
            $table->foreign('user_level_id')->references('id')->on('user_levels');
            $table->foreign('station_id')->references('id')->on('stations');
        });

        Schema::table('specialities_user', function (Blueprint $table) {
            $table->foreign('user_id')->references('id')->on('users');
            $table->foreign('speciality_id')->references('id')->on('specialities');
        });

        Schema::table('rescue_action_users', function (Blueprint $table) {
            $table->foreign('user_id')->references('id')->on('users');
            $table->foreign('rescue_action_id')->references('id')->on('rescue_actions');
        });

        Schema::table('rescue_actions', function (Blueprint $table) {
            $table->foreign('leader_id')->references('id')->on('users');
            $table->foreign('action_type_id')->references('id')->on('action_types');
            $table->foreign('station_id')->references('id')->on('stations');
        });

        Schema::table('phones', function (Blueprint $table) {
            $table->foreign('user_id')->references('id')->on('users');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
    }
}
