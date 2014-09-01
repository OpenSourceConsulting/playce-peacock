/*
 * File: app.js
 *
 * This file was generated by Sencha Architect version 3.0.4.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

// @require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});


Ext.application({

    requires: [
        'Ext.util.Point'
    ],
    models: [
        'instanceModel',
        'instanceSoftwareModel',
        'instanceOsModel',
        'monitoringChartModel',
        'UserModel',
        'HypervisorModel',
        'VMModel',
        'NetworkModel',
        'DiskModel',
        'TemplateModel',
        'MachineModel',
        'SessionModel',
        'PackageModel',
        'DataCenterModel',
        'ClusterModel',
        'AlmUserModel',
        'AccountModel',
        'GroupModel',
        'ProjectModel',
        'SpaceSummaryModel',
        'AlmGroupModel',
        'InstanceMonitoringModel',
        'AlmJobModel'
    ],
    stores: [
        'MenuTreeStore',
        'tempChartData',
        'tempGridData',
        'instanceListStore',
        'instanceSoftwareListStore',
        'instanceOsListStore',
        'instanceMonitoringChartStore',
        'UserStore',
        'HypervisorStore',
        'VMStore',
        'VMNetworkStore',
        'VMDiskStore',
        'TemplateStore',
        'MachineStore',
        'SessionStore',
        'ComboHypervisorStore',
        'InstancePackageStore',
        'ComboDataCenterStore',
        'ComboClusterStore',
        'ComboTemplateStore',
        'AlmUserStore',
        'AccountStore',
        'ComboGroupStore',
        'ComboAccountStore',
        'AlmProjectStore',
        'AlmSpaceStore',
        'AlmGroupStore',
        'InstanceMonitoringStore',
        'MonitoringPopupStore',
        'AlmGroupUsersStore',
        'AlmJobStore'
    ],
    views: [
        'peacockViewport',
        'loginWindow',
        'dashboardPanel',
        'instancesContainer',
        'CLIWindow',
        'FstabWindow',
        'CrontabWindow',
        'ManageAccountWindow',
        'RegAccountWindow',
        'softwareInstallWindow',
        'rhevmContainer',
        'RegTemplateWindow',
        'RegRhevmWindow',
        'userContainer',
        'blankPanel',
        'almContainer',
        'almUserWindow',
        'AlmGroupWindow',
        'AlmUsersWindow',
        'softwareInstallLogWindow',
        'softwareConfigWindow',
        'MonitoringChartWindow',
        'UserWindow',
        'EditInstanceWindow',
        'RegInstanceWindow',
        'almProjectSpaceWindow',
        'AlmProjectWindow',
        'almProjectJobWindow'
    ],
    controllers: [
        'LoginController',
        'SettingsMenuController',
        'MenuController',
        'GlobalController',
        'InstancesController',
        'SoftwareInstallController',
        'RHEVMController',
        'ALMController',
        'UserController'
    ],
    name: 'MyApp',

    launch: function() {
        Ext.create('MyApp.view.peacockViewport');
    }

});
