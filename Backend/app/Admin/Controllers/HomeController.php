<?php

namespace App\Admin\Controllers;

use App\Http\Controllers\Controller;
use App\RescueActions;
use App\Stations;
use App\User;
use Encore\Admin\Facades\Admin;
use Encore\Admin\Layout\Column;
use Encore\Admin\Layout\Content;
use Encore\Admin\Layout\Row;
use Encore\Admin\Widgets\Box;
use Encore\Admin\Widgets\Chart\Bar;
use Encore\Admin\Widgets\Chart\Doughnut;
use Encore\Admin\Widgets\Chart\Line;
use Encore\Admin\Widgets\Chart\Pie;
use Encore\Admin\Widgets\Chart\PolarArea;
use Encore\Admin\Widgets\Chart\Radar;
use Encore\Admin\Widgets\Collapse;
use Encore\Admin\Widgets\InfoBox;
use Encore\Admin\Widgets\Tab;
use Encore\Admin\Widgets\Table;

class HomeController extends Controller
{
    public function index()
    {
        return Admin::content(function (Content $content) {

            $content->header('Nadzorna ploča');
            $content->description('');

            $content->row(function ($row) {
                $row->column(3, new InfoBox('Članova HGSS-a', 'users', 'aqua', '/admin/users', number_format(User::count())));
                $row->column(3, new InfoBox('Aktivnih članova', 'thumbs-up', 'red', null, number_format(652)));
                $row->column(3, new InfoBox('Akcija spašavanja', 'street-view', 'green', '/admin/rescue_actions', number_format(RescueActions::count())));
                $row->column(3, new InfoBox('Stanica', 'hospital-o', 'yellow', '/admin/stations', number_format(Stations::count())));
            });

            $content->row(function (Row $row) {

                $row->column(6, function (Column $column) {

                    $tab = new Tab();

                    $fromDb = array_count_values(RescueActions::with('actionType')->get()->pluck('actionType.name')->toArray());

                    $data = [];
                    foreach($fromDb as $value => $count) {
                        $data[] = [$value, $count, 0];
                    }

                    $pie = new Pie($data);

                    $tab->add('Pie', $pie);

//                    $table = new Table(['Stanica', 'Br. akcija', 'Br. članova koji su sudjelovali'], $data);

//                    $tab->add('Table', $table);
//                    $tab->add('Text', 'blablablabla....');

//                    $tab->dropDown([['Orders', '/admin/orders'], ['administrators', '/admin/administrators']]);
                    $tab->title('Kategorije akcija');

                    $column->append($tab);

                    $collapse = new Collapse();

                    $fromDb = array_count_values(RescueActions::with('station')->get()->pluck('station.name')->toArray());

                    $data = [];
                    foreach($fromDb as $value => $count) {
                        if(count($data) > 9) break;
                        $data[] = [$value, [$count]];
                    }


                    $bar = new Bar(
                        ["Najaktivnije stanice"],
                        $data
                    );
                    $collapse->add('Bar', $bar);
//                    $collapse->add('Orders', new Table());
                    $column->append($collapse);
                });

                $row->column(6, function (Column $column) {

                    $users = User::with('specialities')->get();
                    $vals = [];
                    foreach($users as $user) {
                        foreach($user->specialities as $speciality) {
                            $vals[] = $speciality->name;
                        }
                    }

                    $fromDb = array_count_values($vals);

                    $data = [];
                    foreach($fromDb as $value => $count) {
                        $data[] = [$value, $count];
                    }


                    $doughnut = new Doughnut($data);
                    $column->append((new Box('Specijalnosti', $doughnut))->removable()->collapsable()->style('info'));

                    $column->append((new Box('Akcije kroz vrijeme', new Line()))->removable()->collapsable()->style('danger'));
                });

            });

//            $headers = ['Id', 'Email', 'Name', 'Company', 'Last Login', 'Status'];
//            $rows = [
//                [1, 'labore21@yahoo.com', 'Ms. Clotilde Gibson', 'Goodwin-Watsica', '1997-08-13 13:59:21', 'open'],
//                [2, 'omnis.in@hotmail.com', 'Allie Kuhic', 'Murphy, Koepp and Morar', '1988-07-19 03:19:08', 'blocked'],
//                [3, 'quia65@hotmail.com', 'Prof. Drew Heller', 'Kihn LLC', '1978-06-19 11:12:57', 'blocked'],
//                [4, 'xet@yahoo.com', 'William Koss', 'Becker-Raynor', '1988-09-07 23:57:45', 'open'],
//                [5, 'ipsa.aut@gmail.com', 'Ms. Antonietta Kozey Jr.', 'Braun Ltd', '2013-10-16 10:00:01', 'open'],
//            ];
//
//            $content->row((new Box('Table', new Table($headers, $rows)))->style('info')->solid());
        });
    }
}
