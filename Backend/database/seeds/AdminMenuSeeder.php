<?php

use Illuminate\Database\Seeder;
use \Illuminate\Support\Facades\DB;

class AdminMenuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('admin_menu')->insert([
            [
                'parent_id' => 0,
                'order' => 12,
                'title' => 'Stanice',
                'icon' => 'fa-hospital-o',
                'uri' => 'stations'
            ],
            [
                'parent_id' => 0,
                'order' => 13,
                'title' => 'Članovi',
                'icon' => 'fa-users',
                'uri' => 'users'
            ],
            [
                'parent_id' => 0,
                'order' => 14,
                'title' => 'Administracija',
                'icon' => 'fa-gears',
                'uri' => ''
            ],
            [
                'parent_id' => 14,
                'order' => 15,
                'title' => 'Kategorije akcija',
                'icon' => 'fa-gears',
                'uri' => 'action_types'
            ],
            [
                'parent_id' => 14,
                'order' => 16,
                'title' => 'Specijalnosti volontera',
                'icon' => 'fa-gears',
                'uri' => 'specialities'
            ],
            [
                'parent_id' => 14,
                'order' => 17,
                'title' => 'Razine članova',
                'icon' => 'fa-gears',
                'uri' => 'user_levels'
            ],
            [
                'parent_id' => 0,
                'order' => 18,
                'title' => 'Akcije',
                'icon' => 'fa-street-view',
                'uri' => 'rescue_actions'
            ],
            [
                'parent_id' => 0,
                'order' => 19,
                'title' => 'Ivještaj',
                'icon' => 'fa-line-chart',
                'uri' => 'reports'
            ],
        ]);
    }
}
