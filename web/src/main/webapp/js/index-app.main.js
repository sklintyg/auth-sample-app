/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

//Register module
angular.module('rhsIndexApp', []);

angular.module('rhsIndexApp')
    .controller('IndexController', ['$scope', '$http', function($scope, $http) {
        'use strict';

        $scope.user = {};
        $scope.loggedIn = true;
        $scope.intygsId = '9020fbb9-e387-40b0-ba75-ac2746e4736b';

        $scope.loadConfig = function() {
            $http.get('/api/config', null).then(function(response) {
                $scope.config = response.data;
            })
        };

        $scope.loadUser = function() {
            $http.get('/api/user', null).then(function(response) {
                if (response.data.authenticated) {
                    $scope.user = response.data.userModel;
                    $scope.loggedIn = true;
                }
            });
        };

        $scope.exchangeToken = function() {
            $http.get('/api/user/exchange').then(function(response) {
                $scope.token = response.data.userModel.token;
                $scope.loadUser();
            });
        };

        $scope.exchangePreStoredToken = function() {
            $http.get('/api/user/prestored').then(function(response) {
                $scope.token = response.data.userModel.token;
            });
        };

        $scope.enableOpenButton = function() {
            return angular.isDefined($scope.intygsId) && $scope.intygsId.length > 4;
        };

        $scope.authenticate = function() {
            $http.get($scope.config.webcertUrl + '/oauth/token', {
                headers: {'Authorization': 'Bearer:' + $scope.user.accessToken}
               }).then(function() {
                    $scope.openWebcert();
                })
        };

        $scope.openWebcert = function() {
            $window.open($scope.config.webcertUrl + '/visa/intyg/' + $scope.intygsId, '_blank');
        };

        $scope.loadConfig();
        $scope.loadUser();

    }]);
