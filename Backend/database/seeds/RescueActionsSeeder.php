<?php

use Carbon\Carbon;
use Illuminate\Database\Seeder;

class RescueActionsSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $stations = \App\Stations::all();
        $actionTypes = \App\ActionTypes::all();

        for($i = 0; $i < 4320; ++$i) {
            $date = Carbon::create(2017, 1, 1, 0, 0, 0)->addWeeks(rand(1,12));

            DB::table('rescue_actions')->insert([
                [
                    'leader_id' => $i % 3 == 0 ? 1 : 2,
                    'action_type_id' => $actionTypes->random()->id,
                    'name' => 'Akcija ' . ($i + 1),
                    'description' => 'opis trenutne akcije ' . ($i + 1),
                    'location' => 'Sljeme' . $i,
                    'station_id' => $stations->random()->id,
                    'created_at' => $date->format('Y-m-d H:i:s'),
                    'end_time' => $date->addDays(rand(1,4))->format('Y-m-d H:i:s')
                ]
            ]);
        }

        for($i = 0; $i < 3420; ++$i) {
            $date = Carbon::create(2017, 1, 1, 0, 0, 0)->addWeeks(rand(1,12));

            DB::table('rescue_actions')->insert([
                [
                    'leader_id' => $i % 3 == 0 ? 1 : 2,
                    'action_type_id' => $actionTypes->random()->id,
                    'name' => 'Akcija ' . ($i + 1),
                    'description' => 'opis trenutne akcije ' . ($i + 1),
                    'location' => 'Sljeme' . $i,
                    'station_id' => $stations->random()->id,
                    'created_at' => $date->format('Y-m-d H:i:s')
                ]
            ]);
        }
    }
}
