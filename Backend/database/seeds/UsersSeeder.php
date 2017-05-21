<?php

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class UsersSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $faker = Faker\Factory::create('hr_HR');

        DB::table('users')->insert([
            [
                'name' => 'Imenko Prezimenko',
                'user_level_id' => '3',
                'station_id' => '1',
                'email' => 'iprez@hgss.hr',
                'password' => bcrypt('1234'),
            ]
        ]);

        DB::table('users')->insert([
            [
                'name' => 'Drugi user',
                'user_level_id' => '3',
                'station_id' => '1',
                'email' => 'josjedan@hgss.hr',
                'password' => bcrypt('1234'),
            ]
        ]);

        $stations = \App\Stations::all()->pluck('id');
        $pass = bcrypt('1234');

        $specialities = \App\Specialities::all();

        for($i = 0; $i < 147; ++$i) {
            $user = new \App\User([
                    'name' => $faker->name,
                    'user_level_id' => rand(1, 3),
                    'station_id' => $stations->random(),
                    'email' => $faker->email.$i,
                    'password' => $pass,
                    'address' => $faker->address
            ]);
            $user->save();
            foreach($specialities->random(rand(1, 3))->pluck('id')->values()->toArray() as $id => $val) {
                $user->specialities()->attach($val);
            }
        }
    }
}
