/*
 * File: app/store/VMStore.js
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

Ext.define('MyApp.store.VMStore', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.VMModel',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            model: 'MyApp.model.VMModel',
            storeId: 'VMStore',
            proxy: me.processMyAjaxProxy({
                type: 'ajax',
                timeout: 180000,
                url: 'rhevm/vms/list',
                reader: {
                    type: 'json',
                    root: 'list'
                }
            }),
            listeners: {
                load: {
                    fn: me.onJsonstoreLoad,
                    scope: me
                }
            }
        }, cfg)]);
    },

    processMyAjaxProxy: function(config) {
        config.actionMethods = {create: "POST", read: "POST", update: "POST", destroy: "POST"};

        return config;
    },

    onJsonstoreLoad: function(store, records, successful, eOpts) {
        var defaultRow = (RHEVMConstants.page-1)*100;

        var pagingDesc = "";
        if(store.getTotalCount() == 0) {
            pagingDesc = (defaultRow + 1) + " ~ N/A";
        } else {
            pagingDesc = (defaultRow + 1) + " ~ " + (defaultRow + store.getTotalCount());
        }

        Ext.getCmp("vmPagingLabel").setText(pagingDesc);

        if(RHEVMConstants.page == 1) {
            Ext.getCmp("vmPagingLeftBtn").setDisabled(true);
        } else {
            Ext.getCmp("vmPagingLeftBtn").setDisabled(false);
        }

        if(store.getTotalCount() < 100) {
            Ext.getCmp("vmPagingRightBtn").setDisabled(true);
        } else {
            Ext.getCmp("vmPagingRightBtn").setDisabled(false);
        }

    }

});