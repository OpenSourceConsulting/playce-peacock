/*
 * File: app/store/FeedStore.js
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

Ext.define('MyApp.store.FeedStore', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.Feed'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.Feed',
            storeId: 'MyStore',
            data: [
                {
                    title: 'Sencha Blog',
                    url: 'http://feeds.feedburner.com/extblog'
                },
                {
                    title: 'Sencha Forums',
                    url: 'http://sencha.com/forum/external.php?type=RSS2'
                },
                {
                    title: 'Ajaxian',
                    url: 'http://feeds.feedburner.com/ajaxian'
                }
            ]
        }, cfg)]);
    }
});