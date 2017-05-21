<?php

use Illuminate\Database\Seeder;

class RescueActionUsersSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        for($i = 0; $i < 20; ++$i) {
            DB::table('rescue_action_users')->insert([
                [
                    'user_id' => ($i % 2 + 1),
                    'rescue_action_id' => ($i + 1),
                    'accepted' => $i % 2
                ]
            ]);
        }
    }
}
