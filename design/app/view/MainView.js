/*
 * File: app/view/MainView.js
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

Ext.define('MyApp.view.MainView', {
    extend: 'Ext.container.Viewport',
    alias: 'widget.mainview',

    requires: [
        'Ext.grid.Panel',
        'Ext.grid.View',
        'Ext.grid.column.Column',
        'Ext.XTemplate',
        'Ext.chart.Chart',
        'Ext.chart.axis.Category',
        'Ext.chart.axis.Numeric',
        'Ext.chart.series.Column'
    ],

    itemId: 'mainView',
    layout: 'border',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    region: 'west',
                    split: true,
                    itemId: 'leftPanel',
                    width: 500,
                    title: 'Car Listings',
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'gridpanel',
                            flex: 1,
                            itemId: 'carGrid',
                            store: 'CarDataStore',
                            columns: [
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'manufacturer',
                                    text: 'Manufacturer'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'model',
                                    text: 'Model'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'price',
                                    text: 'Price'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'wiki',
                                    text: 'Wiki'
                                },
                                {
                                    xtype: 'gridcolumn',
                                    dataIndex: 'img',
                                    text: 'Img'
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    region: 'center',
                    itemId: 'centerPanel',
                    layout: 'border',
                    items: [
                        {
                            xtype: 'panel',
                            flex: 1,
                            region: 'center',
                            split: true,
                            itemId: 'detailPanel',
                            tpl: [
                                '<div style="padding: 10px">',
                                '    <img src="data/{img}" style="float: right; border: 1px solid #999" />',
                                '    <b>Manufacturer</b>: {manufacturer}<br>',
                                '    <b>Model</b>: <a href="{wiki}" target="_blank">{model}</a><br>',
                                '    <b>Price</b>: {price:usMoney}<br>',
                                '</div>'
                            ],
                            layout: 'fit',
                            title: 'Details'
                        },
                        {
                            xtype: 'panel',
                            flex: 1,
                            region: 'south',
                            split: true,
                            height: 150,
                            itemId: 'chartPanel',
                            layout: 'fit',
                            title: 'Data',
                            items: [
                                {
                                    xtype: 'chart',
                                    itemId: 'qualityChart',
                                    animate: true,
                                    insetPadding: 20,
                                    store: 'CarChartStore',
                                    axes: [
                                        {
                                            type: 'Category',
                                            fields: [
                                                'name'
                                            ],
                                            title: 'Quality',
                                            position: 'bottom'
                                        },
                                        {
                                            type: 'Numeric',
                                            fields: [
                                                'rating'
                                            ],
                                            majorTickSteps: 4,
                                            position: 'bottom',
                                            title: 'Score',
                                            maximum: 5,
                                            minimum: 0
                                        }
                                    ],
                                    series: [
                                        {
                                            type: 'column',
                                            label: {
                                                display: 'insideEnd',
                                                field: 'rating',
                                                color: '#333',
                                                'text-anchor': 'middle'
                                            },
                                            xField: 'name',
                                            yField: 'rating'
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