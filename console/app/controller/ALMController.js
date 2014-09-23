/*
 * File: app/controller/ALMController.js
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

Ext.define('MyApp.controller.ALMController', {
    extend: 'Ext.app.Controller',

    onAlmRepositoryGridCellClick: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        //ALM Group Grid Item Click

        if(cellIndex > 3) {
            //Action Column 예외처리
            return;
        }

        if(almConstants.selectRow == null || almConstants.selectRow.get("repositoryCode") != record.get("repositoryCode")) {

            almConstants.selectRow = record;

            var detailPanel = Ext.getCmp("almRepositoryDetailPanel");
            detailPanel.layout.setActiveItem(1);

            var repositoryForm = Ext.getCmp("almRepositoryForm");

            repositoryForm.getForm().reset();

            repositoryForm.getForm().waitMsgTarget = repositoryForm.getEl();

            repositoryForm.getForm().load({
                 url : GLOBAL.urlPrefix + "alm/repository/" + almConstants.selectRow.get("repositoryCode")
                ,method : 'GET'
                ,waitMsg:'Loading...'
            });

            Ext.getCmp("almRepositoryTitleLabel").setText("<h2>&nbsp;&nbsp;&nbsp;"+almConstants.selectRow.get("repositoryCode")+"</h2>", false);
        }
    },

    onAlmProjectGridCellClick: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        //ALM Project Grid Item Click

        if(almConstants.selectRow == null || almConstants.selectRow.get("projectCode") != record.get("projectCode")) {

            almConstants.selectRow = record;

            this.selectAlmProjectGrid();
        }
    },

    onAlmUserGridCellClick: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        //ALM User Grid Item Click

        if(cellIndex > 5) {
            //Action Column 예외처리
            return;
        }

        if(almConstants.selectRow == null || almConstants.selectRow.get("userId") != record.get("userId")) {

            almConstants.selectRow = record;

            this.selectAlmUserGrid();
        }
    },

    onAlmUserGridBeforeItemContextMenu: function(dataview, record, item, index, e, eOpts) {
        //ALM User Grid Right Click Menu 호출

        var position = e.getXY();
        e.stopEvent();

        almConstants.actionRow = record;

        almConstants.userContextMenu.showAt(position);


    },

    onAlmGroupGridBeforeItemContextMenu: function(dataview, record, item, index, e, eOpts) {
        /*ALM Group Grid Right Click Menu 호출

        var position = e.getXY();
        e.stopEvent();

        almConstants.selectRow = record;

        almConstants.groupContextMenu.showAt(position);

        */
    },

    onAlmTabPanelTabChange: function(tabPanel, newCard, oldCard, eOpts) {
        if(newCard.title == "Project"){

            this.searchAlmProject();

        } else if(newCard.title == "User"){

            this.searchAlmUser(true);

        } else if(newCard.title == "Group"){

            this.searchAlmGroup(true);

        } else if(newCard.title == "Repository"){

            almConstants.selectRow = null;

            Ext.getCmp("almRepositoryGrid").getStore().load();

            var detailPanel = Ext.getCmp("almRepositoryDetailPanel");
            detailPanel.layout.setActiveItem(0);

        }
    },

    onProjectTabPanelTabChange: function(tabPanel, newCard, oldCard, eOpts) {
        if(newCard.title == "Summary"){

            this.selectAlmProjectGrid();

        } else if(newCard.title == "User"){

            var userStore = Ext.getCmp("almProjectUserGrid").getStore();
            userStore.getProxy().url = GLOBAL.urlPrefix + 'alm/groupmanagement/'+almConstants.selectRow.get("projectCode")+'/users';
            userStore.getProxy().extraParams = {
                limit : 1
            };

            userStore.load();


        } else if(newCard.title == "Confluence"){

            var confStore = Ext.getCmp("almProjectConfluenceGrid").getStore();
            confStore.getProxy().url = GLOBAL.urlPrefix + 'alm/project/'+almConstants.selectRow.get("projectCode")+'/confluence';

            confStore.load();

        } else {

            var jenkinsStore = Ext.getCmp("almProjectJenkinsGrid").getStore();
            jenkinsStore.getProxy().url = GLOBAL.urlPrefix + 'alm/project/'+almConstants.selectRow.get("projectCode")+'/jenkins';

            jenkinsStore.load();
        }
    },

    onAlmGroupGridCellClick: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        //ALM Group Grid Item Click

        if(cellIndex > 3) {
            //Action Column 예외처리
            return;
        }

        if(almConstants.selectRow == null || almConstants.selectRow.get("name") != record.get("name")) {

            almConstants.selectRow = record;

            var groupDetailPanel = Ext.getCmp("almGroupDetailPanel");
            groupDetailPanel.layout.setActiveItem(1);

            Ext.getCmp("almGroupTabPanel").setActiveTab(0);

            this.searchAlmGroupDetail(0);

        }
    },

    onAlmGroupTabPanelTabChange: function(tabPanel, newCard, oldCard, eOpts) {

        if(newCard.title == "Summary"){

            this.searchAlmGroupDetail(0);

        } else {

            Ext.getCmp("almGroupUserGrid").reconfigure(Ext.getCmp("almGroupUserGrid").store, Ext.getCmp("almGroupUserGrid").initialConfig.columns);

            this.searchAlmGroupDetail(1);

        }
    },

    onInputAlmUserNameKeydown: function(textfield, e, eOpts) {
        //User Name Search
        if(e.getKey() == e.ENTER){

            almConstants.page = 1;
            this.searchAlmUser();
        }
    },

    onInputAlmGroupNameKeydown: function(textfield, e, eOpts) {
        //User Name Search
        if(e.getKey() == e.ENTER){

            almConstants.page = 1;
            this.searchAlmGroup();
        }
    },

    onInputWizardUserNameKeydown: function(textfield, e, eOpts) {
        //User Name Search
        if(e.getKey() == e.ENTER){

            almConstants.popPage = 1;
            this.searchWizardUser();
        }
    },

    init: function(application) {
                //ALM Menu Config Setting

                var alm = this;

                var almUserGridContextMenu = new Ext.menu.Menu({
                    items:
                    [
                    { text: 'Edit',
                        handler: function() {
                            alm.showAlmUserWindow('edit');
                        }
                    },
                    { text: 'Delete',
                        handler: function() {
                            alm.deleteAlmUserWindow();
                        }
                    }/*,
                    { text: 'Clone User',
                        handler: function() {
                            alm.showAlmUserWindow('clone');
                        }
                    }*/
                    ]

                });

                var almGroupGridContextMenu = new Ext.menu.Menu({
                    items:
                    [
                    { text: 'Edit',
                        handler: function() {
                            alm.showAlmGroupWindow();
                        }
                    },
                    { text: 'Delete',
                        handler: function() {
                            alert('delete');
                        }
                    }
                    ]

                });

                //ALM Menu Constants
                Ext.define('almConstants', {
                    singleton: true,
                    me : alm,
                    userContextMenu: almUserGridContextMenu,
                    groupContextMenu: almGroupGridContextMenu,
                    selectRow : null,
                    actionRow : null,
                    page : null,
                    popPage : null
                });


        this.control({
            "#almRepositoryGrid": {
                cellclick: this.onAlmRepositoryGridCellClick
            },
            "#almProjectGrid": {
                cellclick: this.onAlmProjectGridCellClick
            },
            "#almUserGrid": {
                cellclick: this.onAlmUserGridCellClick,
                beforeitemcontextmenu: this.onAlmUserGridBeforeItemContextMenu
            },
            "#almGroupGrid": {
                beforeitemcontextmenu: this.onAlmGroupGridBeforeItemContextMenu,
                cellclick: this.onAlmGroupGridCellClick
            },
            "#almTabPanel": {
                tabchange: this.onAlmTabPanelTabChange
            },
            "#projectTabPanel": {
                tabchange: this.onProjectTabPanelTabChange
            },
            "#almGroupTabPanel": {
                tabchange: this.onAlmGroupTabPanelTabChange
            },
            "#inputAlmUserName": {
                keydown: this.onInputAlmUserNameKeydown
            },
            "#inputAlmGroupName": {
                keydown: this.onInputAlmGroupNameKeydown
            },
            "#inputWizardUserName": {
                keydown: this.onInputWizardUserNameKeydown
            }
        });
    },

    searchAlmProject: function(init) {

        if(init) {

            Ext.getCmp("almTabPanel").setActiveTab(0);
            Ext.getCmp("almProjectGrid").reconfigure(Ext.getCmp("almProjectGrid").store, Ext.getCmp("almProjectGrid").initialConfig.columns);
        }

        almConstants.selectRow = null;

        Ext.getCmp("almProjectGrid").getStore().load();

        var detailPanel = Ext.getCmp("almProjectDetailPanel");
        detailPanel.layout.setActiveItem(0);
    },

    searchAlmUser: function(init, pagingType) {

        if(init) {
            Ext.getCmp("almUserGrid").reconfigure(Ext.getCmp("almUserGrid").store, Ext.getCmp("almUserGrid").initialConfig.columns);
            almConstants.page = 1;
            Ext.getCmp("inputAlmUserName").setValue("");
        }

        if(pagingType == 'left' && almConstants.page > 1) {
            almConstants.page-- ;
        } else if(pagingType == 'right') {
            almConstants.page++ ;
        }

        almConstants.selectRow = null;

        Ext.getCmp("almUserGrid").getStore().load({
            params:{
                limit : almConstants.page,
                search : Ext.getCmp("inputAlmUserName").getValue()
            }
        });

        var detailPanel = Ext.getCmp("almUserDetailPanel");
        detailPanel.layout.setActiveItem(0);

    },

    searchPopAlmUser: function(init, pagingType) {

        if(init) {
            almConstants.popPage = 1;
            Ext.getCmp("inputPopAlmUserName").setValue("");
        }

        if(pagingType == 'left' && almConstants.popPage > 1) {
            almConstants.popPage-- ;
        } else if(pagingType == 'right') {
            almConstants.popPage++ ;
        }

        Ext.getCmp("popAlmUsersGrid").getStore().load({
            params:{
                limit : almConstants.popPage,
                search : Ext.getCmp("inputPopAlmUserName").getValue()
            }
        });

    },

    searchWizardUser: function(init, pagingType) {

        if(init) {
            almConstants.popPage = 1;
            Ext.getCmp("inputWizardUserName").setValue("");
        }

        if(pagingType == 'left' && almConstants.popPage > 1) {
            almConstants.popPage-- ;
        } else if(pagingType == 'right') {
            almConstants.popPage++ ;
        }

        Ext.getCmp("wizardAddUserGrid").getStore().load({
            params:{
                limit : almConstants.popPage,
                search : Ext.getCmp("inputWizardUserName").getValue()
            }
        });

    },

    searchAlmGroup: function(init, pagingType) {

        if(init) {
            Ext.getCmp("almGroupGrid").reconfigure(Ext.getCmp("almGroupGrid").store, Ext.getCmp("almGroupGrid").initialConfig.columns);
            almConstants.page = 1;
            Ext.getCmp("inputAlmGroupName").setValue("");
        }

        if(pagingType == 'left' && almConstants.page > 1) {
            almConstants.page-- ;
        } else if(pagingType == 'right') {
            almConstants.page++ ;
        }

        almConstants.selectRow = null;

        Ext.getCmp("almGroupGrid").getStore().load({
            params:{
                limit : almConstants.page,
                search : Ext.getCmp("inputAlmGroupName").getValue()
            }
        });

        var detailPanel = Ext.getCmp("almGroupDetailPanel");
        detailPanel.layout.setActiveItem(0);

    },

    selectAlmProjectGrid: function() {

        var userDetailPanel = Ext.getCmp("almProjectDetailPanel");
        userDetailPanel.layout.setActiveItem(1);

        Ext.getCmp("projectTabPanel").setActiveTab(0);

        //Project Data Loading

        var projectForm = Ext.getCmp("almProjectForm");

        projectForm.getForm().reset();

        projectForm.getForm().waitMsgTarget = projectForm.getEl();

        projectForm.getForm().load({
            url : GLOBAL.urlPrefix + "alm/project/" + almConstants.selectRow.get("projectCode")
            ,method : 'GET'
            ,waitMsg:'Loading...'
        });

        Ext.getCmp("almProjectTitleLabel").setText("<h2>"+almConstants.selectRow.get("projectName")+"</h2>", false);
    },

    selectAlmUserGrid: function() {

        var userDetailPanel = Ext.getCmp("almUserDetailPanel");
        userDetailPanel.layout.setActiveItem(1);

        //User Data Loading

        var userForm = Ext.getCmp("almUserForm");

        userForm.getForm().reset();

        userForm.getForm().waitMsgTarget = userForm.getEl();
        //alert(almConstants.selectRow.get("username"))
        userForm.getForm().load({
             /*params : {
                username : almConstants.selectRow.get("username")
            }
            ,*/url : GLOBAL.urlPrefix + "alm/usermanagement/" + almConstants.selectRow.get("userId")
            ,method : 'GET'
            ,waitMsg:'Loading...'
        });

        Ext.getCmp("almUserTitleLabel").setText("<h2>&nbsp;&nbsp;&nbsp;"+almConstants.selectRow.get("displayName")+"</h2>", false);
    },

    showAlmUserWindow: function(type) {
        var almUserWindow = Ext.create("widget.AlmUserWindow");

        almUserWindow.show();

        if(type == 'edit' || type == 'clone') {

            if(type == 'edit') {
                almUserWindow.setTitle("Edit User");
            } else {
                almUserWindow.setTitle("Clone User");
            }

            var userForm = Ext.getCmp("popAlmUserForm");

            userForm.getForm().waitMsgTarget = userForm.getEl();

            userForm.getForm().load({
                 url : GLOBAL.urlPrefix + "alm/usermanagement/" + almConstants.actionRow.get("userId")
                ,method : 'GET'
                ,waitMsg:'Loading...'
                ,success: function(form, action) {

                    if(type == 'edit') {
                        form.findField('userId').setRawValue(almConstants.actionRow.get("userId"));
                        form.findField('name').setReadOnly(true);
                    }

                    form.findField('email').setRawValue(form.findField('emailAddress').getRawValue());

                    form.findField('password').setDisabled(true);
                    form.findField('confirmPassword').setDisabled(true);

                    form.findField('password').hide();
                    form.findField('confirmPassword').hide();

                    almUserWindow.setHeight(250);
                }
            });
        }
    },

    showAlmGroupWindow: function() {
        var almGroupWindow = Ext.create("widget.AlmGroupWindow");

        almGroupWindow.show();
    },

    searchAlmGroupDetail: function(tabIdx) {

        if(tabIdx == 0) {

            //Group Summary Data Loading

            var groupForm = Ext.getCmp("almGroupForm");

            groupForm.getForm().reset();

            groupForm.getForm().waitMsgTarget = groupForm.getEl();
            groupForm.getForm().load({
                 url : GLOBAL.urlPrefix + "alm/groupmanagement/" + almConstants.selectRow.get("name")
                ,method : 'GET'
                ,waitMsg:'Loading...'
            });

            Ext.getCmp("almGroupTitleLabel").setText("<h2>&nbsp;&nbsp;&nbsp;Group : "+almConstants.selectRow.get("name")+"</h2>", false);

        } else {

            var userStore = Ext.getCmp("almGroupUserGrid").getStore();
            userStore.getProxy().url = GLOBAL.urlPrefix + 'alm/groupmanagement/'+almConstants.selectRow.get("name")+'/users';
            userStore.getProxy().extraParams = {
                limit : 1
            };

            userStore.load();

        }
    },

    goAlmWizardPanel: function(cardNum) {
        var cardPanel = Ext.getCmp("almWizardCardPanel");

        if(cardNum != 0) {
            var activeItem = cardPanel.getLayout().activeItem;
            var activeIndex = cardPanel.items.indexOf(activeItem);
            var form = Ext.getCmp("addProjectForm").getForm();

            if(activeIndex == 0) {
                if(!form.isValid()) {
                    return;
                } else {
                    var duplYn = Ext.getCmp("proejctCodeDuplYnField").getValue();
                    if(duplYn != "Y") {
                        Ext.Msg.alert('Message', 'Project Code를 중복확인하시기 바랍니다.');
                        return;
                    }
                }
            }

            if(cardNum == 1) {

                Ext.getCmp('wizardAddSpaceGrid').getStore().load();

            } else if(cardNum == 2) {

                this.searchWizardUser(true);

            } else if(cardNum == 3) {
                var reviewForm = Ext.getCmp("reviewProjectForm").getForm();
                reviewForm.findField("projectName").setValue(form.findField("projectName").getValue());
                reviewForm.findField("projectDescription").setValue(form.findField("projectDescription").getValue());
                reviewForm.findField("groupDescription").setValue(form.findField("groupDescription").getValue());
                reviewForm.findField("repository").setValue(form.findField("repository").getValue());
            }

        }

        cardPanel.layout.setActiveItem(cardNum);

        for(var i=0;i<4;i++) {

            if(i == cardNum) {
                Ext.getCmp("almWizardStepLabel"+i).addCls("wizard-label-on");
            } else {
                Ext.getCmp("almWizardStepLabel"+i).removeCls("wizard-label-on");
            }

        }
    },

    deleteAlmUserWindow: function() {

        //User 삭제

        Ext.MessageBox.confirm('Confirm', '삭제 하시겠습니까?', function(btn){

            if(btn == "yes"){

                Ext.Ajax.request({
                    url: GLOBAL.urlPrefix + "alm/usermanagement/" + almConstants.actionRow.get("userId"),
                    method : 'DELETE',
                    headers: { 'Content-Type': 'application/json' },
                    disableCaching : true,
                    waitMsg: 'Delete ALM User...',
                    success: function(response){
                        var msg = Ext.JSON.decode(response.responseText).msg;
                        Ext.MessageBox.alert('알림', msg);

                        almConstants.selectRow = null;

                        Ext.getCmp("almUserGrid").getStore().reload();
                        Ext.getCmp("almUserDetailPanel").layout.setActiveItem(0);

                    }
                });
            }

        });
    }

});
