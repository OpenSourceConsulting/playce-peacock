/*
 * File: app/controller/RHEVMController.js
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

Ext.define('MyApp.controller.RHEVMController', {
    extend: 'Ext.app.Controller',

    onHypervisorGridSelect: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        //RHEVM Grid Item Click

        if(cellIndex > 8) {
            //Action Column 예외처리
            return;
        }

        if(RHEVMConstants.selectRow == null || RHEVMConstants.selectRow.get("hypervisorId") != record.get("hypervisorId")) {

            RHEVMConstants.selectRow = record;

            this.selectHypervisorGrid();
        }
    },

    onRhevmTabPanelTabChange: function(tabPanel, newCard, oldCard, eOpts) {

        //RHEVM Tab Change
        if(newCard.title == "Templates"){

            Ext.getCmp("searchRhevmTemplateName").setValue("");
            Ext.getCmp("rhevmTemplateGrid").reconfigure(Ext.getCmp("rhevmTemplateGrid").store, Ext.getCmp("rhevmTemplateGrid").initialConfig.columns);

            this.searchRhevmChildGrid("rhevmTemplateGrid");

        } else {

            Ext.getCmp("searchRhevmVMName").setValue("");
            Ext.getCmp("rhevmVMGrid").reconfigure(Ext.getCmp("rhevmVMGrid").store, Ext.getCmp("rhevmVMGrid").initialConfig.columns);

            this.searchRhevmChildGrid("rhevmVMGrid");

        }
    },

    onRhevmVMGridItemClick: function(dataview, record, item, index, e, eOpts) {

        //RHEVM VM 상세 조회

        if(RHEVMConstants.childSelectRow == null || RHEVMConstants.childSelectRow.get("vmId") != record.get("vmId")) {

            RHEVMConstants.childSelectRow = record;

            this.selectRhevmChildGrid('vm');
        }
    },

    onRhevmVMGridBeforeItemContextMenu: function(dataview, record, item, index, e, eOpts) {

        //RHEVM VM Grid Right Click Menu 호출

        var position = e.getXY();
        e.stopEvent();

        RHEVMConstants.childActionRow = record;

        var menu = RHEVMConstants.rhevmVMContextMenu;
        var status = record.get('status');

        menu.items.each(function( item ) {

            if(status == 'up') {

                if(item.text == 'Start')				item.setDisabled(true);
                else if(item.text == 'Stop')			item.setDisabled(false);
                else if(item.text == 'Shutdown')		item.setDisabled(false);
                else if(item.text == 'Remove')			item.setDisabled(true);
                else if(item.text == 'Export')			item.setDisabled(true);
                else if(item.text == 'Make Template')	item.setDisabled(true);

            } else if(status == 'down') {

                if(item.text == 'Start')				item.setDisabled(false);
                else if(item.text == 'Stop')			item.setDisabled(true);
                else if(item.text == 'Shutdown')		item.setDisabled(true);
                else if(item.text == 'Remove')			item.setDisabled(false);
                else if(item.text == 'Export')			item.setDisabled(false);
                else if(item.text == 'Make Template')	item.setDisabled(false);

            } else {
                item.setDisabled(true);
            }

        });

        menu.showAt(position);
    },

    onRhevmTemplateGridSelect: function(dataview, record, item, index, e, eOpts) {

        //RHEVM Template 상세 조회

        if(RHEVMConstants.childSelectRow == null || RHEVMConstants.childSelectRow.get("templateId") != record.get("templateId")) {

            RHEVMConstants.childSelectRow = record;

            this.selectRhevmChildGrid('template');
        }

    },

    onHypervisorGridBeforeItemContextMenu: function(dataview, record, item, index, e, eOpts) {

        //RHEVM Right Click Menu 호출

        var position = e.getXY();
        e.stopEvent();

        RHEVMConstants.actionRow = record;

        RHEVMConstants.rhevmContextMenu.showAt(position);
    },

    onSearchRhevmVMNameKeydown: function(textfield, e, eOpts) {

        //RHEVM VM Name Search
        if(e.getKey() == e.ENTER){
            this.searchRhevmChildGrid('rhevmVMGrid');
        }
    },

    onSearchRhevmTemplateNameKeydown: function(textfield, e, eOpts) {
        //RHEVM Template Name Search
        if(e.getKey() == e.ENTER){
            this.searchRhevmChildGrid('rhevmTemplateGrid');
        }
    },

    onRhevmTemplateGridBeforeItemContextMenu: function(dataview, record, item, index, e, eOpts) {
        //RHEVM Template Right Click Menu 호출

        var position = e.getXY();
        e.stopEvent();

        RHEVMConstants.childActionRow = record;

        RHEVMConstants.rhevmTemplateContextMenu.showAt(position);
    },

    onRhevmTabDetailTabPanelTabChange: function(tabPanel, newCard, oldCard, eOpts) {
        //RHEVM Detail Child Tab Change

        var type;

        if(Ext.getCmp("rhevmTabPanel").getActiveTab().title == "Virtual Machines"){
            type = "vm";
        } else {
            type = "template";
        }

        this.searchRhevmChildDetailTab(type, newCard.title);

    },

    init: function(application) {
                var rhevm = this;

                var rhevmGridContextMenu = new Ext.menu.Menu({
                    items:
                    [
                    { text: 'Edit',
                        handler: function() {
                            rhevm.showRhevmWindow('edit');
                        }
                    },
                    { text: 'Delete',
                        handler: function() {
                            rhevm.deleteRhevm();
                        }
                    }
                    ]

                });

                var rhevmVMGridContextMenu = new Ext.menu.Menu({
                    items:
                    [
                    { text: 'Start',
                        handler: function() {
                            rhevm.controlVMStatus('Start');
                        }
                    },
                    { text: 'Stop',
                        handler: function() {
                            rhevm.controlVMStatus('Stop');
                        }
                    },
                    { text: 'Shutdown',
                        handler: function() {
                            rhevm.controlVMStatus('Shutdown');
                        }
                    },
                    { text: 'Remove',
                        handler: function() {
                            rhevm.controlVMStatus('Remove');
                        }
                    },
                    { text: 'Export',
                        handler: function() {
                            rhevm.controlVMStatus('Export');
                        }
                    },
                    { text: 'Make Template',
                        handler: function() {
                            rhevm.showTemplateWindow();
                        }
                    }
                    ]

                });

                var rhevmTemplateGridContextMenu = new Ext.menu.Menu({
                    items:
                    [
                    { text: 'Instance Create',
                        handler: function() {
                            rhevm.popCreateInstance();
                        }
                    },
                    { text: 'Remove',
                        handler: function() {
                            rhevm.deleteTemplate();
                        }
                    }
                    ]

                });

                //RHEVM Menu Constants
                Ext.define('RHEVMConstants', {
                    singleton: true,
                    me : rhevm,
                    rhevmContextMenu: rhevmGridContextMenu,
                    rhevmVMContextMenu: rhevmVMGridContextMenu,
                    rhevmTemplateContextMenu: rhevmTemplateGridContextMenu,
                    selectRow : null,
                    actionRow : null,
                    childSelectRow : null,
                    childActionRow : null
                });

        this.control({
            "#hypervisorGrid": {
                cellclick: this.onHypervisorGridSelect,
                beforeitemcontextmenu: this.onHypervisorGridBeforeItemContextMenu
            },
            "#rhevmTabPanel": {
                tabchange: this.onRhevmTabPanelTabChange
            },
            "#rhevmVMGrid": {
                itemclick: this.onRhevmVMGridItemClick,
                beforeitemcontextmenu: this.onRhevmVMGridBeforeItemContextMenu
            },
            "#rhevmTemplateGrid": {
                itemclick: this.onRhevmTemplateGridSelect,
                beforeitemcontextmenu: this.onRhevmTemplateGridBeforeItemContextMenu
            },
            "#searchRhevmVMName": {
                keydown: this.onSearchRhevmVMNameKeydown
            },
            "#searchRhevmTemplateName": {
                keydown: this.onSearchRhevmTemplateNameKeydown
            },
            "#rhevmTabDetailTabPanel": {
                tabchange: this.onRhevmTabDetailTabPanelTabChange
            }
        });
    },

    searchHypervisor: function(init) {

        //RHEVM List 조회

        if(init) {
            Ext.getCmp("hypervisorGrid").reconfigure(Ext.getCmp("hypervisorGrid").store, Ext.getCmp("hypervisorGrid").initialConfig.columns);
        }

        RHEVMConstants.selectRow = null;
        RHEVMConstants.childSelectRow = null;

        Ext.getCmp("hypervisorGrid").getStore().load();

        var detailPanel = Ext.getCmp("rhevmDetailPanel");
        detailPanel.layout.setActiveItem(0);
    },

    selectHypervisorGrid: function() {

        //RHEVM Grid Select
        var detailPanel = Ext.getCmp("rhevmDetailPanel");
        detailPanel.layout.setActiveItem(1);

        Ext.getCmp("rhevmTabPanel").setActiveTab(0);

        Ext.getCmp("searchRhevmVMName").setRawValue("");
        Ext.getCmp("searchRhevmTemplateName").setRawValue("");

        this.searchRhevmChildGrid();

    },

    showRhevmWindow: function(type) {
        var rhevmWindow = Ext.create("widget.RegRhevmWindow");
        rhevmWindow.show();

        if(type == 'edit') {

            rhevmWindow.setTitle("Edit RHEVM");

            var rhevmForm = Ext.getCmp("rhevmForm");

            rhevmForm.getForm().waitMsgTarget = rhevmForm.getEl();

            rhevmForm.getForm().load({
                params : {
                    hypervisorId : RHEVMConstants.actionRow.get("hypervisorId")
                }
                ,url : GLOBAL.urlPrefix + "hypervisor/selectHypervisor"
                ,waitMsg:'Loading...'
                ,success: function(form, action) {

                    var password = form.findField('rhevmPassword').getValue();

                    form.findField('confirmPasswd').setRawValue(password);
                }
            });

        }


    },

    deleteRhevm: function() {

        Ext.MessageBox.confirm('Confirm', '삭제 하시겠습니까?', function(btn){

            if(btn == "yes"){

                Ext.Ajax.request({
                    url: GLOBAL.urlPrefix + "hypervisor/deleteHypervisor",
                    params : {
                        hypervisorId : RHEVMConstants.actionRow.get("hypervisorId")
                    },
                    disableCaching : true,
                    waitMsg: 'Delete RHEVM...',
                    success: function(response){
                        var msg = Ext.JSON.decode(response.responseText).msg;
                        Ext.MessageBox.alert('알림', msg);

                        RHEVMConstants.selectRow = null;

                        Ext.getCmp("hypervisorGrid").getStore().reload();
                        Ext.getCmp("rhevmDetailPanel").layout.setActiveItem(0);

                    }
                });
            }

        });

    },

    searchRhevmChildGrid: function(grid_id) {

        //RHEVM Detail Tab 조회

        RHEVMConstants.childSelectRow = null;

        var detailDPanel = Ext.getCmp("rhevmTabDetailPanel");
        detailDPanel.layout.setActiveItem(0);
        detailDPanel.collapse();

        if(grid_id == null) {

            //Virtual Machines Data Loading
            var vmGrid = Ext.getCmp('rhevmVMGrid');
            vmGrid.reconfigure(vmGrid.store, vmGrid.initialConfig.columns);

            vmGrid.getStore().load({
                params:{
                    hypervisorId : RHEVMConstants.selectRow.get("hypervisorId")
                }
            });

        } else {

            var searchName = '';
            if(grid_id == 'rhevmVMGrid') {
                searchName = Ext.getCmp("searchRhevmVMName").getRawValue();
            } else {
                searchName = Ext.getCmp("searchRhevmTemplateName").getRawValue();
            }

            var grid = Ext.getCmp(grid_id);
            grid.getStore().load({
                params:{
                    hypervisorId : RHEVMConstants.selectRow.get("hypervisorId"),
                    name : searchName
                }
            });

        }
    },

    showTemplateWindow: function() {

        //Template 생성 팝업 호출

        var templateWindow = Ext.create("widget.RegTemplateWindow");

        templateWindow.show();

        var templateForm = Ext.getCmp("templateForm");
        templateForm.getForm().findField("hypervisorId").setRawValue(RHEVMConstants.selectRow.get("hypervisorId"));
        templateForm.getForm().findField("vmId").setRawValue(RHEVMConstants.childActionRow.get("vmId"));
    },

    controlVMStatus: function(status) {

        //VM 제어

        var controlUrl = '';

        if(status == 'Start') {

            controlUrl = 'rhevm/vms/start';

        } else if(status == 'Stop') {

            controlUrl = 'rhevm/vms/stop';

        } else if(status == 'Shutdown') {

            controlUrl = 'rhevm/vms/shutdown';

        } else if(status == 'Remove') {

            controlUrl = 'rhevm/vms/remove';

        } else if(status == 'Export') {

            controlUrl = 'rhevm/vms/export';

        }

        Ext.MessageBox.confirm('Confirm', 'VM을 ' + status + '하시겠습니까?', function(btn){

            if(btn == "yes"){

                Ext.Ajax.request({
                    url: GLOBAL.urlPrefix + controlUrl,
                    params : {
                        hypervisorId : RHEVMConstants.selectRow.get("hypervisorId"),
                        vmId : RHEVMConstants.childActionRow.get("vmId")
                    },
                    disableCaching : true,
                    waitMsg: status + ' VM...',
                    success: function(response){
                        var msg = Ext.JSON.decode(response.responseText).msg;
                        Ext.MessageBox.alert('알림', msg);

                        Ext.getCmp('rhevmVMGrid').getStore().reload();
                    }
                });
            }

        });
    },

    deleteTemplate: function() {

        //Template Delete

        Ext.MessageBox.confirm('Confirm', '삭제 하시겠습니까?', function(btn){

            if(btn == "yes"){

                Ext.Ajax.request({
                    url: GLOBAL.urlPrefix + "rhevm/templates/remove",
                    params : {
                        hypervisorId : RHEVMConstants.selectRow.get("hypervisorId"),
                        templateId : RHEVMConstants.childActionRow.get("templateId")
                    },
                    disableCaching : true,
                    waitMsg: 'Delete Template...',
                    success: function(response){
                        var msg = Ext.JSON.decode(response.responseText).msg;
                        Ext.MessageBox.alert('알림', msg);

                        Ext.getCmp('rhevmTemplateGrid').getStore().reload();

                    }
                });
            }

        });

    },

    selectRhevmChildGrid: function(type) {

        //RHEVM VM 상세 조회
        var detailTab = Ext.getCmp("rhevmTabDetailPanel");
        detailTab.expand();

        detailTab.layout.setActiveItem(1);

        Ext.getCmp("rhevmTabDetailTabPanel").setActiveTab(0);

        this.searchRhevmChildDetailTab(type, "General");
    },

    searchRhevmChildDetailTab: function(type, tabTitle) {

        //RHEVM VM 상세 정보 조회(Tab 별)
        var searchParam, searchUrl;

        if(tabTitle == "General") {

            //General Data Loading
            var generalform = Ext.getCmp("rhevmGeneralForm");

            generalform.getForm().reset();

            if(type == 'vm') {

                searchUrl = GLOBAL.urlPrefix + "rhevm/vms/info";
                searchParam = {
                    hypervisorId : RHEVMConstants.selectRow.get("hypervisorId"),
                    vmId : RHEVMConstants.childSelectRow.get("vmId")
                };

                generalform.getForm().findField('template').show();
                generalform.getForm().findField('type').hide();

            } else {

                searchUrl = GLOBAL.urlPrefix + "rhevm/templates/info";
                searchParam = {
                    hypervisorId : RHEVMConstants.selectRow.get("hypervisorId"),
                    templateId : RHEVMConstants.childSelectRow.get("templateId")
                };

                generalform.getForm().findField('template').hide();
                generalform.getForm().findField('type').show();

            }

            generalform.getForm().waitMsgTarget = generalform.getEl();

            generalform.getForm().load({
                params : searchParam
                ,url : searchUrl
                ,waitMsg:'Loading...'
                ,success: function(form, action) {
                    var socket = form.findField('sockets').getRawValue();
                    var cores = form.findField('cores').getRawValue();

                    form.findField('core_socket').setRawValue((socket*cores) + ' (' + socket + ' Socket(s), ' + cores + ' Core(s) per Socket)');
                }
            });

        } else if(tabTitle == "Network Interfaces") {

            //Network Interfaces Data Loading
            var networkGrid = Ext.getCmp('rhevmNetworkGrid');
            networkGrid.reconfigure(networkGrid.store, networkGrid.initialConfig.columns);

            if(type == 'vm') {

                searchUrl = GLOBAL.urlPrefix + "rhevm/vms/nics";
                searchParam = {
                    hypervisorId : RHEVMConstants.selectRow.get("hypervisorId"),
                    vmId : RHEVMConstants.childSelectRow.get("vmId")
                };

            } else {

                searchUrl = GLOBAL.urlPrefix + "rhevm/templates/nics";
                searchParam = {
                    hypervisorId : RHEVMConstants.selectRow.get("hypervisorId"),
                    templateId : RHEVMConstants.childSelectRow.get("templateId")
                };

                networkGrid.getView().headerCt.getGridColumns()[6].setVisible(false);
                networkGrid.getView().headerCt.getGridColumns()[6].hideable = false;
            }

            networkGrid.getStore().getProxy().url = searchUrl;
            networkGrid.getStore().load({
                params:searchParam
            });

        } else if(tabTitle == "Disks") {

            //Disk Data Loading
            var diskGrid = Ext.getCmp('rhevmDiskGrid');
            diskGrid.reconfigure(diskGrid.store, diskGrid.initialConfig.columns);

            if(type == 'vm') {

                searchUrl = GLOBAL.urlPrefix + "rhevm/vms/disks";
                searchParam = {
                    hypervisorId : RHEVMConstants.selectRow.get("hypervisorId"),
                    vmId : RHEVMConstants.childSelectRow.get("vmId")
                };

            } else {

                searchUrl = GLOBAL.urlPrefix + "rhevm/templates/disks";
                searchParam = {
                    hypervisorId : RHEVMConstants.selectRow.get("hypervisorId"),
                    templateId : RHEVMConstants.childSelectRow.get("templateId")
                };

                diskGrid.getView().headerCt.getGridColumns()[1].setVisible(false);
                diskGrid.getView().headerCt.getGridColumns()[1].hideable = false;

            }

            diskGrid.getStore().getProxy().url = searchUrl;
            diskGrid.getStore().load({
                params:searchParam
            });

        }
    },

    popCreateInstance: function() {

        //Instance 생성 팝업 호출(Template 지정)

        var regInstanceWindow = Ext.create("widget.RegInstanceWindow");
        regInstanceWindow.show();

        var form = Ext.getCmp('instanceForm').getForm();

        form.findField("hypervisorId").setValue(RHEVMConstants.selectRow.get("hypervisorId"));
        form.findField("displayHypervisor").setValue(RHEVMConstants.selectRow.get("rhevmName"));
        form.findField("template").setValue(RHEVMConstants.childActionRow.get("templateId"));
        form.findField("displayTemplate").setValue(RHEVMConstants.childActionRow.get("name"));
        form.findField("memory").setValue(RHEVMConstants.childActionRow.get("memory"));
        form.findField("sockets").setValue(RHEVMConstants.childActionRow.get("sockets"));
        form.findField("cores").setValue("1");

        form.findField("hypervisorId").hide();
        form.findField("template").hide();
        form.findField("displayHypervisor").show();
        form.findField("displayTemplate").show();
    }

});
