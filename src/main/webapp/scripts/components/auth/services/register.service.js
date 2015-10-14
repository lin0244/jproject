'use strict';

angular.module('jprojectApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


