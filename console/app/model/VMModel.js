/*
 * File: app/model/VMModel.js
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

Ext.define('MyApp.model.VMModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.Field'
    ],

    fields: [
        {
            name: 'hypervisorId',
            type: 'int'
        },
        {
            name: 'rhevmId',
            type: 'string'
        },
        {
            name: 'vmId',
            type: 'string'
        },
        {
            name: 'name',
            type: 'string'
        },
        {
            name: 'description',
            type: 'string'
        },
        {
            name: 'type',
            type: 'string'
        },
        {
            name: 'status',
            type: 'string'
        },
        {
            name: 'dataCenter',
            type: 'string'
        },
        {
            name: 'domain',
            type: 'string'
        },
        {
            name: 'cluster',
            type: 'string'
        },
        {
            name: 'host',
            type: 'string'
        },
        {
            name: 'os',
            type: 'string'
        },
        {
            name: 'template',
            type: 'string'
        },
        {
            name: 'memory',
            type: 'string'
        },
        {
            name: 'socket',
            type: 'int'
        },
        {
            name: 'cores',
            type: 'int'
        },
        {
            name: 'priority',
            type: 'string'
        },
        {
            name: 'origin',
            type: 'string'
        },
        {
            name: 'display',
            type: 'string'
        },
        {
            name: 'ipAddr',
            type: 'string'
        },
        {
            name: 'boot',
            type: 'string'
        },
        {
            name: 'startTime',
            type: 'string'
        },
        {
            name: 'creationTime',
            type: 'string'
        },
        {
            name: 'haEnabled',
            type: 'string'
        },
        {
            name: 'haPriority',
            type: 'string'
        }
    ]
});