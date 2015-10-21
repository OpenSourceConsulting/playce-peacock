/*
 * File: app/view/renameFileWindow.js
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

Ext.define('MyApp.view.renameFileWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.renameFileWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.FieldSet',
        'Ext.form.field.Text',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],

    height: 172,
    width: 400,
    resizable: false,
    layout: 'border',
    title: 'Rename',
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    region: 'center',
                    id: 'renameFileTopPanel',
                    itemId: 'renameFileTopPanel',
                    items: [
                        {
                            xtype: 'form',
                            height: 156,
                            id: 'renameFileFormPanel',
                            itemId: 'renameFileFormPanel',
                            bodyPadding: 10,
                            items: [
                                {
                                    xtype: 'fieldset',
                                    padding: 10,
                                    title: 'File Data ',
                                    items: [
                                        {
                                            xtype: 'textfield',
                                            anchor: '100%',
                                            id: 'renameFileName',
                                            itemId: 'renameFileName',
                                            fieldLabel: 'File Name',
                                            labelWidth: 110,
                                            name: 'renameFileName'
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
                            id: 'trnameFileToolbar',
                            itemId: 'trnameFileToolbar',
                            layout: {
                                type: 'hbox',
                                pack: 'center'
                            },
                            items: [
                                {
                                    xtype: 'button',
                                    handler: function(button, e) {
                                        objectConstants.me.renameObjectFile();
                                    },
                                    padding: '2 20 2 20',
                                    text: 'Ok'
                                },
                                {
                                    xtype: 'button',
                                    handler: function(button, e) {
                                        var myForm = Ext.getCmp("renameFileFormPanel");

                                        myForm.getForm().findField("renameFileName").setValue('');


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