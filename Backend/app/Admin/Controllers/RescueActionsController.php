<?php

namespace App\Admin\Controllers;

use Twilio;
use App\ActionTypes;
use App\RescueActions;

use App\Stations;
use App\User;
use Encore\Admin\Form;
use Encore\Admin\Grid;
use Encore\Admin\Facades\Admin;
use Encore\Admin\Layout\Content;
use App\Http\Controllers\Controller;
use Encore\Admin\Controllers\ModelForm;

class RescueActionsController extends Controller
{
    use ModelForm;

    /**
     * Index interface.
     *
     * @return Content
     */
    public function index()
    {
        return Admin::content(function (Content $content) {

            $content->header('Akcije spašavanja');
            $content->description('');

            $content->body($this->grid());
        });
    }

    /**
     * Edit interface.
     *
     * @param $id
     * @return Content
     */
    public function edit($id)
    {
        return Admin::content(function (Content $content) use ($id) {

            $content->header('Uredi akciju spašavanja');
            $content->description('');

            $content->body($this->form()->edit($id));
        });
    }

    /**
     * Create interface.
     *
     * @return Content
     */
    public function create()
    {
        return Admin::content(function (Content $content) {

            $content->header('Nova akcija spašavanja');
            $content->description('');

            $content->body($this->form());
        });
    }

    /**
     * Make a grid builder.
     *
     * @return Grid
     */
    protected function grid()
    {
        return Admin::grid(RescueActions::class, function (Grid $grid) {

            $grid->id('ID')->sortable();
            $grid->actionType()->name('Kategorija');
            $grid->column('name', 'Naziv');
            $grid->column('location', 'Lokacija');
            $grid->station()->name('Zadužena stanica');
            $grid->leader()->name('Voditelj');

            $grid->created_at('Vrijeme kreiranja');
            $grid->updated_at('Vrijeme izmjene');
            $grid->end_time('Vrijeme završetka');

            $grid->actions(function ($actions) {
                $str = "<a href='/admin/map/rescue_actions/{$actions->getKey()}'><i class='fa fa-map'></i></a>'";
                $actions->prepend($str);
           });
        });
    }

    /**
     * Make a form builder.
     *
     * @return Form
     */
    protected function form()
    {
        return Admin::form(RescueActions::class, function (Form $form) {

            $form->display('id', 'ID');
            $form->select('action_type_id', 'Kategorija')->options(ActionTypes::all()->pluck('name', 'id'));
            $form->text('name', 'Naziv');
            $form->textarea('description', 'Opis');
            $form->text('location', 'Lokacija');

            $form->select('leader_id', 'Voditelj')->options(User::all()->pluck('name', 'id'));
            $options = Stations::all()->pluck('name', 'id');
            $form->select('station_id', 'Zadužena stanica')->options($options);

            $form->saved(function (Form $form) {
                if($form->model()->wasRecentlyCreated) {
                    $user = User::find($form->leader_id);
                    $form->model()->users()->attach($form->leader_id);
                    foreach($user->phones as $phone) {
                        Twilio::message($phone->number, 'PAŽNJA! Kreirana je nova akcija: ' . $form->name . ' ('. $form->location . ')');
                    }
                }
            });
        });
    }
}
