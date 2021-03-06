/*
 * File: app/view/objectContainer.js
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

Ext.define('MyApp.view.objectContainer', {
    extend: 'Ext.container.Container',
    alias: 'widget.objectContainer',

    requires: [
        'MyApp.view.objectBucketsContainer',
        'MyApp.view.objectFilesContainer',
        'Ext.container.Container'
    ],

    height: 750,
    id: 'objectContainer',
    itemId: 'objectContainer',
    width: 1000,
    layout: 'border',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'container',
                    region: 'center',
                    id: 'objectCenterContainer',
                    itemId: 'objectCenterContainer',
                    layout: 'card',
                    items: [
                        {
                            xtype: 'objectBucketsContainer'
                        },
                        {
                            xtype: 'objectFilesContainer'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});