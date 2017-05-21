<?php

use Illuminate\Routing\Router;

Admin::registerHelpersRoutes();

Route::group([
    'prefix'        => config('admin.prefix'),
    'namespace'     => Admin::controllerNamespace(),
    'middleware'    => ['web', 'admin'],
], function (Router $router) {

    $router->get('/', 'HomeController@index');

    $router->resource('action_types', ActionTypesController::class);
    $router->resource('phones', PhonesController::class);
    $router->resource('rescue_actions', RescueActionsController::class);
    $router->resource('specialities', SpecialitiesController::class);
    $router->resource('stations', StationsController::class);
    $router->resource('users', UserController::class);
    $router->resource('user_levels', UserLevelController::class);
    $router->get('/map', 'MapController@index');
    $router->get('/map/rescue_actions/{id}', 'MapController@rescue_action');
    $router->get('/reports', 'ReportsController@index');

});
