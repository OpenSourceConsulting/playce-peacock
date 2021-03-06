/*
 * File: app/view/osdAddWindow.js
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

Ext.define('MyApp.view.osdAddWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.osdAddWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.FieldSet',
        'Ext.form.field.Text',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],

    height: 200,
    width: 400,
    resizable: false,
    layout: 'border',
    title: 'Add OSD',
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    region: 'center',
                    id: 'osdAddTopPanel',
                    itemId: 'osdAddTopPanel',
                    items: [
                        {
                            xtype: 'form',
                            height: 156,
                            id: 'osdAddFormPanel',
                            itemId: 'osdAddFormPanel',
                            bodyPadding: 10,
                            items: [
                                {
                                    xtype: 'fieldset',
                                    padding: 10,
                                    title: ' OSD Data ',
                                    items: [
                                        {
                                            xtype: 'textfield',
                                            anchor: '100%',
                                            id: 'osdAddHost',
                                            itemId: 'osdAddHost',
                                            fieldLabel: 'Host',
                                            labelAlign: 'right',
                                            labelWidth: 40,
                                            name: 'osdAddHost',
                                            allowBlank: false,
                                            allowOnlyWhitespace: false,
                                            enforceMaxLength: true,
                                            maskRe: /[a-z0-9_\-]/,
                                            maxLength: 32,
                                            regex: /[a-z0-9_\-]/
                                        },
                                        {
                                            xtype: 'textfield',
                                            anchor: '100%',
                                            id: 'osdAddPath',
                                            itemId: 'osdAddPath',
                                            fieldLabel: 'Path',
                                            labelAlign: 'right',
                                            labelWidth: 40,
                                            name: 'osdAddPath',
                                            allowBlank: false,
                                            allowOnlyWhitespace: false,
                                            enforceMaxLength: true,
                                            maskRe: /[a-zA-Z0-9_\-\/\.]/,
                                            maxLength: 80,
                                            regex: /[a-zA-Z0-9_\-\/\.]/
                                        }
                                    ]
                                }
                            ]
                        }
                    ],
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'bottom',
                            id: 'osdAddToolbar',
                            itemId: 'osdAddToolbar',
                            layout: {
                                type: 'hbox',
                                pack: 'center'
                            },
                            items: [
                                {
                                    xtype: 'button',
                                    handler: function(button, e) {
                                        storageConstants.me.addStorageOsd();
                                    },
                                    padding: '2 20 2 20',
                                    text: 'Ok'
                                },
                                {
                                    xtype: 'button',
                                    handler: function(button, e) {
                                        var myForm = Ext.getCmp("osdAddFormPanel");

                                        myForm.getForm().findField("osdAddHost").setValue('');
                                        myForm.getForm().findField("osdAddPath").setValue('');

                                    },
                                    padding: '2 12 2 12',
                                    text: 'Clear'
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