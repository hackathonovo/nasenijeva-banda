<?php

namespace App\Admin\Controllers;

use App\Stations;

use Encore\Admin\Form;
use Encore\Admin\Grid;
use Encore\Admin\Facades\Admin;
use Encore\Admin\Layout\Content;
use App\Http\Controllers\Controller;
use Encore\Admin\Controllers\ModelForm;

class StationsController extends Controller
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

            $content->header('Stanice');

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

            $content->header('header');
            $content->description('description');

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

            $content->header('header');
            $content->description('description');

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
        return Admin::grid(Stations::class, function (Grid $grid) {

            $grid->id('ID')->sortable();
            $grid->column('name', 'Naziv');
//            $grid->column('address');
            $grid->column('city', 'Grad');
            $grid->users('Br. članova')->display(function ($users) {
                $count = count($users);
                return "<span class='label label-warning'>{$count}</span>";
            });
//            $grid->column('description');

            $grid->created_at();
            $grid->updated_at();
        });
    }

    /**
     * Make a form builder.
     *
     * @return Form
     */
    protected function form()
    {
        return Admin::form(Stations::class, function (Form $form) {

            $form->display('id', 'ID');

            $form->text('name', 'Naziv');
            $form->text('address', 'Adresa');
            $form->text('city', 'Grad');
            $form->text('description', 'Opis');

            $form->display('created_at', 'Dodana');
            $form->display('updated_at', 'Uređena');
        });
    }
}
