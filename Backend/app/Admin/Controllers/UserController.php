<?php

namespace App\Admin\Controllers;

use App\Specialities;
use App\User;

use App\UserLevel;
use App\Stations;
use Encore\Admin\Form;
use Encore\Admin\Grid;
use Encore\Admin\Facades\Admin;
use Encore\Admin\Layout\Content;
use App\Http\Controllers\Controller;
use Encore\Admin\Controllers\ModelForm;
use Illuminate\Support\Facades\Hash;

class UserController extends Controller
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

            $content->header('Članovi HGSS-a');
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

            $content->header('Uredi člana');
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

            $content->header('Novi član');
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
        return Admin::grid(User::class, function (Grid $grid) {

            $grid->id('ID')->sortable();
            $grid->column('name', 'Ime i prezime');
            $grid->column('email');
            $grid->user_level()->name('Razina članstva');
            $grid->station()->name('Matična stanica');

            $grid->specialities('Specijalnosti')->pluck('name')->display(function($specialities) { return join(', ', $specialities->toArray()); });

            $grid->created_at('Član HGSS-a od');
            $grid->is_active('Aktivan')->display(function($is_active) {
                return $is_active ? '<i class="fa fa-check"></i>' : '';
            });
//            $grid->updated_at();

            $grid->filter(function($filter){

//                // If you have too many filters，you can use a modal window to handle them.
                $filter->useModal();
//
                // sql: ... WHERE `user.name` LIKE "%$name%";
                $filter->like('name', 'name');
//
//                 sql: ... WHERE `user.email` = $email;
//                $filter->is('emial', 'Email');
//
                // sql: ... WHERE `user.created_at` BETWEEN $start AND $end;
                $filter->between('created_at', 'Created Time')->datetime();
//
//                // sql: ... WHERE `article.author_id` = $id;
//                $filter->is('author_id', 'Author')->select(User::all()->pluck('name', 'id'));
//
//                // sql: ... WHERE `title` LIKE "%$input" OR `content` LIKE "%$input";
//                $filter->where(function ($query) {
//
//                    $query->where('title', 'like', "%{$this->input}%")
//                        ->orWhere('content', 'like', "%{$this->input}%");
//
//                }, 'Text');
//
//                // sql: ... WHERE `rate` >= 6 AND `created_at` = {$input};
//                $filter->where(function ($query) {
//
//                    $query->whereRaw("`rate` >= 6 AND `created_at` = {$this->input}");
//
//                }, 'Text');
//
                // relation filter, filter columns in relation `profile`
                $filter->where(function ($query) {

                    $input = $this->input;

                    $query->whereHas('station', function ($query) use ($input) {
                        $query->where('name', 'like', "%{$input}%");
                    });

                }, 'Stanica');

                $filter->where(function ($query) {

                    $input = $this->input;

                    $query->whereHas('specialities', function ($query) use ($input) {
                        $query->where('name', 'like', "%{$input}%");
                    });

                }, 'Specijalnost');
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
        return Admin::form(User::class, function (Form $form) {

            $form->tab('Osnovne informacije', function ($form) {
                $form->display('id', 'ID');
                $form->text('name', 'Naziv');
                $form->text('email', 'Email');
                $form->password('password', 'Lozinka');
                $form->text('address', 'Adresa');
                $form->text('availability', 'Dostupnost tijekom dana')->placeholder('Npr. nedostupan radnim danom 8-16');

                $form->hasMany('phones', function (Form\NestedForm $form) {
                    $form->text('number', 'Br. telefona')->placeholder('+385...')->rules('required');
                });

                // callback before save
                $form->saving(function (Form $form) {
                    if ($form->password && $form->model()->password != $form->password) {
                        $form->password = bcrypt($form->password);
                    }
                });
            })->tab('HGSS', function ($form) {
                $form->select('user_level_id', 'Kategorija')->options(UserLevel::all()->pluck('name', 'id'));
                $form->select('station_id', 'Matična stanica')->options(Stations::all()->pluck('name', 'id'));
                $form->multipleSelect('specialities', 'Specijalnosti')->options(Specialities::all()->pluck('name', 'id'));


                $form->date('created_at', 'Član HGSS-a od');

                $form->switch('is_active', 'Aktivan?');
            })->tab('Certifikati i tečajevi', function ($form) {
                $form->switch('first_aid', 'Položena prva pomoć');
                $form->switch('winter_search', 'Potraga u zimskim mjesecima');
                $form->switch('summer_search', 'Potraga u ljetnim mjesecima');

//                $form->hasMany('other_certificates', function (Form\NestedForm $form) {
//                    $form->text('name', 'Naziv tečaja/certifikata')->rules('required');
//                    $form->date('created_at', 'Datum polaganja');
//                });
            });

        });
    }
}
