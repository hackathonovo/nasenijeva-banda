<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateRescueActionsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('rescue_actions', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('leader_id')->unsigned();
            $table->integer('action_type_id')->unsigned();
            $table->integer('station_id')->unsigned()->nullable();
            $table->string('name');
            $table->text('description');
            $table->string('location');
            $table->timestamp('end_time')->nullable();
            $table->timestamp('created_at')->default(DB::raw('CURRENT_TIMESTAMP'));
            $table->timestamp('updated_at')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));
            $table->softDeletes();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('rescue_actions');
    }
}
