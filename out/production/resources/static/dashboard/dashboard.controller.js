(function () {
    'use strict';

    angular
        .module('app')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['UserService', '$location', '$rootScope', 'FlashService', '$scope'];
    function DashboardController(UserService, $location, $rootScope, FlashService, $scope) {
        var vm = this;

        $scope.username = null;
        $scope.fixedPrediction = null;
        $scope.data = loadAllPrediction;

        initController();

        function initController() {
            loadFixedPrediction();
            loadAllPrediction();
        }

        function loadFixedPrediction() {
            UserService.GetFixedPredictionsByDate("17")
                .then(function (fixedPrediction) {
                    console.log(fixedPrediction);
                    $scope.fixedPrediction = fixedPrediction;
                });
        }

        function loadAllPrediction() {
            UserService.GetAllUserPredictionsByDate("17")
                .then(function (result) {
                    console.log(result);
                    $scope.data = result
                    console.log($scope.data);
                });

        }
    }

})();