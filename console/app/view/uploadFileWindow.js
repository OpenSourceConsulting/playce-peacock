/*
 * File: app/view/uploadFileWindow.js
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

Ext.define('MyApp.view.uploadFileWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.uploadFileWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.FieldSet',
        'Ext.form.field.File',
        'Ext.form.field.Hidden',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],

    height: 172,
    width: 520,
    resizable: false,
    layout: 'border',
    title: 'Upload',
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                    region: 'center',
                    id: 'uploadFileTopPanel',
                    itemId: 'uploadFileTopPanel',
                    items: [
                        {
                            xtype: 'form',
                            height: 156,
                            id: 'uploadFileFormPanel',
                            itemId: 'uploadFileFormPanel',
                            bodyPadding: 10,
                            items: [
                                {
                                    xtype: 'fieldset',
                                    padding: 10,
                                    title: 'Upload File ',
                                    items: [
                                        {
                                            xtype: 'filefield',
                                            anchor: '100%',
                                            id: 'uploadFileName',
                                            itemId: 'uploadFileName',
                                            fieldLabel: 'File Name',
                                            labelAlign: 'right',
                                            labelWidth: 68,
                                            name: 'file',
                                            allowBlank: false,
                                            enforceMaxLength: false
                                        },
                                        {
                                            xtype: 'hiddenfield',
                                            anchor: '100%',
                                            id: 'uploadBucketName',
                                            itemId: 'uploadBucketName',
                                            fieldLabel: 'Label',
                                            name: 'bucketName'
                                        },
                                        {
                                            xtype: 'hiddenfield',
                                            anchor: '100%',
                                            id: 'uploadParentPath',
                                            itemId: 'uploadParentPath',
                                            fieldLabel: 'Label',
                                            name: 'parentPath'
                                        }
                                    ]
                                }
                            ]
                        }
                    ],
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'bottom',
                            id: 'uploadFileToolbar',
                            itemId: 'uploadFileToolbar',
                            layout: {
                                type: 'hbox',
                                pack: 'center'
                            },
                            items: [
                                {
                                    xtype: 'button',
                                    handler: function(button, e) {
                                        var myForm = Ext.getCmp("uploadFileFormPanel");
                                        myForm.getForm().findField('uploadBucketName').setValue(objectConstants.currentBucket);
                                        myForm.getForm().findField('uploadParentPath').setValue(objectConstants.currentFolder);

                                        if(myForm.isValid()){
                                            myForm.getForm().submit({
                                                url: GLOBAL.urlPrefix + "ceph/object/object",
                                                method: 'POST',
                                                waitMsg : 'Uploading file...',
                                                success: function(fp, res){
                                                    var data = Ext.decode(res.response.responseText);
                                                    Ext.Msg.show({
                                                        title:'Information',
                                                        msg: 'File Upload Complete.',
                                                        buttons: Ext.Msg.OK,
                                                        icon: Ext.Msg.INFO
                                                    });

                                                    myForm.up('window').close();
                                                    objectConstants.me.setObjectFilesData();
                                                },
                                                failure: function(response){
                                                    Ext.Msg.show({
                                                        title:'Error',
                                                        msg: 'Error on Upload File.',
                                                        buttons: Ext.Msg.OK,
                                                        icon: Ext.Msg.ERROR
                                                    });

                                                    myForm.up('window').close();
                                                }
                                            });
                                        } else {
                                            Ext.MessageBox.alert('알림', '유효한 파일을 선택하세요.');
                                        }

                                    },
                                    padding: '2 20 2 20',
                                    text: 'Ok'
                                },
                                {
                                    xtype: 'button',
                                    handler: function(button, e) {
                                        var myForm = Ext.getCmp("uploadFileFormPanel");

                                        myForm.getForm().findField("uploadFileName").reset();

                                    },
                                    padding: '2 12 2 12',
                                    text: 'Clear'
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