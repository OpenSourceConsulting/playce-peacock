/*
 * File: app/model/SessionModel.js
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

Ext.define('MyApp.model.SessionModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.Field'
    ],

    fields: [
        {
            name: 'userId',
            type: 'int'
        },
        {
            name: 'loginId',
            type: 'string'
        },
        {
            name: 'userName',
            type: 'string'
        },
        {
            name: 'passwd',
            type: 'string'
        },
        {
            name: 'deptName',
            type: 'string'
        },
        {
            name: 'email',
            type: 'string'
        },
        {
            name: 'lastLogon',
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
        },
        {
            name: 'authorities',
            type: 'auto'
        }
    ]
});