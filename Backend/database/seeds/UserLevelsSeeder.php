<?php

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class UserLevelsSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('user_levels')->insert([
            [
                'name' => 'Pridruženi član'
            ],
            [
                'name' => 'Pripravnik',
            ],
            [
                'name' => 'Spasitelj'
            ]
        ]);
    }
}
