<?php

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $this->call(SpecialitiesSeeder::class);
        $this->call(UserLevelsSeeder::class);
        $this->call(StationsSeeder::class);
        $this->call(UsersSeeder::class);
        $this->call(ActionTypeSeeder::class);
        $this->call(RescueActionsSeeder::class);
        $this->call(RescueActionUsersSeeder::class);
        $this->call(AdminMenuSeeder::class);
    }
}
