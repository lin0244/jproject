'use strict';

angular.module('jprojectApp')
    .factory('TaskComment', function ($resource, DateUtils) {
        return $resource('api/taskComments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.postedOn = DateUtils.convertDateTimeFromServer(data.postedOn);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
