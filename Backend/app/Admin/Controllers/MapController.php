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

class MapController extends Controller
{
    public function index()
    {
        return Admin::content(function (Content $content) {
            $content->header('Članovi HGSS-a');

            $content->body(view('map_members'));
        });
    }

    public function rescue_action($id)
    {
        return Admin::content(function (Content $content) {
            $content->header('Tijek potrage');

            $content->body(view('map_rescue_action'));
        });
    }
}
