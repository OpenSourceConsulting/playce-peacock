/*
 * File: app/controller/LoginController.js
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

Ext.define('MyApp.controller.LoginController', {
    extend: 'Ext.app.Controller',

    refs: [
        {
            ref: 'userName',
            selector: '#userName'
        },
        {
            ref: 'password',
            selector: '#password'
        },
        {
            ref: 'passwdResetLabel',
            selector: '#passwdResetLabel'
        },
        {
            ref: 'userSplitBtn',
            selector: '#userSplitBtn'
        }
    ],

    doLogin: function(button, e, eOpts) {
        /*
        // loginBtn에 fireEvent()를 이용한 click 이벤트 실행 시 button 은 undefined로 넘어옴.

        var form = button.up('form'),				// Login form
            formWindow = button.up('window'),		// Login form window
            values = form.getValues();				// Form values
        */

        var form = Ext.getCmp("loginForm"),			// Login form
            formWindow = Ext.getCmp('loginWindow'),	// Login form window
            values = form.getValues();				// Form values

        var me = this;

        var userName = this.getUserName(),
            password = this.getPassword();

        var userNameVal = userName.getValue(),
            passwordVal = password.getValue();

        // Success
        var successCallback = function(resp, ops) {

        //     Ext.Msg.show({
        //         title: "Form Values",
        //         msg: "userName : " + form.getForm().findField('userName').getValue() + "<br/>password : " + form.getForm().findField('password').getValue(),
        //         buttons: Ext.Msg.OK,
        //         fn: function(choice) {
        //         },
        //         icon: Ext.Msg.INFO
        //     });

            // Close window
            formWindow.destroy();
            Ext.getCmp("peacockViewport").layout.setActiveItem(1);
            me.getUserSplitBtn().setText(userNameVal);

            var treeStore = Ext.create('Ext.data.TreeStore', {
                storeId: 'treeStore',
                root: {
                    expanded: true,
                    text: 'Server List',
                    children: [
                        {
                            text: '<b>Dashboard</b>',
                            id: 'MENU_01',
                            leaf: true,
                            iconCls: 'no-icon',
                            cls: 'parent-node last-children'
                        },
                        {
                            text: '<b>Server Management</b>',
                            id: 'MENU_02',
                            expanded: true,
                            iconCls: 'no-icon',
                            cls: 'parent-node',
                            children: [
                                {
                                    text: 'Instances1',
                                    id: 'MENU_02_01',
                                    leaf: true,
                                    iconCls: 'no-icon'
                                },
                                {
                                    text: 'Instances2',
                                    id: 'MENU_02_02',
                                    leaf: true,
                                    iconCls: 'no-icon'
                                },
                                {
                                    text: 'Instances3',
                                    id: 'MENU_02_03',
                                    leaf: true,
                                    iconCls: 'no-icon',
                                    cls: 'last-children'
                                }
                            ]
                        },
                        {
                            text: '<b>RHEV Manager</b>',
                            id: 'MENU_03',
                            expanded: true,
                            iconCls: 'no-icon',
                            cls: 'parent-node',
                            children: [
                                {
                                    text: 'Virtual Machines',
                                    id: 'MENU_03_01',
                                    leaf: true,
                                    iconCls: 'no-icon'
                                },
                                {
                                    text: 'Templates',
                                    id: 'MENU_03_02',
                                    leaf: true,
                                    iconCls: 'no-icon',
                                    cls: 'last-children'
                                }
                            ]
                        },
                        {
                            text: '<b>Scaling</b>',
                            id: 'MENU_04',
                            expanded: true,
                            iconCls: 'no-icon',
                            cls: 'parent-node',
                            children: [
                                {
                                    text: 'Auto Sacling',
                                    id: 'MENU_04_01',
                                    leaf: true,
                                    iconCls: 'no-icon'
                                },
                                {
                                    text: 'Load Balancers',
                                    id: 'MENU_04_02',
                                    leaf: true,
                                    iconCls: 'no-icon',
                                    cls: 'last-children'
                                }
                            ]
                        },
                        {
                            text: '<b>Etc</b>',
                            id: 'MENU_05',
                            expanded: true,
                            iconCls: 'no-icon',
                            cls: 'parent-node',
                            children: [
                                {
                                    text: 'sub menu1',
                                    id: 'MENU_05_01',
                                    leaf: true,
                                    iconCls: 'no-icon'
                                },
                                {
                                    text: 'sub menu2',
                                    id: 'MENU_05_02',
                                    leaf: true,
                                    iconCls: 'no-icon'
                                },
                                {
                                    text: 'sub menu3',
                                    id: 'MENU_05_03',
                                    leaf: true,
                                    iconCls: 'no-icon'
                                },
                                {
                                    text: 'sub menu4',
                                    id: 'MENU_05_04',
                                    leaf: true,
                                    iconCls: 'no-icon',
                                    cls: 'last-children'
                                }
                            ]
                        },
                        {
                            text: '<b>System Configuration</b>',
                            id: 'MENU_99',
                            expanded: true,
                            iconCls: 'no-icon',
                            cls: 'parent-node',
                            children: [
                                {
                                    text: 'Menu Management',
                                    id: 'MENU_99_01',
                                    leaf: true,
                                    iconCls: 'no-icon'
                                },
                                {
                                    text: 'Group Mamagement',
                                    id: 'MENU_99_02',
                                    leaf: true,
                                    iconCls: 'no-icon'
                                },
                                {
                                    text: 'User Management',
                                    id: 'MENU_99_03',
                                    leaf: true,
                                    iconCls: 'no-icon',
                                    cls: 'last-children'
                                }
                            ]
                        }
                    ]
                },
                fields: [
                    {
                        name: 'text'
                    }
                ]
            });


        /*
            Ext.getCmp("menuPanel").remove(Ext.getCmp("menuTreePanel"));

            var menuTreePanel = Ext.create('Ext.tree.Panel', {
                flex: 1,
                id: 'menuTreePanel',
                itemId: 'menuTreePanel',
                autoScroll: true,
                store: menuTreeStore,
                rootVisible: false,
                useArrows: true,
                viewConfig: {
                    autoScroll: true,
                    preserveScrollOnRefresh: true
                }
            });


            Ext.getCmp("menuPanel").add(menuTreePanel);
        */

            var menuTreeStore = Ext.getStore("MenuTreeStore");

            // show settings menu when logged in as admin
            if (userNameVal !== "admin") {
                Ext.getCmp("adminMenuSeparator").hide();
                Ext.getCmp("adminMenu").hide();

                var rootNode = treeStore.getRootNode();
                rootNode.lastChild.remove();
                menuTreeStore.setRootNode(rootNode);
            } else {
                menuTreeStore.setRootNode(treeStore.getRootNode());
            }

            Ext.getCmp("menuTreeView").selModel.select(0);
            Ext.getCmp("menuTreeView").fireEvent('cellclick', Ext.getCmp("menuTreeView"), null, null, menuTreeStore.getRootNode().firstChild);
        };

        // Failure
        var failureCallback = function(resp, ops) {

            // Show login failure error
            Ext.Msg.alert({
                title: "Login Failure",
                msg: resp,
                buttons: Ext.Msg.OK,
                fn: function(choice) {
                    password.setValue("");
                    password.focus();
                },
                icon: Ext.Msg.ERROR
            });
        };

        if (userNameVal === "") {

            // username must not be null.
            Ext.Msg.show({
                title: "Message",
                msg: "사용자아이디를 입력해주세요.",
                buttons: Ext.Msg.OK,
                fn: function(choice) {
                    userName.focus();
                },
                icon: Ext.Msg.WARNING
            });
        } else if (passwordVal === "") {

            // password must not be null.
            Ext.Msg.show({
                title: "Message",
                msg: "비밀번호를 입력해주세요.",
                buttons: Ext.Msg.OK,
                fn: function(choice) {
                    password.focus();
                },
                icon: Ext.Msg.WARNING
            });
        } else {

            /*
            //TODO: Login using server-side authentication service
            Ext.Ajax.request({
                url: "/login",
                params: values,
                success: successCallback,
                failure: failureCallback
            });
            */

            // Just run success for now
            successCallback();

            // Just run failure for now
            //failureCallback("Not implemented yet.");
        }
    },

    detectPasswordSpecialkey: function(field, e, eOpts) {
        /*
        component.getEl().on('keypress', function(e) {
            if (e.getKey() == e.ENTER) {
                Ext.getCmp("loginBtn").fireEvent("click");
            }
        });
        */

        if (e.getKey() == e.ENTER) {
            this.doLogin();
        }
    },

    detectUsernameSpecialkey: function(field, e, eOpts) {
        /*
        component.getEl().on('keypress', function(e) {
            if (e.getKey() == e.ENTER) {
                Ext.getCmp("loginBtn").fireEvent("click");
            }
        });
        */

        if (e.getKey() == e.ENTER) {
            this.doLogin();
        }
    },

    popupLoginWindow: function(button, e, eOpts) {
        // Create new login form window
        var login = Ext.create("widget.loginWindow");

        // Show window
        login.show();

        this.getUserName().focus();

        /**
         * 비밀번호 재설정 Label click event를 catch 하도록 설정
         */
        this.getPasswdResetLabel().getEl().on('click', function() {
            Ext.Msg.show({
                title: "Message",
                msg: "Password reset funtion isn't implemented yet.",
                buttons: Ext.Msg.OK,
                fn: function(choice) {
                    // do nothing.
                },
                icon: Ext.Msg.QUESTION
            });
        });
    },

    doEditProfile: function(item, e, eOpts) {
        Ext.Msg.show({
            title: "Message",
            msg: "Edit Profile funtion isn't implemented yet.",
            buttons: Ext.Msg.OK,
            fn: function(choice) {
                // do nothing.
            },
            icon: Ext.Msg.QUESTION
        });
    },

    doLogout: function(item, e, eOpts) {
        Ext.getCmp("centerContainer").layout.setActiveItem(0);
        Ext.getCmp("peacockViewport").layout.setActiveItem(0);
    },

    doSettings: function(item, e, eOpts) {
        Ext.getCmp("centerContainer").layout.setActiveItem(1);
    },

    introPanelActivate: function(component, eOpts) {
        // Create new login form window
        var login = Ext.create("widget.loginWindow");

        // Show window
        login.show();

        this.getUserName().focus();

        /**
         * 비밀번호 재설정 Label click event를 catch 하도록 설정
         */
        this.getPasswdResetLabel().getEl().on('click', function() {
            Ext.Msg.show({
                title: "Message",
                msg: "Password reset funtion isn't implemented yet.",
                buttons: Ext.Msg.OK,
                fn: function(choice) {
                    // do nothing.
                },
                icon: Ext.Msg.QUESTION
            });
        });
    },

    init: function(application) {
        this.control({
            "#loginBtn": {
                click: this.doLogin
            },
            "#password": {
                specialkey: this.detectPasswordSpecialkey
            },
            "#userName": {
                specialkey: this.detectUsernameSpecialkey
            },
            "#topLoginBtn": {
                click: this.popupLoginWindow
            },
            "#editProfileMenu": {
                click: this.doEditProfile
            },
            "#logoutMenu": {
                click: this.doLogout
            },
            "#adminMenu": {
                click: this.doSettings
            },
            "#introPanel": {
                activate: this.introPanelActivate
            }
        });
    }

});
