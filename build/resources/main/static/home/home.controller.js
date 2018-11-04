(function () {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['UserService', '$location', '$rootScope', 'FlashService', '$scope'];
    function HomeController(UserService, $location, $rootScope, FlashService, $scope) {
        var vm = this;

        $scope.user = null;
        $scope.username = null;
        $scope.attempt = 0;
        $scope.fixedPrediction = null;
        vm.updateValue = updateValue;
        $scope.isLoading = true;

        initController();

        function initController() {
            $scope.isLoading = true;
            loadCurrentUser();
            loadFixedPrediction();
            $scope.isLoading = false;
        }

        function loadFixedPrediction() {
            UserService.GetFixedPredictionsByDate("17")
                .then(function (fixedPrediction) {
                    console.log(fixedPrediction);
                    $scope.fixedPrediction = fixedPrediction;
                });
        }

        function loadCurrentUser() {
            UserService.GetUserPredictionsByUserName($rootScope.globals.currentUser.username)
                .then(function (user) {
                    console.log(user);
                    $scope.user = user;
                    $scope.username = $rootScope.globals.currentUser.username;
                    $scope.attempt = user.attempt;
                });
        }

        function updateValue() {
            vm.dataLoading = true;
            vm.user.username = $rootScope.globals.currentUser.username;
            vm.user.date = $scope.fixedPrediction.date;
            vm.user.value = vm.user.myValue;
            console.log(vm.user);
            UserService.CreateUserPredictions(vm.user)
            .then(function (response) {
            //console.log(response);
                    if (response.success) {
                        FlashService.Success('Update successful', true);
                        $location.path('/');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }

    }

})();