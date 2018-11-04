(function () {
    'use strict';

    angular
        .module('app')
        .controller('ResultController', ResultController);

    ResultController.$inject = ['UserService', '$location', '$rootScope', 'FlashService', '$scope'];
    function ResultController(UserService, $location, $rootScope, FlashService, $scope) {
        var vm = this;

        $scope.fixedPredictions = null;

//var obj = JSON.parse('[{"date":"17","value":"50","finalValue":"45","names":["Name:bipni,UserName:bipin,Value:23,Attempts:1","Name:trest,UserName:test,Value:34,Attempts:1","Name:tres2t,UserName:test,Value:34,Attempts:1","Name:tre4t,UserName:test,Value:34,Attempts:1"]}]');

        initController();

        function initController() {
            loadFixedPrediction();
        }

        function loadFixedPrediction() {
            UserService.GetResult()
                .then(function (fixedPredictions) {
                    console.log(fixedPredictions);
                    $scope.fixedPredictions = fixedPredictions;
                });
                //$scope.fixedPredictions = obj;
        }

    }

})();