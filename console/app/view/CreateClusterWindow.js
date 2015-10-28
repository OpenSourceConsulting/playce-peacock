/*
 * File: app/view/CreateClusterWindow.js
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

Ext.define('MyApp.view.CreateClusterWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.CreateClusterWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.field.Number',
        'Ext.form.field.ComboBox',
        'Ext.grid.Panel',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
        'Ext.grid.View',
        'Ext.grid.column.Column'
    ],

    height: 800,
    width: 1000,
    resizable: false,
    title: 'Input Environment',
    modal: true,

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    flex: 2,
                    id: 'GeneralConfigurationPanel',
                    itemId: 'GeneralConfigurationPanel',
                    minHeight: 168,
                    title: 'General Configuration',
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'form',
                            flex: 1,
                            id: 'GeneralConfigurationFormPanel1',
                            itemId: 'GeneralConfigurationFormPanel1',
                            bodyPadding: 10,
                            items: [
                                {
                                    xtype: 'numberfield',
                                    anchor: '100%',
                                    id: 'clusterReplicaSize',
                                    itemId: 'clusterReplicaSize',
                                    fieldLabel: 'Replica Size',
                                    labelAlign: 'right',
                                    labelWidth: 142,
                                    name: 'replicaSize',
                                    allowBlank: false,
                                    allowOnlyWhitespace: false,
                                    enforceMaxLength: true,
                                    maxLength: 6,
                                    allowDecimals: false,
                                    allowExponential: false,
                                    maxValue: 32,
                                    minValue: 2
                                },
                                {
                                    xtype: 'combobox',
                                    anchor: '100%',
                                    id: 'clusterPgNum',
                                    itemId: 'clusterPgNum',
                                    fieldLabel: 'PG Num',
                                    labelAlign: 'right',
                                    labelWidth: 142,
                                    name: 'pgNum',
                                    maxLength: 32,
                                    displayField: 'value',
                                    store: 'PgNumComboArrayStore',
                                    valueField: 'value'
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    id: 'clusterPublicNetwork',
                                    itemId: 'clusterPublicNetwork',
                                    fieldLabel: 'Public Network',
                                    labelAlign: 'right',
                                    labelWidth: 142,
                                    name: 'publicNetwork',
                                    allowBlank: false,
                                    allowOnlyWhitespace: false,
                                    maskRe: /[0-9\/\.]/
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    id: 'clusterClusterNetwork',
                                    itemId: 'clusterClusterNetwork',
                                    fieldLabel: 'Cluster Network',
                                    labelAlign: 'right',
                                    labelWidth: 142,
                                    name: 'clusterNetwork',
                                    allowBlank: false,
                                    allowOnlyWhitespace: false,
                                    maskRe: /[0-9\/\.]/
                                }
                            ]
                        },
                        {
                            xtype: 'form',
                            flex: 1,
                            id: 'GeneralConfigurationFormPanel2',
                            itemId: 'GeneralConfigurationFormPanel2',
                            bodyPadding: 10,
                            items: [
                                {
                                    xtype: 'numberfield',
                                    anchor: '100%',
                                    id: 'clusterJournalSize',
                                    itemId: 'clusterJournalSize',
                                    fieldLabel: 'Journal Size',
                                    labelAlign: 'right',
                                    labelWidth: 142,
                                    name: 'journalSize',
                                    allowBlank: false,
                                    allowOnlyWhitespace: false,
                                    enforceMaxLength: true,
                                    maxLength: 14,
                                    allowDecimals: false,
                                    allowExponential: false,
                                    maxValue: 10737418240,
                                    minValue: 2
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    id: 'clusterMonNetworkInterface',
                                    itemId: 'clusterMonNetworkInterface',
                                    fieldLabel: 'mon Network Interface',
                                    labelAlign: 'right',
                                    labelWidth: 142,
                                    name: 'monNetworkInterface',
                                    allowBlank: false,
                                    allowOnlyWhitespace: false,
                                    maskRe: /[A-Za-z0-9_\-]/
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    id: 'clusterFileSystem',
                                    itemId: 'clusterFileSystem',
                                    fieldLabel: 'File System',
                                    labelAlign: 'right',
                                    labelWidth: 142,
                                    name: 'fileSystem',
                                    allowBlank: false,
                                    allowOnlyWhitespace: false,
                                    maskRe: /[a-zA-Z0-9_\-\/\.]/
                                },
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    id: 'clusterNtpServer',
                                    itemId: 'clusterNtpServer',
                                    fieldLabel: 'Ntp Server',
                                    labelAlign: 'right',
                                    labelWidth: 142,
                                    name: 'ntpServer',
                                    allowBlank: false,
                                    allowOnlyWhitespace: false,
                                    maskRe: /[a-zA-Z0-9_\-\/\.]/
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    flex: 5,
                    id: 'ClusterServersPanel',
                    itemId: 'ClusterServersPanel',
                    layout: 'border',
                    title: 'Cluster Servers',
                    items: [
                        {
                            xtype: 'gridpanel',
                            region: 'center',
                            id: 'ClusterServersGrid',
                            itemId: 'ClusterServersGrid',
                            columnLines: true,
                            forceFit: true,
                            store: 'storageClusterServersArrayStore',
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    height: 32,
                                    id: 'ClusterServersToolbar',
                                    itemId: 'ClusterServersToolbar',
                                    items: [
                                        {
                                            xtype: 'button',
                                            id: 'clusterAddServer',
                                            itemId: 'clusterAddServer',
                                            width: 100,
                                            text: 'Add Server'
                                        }
                                    ]
                                }
                            ],
                            viewConfig: {
                                id: 'ClusterServersGridView',
                                itemId: 'ClusterServersGridView'
                            },
                            columns: [
                                {
                                    xtype: 'gridcolumn',
                                    align: 'center',
                                    dataIndex: 'hostname',
                                    text: 'Hostname'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    align: 'center',
                                    dataIndex: 'ip',
                                    text: 'Ip'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    align: 'center',
                                    dataIndex: 'type',
                                    text: 'Type'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    align: 'center',
                                    dataIndex: 'username',
                                    text: 'User'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                        var s = "";
                                        for (var i = 0; i < value.length; i++) s = s + "*";

                                        return s;

                                    },
                                    align: 'center',
                                    dataIndex: 'password',
                                    text: 'Password'
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    flex: 3,
                    id: 'OsdDevicePathPanel',
                    itemId: 'OsdDevicePathPanel',
                    layout: 'border',
                    title: 'Osd Device Path',
                    items: [
                        {
                            xtype: 'gridpanel',
                            flex: 1,
                            region: 'center',
                            id: 'OsdDevicePathGrid',
                            itemId: 'OsdDevicePathGrid',
                            columnLines: true,
                            forceFit: true,
                            hideHeaders: true,
                            store: 'storageOsdDevicePathArrayStore',
                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'top',
                                    height: 32,
                                    id: 'OsdDevicePathToolbar',
                                    itemId: 'OsdDevicePathToolbar',
                                    items: [
                                        {
                                            xtype: 'button',
                                            id: 'clusterAddPath',
                                            itemId: 'clusterAddPath',
                                            width: 100,
                                            text: 'Add Path'
                                        }
                                    ]
                                }
                            ],
                            viewConfig: {
                                border: 1,
                                id: 'OsdDevicePathGridView',
                                itemId: 'OsdDevicePathGridView'
                            },
                            columns: [
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'path',
                                    text: 'Path'
                                }
                            ]
                        }
                    ]
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    flex: 1,
                    dock: 'bottom',
                    height: 32,
                    id: 'CreateClusterToolbar',
                    itemId: 'CreateClusterToolbar',
                    layout: {
                        type: 'hbox',
                        pack: 'center'
                    },
                    items: [
                        {
                            xtype: 'button',
                            id: 'createCluster',
                            itemId: 'createCluster',
                            width: 120,
                            text: 'Create Cluster'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});