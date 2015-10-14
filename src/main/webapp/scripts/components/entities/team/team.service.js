'use strict';

angular.module('jprojectApp')
    .factory('Team', function ($resource, DateUtils) {
        return $resource('api/teams/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
