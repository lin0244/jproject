 'use strict';

angular.module('jprojectApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-jprojectApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-jprojectApp-params')});
                }
                return response;
            }
        };
    });
