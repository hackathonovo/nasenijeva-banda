<?php

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class SpecialitiesSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('specialities')->insert([
            [
                'name' => 'medicina',
                'description' => '',
            ],
            [
                'name' => 'speleolog',
                'description' => 'špilje n stuff',
            ],
            [
                'name' => 'ronjenje',
                'description' => 'voda',
            ],
            [
                'name' => 'skijanje',
                'description' => 'alpinizam',
            ]
        ]);
    }
}
