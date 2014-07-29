/*
 * File: app/view/almContainer.js
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

Ext.define('MyApp.view.almContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.almcontainer',

    requires: [
        'Ext.tab.Panel',
        'Ext.tab.Tab',
        'Ext.grid.Panel',
        'Ext.grid.View',
        'Ext.toolbar.Toolbar',
        'Ext.button.Cycle',
        'Ext.menu.Menu',
        'Ext.menu.CheckItem',
        'Ext.toolbar.Separator',
        'Ext.form.field.Text',
        'Ext.form.Label',
        'Ext.form.Panel',
        'Ext.form.FieldContainer',
        'Ext.grid.column.Number',
        'Ext.grid.column.Date',
        'Ext.grid.column.Boolean',
        'Ext.toolbar.Spacer'
    ],

    height: 755,
    id: 'almContainer',
    itemId: 'almContainer',
    width: 1000,
    layout: 'border',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'tabpanel',
                    region: 'center',
                    padding: '5 0 0 0',
                    style: 'background:#ffffff',
                    bodyPadding: '',
                    activeTab: 0,
                    plain: true,
                    layout: {
                        type: 'card',
                        deferredRender: false
                    },
                    items: [
                        {
                            xtype: 'panel',
                            layout: 'border',
                            title: 'User',
                            items: [
                                {
                                    xtype: 'gridpanel',
                                    flex: 1,
                                    region: 'center',
                                    id: 'almUserGrid',
                                    itemId: 'almUserGrid',
                                    minHeight: 100,
                                    autoScroll: true,
                                    columnLines: true,
                                    store: 'tempGridData',
                                    dockedItems: [
                                        {
                                            xtype: 'toolbar',
                                            dock: 'top',
                                            id: 'instancesToolbar2',
                                            itemId: 'instancesToolbar',
                                            width: 150,
                                            items: [
                                                {
                                                    xtype: 'button',
                                                    handler: function(button, e) {
                                                        almConstants.me.showAlmUserWindow();
                                                    },
                                                    id: 'createUserBtn1',
                                                    itemId: 'createUserBtn',
                                                    text: 'Create New User'
                                                },
                                                {
                                                    xtype: 'cycle',
                                                    id: 'actionCycle1',
                                                    itemId: 'actionCycle',
                                                    showText: true,
                                                    menu: {
                                                        xtype: 'menu',
                                                        id: 'actionList1',
                                                        itemId: 'actionList',
                                                        width: 120,
                                                        items: [
                                                            {
                                                                xtype: 'menucheckitem',
                                                                id: 'actions1',
                                                                itemId: 'actions',
                                                                text: 'Actions'
                                                            }
                                                        ]
                                                    }
                                                },
                                                {
                                                    xtype: 'tbseparator'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    id: 'inputUserName1',
                                                    itemId: 'inputUserName',
                                                    fieldLabel: 'Filtering',
                                                    labelWidth: 60,
                                                    emptyText: 'Search User Name'
                                                }
                                            ]
                                        }
                                    ],
                                    columns: [
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'id',
                                            text: 'Id'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'panel',
                                    flex: 1,
                                    region: 'south',
                                    split: true,
                                    height: 150,
                                    id: 'almUserDetailPanel',
                                    itemId: 'almUserDetailPanel',
                                    items: [
                                        {
                                            xtype: 'label',
                                            html: '<h2>&nbsp;&nbsp;Administrator</h2>',
                                            margin: '',
                                            padding: '0 10 0 0',
                                            text: ''
                                        },
                                        {
                                            xtype: 'form',
                                            id: 'almUserForm',
                                            itemId: 'almUserForm',
                                            padding: '',
                                            defaults: {
                                                border: false,
                                                xtype: 'panel',
                                                flex: 1,
                                                layout: 'anchor'
                                            },
                                            bodyPadding: 10,
                                            bodyStyle: 'padding:5px 5px 0',
                                            header: false,
                                            fieldDefaults: {
                                                msgTarget: 'side',
                                                margin: '0 10',
                                                readOnly: true
                                            },
                                            waitMsgTarget: 'instDescForm',
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
                                                            fieldLabel: 'User ID'
                                                        },
                                                        {
                                                            xtype: 'textfield',
                                                            fieldLabel: 'Dept Name'
                                                        }
                                                    ]
                                                },
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
                                                            fieldLabel: 'User Name'
                                                        },
                                                        {
                                                            xtype: 'textfield',
                                                            fieldLabel: 'Email'
                                                        }
                                                    ]
                                                },
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
                                                            fieldLabel: 'Is Active'
                                                        },
                                                        {
                                                            xtype: 'textfield',
                                                            fieldLabel: 'Assigned to'
                                                        }
                                                    ]
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            layout: 'border',
                            title: 'Group',
                            items: [
                                {
                                    xtype: 'gridpanel',
                                    flex: 1,
                                    region: 'center',
                                    id: 'almGroupGrid',
                                    itemId: 'almGroupGrid',
                                    minHeight: 100,
                                    autoScroll: true,
                                    columnLines: true,
                                    store: 'tempGridData',
                                    dockedItems: [
                                        {
                                            xtype: 'toolbar',
                                            dock: 'top',
                                            id: 'instancesToolbar3',
                                            itemId: 'instancesToolbar',
                                            width: 150,
                                            items: [
                                                {
                                                    xtype: 'button',
                                                    handler: function(button, e) {
                                                        almConstants.me.showAlmGroupWindow();
                                                    },
                                                    id: 'createUserBtn2',
                                                    itemId: 'createUserBtn',
                                                    text: 'Create New Group'
                                                },
                                                {
                                                    xtype: 'cycle',
                                                    id: 'actionCycle2',
                                                    itemId: 'actionCycle',
                                                    showText: true,
                                                    menu: {
                                                        xtype: 'menu',
                                                        id: 'actionList2',
                                                        itemId: 'actionList',
                                                        width: 120,
                                                        items: [
                                                            {
                                                                xtype: 'menucheckitem',
                                                                id: 'actions2',
                                                                itemId: 'actions',
                                                                text: 'Actions'
                                                            }
                                                        ]
                                                    }
                                                },
                                                {
                                                    xtype: 'tbseparator'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    id: 'inputUserName2',
                                                    itemId: 'inputUserName',
                                                    fieldLabel: 'Filtering',
                                                    labelWidth: 60,
                                                    emptyText: 'Search Group Name'
                                                }
                                            ]
                                        }
                                    ],
                                    columns: [
                                        {
                                            xtype: 'gridcolumn',
                                            dataIndex: 'id',
                                            text: 'Id'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'panel',
                                    flex: 1,
                                    region: 'south',
                                    split: true,
                                    height: 150,
                                    id: 'almGroupDetailPanel',
                                    itemId: 'almGroupDetailPanel',
                                    items: [
                                        {
                                            xtype: 'label',
                                            html: '<h2>&nbsp;&nbsp;Group: jira-administrator</h2>',
                                            margin: '',
                                            padding: '0 10 0 0',
                                            text: ''
                                        },
                                        {
                                            xtype: 'tabpanel',
                                            margin: '',
                                            padding: '0 10 10 10',
                                            style: 'background:#ffffff',
                                            bodyBorder: false,
                                            activeTab: 0,
                                            plain: true,
                                            items: [
                                                {
                                                    xtype: 'panel',
                                                    title: 'Summary',
                                                    items: [
                                                        {
                                                            xtype: 'form',
                                                            id: 'almGroupForm',
                                                            itemId: 'almGroupForm',
                                                            padding: '30 0 0 0',
                                                            defaults: {
                                                                border: false,
                                                                xtype: 'panel',
                                                                flex: 1,
                                                                layout: 'anchor'
                                                            },
                                                            bodyPadding: 10,
                                                            bodyStyle: 'padding:5px 5px 0',
                                                            header: false,
                                                            fieldDefaults: {
                                                                msgTarget: 'side',
                                                                margin: '0 10',
                                                                readOnly: true
                                                            },
                                                            waitMsgTarget: 'instDescForm',
                                                            items: [
                                                                {
                                                                    xtype: 'textfield',
                                                                    padding: '10 10 10 10',
                                                                    width: 600,
                                                                    fieldLabel: 'Group Directory',
                                                                    labelWidth: 150
                                                                },
                                                                {
                                                                    xtype: 'textfield',
                                                                    padding: '10 10 10 10',
                                                                    width: 600,
                                                                    fieldLabel: 'Users(in this group)',
                                                                    labelWidth: 150
                                                                },
                                                                {
                                                                    xtype: 'textfield',
                                                                    padding: '10 10 10 10',
                                                                    width: 600,
                                                                    fieldLabel: 'Creation Time',
                                                                    labelWidth: 150
                                                                }
                                                            ]
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'panel',
                                                    title: 'Users',
                                                    items: [
                                                        {
                                                            xtype: 'gridpanel',
                                                            id: 'almGroupUserGrid',
                                                            itemId: 'almGroupUserGrid',
                                                            columnLines: true,
                                                            columns: [
                                                                {
                                                                    xtype: 'gridcolumn',
                                                                    dataIndex: 'string',
                                                                    text: 'String'
                                                                },
                                                                {
                                                                    xtype: 'numbercolumn',
                                                                    dataIndex: 'number',
                                                                    text: 'Number'
                                                                },
                                                                {
                                                                    xtype: 'datecolumn',
                                                                    dataIndex: 'date',
                                                                    text: 'Date'
                                                                },
                                                                {
                                                                    xtype: 'booleancolumn',
                                                                    dataIndex: 'bool',
                                                                    text: 'Boolean'
                                                                }
                                                            ],
                                                            dockedItems: [
                                                                {
                                                                    xtype: 'toolbar',
                                                                    dock: 'top',
                                                                    items: [
                                                                        {
                                                                            xtype: 'tbspacer',
                                                                            flex: 1
                                                                        },
                                                                        {
                                                                            xtype: 'button',
                                                                            handler: function(button, e) {
                                                                                var almGroupUsersWindow = Ext.create("widget.almGroupUsersWindow");

                                                                                almGroupUsersWindow.show();
                                                                            },
                                                                            width: 150,
                                                                            text: 'Add Users to Group'
                                                                        }
                                                                    ]
                                                                }
                                                            ]
                                                        }
                                                    ]
                                                }
                                            ]
                                        }
                                    ]
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