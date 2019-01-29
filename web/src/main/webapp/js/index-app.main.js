/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
angular.module('indexApp', []);

angular.module('indexApp')
    .controller('IndexController', ['$scope', '$http', function($scope, $http) {
        'use strict';

        $scope.user = {};
        $scope.loggedIn = false;


        $scope.fields = [
            {
                id: 'certId',
                label: 'intygId',
                placeholder: 'utkast/intygsid',
                model: '' // 9020fbb9-e387-40b0-ba75-ac2746e4736b
            },
            {
                id: 'alternatePatientSSn',
                label: 'nytt personnummer',
                placeholder: 'alternatePatientSSn',
                model: ''
            },
            {
                id: 'fornamn',
                label: 'Förnamn',
                placeholder: 'Förnamn',
                model: '',
                canDisable: true
            },
            {
                id: 'mellannamn',
                label: 'Mellannamn',
                placeholder: 'Mellannamn',
                model: '',
                canDisable: true
            },
            {
                id: 'efternamn',
                label: 'Efternamn',
                placeholder: 'Efternamn',
                model: '',
                canDisable: true
            },
            {
                id: 'postadress',
                label: 'Postadress',
                placeholder: 'Postadress',
                model: '',
                canDisable: true
            },
            {
                id: 'postnummer',
                label: 'Postnummer',
                placeholder: 'Postnr',
                model: '',
                canDisable: true
            },
            {
                id: 'postort',
                label: 'Postort',
                placeholder: 'Postort',
                model: '',
                canDisable: true
            },
            {
                id: 'ref',
                label: 'ref',
                placeholder: 'Ref',
                model: ''
            },
            {
                id: 'enhet',
                label: 'enhet',
                placeholder: 'enhetsid',
                model: undefined
            },
            {
                id: 'responsibleHospName',
                label: 'responsibleHospName',
                placeholder: 'responsibleHospName',
                model: ''
            },
            {
                id: 'sjf',
                label: 'Sammanhållen Journalföring',
                checkbox: true,
                model: true
            },
            {
                id: 'kopieringOK',
                label: 'kopieringOK',
                checkbox: true,
                model: true
            },
            {
                id: 'avliden',
                label: 'patient avliden',
                checkbox: true,
                model: undefined
            },
            {
                id: 'inaktivEnhet',
                label: 'inaktiv enhet',
                checkbox: true,
                model: undefined
            }

        ];

        function loadConfig() {
            $http.get('/api/config', null).then(function(response) {
                $scope.config = response.data;
            })
        }

         function loadUser() {
            $http.get('/api/user', null).then(function(response) {
                if (response.data.authenticated) {
                    $scope.user = response.data.userModel;
                    $scope.loggedIn = true;
                } else {
                    $scope.loggedIn = false;
                }
            });
        }

        loadConfig();
        loadUser();

        $scope.exchangeToken = function() {
            $http.get('/api/user/exchange').then(function(response) {
                $scope.token = response.data.userModel.token;
                loadUser();
            });
        };

        $scope.enableOpenButton = function() {
            return $scope.user.accessToken;
        };

         $scope.loginDjupintegration = function() {
              try {
                  var url = $scope.config.webcertUrl + '/oauth/token';

                  var data = [];
                  angular.forEach($scope.fields, function(value) {
                      if( value.disabled !== true) {
                          data.push({
                              id: value.id,
                              data: value.model
                          });
                      }
                  });

                  // submit data
                  sendData(url, data);
              } catch(e) {
                  alert(e);
              }
          };

        function sendData(url, data) {
            var form = document.createElement('form');

            form.method = 'post';
            form.action = url;
            form.enctype = 'application/x-www-form-urlencoded';

            data.push({
                id: 'access_token',
                data: $scope.user.accessToken
            });

            angular.forEach(data, function(row) {
                var node = document.createElement('input');
                node.name  = row.id;
                node.value = row.data === undefined ? '' : row.data.toString();

                form.appendChild(node);
            });

            // To be sent, the form needs to be attached to the main document.
            form.style.display = "none";
            document.body.appendChild(form);

            form.submit();

            // Once the form is sent, remove it.
            document.body.removeChild(form);
        }

    }]);
