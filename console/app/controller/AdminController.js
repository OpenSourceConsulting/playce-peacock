/*
 * File: app/controller/AdminController.js
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

Ext.define('MyApp.controller.AdminController', {
    extend: 'Ext.app.Controller',

    onSearchUserNameKeydown: function(textfield, e, eOpts) {
        //User Name Search
        if(e.getKey() == e.ENTER){
            this.searchUser();
        }
    },

    onUserGridSelect: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        //User Grid Item Click
        if(cellIndex > 5) {
            //Action Column 예외처리
            return;
        }

        if(userConstants.selectRow == null || userConstants.selectRow.get("userId") != record.get("userId")) {

            userConstants.selectRow = record;

            this.searchUserDetail();
        }
    },

    onUserGridBeforeItemContextMenu: function(dataview, record, item, index, e, eOpts) {
        //User Grid Right Click Menu 호출

        var position = e.getXY();
        e.stopEvent();

        userConstants.actionRow = record;

        userConstants.contextMenu.showAt(position);
    },

    onAdminTabPanelTabChange: function(tabPanel, newCard, oldCard, eOpts) {
        if(newCard.title == "User"){

            this.searchUser(true);

        } else {

            this.searchUserPermission(true);

        }
    },

    onUserPermissionGridCellClick: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        //User Grid Item Click
        if(cellIndex > 3) {
            //Action Column 예외처리
            return;
        }

        if(userConstants.selectRow == null || userConstants.selectRow.get("permId") != record.get("permId")) {

            userConstants.selectRow = record;

            //User 상세 조회

            var detailPanel = Ext.getCmp("userPermissionDetailPanel");
            detailPanel.layout.setActiveItem(1);

            this.searchUserPermissionDetail(0);
        }
    },

    onSearchUserPermissionNameKeydown: function(textfield, e, eOpts) {
        //User Name Search
        if(e.getKey() == e.ENTER){
            this.searchUserPermission();
        }
    },

    onTabpanelTabChange: function(tabPanel, newCard, oldCard, eOpts) {
        if(newCard.title == 'Detail') {

            this.searchUserPermissionDetail(0);

        } else {

            Ext.getCmp("searchPermissionUserName").setValue("");

            this.searchUserPermissionDetail(1);

        }
    },

    onSearchPermissionUserNameKeydown: function(textfield, e, eOpts) {
        //User Name Search
        if(e.getKey() == e.ENTER){
            this.searchUserPermissionDetail(1);
        }
    },

    onSearchPopUserNameKeydown: function(textfield, e, eOpts) {
        //User Name Search
        if(e.getKey() == e.ENTER){
            this.searchPopAllUser();
        }
    },

    searchUser: function(init) {
        //User Grid Data Search

        if(init) {

            Ext.getCmp("searchUserName").setValue("");
            Ext.getCmp("userGrid").reconfigure(Ext.getCmp("userGrid").store, Ext.getCmp("userGrid").initialConfig.columns);

        }

        userConstants.selectRow = null;

        Ext.getCmp("userGrid").getStore().load({
            params:{
                search : Ext.getCmp("searchUserName").getRawValue()
            }
        });

        var detailPanel = Ext.getCmp("userDetailPanel");
        detailPanel.layout.setActiveItem(0);
    },

    searchUserPermission: function(init) {
        //User Grid Data Search

        if(init) {

            Ext.getCmp("searchUserPermissionName").setValue("");
            Ext.getCmp("userPermissionGrid").reconfigure(Ext.getCmp("userPermissionGrid").store, Ext.getCmp("userPermissionGrid").initialConfig.columns);

        }

        userConstants.selectRow = null;

        var permissionStore = Ext.getCmp('userPermissionGrid').getStore();

        permissionStore.getProxy().extraParams = {
            search : Ext.getCmp("searchUserPermissionName").getRawValue()
        };

        permissionStore.load();

        var detailPanel = Ext.getCmp("userPermissionDetailPanel");
        detailPanel.layout.setActiveItem(0);
    },

    init: function(application) {
                //User Menu Config Setting

                var user = this;

                var userGridContextMenu = new Ext.menu.Menu({
                    items:
                    [
                    { text: 'Edit',
                        handler: function() {
                            user.showUserWindow('edit');
                        }
                    },
                    { text: 'Delete',
                        handler: function() {
                            user.deleteUser();
                        }
                    }
                    ]

                });

                //User Menu Constants
                Ext.define('userConstants', {
                    singleton: true,
                    me : user,
                    contextMenu: userGridContextMenu,
                    selectRow : null,
                    actionRow : null
                });


        this.control({
            "#searchUserName": {
                keydown: this.onSearchUserNameKeydown
            },
            "#userGrid": {
                cellclick: this.onUserGridSelect,
                beforeitemcontextmenu: this.onUserGridBeforeItemContextMenu
            },
            "#adminTabPanel": {
                tabchange: this.onAdminTabPanelTabChange
            },
            "#userPermissionGrid": {
                cellclick: this.onUserPermissionGridCellClick
            },
            "#searchUserPermissionName": {
                keydown: this.onSearchUserPermissionNameKeydown
            },
            "#userPermissionDetailTabPanel": {
                tabchange: this.onTabpanelTabChange
            },
            "#searchPermissionUserName": {
                keydown: this.onSearchPermissionUserNameKeydown
            },
            "#searchPopUserName": {
                keydown: this.onSearchPopUserNameKeydown
            }
        });
    },

    showUserWindow: function(type, user_id) {

        //User Popup 호출

        var userWindow = Ext.create("widget.userWindow");

        userWindow.show();

        var editUrl = GLOBAL.urlPrefix + "user/getMyAccount";

        if(type != 'new') {

            userWindow.setTitle("Edit User");

            if(user_id == null) {
                user_id = userConstants.actionRow.get("userId");

                editUrl = GLOBAL.urlPrefix + "user/getUser";
            }

            var userForm = Ext.getCmp("userForm");

            userForm.getForm().waitMsgTarget = userForm.getEl();

            userForm.getForm().load({
                params : {
                    userId : user_id
                }
                ,url : editUrl
                ,waitMsg:'Loading...'
                ,success: function(form, action) {

                    var password = form.findField('passwd').getValue();

                    form.findField('confirmPasswd').setRawValue(password);
                    form.findField('editType').setRawValue(type);
                }
            });
        }


    },

    deleteUser: function() {

        //User 삭제

        Ext.MessageBox.confirm('Confirm', '삭제 하시겠습니까?', function(btn){

            if(btn == "yes"){

                Ext.Ajax.request({
                    url: GLOBAL.urlPrefix + "user/delete",
                    params : {
                        userId : userConstants.actionRow.get("userId")
                    },
                    disableCaching : true,
                    waitMsg: 'Delete User...',
                    success: function(response){
                        var msg = Ext.JSON.decode(response.responseText).msg;
                        Ext.MessageBox.alert('알림', msg);

                        userConstants.selectRow = null;

                        Ext.getCmp("userGrid").getStore().reload();
                        Ext.getCmp("userDetailPanel").layout.setActiveItem(0);

                    }
                });
            }

        });
    },

    deleteUserPermission: function() {

        //User Permission 삭제

        Ext.MessageBox.confirm('Confirm', '삭제 하시겠습니까?', function(btn){

            if(btn == "yes"){

                Ext.Ajax.request({
                    url: GLOBAL.urlPrefix + "permission/delete",
                    params : {
                        permId : userConstants.actionRow.get("permId")
                    },
                    disableCaching : true,
                    waitMsg: 'Delete User Permission...',
                    success: function(response){
                        var msg = Ext.JSON.decode(response.responseText).msg;
                        Ext.MessageBox.alert('알림', msg);

                        userConstants.selectRow = null;

                        Ext.getCmp("userPermissionGrid").getStore().reload();
                        Ext.getCmp("userPermissionDetailPanel").layout.setActiveItem(0);

                    }
                });
            }

        });
    },

    searchUserDetail: function() {

        //User 상세 조회

        var userDetailPanel = Ext.getCmp("userDetailPanel");
        userDetailPanel.layout.setActiveItem(1);

        //User Data Loading

        var userForm = Ext.getCmp("getUserForm");

        userForm.getForm().reset();

        userForm.getForm().waitMsgTarget = userForm.getEl();

        userForm.getForm().load({
            params : {
                userId : userConstants.selectRow.get("userId")
            }
            ,url : GLOBAL.urlPrefix + "user/getUser"
            ,waitMsg:'Loading...'
        });

        Ext.getCmp("userTitleLabel").setText("<h2>&nbsp;&nbsp;&nbsp;"+userConstants.selectRow.get("userName")+"</h2>", false);
    },

    changeMenuAuth: function(grid, field, rowIndex, checked) {
        var view = grid.getView(),
            node = view.getNode(rowIndex),
            record = view.getRecord(node),
            thread = record.get('thread');


        var allRecords = grid.getRecords();

        if(thread.length == 2) {

            Ext.each(allRecords, function (rec) {

                if(rec.get('thread').substring(0,2) == thread) {
                    rec.set(field, checked);
                    if(field == 'isWrite' && checked == true) {
                        rec.set("isRead", checked);
                    }
                }
            });


        } else {

            var parentThread = thread.substring(0, 2);
            var changeCheck = checked;
            Ext.each(allRecords, function (rec) {

                if( rec.get('thread').length > 2 &&  rec.get('thread').substring(0,2) == parentThread ) {

                    if(rec.get(field) == true){
                        changeCheck = true;
                    }

                }

                if(field == 'isWrite' && checked == true && rec.get('thread') == thread ) {
                    rec.set("isRead", checked);
                }

            });

            Ext.each(allRecords, function (rec) {

                if( rec.get('thread') == parentThread ) {
                    rec.set(field, changeCheck);
                    if(field == 'isWrite' && changeCheck == true) {
                        rec.set("isRead", changeCheck);
                    }
                }

            });

        }

    },

    searchUserPermissionDetail: function(index) {

        Ext.getCmp("userPermissionDetailTabPanel").setActiveTab(index);

        if(index == 0) {

            //User Permission Detail Loading

            Ext.getCmp("permittionDetailTitleLabel").setText("<h2>&nbsp;&nbsp;&nbsp;"+userConstants.selectRow.get("permNm")+"</h2>", false);

            Ext.getCmp("detailPermissionForm").getForm().loadRecord(userConstants.selectRow);

            var menuStore = Ext.getStore("PermissionMenuTreeStore");

            Ext.getCmp("permissionMenuTreeGrid").bindStore(menuStore);

            menuStore.getProxy().extraParams = {
                permId : userConstants.selectRow.get("permId")
            };

            menuStore.load({
                callback : function(records, options, success) {
                    Ext.getCmp("permissionMenuTreeGrid").expandAll();
                }
            });

        } else {

            var userStore = Ext.getCmp('permissionUsersGrid').getStore();

            userStore.getProxy().extraParams = {
                permId : userConstants.selectRow.get("permId"),
                search : Ext.getCmp("searchPermissionUserName").getRawValue()
            };

            userStore.load();
        }
    },

    searchPopAllUser: function(init) {

        if(init) {
            Ext.getCmp("searchPopUserName").setValue("");
        }

        var userStore = Ext.getCmp('allPermissionUserGrid').getStore();

        userStore.getProxy().extraParams = {
            search : Ext.getCmp("searchPopUserName").getRawValue()
        };

        userStore.load();
    }

});
