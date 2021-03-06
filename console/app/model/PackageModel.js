/*
 * File: app/model/PackageModel.js
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

Ext.define('MyApp.model.PackageModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.Field'
    ],

    fields: [
        {
            name: 'pkgId',
            type: 'int'
        },
        {
            name: 'machineId',
            type: 'string'
        },
        {
            name: 'name',
            type: 'string'
        },
        {
            name: 'arch',
            type: 'string'
        },
        {
            name: 'size',
            type: 'string'
        },
        {
            name: 'version',
            type: 'string'
        },
        {
            name: 'releaseInfo',
            type: 'string'
        },
        {
            name: 'installDate',
            type: 'string'
        },
        {
            name: 'summary',
            type: 'string'
        },
        {
            name: 'description',
            type: 'string'
        },
        {
            name: 'regUserId',
            type: 'int'
        },
        {
            name: 'regDt',
            type: 'string'
        },
        {
            name: 'updUserId',
            type: 'int'
        },
        {
            name: 'updDt',
            type: 'string'
        }
    ]
});