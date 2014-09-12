/*
 * File: app/controller/MenuController.js
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

Ext.define('MyApp.controller.MenuController', {
    extend: 'Ext.app.Controller',

    onMenuViewClick: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        /**
         * 메뉴 선택(cellclick) 시 수행되는 function()
         */
        if (record.get('leaf') && GLOBAL.lastSelectedMenuId != record.get('id')) {

            GLOBAL.lastSelectedMenuId = record.get('id');
            Ext.getCmp('centerContainer').layout.setActiveItem(record.get('activeRow'));

            //Instance Chart Monitoring Stop
            clearInterval(instancesConstants.chartInterval);

            if(record.get('id') == 'MENU_01') {

                dashboardConstants.me.showDashBoard();

            } else if(record.get('id') == 'MENU_02') {

                instancesConstants.me.initInstance();
                instancesConstants.me.searchInstance(true);

            } else if(record.get('id') == 'MENU_03') {

                RHEVMConstants.me.searchHypervisor(true);

            } else if(record.get('id') == 'MENU_04') {

                almConstants.me.searchAlmProject(true);

            } else if(record.get('id') == 'MENU_05') {

                userConstants.me.searchUser(true);
           }

        }
    },

    rhevComboSelect: function(combo, records, eOpts) {

    },

    init: function(application) {
        this.control({
            "#menuTreeView": {
                cellclick: this.onMenuViewClick
            },
            "#rhevCombo": {
                select: this.rhevComboSelect
            }
        });
    }

});
