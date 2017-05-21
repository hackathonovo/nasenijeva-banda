<?php

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class StationsSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('stations')->insert([
            [
                'name' => 'Bjelovar',
                'address' => 'Mlinovac 21',
                'city' => 'Bjelovar',
            ],
            [
                'name' => 'Čakovec',
                'address' => 'Športska 2',
                'city' => 'Čakovec',
            ],
            [
                'name' => 'Delnice',
                'address' => 'Matice Hrvatske 21',
                'city' => 'Delnice',
            ],
            [
                'name' => 'Dubrovnik',
                'address' => 'Dr. Roka Mišetića bb',
                'city' => 'Dubrovnik',
            ],
            [
                'name' => 'Gospić',
                'address' => 'Kaniža Gospićka 4',
                'city' => 'Gospić',
            ],
            [
                'name' => 'Karlovac',
                'address' => 'Vlatka Mačeka 48',
                'city' => 'Karlovac',
            ],
            [
                'name' => 'Koprivnica',
                'address' => 'Hrvatske državnosti 7',
                'city' => 'Koprivnica',
            ],
            [
                'name' => 'Krapina',
                'address' => 'Vladimira Nazora 56, Zlatar Bistrica',
                'city' => 'Krapina',
            ],
            [
                'name' => 'Makarska',
                'address' => 'A.G. Matoša 1',
                'city' => 'Makarska',
            ],
            [
                'name' => 'Novska',
                'address' => 'Osječka 20',
                'city' => 'Novska',
            ],
            [
                'name' => 'Ogulin',
                'address' => 'Bernardina Frankopana 18',
                'city' => 'Ogulin',
            ],
            [
                'name' => 'Orahovica',
                'address' => 'Trg sv. Florijana 2',
                'city' => 'Orahovica',
            ],
            [
                'name' => 'Orebić',
                'address' => 'Šetalište kneza Domagoja 32',
                'city' => 'Orebić',
            ],
            [
                'name' => 'Osijek',
                'address' => 'Kneza Trpimira 23',
                'city' => 'Osijek',
            ],
            [
                'name' => 'Požega',
                'address' => 'Republike Hrvatske 1 C',
                'city' => 'Požega',
            ],
            [
                'name' => 'Pula',
                'address' => 'Narodnog Doma 2',
                'city' => 'Pula',
            ],
            [
                'name' => 'Rijeka',
                'address' => 'Franje Matkovića 7a',
                'city' => 'Rijeka',
            ],
            [
                'name' => 'Samobor',
                'address' => 'Perkovčeva 59',
                'city' => 'Samobor',
            ],
            [
                'name' => 'Šibenik',
                'address' => 'Put Tvornice 33',
                'city' => 'Šibenik',
            ],
            [
                'name' => 'Slavonski Brod',
                'address' => 'Ulica fra Kaje Adžića 10B',
                'city' => 'Slavonski Brod',
            ],
            [
                'name' => 'Split',
                'address' => 'Šibenska ulica 41',
                'city' => 'Split',
            ],
            [
                'name' => 'Varaždin',
                'address' => 'Stanka Vraza 15',
                'city' => 'Varaždin',
            ],
            [
                'name' => 'Vinkovci',
                'address' => 'Glagoljaška 27 A',
                'city' => 'Vinkovci',
            ],
            [
                'name' => 'Zadar',
                'address' => 'Andrije Hebranga 11a',
                'city' => 'Zadar',
            ],
            [
                'name' => 'Zagreb',
                'address' => 'Radićeva 23',
                'city' => 'Zagreb',
            ],
        ]);
    }
}
