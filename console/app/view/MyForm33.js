/*
 * File: app/view/MyForm33.js
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

Ext.define('MyApp.view.MyForm33', {
    extend: 'Ext.form.Panel',
    alias: 'widget.myform33',

    requires: [
        'Ext.form.FieldContainer',
        'Ext.XTemplate',
        'Ext.button.Button',
        'Ext.form.field.ComboBox'
    ],

    id: 'addProjectForm',
    itemId: 'addProjectForm1',
    bodyPadding: 15,
    waitMsgTarget: true,

    initComponent: function() {
        var me = this;

        me.initialConfig = Ext.apply({
            waitMsgTarget: true
        }, me.initialConfig);

        Ext.applyIf(me, {
            fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 120
            },
            items: [
                {
                    xtype: 'fieldcontainer',
                    height: 34,
                    defaults: {
                        flex: 1
                    },
                    fieldLabel: 'Label',
                    hideLabel: true,
                    layout: {
                        type: 'hbox',
                        align: 'middle'
                    },
                    items: [
                        {
                            xtype: 'textfield',
                            flex: 5,
                            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                            ],
                            fieldLabel: 'Project Code',
                            name: 'projectCode',
                            allowBlank: false,
                            vtype: 'template',
                            listeners: {
                                change: {
                                    fn: me.onTextfieldChange,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'button',
                            handler: function(button, e) {
                                var projectCode = Ext.getCmp("addProjectForm").getForm().findField('projectCode').getValue();

                                if(projectCode == '') {
                                    Ext.Msg.alert('Error', 'Project Code를 입력하세요.');
                                }

                                Ext.Ajax.request({
                                    url: GLOBAL.urlPrefix + "alm/project/wizard/" + projectCode,
                                    method: 'GET',
                                    headers: { 'Content-Type': 'application/json' },
                                    waitMsg: 'Project Code Checking...',
                                    success: function (response) {

                                        var responseData = Ext.JSON.decode(response.responseText);

                                        if(responseData.success) {

                                            Ext.Msg.alert('Success', responseData.msg);
                                            Ext.getCmp("proejctCodeDuplYnField").setValue('Y');
                                        } else {

                                            Ext.Msg.alert('Failure', responseData.msg);

                                        }

                                    },
                                    failure: function (response) {
                                        var msg = Ext.JSON.decode(response.responseText).msg;

                                        Ext.Msg.alert('Failure', msg);
                                    }
                                });

                            },
                            flex: 1,
                            id: 'projectDuplBtn',
                            itemId: 'projectDuplBtn',
                            margin: '0 0 0 10',
                            text: '중복확인'
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    padding: '',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    fieldLabel: 'Project Name',
                    name: 'projectName',
                    allowBlank: false
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    margin: '0 0 15 0',
                    padding: '',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    fieldLabel: 'Project Description',
                    name: 'projectDescription',
                    allowBlank: false
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    margin: '0 0 15 0',
                    padding: '',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    fieldLabel: 'Group Description',
                    name: 'groupDescription',
                    allowBlank: false
                },
                {
                    xtype: 'combobox',
                    anchor: '100%',
                    margin: '0 0 15 0',
                    padding: '',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    fieldLabel: 'Select Repository',
                    name: 'repository',
                    allowBlank: false,
                    displayField: 'repositoryCode',
                    store: 'ComboAlmRepositoryStore',
                    valueField: 'repositoryCode'
                }
            ]
        });

        me.callParent(arguments);
    },

    onTextfieldChange: function(field, newValue, oldValue, eOpts) {
        if(Ext.getCmp("proejctCodeDuplYnField") != null){
            Ext.getCmp("proejctCodeDuplYnField").setValue("");
        }
    }

});