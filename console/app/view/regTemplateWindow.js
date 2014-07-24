/*
 * File: app/view/regTemplateWindow.js
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

Ext.define('MyApp.view.regTemplateWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.regTemplateWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.field.Text',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],

    height: 160,
    id: 'regTemplateWindow',
    itemId: 'regTemplateWindow',
    width: 450,
    layout: 'border',
    title: 'Make Template',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    flex: 1,
                    region: 'center',
                    header: false,
                    title: 'regTemplatePanel',
                    items: [
                        {
                            xtype: 'form',
                            id: 'templateForm',
                            itemId: 'templateForm',
                            bodyPadding: 15,
                            items: [
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    fieldLabel: 'Name',
                                    labelWidth: 120
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    fieldLabel: 'Description',
                                    labelWidth: 120
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
                                    id: 'makeTemplateBtn',
                                    itemId: 'makeTemplateBtn',
                                    margin: '0 15 0 0',
                                    padding: '2 10 2 10',
                                    text: 'OK'
                                },
                                {
                                    xtype: 'button',
                                    handler: function(button, e) {
                                        button.up("window").close();
                                    },
                                    id: 'templateCancelBtn',
                                    itemId: 'templateCancelBtn',
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