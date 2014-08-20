/*
 * File: app/view/EditInstanceNameWindow.js
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

Ext.define('MyApp.view.EditInstanceNameWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.EditInstanceNameWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.field.Text',
        'Ext.XTemplate',
        'Ext.form.field.Hidden',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],

    height: 130,
    id: 'regTemplateWindow1',
    itemId: 'regTemplateWindow',
    width: 450,
    layout: 'border',
    title: 'Edit Instance Name',
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    flex: 1,
                    region: 'center',
                    header: false,
                    title: 'instanceNamePanel',
                    items: [
                        {
                            xtype: 'form',
                            id: 'instanceNameForm',
                            itemId: 'instanceNameForm',
                            bodyPadding: 15,
                            fieldDefaults: {
                                msgTarget: 'side'
                            },
                            items: [
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                                    fieldLabel: 'Name',
                                    labelWidth: 120,
                                    name: 'displayName',
                                    allowBlank: false,
                                    vtype: 'template'
                                },
                                {
                                    xtype: 'hiddenfield',
                                    anchor: '100%',
                                    fieldLabel: 'Label',
                                    name: 'machineId'
                                }
                            ]
                        }
                    ],
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
                                        var instanceForm = Ext.getCmp("instanceNameForm");

                                        var machineId = instanceForm.getForm().findField("machineId").getValue();

                                        instanceForm.getForm().submit({
                                            clientValidation: true,
                                            url: GLOBAL.urlPrefix + "machine/updateMachine",
                                            params: {
                                                newStatus: 'delivered'
                                            },
                                            waitMsg: 'Saving Data...',
                                            success: function(form, action) {
                                                Ext.Msg.alert('Success', action.result.msg);

                                                Ext.getCmp('instancesGrid').getStore().load({

                                                    callback:function(records, operation, success){

                                                        Ext.each(records, function(record) {

                                                            if(record.get("machineId") == machineId) {
                                                                Ext.getCmp('instancesGrid').getSelectionModel().select(record,true,false);

                                                                instancesConstants.selectRow = record;
                                                                instancesConstants.me.selectInstanceGrid();
                                                            }
                                                        });
                                                    }

                                                });
                                                templateForm.up('window').close();
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
                                    padding: '2 10 2 10',
                                    text: 'OK'
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
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});