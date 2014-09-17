/*
 * File: app/view/SoftwareConfigWindow.js
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

Ext.define('MyApp.view.SoftwareConfigWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.SoftwareConfigWindow',

    requires: [
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
        'Ext.form.Panel',
        'Ext.form.field.ComboBox',
        'Ext.form.field.Display',
        'Ext.form.field.Hidden',
        'Ext.form.FieldSet',
        'Ext.form.field.TextArea',
        'Ext.form.field.Checkbox'
    ],

    height: 720,
    id: 'softwareConfigWindow',
    itemId: 'softwareConfigWindow',
    width: 500,
    layout: 'border',
    title: 'Edit Config',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    layout: {
                        type: 'hbox',
                        pack: 'center'
                    },
                    items: [
                        {
                            xtype: 'button',
                            handler: function(button, e) {


                                var configForm = Ext.getCmp("editSoftwareConfigForm");

                                configForm.getForm().submit({
                                    clientValidation: true,
                                    url: GLOBAL.urlPrefix + "config/updateConfig",
                                    params: {
                                        newStatus: 'delivered'
                                    },
                                    waitMsg: 'Save Config Data...',
                                    success: function(form, action) {
                                        Ext.Msg.alert('Success', action.result.msg);

                                        Ext.getCmp('instanceSoftwareGrid').getStore().load();

                                        configForm.up('window').close();
                                    },
                                    failure: function(form, action) {

                                        switch (action.failureType) {
                                            case Ext.form.action.Action.CLIENT_INVALID:
                                            Ext.Msg.alert('Failure', '유효하지 않은 입력값이 존재합니다.');
                                            break;
                                            case Ext.form.action.Action.CONNECT_FAILURE:
                                            Ext.Msg.alert('Failure', 'Server communication failed');
                                            break;
                                            case Ext.form.action.Action.SERVER_INVALID:
                                            Ext.Msg.alert('Failure', action.result.msg);
                                        }
                                    }
                                });

                            },
                            margin: '0 15 0 0',
                            padding: '2 7 2 7',
                            text: 'Save'
                        },
                        {
                            xtype: 'button',
                            handler: function(button, e) {
                                Ext.MessageBox.confirm('Confirm', '작업을 취소하시겠습니까?', function(btn){

                                    if(btn == "yes"){
                                        button.up("window").close();
                                    }

                                });

                            },
                            margin: '0 0 0 0',
                            padding: '2 5 2 5',
                            text: 'Cancel'
                        }
                    ]
                }
            ],
            items: [
                {
                    xtype: 'form',
                    region: 'center',
                    id: 'editSoftwareConfigForm',
                    itemId: 'editSoftwareConfigForm',
                    header: false,
                    title: 'My Form',
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            border: '',
                            padding: '10 0 0 10',
                            vertical: true,
                            items: [
                                {
                                    xtype: 'combobox',
                                    itemId: 'mycombobox3',
                                    width: 350,
                                    fieldLabel: 'Config File Name',
                                    labelWidth: 130,
                                    name: 'configFileName',
                                    displayField: 'configFileName',
                                    store: 'ComboSoftwareConfigFileStore',
                                    typeAhead: true,
                                    valueField: 'configFileName',
                                    listeners: {
                                        change: {
                                            fn: me.onMycombobox3Change,
                                            scope: me
                                        }
                                    }
                                },
                                {
                                    xtype: 'combobox',
                                    itemId: 'mycombobox4',
                                    width: 350,
                                    fieldLabel: 'Config File Version',
                                    labelWidth: 130,
                                    name: 'configFileId',
                                    displayField: 'configFileId',
                                    store: 'ComboSoftwareConfigVersionStore',
                                    typeAhead: true,
                                    valueField: 'configFileId',
                                    listeners: {
                                        change: {
                                            fn: me.onMycombobox4Change,
                                            scope: me
                                        }
                                    }
                                },
                                {
                                    xtype: 'displayfield',
                                    padding: '20 0 0 0',
                                    fieldLabel: 'Path',
                                    name: 'configFilePath',
                                    value: 'Display Field'
                                },
                                {
                                    xtype: 'hiddenfield',
                                    fieldLabel: 'Label',
                                    name: 'machineId'
                                },
                                {
                                    xtype: 'hiddenfield',
                                    fieldLabel: 'Label',
                                    name: 'softwareId'
                                }
                            ]
                        }
                    ],
                    items: [
                        {
                            xtype: 'panel',
                            id: 'softwareInstallPanel1',
                            itemId: 'softwareInstallPanel',
                            padding: '0 10 10 10',
                            header: false,
                            title: 'sofrwareInstallPanel',
                            items: [
                                {
                                    xtype: 'fieldset',
                                    title: 'Configuration in DataBase',
                                    items: [
                                        {
                                            xtype: 'textareafield',
                                            anchor: '100%',
                                            height: 200,
                                            fieldLabel: 'Label',
                                            hideLabel: true,
                                            name: 'configFileContents'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'fieldset',
                                    collapsible: true,
                                    title: '     Configuration in System',
                                    items: [
                                        {
                                            xtype: 'textareafield',
                                            anchor: '100%',
                                            height: 200,
                                            fieldLabel: 'Label',
                                            hideLabel: true,
                                            name: 'configSystemContents',
                                            readOnly: true
                                        }
                                    ]
                                },
                                {
                                    xtype: 'checkboxfield',
                                    fieldLabel: 'Restart Service',
                                    name: 'autoRestart',
                                    boxLabel: 'Restart Service after Save',
                                    checked: true,
                                    inputValue: 'Y',
                                    uncheckedValue: 'N'
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onMycombobox3Change: function(field, newValue, oldValue, eOpts) {
        var form = field.up('form').getForm();

        var versionStore = Ext.getStore("ComboSoftwareConfigVersionStore");

        versionStore.getProxy().extraParams = {
            machineId : instancesConstants.selectRow.get("machineId"),
            softwareId : form.findField("softwareId").getValue(),
            configFileName : newValue
        };
        versionStore.load();

        form.findField("configFileContents").setValue("");
        form.findField("configSystemContents").setValue("");
    },

    onMycombobox4Change: function(field, newValue, oldValue, eOpts) {

        var store = field.getStore();
        var record = store.findRecord("configFileId", newValue);
        var form = field.up('form').getForm();

        var fileLocation = "";
        var fileName = "";

        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "config/getConfigDetail",
            params : {
                machineId : instancesConstants.selectRow.get("machineId"),
                softwareId : form.findField("softwareId").getValue(),
                configFileId : newValue
            },
            disableCaching : true,
            success: function(response){

                var responseData = Ext.JSON.decode(response.responseText);

                if(responseData.success) {

                    fileLocation = responseData.data.configFileLocation;
                    fileName = responseData.data.configFileName;

                    form.findField("configFilePath").setValue(fileLocation + "/" + fileName);
                    form.findField("configFileContents").setValue(responseData.data.configFileContents);

                    Ext.Ajax.request({
                        url: GLOBAL.urlPrefix + "config/getSystemConfig",
                        params : {
                            machineId : instancesConstants.selectRow.get("machineId"),
                            configFileLocation : fileLocation,
                            configFileName : fileName
                        },
                        disableCaching : true,
                        success: function(response2){

                            var responseData2 = Ext.JSON.decode(response2.responseText);

                            if(responseData2.success) {

                                form.findField("configSystemContents").setValue(responseData2.data);

                            }

                        }
                    });

                }

            }
        });

    }

});