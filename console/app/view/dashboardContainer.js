/*
 * File: app/view/dashboardContainer.js
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

Ext.define('MyApp.view.dashboardContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.dashboardcontainer',

    requires: [
        'Ext.grid.Panel',
        'Ext.grid.View',
        'Ext.grid.column.Number',
        'Ext.chart.Chart',
        'Ext.chart.axis.Category',
        'Ext.chart.axis.Numeric',
        'Ext.chart.series.Bar',
        'Ext.chart.Legend'
    ],

    height: 755,
    id: 'dashboardContainer',
    itemId: 'dashboardContainer',
    style: 'background-color:#ffffff;',
    width: 1000,
    autoScroll: true,

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'gridpanel',
                    flex: 2,
                    frame: true,
                    id: 'serverSummaryGrid',
                    itemId: 'serverSummaryGrid',
                    margin: '10 10 5 10',
                    minHeight: 150,
                    title: 'Server Summary',
                    columnLines: true,
                    forceFit: true,
                    store: 'DashboardServerStore',
                    columns: [
                        {
                            xtype: 'gridcolumn',
                            minWidth: 150,
                            dataIndex: 'rhevmName',
                            text: 'RHEV Manager'
                        },
                        {
                            xtype: 'gridcolumn',
                            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                metaData.tdAttr = 'style="cursor: pointer;color:#157fcc;font-weight: bold;"';
                                return value;
                            },
                            minWidth: 70,
                            dataIndex: 'vmCnt',
                            text: 'VMs (up)'
                        },
                        {
                            xtype: 'gridcolumn',
                            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                metaData.tdAttr = 'style="cursor: pointer;color:#157fcc;font-weight: bold;"';
                                return value;
                            },
                            minWidth: 70,
                            dataIndex: 'templateCnt',
                            text: 'Templates'
                        },
                        {
                            xtype: 'gridcolumn',
                            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                metaData.tdAttr = 'style="cursor: pointer;color:#157fcc;font-weight: bold;"';
                                return value;
                            },
                            minWidth: 110,
                            defaultWidth: 110,
                            dataIndex: 'agentCnt',
                            text: 'Agents (Running)'
                        },
                        {
                            xtype: 'gridcolumn',
                            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                metaData.tdAttr = 'style="cursor: pointer;color:#157fcc;font-weight: bold;"';
                                return value;
                            },
                            minWidth: 180,
                            dataIndex: 'criticalCnt',
                            text: 'Critical (CPU / Memory / Disk)'
                        },
                        {
                            xtype: 'gridcolumn',
                            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                metaData.tdAttr = 'style="cursor: pointer;color:#157fcc;font-weight: bold;"';
                                return value;
                            },
                            minWidth: 180,
                            dataIndex: 'warningCnt',
                            text: 'Warning (CPU / Memory / Disk)'
                        }
                    ]
                },
                {
                    xtype: 'gridpanel',
                    frame: true,
                    id: 'projectSummaryGrid',
                    itemId: 'projectSummaryGrid',
                    margin: '5 10 5 10',
                    maxHeight: 100,
                    title: 'Project Summary',
                    columnLines: true,
                    forceFit: true,
                    store: 'DashboardProjectStore',
                    columns: [
                        {
                            xtype: 'numbercolumn',
                            minWidth: 100,
                            dataIndex: 'projectSummary',
                            text: 'Project Summary',
                            format: '0,000'
                        },
                        {
                            xtype: 'numbercolumn',
                            minWidth: 100,
                            dataIndex: 'svnRepository',
                            text: 'SVN Repository',
                            format: '0,000'
                        },
                        {
                            xtype: 'numbercolumn',
                            minWidth: 100,
                            dataIndex: 'jenkinsJob',
                            text: 'Jenkins Job',
                            format: '0,000'
                        }
                    ],
                    viewConfig: {
                        autoScroll: false
                    }
                },
                {
                    xtype: 'gridpanel',
                    frame: true,
                    id: 'swSummaryGrid',
                    itemId: 'swSummaryGrid',
                    margin: '5 10 5 10',
                    maxHeight: 100,
                    title: 'S/W Summary',
                    columnLines: true,
                    forceFit: true,
                    store: 'DashboardSwStore',
                    columns: [
                        {
                            xtype: 'numbercolumn',
                            minWidth: 100,
                            dataIndex: 'ewsHttpd',
                            text: 'JBoss EWS(Httpd)',
                            format: '0,000'
                        },
                        {
                            xtype: 'numbercolumn',
                            minWidth: 100,
                            dataIndex: 'ewsTomcat',
                            text: 'JBoss EWS(Tomcat)',
                            format: '0,000'
                        },
                        {
                            xtype: 'numbercolumn',
                            minWidth: 100,
                            dataIndex: 'eap',
                            text: 'JBoss  EAP',
                            format: '0,000'
                        }
                    ],
                    viewConfig: {
                        autoScroll: false
                    }
                },
                {
                    xtype: 'panel',
                    flex: 5,
                    frame: true,
                    margin: '5 10 10 10',
                    minHeight: 300,
                    padding: 10,
                    header: false,
                    title: 'My Panel',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'panel',
                            flex: 1,
                            header: false,
                            title: 'My Panel',
                            layout: {
                                type: 'hbox',
                                align: 'stretch'
                            },
                            items: [
                                {
                                    xtype: 'chart',
                                    flex: 1,
                                    height: 250,
                                    html: 'Top 5 Servers by % CPU Used',
                                    margin: '10 0 10 0',
                                    minWidth: 300,
                                    padding: '',
                                    style: 'font-size: 14px;font-weight: bold;text-align: center;',
                                    width: 356,
                                    shadow: false,
                                    animate: true,
                                    insetPadding: 20,
                                    store: 'DashboardCpuChartStore',
                                    axes: [
                                        {
                                            type: 'Category',
                                            fields: [
                                                'instanceName'
                                            ],
                                            label: {
                                                renderer: function(v) {
                                                    if(v == null) {
                                                        return v;
                                                    }
                                                    
                                                    if(v.legnth > 13) {
                                                        return v.substring(0, 13) + "..";
                                                    } else {
                                                        return v;
                                                    }
                                                }
                                            },
                                            position: 'left'
                                        },
                                        {
                                            type: 'Numeric',
                                            fields: [
                                                'cpuUsed',
                                                'cpuFree'
                                            ],
                                            position: 'bottom',
                                            maximum: 100,
                                            minimum: 0
                                        }
                                    ],
                                    series: [
                                        {
                                            type: 'bar',
                                            label: {
                                                display: 'outside',
                                                color: '#333',
                                                'text-anchor': 'middle',
                                                field: [
                                                    'cpuUsed',
                                                    'cpuFree'
                                                ]
                                            },
                                            title: [
                                                'Used CPU',
                                                'Free CPU'
                                            ],
                                            axis: 'bottom',
                                            xField: 'instanceName',
                                            yField: [
                                                'cpuUsed',
                                                'cpuFree'
                                            ],
                                            stacked: false
                                        }
                                    ],
                                    legend: {

                                    }
                                },
                                {
                                    xtype: 'chart',
                                    flex: 1,
                                    height: 250,
                                    html: 'Top 5 Servers by % Memory Used',
                                    margin: '10 0 10 0',
                                    minWidth: 300,
                                    padding: '',
                                    style: 'font-size: 14px;font-weight: bold;text-align: center;',
                                    width: 356,
                                    shadow: false,
                                    animate: true,
                                    insetPadding: 20,
                                    store: 'DashboardMemoryChartStore',
                                    axes: [
                                        {
                                            type: 'Category',
                                            fields: [
                                                'instanceName'
                                            ],
                                            label: {
                                                renderer: function(v) {
                                                    if(v == null) {
                                                        return v;
                                                    }
                                                    
                                                    if(v.legnth > 13) {
                                                        return v.substring(0, 13) + "..";
                                                    } else {
                                                        return v;
                                                    }
                                                }
                                            },
                                            position: 'left'
                                        },
                                        {
                                            type: 'Numeric',
                                            fields: [
                                                'memoryUsed',
                                                'memoryFree'
                                            ],
                                            label: {
                                                renderer: function(v) {
                                                    return String(v);
                                                }
                                            },
                                            position: 'bottom',
                                            maximum: 100,
                                            minimum: 0
                                        }
                                    ],
                                    series: [
                                        {
                                            type: 'bar',
                                            label: {
                                                display: 'outside',
                                                color: '#333',
                                                'text-anchor': 'middle',
                                                field: [
                                                    'memoryUsed',
                                                    'memoryFree'
                                                ]
                                            },
                                            title: [
                                                'Used Memory',
                                                'Free Memory'
                                            ],
                                            axis: 'bottom',
                                            xField: 'instanceName',
                                            yField: [
                                                'memoryUsed',
                                                'memoryFree'
                                            ],
                                            stacked: false
                                        }
                                    ],
                                    legend: {

                                    }
                                },
                                {
                                    xtype: 'chart',
                                    flex: 1,
                                    height: 250,
                                    html: 'Top 5 Servers by % Storage Used',
                                    margin: '10 0 10 0',
                                    minWidth: 300,
                                    padding: '',
                                    style: 'font-size: 14px;font-weight: bold;text-align: center;',
                                    width: 356,
                                    shadow: false,
                                    animate: true,
                                    insetPadding: 20,
                                    store: 'DashboardDiskChartStore',
                                    axes: [
                                        {
                                            type: 'Category',
                                            fields: [
                                                'instanceName'
                                            ],
                                            label: {
                                                renderer: function(v) {
                                                    if(v == null) {
                                                        return v;
                                                    }
                                                    
                                                    if(v.legnth > 13) {
                                                        return v.substring(0, 13) + "..";
                                                    } else {
                                                        return v;
                                                    }
                                                }
                                            },
                                            position: 'left'
                                        },
                                        {
                                            type: 'Numeric',
                                            fields: [
                                                'storageUsed',
                                                'storageFree'
                                            ],
                                            label: {
                                                renderer: function(v) {
                                                    return String(v);
                                                }
                                            },
                                            position: 'bottom',
                                            maximum: 100,
                                            minimum: 0
                                        }
                                    ],
                                    series: [
                                        {
                                            type: 'bar',
                                            label: {
                                                display: 'outside',
                                                color: '#333',
                                                'text-anchor': 'middle',
                                                field: [
                                                    'storageUsed',
                                                    'storageFree'
                                                ]
                                            },
                                            title: [
                                                'Used Storage',
                                                'Free Storage'
                                            ],
                                            axis: 'bottom',
                                            xField: 'instanceName',
                                            yField: [
                                                'storageUsed',
                                                'storageFree'
                                            ],
                                            stacked: false
                                        }
                                    ],
                                    legend: {

                                    }
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