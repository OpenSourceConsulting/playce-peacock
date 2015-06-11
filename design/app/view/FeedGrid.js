/*
 * File: app/view/FeedGrid.js
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

Ext.define('MyApp.view.FeedGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.feedgrid',

    requires: [
        'Ext.toolbar.Toolbar',
        'Ext.toolbar.Separator',
        'Ext.button.Cycle',
        'Ext.menu.Menu',
        'Ext.menu.CheckItem',
        'Ext.grid.column.Column',
        'Ext.grid.View'
    ],

    cls: 'feed-grid',
    store: 'FeedItemStore',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    cls: 'x-docked-noborder-top',
                    items: [
                        {
                            xtype: 'button',
                            itemId: 'openall',
                            iconCls: 'open-all',
                            text: 'Open All'
                        },
                        {
                            xtype: 'tbseparator'
                        },
                        {
                            xtype: 'cycle',
                            itemId: 'readingpane',
                            text: 'Reading Pane',
                            prependText: 'Preview: ',
                            showText: true,
                            menu: {
                                xtype: 'menu',
                                id: 'reading-menu',
                                items: [
                                    {
                                        xtype: 'menucheckitem',
                                        iconCls: 'preview-bottom',
                                        text: 'Bottom'
                                    },
                                    {
                                        xtype: 'menucheckitem',
                                        iconCls: 'preview-right',
                                        text: 'Right'
                                    },
                                    {
                                        xtype: 'menucheckitem',
                                        iconCls: 'preview-hide',
                                        text: 'Hide'
                                    }
                                ]
                            }
                        },
                        {
                            xtype: 'button',
                            itemId: 'summary',
                            enableToggle: true,
                            iconCls: 'summary',
                            pressed: true,
                            text: 'Summary'
                        }
                    ]
                }
            ],
            columns: [
                {
                    xtype: 'gridcolumn',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        return Ext.String.format('<div class="topic"><b>{0}</b><span class="author">{1}</span></div>', value, record.get('author') || "Unknown");
                    },
                    dataIndex: 'title',
                    text: 'Title',
                    flex: 1
                },
                {
                    xtype: 'gridcolumn',
                    hidden: true,
                    width: 200,
                    dataIndex: 'author',
                    text: 'Author'
                },
                {
                    xtype: 'gridcolumn',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        if (!value) {
                            return '';
                        }

                        var now = new Date(), d = Ext.Date.clearTime(now, true), notime = Ext.Date.clearTime(value, true).getTime();

                        if (notime === d.getTime()) {
                            return 'Today ' + Ext.Date.format(value, 'g:i a');
                        }

                        d = Ext.Date.add(d, 'd', -6);
                        if (d.getTime() <= notime) {
                            return Ext.Date.format(value, 'D g:i a');
                        }

                        return Ext.Date.format(value, 'Y/m/d g:i a');
                    },
                    width: 200,
                    dataIndex: 'pubDate',
                    text: 'Date'
                }
            ],
            viewConfig: {
                itemId: 'view'
            }
        });

        me.processFeedGrid(me);
        me.callParent(arguments);
    },

    processFeedGrid: function(config) {
        config.viewConfig = {
            itemId: 'view',
            plugins: [{
                pluginId: 'preview',
                ptype: 'preview',
                bodyField: 'description',
                expanded: true
            }]
        };
    },

    loadFeed: function(url) {
        var store = this.store;
        store.getProxy().extraParams.feed = url;
        store.load();
    }

});