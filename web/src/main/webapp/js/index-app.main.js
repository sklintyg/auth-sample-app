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
        $scope.loggedIn = false;

        $scope.deeplink = {
                  certId: '9020fbb9-e387-40b0-ba75-ac2746e4736b',
                  certType: '',
                  formData: {
                      alternatePatientSSn: '',
                      fornamn: 'Nils',
                      mellannamn: 'Nisse',
                      efternamn: 'Nygren',
                      postadress: 'Nygatan 14',
                      postnummer: '555 66',
                      postort: 'Nyberga',
                      sjf: true,
                      kopieringOK: true,
                      ref: '',
                      enhet: undefined
                  },
                  excludedFields: {
                      fornamn: false,
                      mellannamn: false,
                      efternamn: false,
                      postadress: false,
                      postnummer: false,
                      postort: false
                  }
              };

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
                } else {
                    $scope.loggedIn = false;
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
                headers: {'Authorization': 'Bearer: ' + $scope.user.accessToken},
                withCredentials: true
               }).then(function() {
                    // $scope.openWebcert();
                    loginDjupintegration();
                })
        };

//        $scope.openWebcert = function() {
//            $window.location($scope.config.webcertUrl + '/visa/intyg/' + $scope.deeplink.certId);
//        };

        $scope.loadConfig();
        $scope.loadUser();

         $scope.loginDjupintegration = function() {
              try {
                  var url = $scope.config.webcertUrl + '/oauth/token',
                      data = $scope.deeplink.formData;
                      angular.forEach($scope.deeplink.excludedFields, function(value, key) {
                          if(value === true) {
                              delete data[key];
                          }
                      });
                  // submit data
                  sendData(url, data);
              } catch(e) {
                  alert(e);
              }
          }

        function sendData(url, data) {
            var name,
                form = document.createElement('form'),
                node = document.createElement('input');

            form.method = 'post';
            form.action = url;
            form.enctype = 'application/x-www-form-urlencoded';

            for(name in data) {
                node.name  = name;
                node.value = data[name] === undefined ? '' : data[name].toString();
                form.appendChild(node.cloneNode());
            }
            var token = document.createElement('input');
            token.name = 'access_token';
            token.value = $scope.user.accessToken;
            form.appendChild(token.cloneNode());

            var certId = document.createElement('input');
            certId.name = 'certId';
            certId.value = $scope.deeplink.certId;
            form.appendChild(certId.cloneNode());

            var certType = document.createElement('input');
            certType.name = 'certId';
            certType.value = $scope.deeplink.certType;
            form.appendChild(certType.cloneNode());

            var enhet = document.createElement('input');
            enhet.name = 'certId';
            enhet.value = $scope.deeplink.certType;
            form.appendChild(enhet.cloneNode());

            // To be sent, the form needs to be attached to the main document.
            form.style.display = "none";
            document.body.appendChild(form);

            form.submit();

            // Once the form is sent, remove it.
            document.body.removeChild(form);
        }

    }]);
