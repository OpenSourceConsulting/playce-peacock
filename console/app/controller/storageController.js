/*
 * File: app/controller/storageController.js
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

Ext.define('MyApp.controller.storageController', {
    extend: 'Ext.app.Controller',

    id: 'storageController',

    onStorageMainContainerActivate: function(component, eOpts) {
        storageConstants.workingGrid = 'storageMainGrid';

        this.setStorageMainData();
    },

    onStorageHostContainerShow: function(component, eOpts) {
        var sUniq = '&_uniq=' + Date.now();
        Ext.getCmp('storageHostImg1').setSrc(storageConstants.griphiteUrl1 + sUniq);
        Ext.getCmp('storageHostImg2').setSrc(storageConstants.griphiteUrl2 + sUniq);

        storageConstants.task.start();
    },

    onStorageHostContainerHide: function(component, eOpts) {
        storageConstants.task.stop();
    },

    onStorageHostButtonClick: function(button, e, eOpts) {
        storageConstants.workingGrid = 'storageHostGrid';

        Ext.getCmp('storageCenterContainer').layout.setActiveItem(1);
        this.setStorageHostData();

    },

    onStorageMonButtonClick: function(button, e, eOpts) {
        storageConstants.workingGrid = 'storageMonGrid';

        Ext.getCmp('storageCenterContainer').layout.setActiveItem(2);
        this.setStorageMonData();
    },

    onStorageOsdButtonClick: function(button, e, eOpts) {
        storageConstants.workingGrid = 'storageOsdGrid';

        Ext.getCmp('storageCenterContainer').layout.setActiveItem(3);
        this.setStorageOsdData();
    },

    onStoragePoolButtonClick: function(button, e, eOpts) {
        storageConstants.workingGrid = 'storagePoolGrid';

        Ext.getCmp('storageCenterContainer').layout.setActiveItem(4);
        this.setStoragePoolData();

    },

    onStoragePgButtonClick: function(button, e, eOpts) {
        storageConstants.workingGrid = 'storagePgGrid';

        Ext.getCmp('storageCenterContainer').layout.setActiveItem(5);
        this.setStoragePgData();
    },

    onStorageUsageButtonClick: function(button, e, eOpts) {
        storageConstants.workingGrid = 'storageUsageGrid';

        Ext.getCmp('storageCenterContainer').layout.setActiveItem(6);
        this.setStorageUsageData();
    },

    onStoragePoolGridpanelCellClick: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        storageConstants.selectRow = record;
        storageConstants.selectIndex = rowIndex;

        var gId = Ext.getCmp(storageConstants.workingGrid).getStore().getAt(rowIndex).get('id');
        var gNm = Ext.getCmp(storageConstants.workingGrid).getStore().getAt(rowIndex).get('name');

        if (gId !== null) {
            Ext.Ajax.request({
                url: GLOBAL.urlPrefix + "ceph/grid/pooldetail/" + gId,
                disableCaching : true,
                success: function(response){
                    var data = Ext.decode(response.responseText);
                    var detail = data.data[0];
                    detail.name = gNm;

                    Ext.getCmp("storagePoolDetail").update(detail);
                }
            });
        }

    },

    onMonAddButtonClick: function(button, e, eOpts) {
        //Add Popup 호출

        storageConstants.editMode = 'add';
        var AddWindow = Ext.create('widget.monAddWindow');
        AddWindow.title = 'Add MON';

        var myForm = Ext.getCmp("monAddFormPanel");
        var userField = myForm.getForm().findField("monAddUser");
        var passField = myForm.getForm().findField("monAddPass");
        userField.show();
        passField.show();

        AddWindow.show();

    },

    onOsdAddButtonClick: function(button, e, eOpts) {
        //Add Popup 호출

        storageConstants.editMode = 'add';
        var AddWindow = Ext.create('widget.osdAddWindow');
        AddWindow.title = 'Add OSD';
        AddWindow.show();

    },

    onPoolAddButtonClick: function(button, e, eOpts) {
        //Add Popup 호출

        storageConstants.editMode = 'add';
        var AddWindow = Ext.create('widget.poolAddWindow');
        AddWindow.title = 'Add POOL';

        var myForm = Ext.getCmp("poolAddFormPanel");
        var nameField = myForm.getForm().findField("poolAddName");
        var sizeField = myForm.getForm().findField("poolAddSize");
        var pgnmField = myForm.getForm().findField("poolAddPgNum");

        nameField.setValue('');
        sizeField.setValue(3);
        pgnmField.setValue(8);

        AddWindow.show();

    },

    onMonGridpanelBeforeItemContextMenu: function(dataview, record, item, index, e, eOpts) {
        var position = e.getXY();
        e.stopEvent();

        storageConstants.selectRow = record;
        storageConstants.selectIndex = index;

        //storageConstants.monContextMenu.showAt(position);
    },

    onOsdGridpanelBeforeItemContextMenu: function(dataview, record, item, index, e, eOpts) {
        var position = e.getXY();
        e.stopEvent();

        storageConstants.selectRow = record;
        storageConstants.selectIndex = index;

        storageConstants.osdContextMenu.showAt(position);
    },

    onPoolGridpanelBeforeItemContextMenu: function(dataview, record, item, index, e, eOpts) {
        var position = e.getXY();
        e.stopEvent();

        storageConstants.selectRow = record;
        storageConstants.selectIndex = index;

        storageConstants.poolContextMenu.showAt(position);
    },

    setStorageMainData: function() {
        Ext.getCmp("storageMainGrid").getStore().load();

        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "ceph/grid/cephstatus",
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);

                var htmlData = '<pre>\r\n';
                htmlData += data.data + '\r\n';
                htmlData += '</pre>';

                Ext.getCmp("storageMainDetail").update(htmlData);
                Ext.getCmp("storageMainDetail").updateLayout();
            }
        });

        this.setStorageTopButtonsText();
    },

    setStorageHostData: function() {
        Ext.getCmp(storageConstants.workingGrid).getStore().load();
        this.setStorageHostButtonText();
    },

    setStorageMonData: function() {
        Ext.getCmp(storageConstants.workingGrid).getStore().load();

        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "ceph/grid/mondetail",
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);
                Ext.getCmp("storageMonDetail").update(data.data);
            }
        });

        this.setStorageMonButtonText();
    },

    setStorageOsdData: function() {
        Ext.getCmp(storageConstants.workingGrid).getStore().load();

        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "ceph/grid/osdlist",
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);
                Ext.getCmp("storageOsdDetail").update(data.data);
            }
        });

        this.setStorageOsdButtonText();
    },

    setStoragePoolData: function() {
        Ext.getCmp(storageConstants.workingGrid).getStore().load({
            callback: function(records, operation, success){
                if (this.count() > 0) {
                    var myGrid = Ext.getCmp(storageConstants.workingGrid);
                    myGrid.selModel.select(0);
                    myGrid.fireEvent('cellclick', myGrid, null, 0, 0, null, 0);
                }
            }
        });
        this.setStoragePoolButtonText();
    },

    setStoragePgData: function() {
        Ext.getCmp(storageConstants.workingGrid).getStore().load();

        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "ceph/grid/pgdetail",
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);
                Ext.getCmp("storagePgDetail").update(data.data[0]);
            }
        });

        this.setStoragePgButtonText();
    },

    setStorageUsageData: function() {
        //Ext.getCmp(storageConstants.workingGrid).getStore().loadPage(1);
        Ext.getCmp(storageConstants.workingGrid).getStore().load();

        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "ceph/grid/usagedetail",
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);
                Ext.getCmp("storageUsageDetail1").update(data.data[0]);
            }
        });

        var myChart = Ext.getCmp('storageUsageDetailChart');
        myChart.getStore().load();

        this.setStorageUsageButtonText();
    },

    setStorageTopButtonsText: function() {
        this.setStorageHostButtonText();
        this.setStorageMonButtonText();
        this.setStorageOsdButtonText();
        this.setStoragePoolButtonText();
        this.setStoragePgButtonText();
        this.setStorageUsageButtonText();

    },

    setStorageHostButtonText: function() {
        Ext.Ajax.request({
            url: Ext.getCmp('storageHostGrid').getStore().getProxy().url,
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);

                var totalCnt = 0;
                var activeCnt = 0;

                Ext.each(data.data, function(list){
                    totalCnt++;
                    if (list.running === true){
                        activeCnt++;
                    }
                });

                var textData = "HOST<br>" + activeCnt.toString() + " / " + totalCnt.toString() + "<br>Running";
                Ext.getCmp("storageHostButton").setText(textData);
            }
        });

    },

    setStorageMonButtonText: function() {
        Ext.Ajax.request({
            url: Ext.getCmp('storageMonGrid').getStore().getProxy().url,
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);

                var totalCnt = 0;
                var activeCnt = 0;

                Ext.each(data.data, function(list){
                    totalCnt++;
                    if (list.in_quorum === true){
                        activeCnt++;
                    }
                });

                var textData = "MON<br>" + activeCnt.toString() + " / " + totalCnt.toString() + "<br>Quorum";
                Ext.getCmp("storageMonButton").setText(textData);
            }
        });

    },

    setStorageOsdButtonText: function() {
        Ext.Ajax.request({
            url: Ext.getCmp('storageOsdGrid').getStore().getProxy().url,
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);

                var totalCnt = 0;
                var activeCnt = 0;

                Ext.each(data.data, function(list){
                    totalCnt++;
                    if ((list.status == 'up/in') || (list.status == 'in/up')){
                        activeCnt++;
                    }
                });

                var textData = "OSD<br>" + activeCnt.toString() + " / " + totalCnt.toString() + "<br>In & Up";
                Ext.getCmp("storageOsdButton").setText(textData);
            }
        });

    },

    setStoragePoolButtonText: function() {
        Ext.Ajax.request({
            url: Ext.getCmp('storagePoolGrid').getStore().getProxy().url,
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);

                var totalCnt = 0;
                var activeCnt = 0;

                if (Ext.isArray(data.data)) {
                    totalCnt = data.data.length;
                }

                var textData = "POOL<br>" + totalCnt.toString() + "<br>Active";
                Ext.getCmp("storagePoolButton").setText(textData);
            }
        });

    },

    setStoragePgButtonText: function() {
        Ext.Ajax.request({
            url: Ext.getCmp('storagePgGrid').getStore().getProxy().url,
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);

                var totalCnt = 0;
                var activeCnt = 0;

                Ext.each(data.data, function(list){
                    totalCnt += list.num;
                    if (list.name == 'active+clean'){
                        activeCnt += list.num;
                    }
                });

                var textData = "PG Status<br>" + activeCnt.toString() + " / " + totalCnt.toString() + "<br>Active/Clean";
                Ext.getCmp("storagePgButton").setText(textData);
            }
        });

    },

    setStorageUsageButtonText: function() {
        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "ceph/grid/usagedetail",
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);

                var totalCnt = 0;
                var activeCnt = 0;
                var units = '';

                Ext.each(data.data, function(list){
                    totalCnt += list.total_kb;
                    activeCnt += list.total_kb_used;
                });

                if (totalCnt > 1073741824){
                    totalCnt  /= 1073741824;  // Kb -> Tb (1024 * 1024 * 1024)
                    activeCnt /= 1073741824;  // Kb -> Tb (1024 * 1024 * 1024)
                    units = 'Tb';
                } else {
                    totalCnt  /= 1048576;  // Kb -> Gb (1024 * 1024)
                    activeCnt /= 1048576;     // Kb -> Gb (1024 * 1024)
                    units = 'Gb';
                }

                var textData = "Usage<br>&nbsp;<br>" + activeCnt.toFixed(1) + units + " / " + totalCnt.toFixed(1) + units;
                Ext.getCmp("storageUsageButton").setText(textData);
            }
        });

    },

    addStorageMon: function() {
        //Add Mon Execute

        var myForm = Ext.getCmp("monAddFormPanel");
        if (myForm.getForm().isValid() !== true) {
            Ext.MessageBox.alert('알림', '유효하지 않은 입력값이 존재합니다.');
            return;
        }

        var host = myForm.getForm().findField("monAddHost").getValue();
        var user = myForm.getForm().findField("monAddUser").getValue();
        var pass = myForm.getForm().findField("monAddPass").getValue();

        var params = 'host=' + host + '&' + 'user=' + user + '&' + 'password=' + pass + '&' + 'mgmt=' + storageConstants.mgmtHost;


        var myGrid = Ext.getCmp("storageMonGrid");
        var myStore = myGrid.getStore();
        var myName = '';

        for (var i = 0; i < myStore.count(); i++) {
            myName = myStore.getAt(i).get('server');
            if (myName == host) {
                alert('Host (' + host + ') already exist.');
                myForm.getForm().findField("monAddHost").focus();
                return;
            }
        }


        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "ceph/mon/add?" + params,
            method : "GET",
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);
                if (data.success === true) {
                    storageConstants.me.setStorageMonData();
                    myForm.up('window').close();
                    storageConstants.me.setStorageMonButtonText();
                } else {
                    Ext.Msg.alert({
                        title: "MON Add Failure",
                        msg: data.msg,
                        buttons: Ext.Msg.OK,
                        fn: function(choice) {
                            myForm.getForm().findField("monAddHost").focus();
                        },
                        icon: Ext.Msg.ERROR
                    });
                }
            }
        });

    },

    addStorageOsd: function() {
        //Add Osd Execute

        var myForm = Ext.getCmp("osdAddFormPanel");
        if (myForm.getForm().isValid() !== true) {
            Ext.MessageBox.alert('알림', '유효하지 않은 입력값이 존재합니다.');
            return;
        }

        var host = myForm.getForm().findField("osdAddHost").getValue();
        var path = myForm.getForm().findField("osdAddPath").getValue();

        var params = 'host=' + host + '&' + 'path=' + path;

        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "ceph/osd/add?" + params,
            method : "GET",
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);
                if (data.success === true) {
                    storageConstants.me.setStorageOsdData();
                    myForm.up('window').close();
                    storageConstants.me.setStorageOsdButtonText();
                } else {
                    Ext.Msg.alert({
                        title: "OSD Add Failure",
                        msg: data.msg,
                        buttons: Ext.Msg.OK,
                        fn: function(choice) {
                            myForm.getForm().findField("osdAddHost").focus();
                        },
                        icon: Ext.Msg.ERROR
                    });
                }
            }
        });

    },

    addStoragePool: function() {
        //Add Pool Execute

        var myForm = Ext.getCmp("poolAddFormPanel");
        if (myForm.getForm().isValid() !== true) {
            Ext.MessageBox.alert('알림', '유효하지 않은 입력값이 존재합니다.');
            return;
        }

        var name = myForm.getForm().findField("poolAddName").getValue();
        var size = myForm.getForm().findField("poolAddSize").getValue();
        var pgnm = myForm.getForm().findField("poolAddPgNum").getValue();

        var params = '';

        if (storageConstants.editMode == 'add') {
            var myGrid = Ext.getCmp("storagePoolGrid");
            var myStore = myGrid.getStore();
            var myName = '';

            for (var i = 0; i < myStore.count(); i++) {
                myName = myStore.getAt(i).get('name');
                if (myName == name) {
                    alert('Pool name(' + name + ') already exist.');
                    myForm.getForm().findField("poolAddName").focus();
                    return;
                }
            }

            params = 'pool=' + name + '&' + 'pg_num=' + pgnm;

            Ext.Ajax.request({
                url: GLOBAL.urlPrefix + "ceph/pool/create?" + params,
                method : "GET",
                disableCaching : true,
                success: function(response){
                    var data = Ext.decode(response.responseText);

                    if (data.success === true) {
                        params = 'name=' + name + '&' + 'size=' + size;
                        Ext.Ajax.request({
                            url: GLOBAL.urlPrefix + "ceph/pool/set/size?" + params,
                            method : "GET",
                            disableCaching : true,
                            success: function(response){
                                var data = Ext.decode(response.responseText);

                                if (data.success !== true) {
                                    Ext.Msg.alert({
                                        title: "Failure",
                                        msg: data.msg,
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.ERROR
                                    });
                                }

                                storageConstants.me.setStoragePoolData();
                                myForm.up('window').close();
                                storageConstants.me.setStoragePoolButtonText();
                            }
                        });
                    } else {
                        Ext.Msg.alert({
                            title: "POOL Add Failure",
                            msg: data.msg,
                            buttons: Ext.Msg.OK,
                            fn: function(choice) {
                                myForm.getForm().findField("poolAddName").focus();
                            },
                            icon: Ext.Msg.ERROR
                        });
                    }
                }
            });
        } else {
            var oName = storageConstants.selectRow.get('name');
            var oSize = storageConstants.selectRow.get('size');
            var oPgnm = storageConstants.selectRow.get('pg_num');

            if (oName != name) {
                var myGrid = Ext.getCmp("storagePoolGrid");
                var myStore = myGrid.getStore();
                var myName = '';

                for (var i = 0; i < myStore.count(); i++) {
                    myName = myStore.getAt(i).get('name');
                    if (myName == name) {
                        alert('Pool name(' + name + ') already exist.');
                        myForm.getForm().findField("poolAddName").focus();
                        return;
                    }
                }

                params = 'name=' + oName + '&' + 'srcpool=' + oName + '&' + 'destpool=' + name;
                Ext.Ajax.request({
                    url: GLOBAL.urlPrefix + "ceph/pool/rename?" + params,
                    method : "GET",
                    disableCaching : true,
                    success: function(response){
                        var data = Ext.decode(response.responseText);

                        if (data.success !== true) {
                            Ext.Msg.alert({
                                title: "POOL Rename Failure",
                                msg: data.msg,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.ERROR
                            });
                        } else {
                            storageConstants.me.setStoragePoolData();
                            storageConstants.me.setStoragePoolButtonText();
                        }
                    }
                });
            }

            if (oSize != size) {
                params = 'name=' + name + '&' + 'size=' + size;
                Ext.Ajax.request({
                    url: GLOBAL.urlPrefix + "ceph/pool/set/size?" + params,
                    method : "GET",
                    disableCaching : true,
                    success: function(response){
                        var data = Ext.decode(response.responseText);

                        if (data.success !== true) {
                            Ext.Msg.alert({
                                title: "POOL Setting Size Failure",
                                msg: data.msg,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.ERROR
                            });
                        } else {
                            storageConstants.me.setStoragePoolData();
                            storageConstants.me.setStoragePoolButtonText();
                        }
                    }
                });
            }

            if (oPgnm != pgnm) {
                params = 'name=' + name + '&' + 'pg_num=' + pgnm;
                Ext.Ajax.request({
                    url: GLOBAL.urlPrefix + "ceph/pool/set/pg_num?" + params,
                    method : "GET",
                    disableCaching : true,
                    success: function(response){
                        var data = Ext.decode(response.responseText);

                        if (data.success !== true) {
                            Ext.Msg.alert({
                                title: "POOL Setting PG Num Failure",
                                msg: data.msg,
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.ERROR
                            });
                        } else {
                            storageConstants.me.setStoragePoolData();
                            storageConstants.me.setStoragePoolButtonText();
                        }
                    }
                });
            }

            myForm.up('window').close();
        }

    },

    deleteStorageMon: function() {
        var name = storageConstants.selectRow.get('name');
        var params = 'name=' + name;

        Ext.Msg.show({
            title:'Confirm',
            msg: 'Delete selected MON?',
            buttons: Ext.Msg.OKCANCEL,
            icon: Ext.Msg.QUESTION,
            fn: function(btn) {
                if (btn === 'ok') {
                    Ext.Ajax.request({
                        url: GLOBAL.urlPrefix + "ceph/mon/remove?" + params,
                        method : "GET",
                        disableCaching : true,
                        success: function(response){
                            var data = Ext.decode(response.responseText);

                            if (data.success === true) {
                                storageConstants.me.setStorageMonData();
                                storageConstants.me.setStorageMonButtonText();
                            } else {
                                Ext.Msg.alert({
                                    title: "MON Delete Failure",
                                    msg: data.msg,
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                });
                            }
                        }
                    });
                }
            }
        });

    },

    deleteStorageOsd: function() {
        var host = storageConstants.selectRow.get('host');
        var id = storageConstants.selectRow.get('id');  // name으로 바꿔야될 것 같음(api 수정 확인)

        var params = 'host=' + host + '&' + 'id=' + id;

        Ext.Msg.show({
            title:'Confirm',
            msg: 'Delete selected OSD?',
            buttons: Ext.Msg.OKCANCEL,
            icon: Ext.Msg.QUESTION,
            fn: function(btn) {
                if (btn === 'ok') {
                    Ext.Ajax.request({
                        url: GLOBAL.urlPrefix + "ceph/osd/delete?" + params,
                        method : "GET",
                        disableCaching : true,
                        success: function(response){
                            var data = Ext.decode(response.responseText);

                            if (data.success === true) {
                                storageConstants.me.setStorageOsdData();
                                storageConstants.me.setStorageOsdButtonText();
                            } else {
                                Ext.Msg.alert({
                                    title: "OSD Delete Failure",
                                    msg: data.msg,
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                });
                            }
                        }
                    });
                }
            }
        });

    },

    editStoragePool: function() {
        //Add Popup 호출(title = Edit)

        storageConstants.editMode = 'edit';
        var AddWindow = Ext.create('widget.poolAddWindow');
        AddWindow.title = 'Edit POOL';

        var myForm = Ext.getCmp("poolAddFormPanel");
        var nameField = myForm.getForm().findField("poolAddName");
        var sizeField = myForm.getForm().findField("poolAddSize");
        var pgnmField = myForm.getForm().findField("poolAddPgNum");

        var name = storageConstants.selectRow.get('name');
        var size = storageConstants.selectRow.get('size');
        var pgnm = storageConstants.selectRow.get('pg_num');

        nameField.setValue(name);
        sizeField.setValue(size);
        pgnmField.setValue(pgnm);

        AddWindow.show();

    },

    deleteStoragePool: function() {
        var name = storageConstants.selectRow.get('name');

        Ext.Msg.show({
            title:'Confirm',
            msg: 'Delete selected POOL?',
            buttons: Ext.Msg.OKCANCEL,
            icon: Ext.Msg.QUESTION,
            fn: function(btn) {
                if (btn === 'ok') {

                    Ext.Ajax.request({
                        url: GLOBAL.urlPrefix + "ceph/pool/delete?pool=" + name,
                        method : "GET",
                        disableCaching : true,
                        success: function(response){
                            var data = Ext.decode(response.responseText);

                            if (data.success === true) {
                                storageConstants.me.setStoragePoolData();
                                storageConstants.me.setStoragePoolButtonText();
                            } else {
                                Ext.Msg.alert({
                                    title: "POOL Delete Failure",
                                    msg: data.msg,
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                });
                            }
                        }
                    });
                }
            }
        });

    },

    setGraphiteUrl: function() {
        storageConstants.mgmtHost = '';
        storageConstants.griphiteUrl1 = 'http://192.168.0.227/render?from=-2hours&until=now&width=800&height=450&hideLegend=true&target=ceph.cluster.a82efafc-bfa3-473e-92f6-25719386b673.pool.all.num_objects';
        storageConstants.griphiteUrl2 = 'http://192.168.0.227/render?from=-2hours&until=now&width=800&height=450&hideLegend=true&target=ceph.cluster.a82efafc-bfa3-473e-92f6-25719386b673.df.total_used_bytes';

        var clusterId = '';

        Ext.Ajax.request({
            url: GLOBAL.urlPrefix + "ceph/grid/cluster",
            method : "GET",
            disableCaching : true,
            success: function(response){
                var data = Ext.decode(response.responseText);
                if (data.success === true) {
                    clusterId = data.data.clusterId;

                    Ext.Ajax.request({
                        url: GLOBAL.urlPrefix + "ceph/grid/cephInfo",
                        method : "GET",
                        disableCaching : true,
                        success: function(response){
                            var data = Ext.decode(response.responseText);
                            if (data.success === true) {
                                storageConstants.mgmtHost = data.data.mgmtHost;

                                var calamariApi = data.data.calamariApiPrefix;
                                var iPos = calamariApi.indexOf('/api');
                                calamariApi = calamariApi.substring(0, iPos);
                                calamariApi += '/render?from=-2hours&until=now&width=800&height=450&hideLegend=true&lineMode=connected&target=';

                                storageConstants.griphiteUrl1 = calamariApi + 'ceph.cluster.' + clusterId + '.pool.all.num_objects';
                                storageConstants.griphiteUrl2 = calamariApi + 'ceph.cluster.' + clusterId + '.df.total_used_bytes';

                                var sUniq = '&_uniq=' + Date.now();
                                Ext.getCmp('storageHostImg1').setSrc(storageConstants.griphiteUrl1 + sUniq);
                                Ext.getCmp('storageHostImg2').setSrc(storageConstants.griphiteUrl2 + sUniq);
                            }
                        }
                    });
                }
            }
        });

    },

    init: function(application) {
                var storage = this;

                var monGridContextMenu = new Ext.menu.Menu({
                    items:
                    [
                    /*
                    { text: 'Edit',
                    handler: function() {
                    storage.editStorageMon();
                    }
                    },
                    */
                    { text: 'Delete',
                        handler: function() {
                            storage.deleteStorageMon();
                        }
                    }
                    ]
                });

                var poolGridContextMenu = new Ext.menu.Menu({
                    items:
                    [
                    { text: 'Edit',
                        handler: function() {
                            storage.editStoragePool();
                        }
                    },
                    { text: 'Delete',
                        handler: function() {
                            storage.deleteStoragePool();
                        }
                    }
                    ]
                });

                var osdGridContextMenu = new Ext.menu.Menu({
                    items:
                    [
                    { text: 'Delete',
                        handler: function() {
                            storage.deleteStorageOsd();
                        }
                    }
                    ]
                });

                Ext.define('storageConstants', {
                    singleton: true,
                    me : storage,
                    task: null,
                    griphiteUrl1: '',
                    griphiteUrl2: '',
                    mgmtHost: '',
                    chartInterval : 60,
                    monContextMenu: monGridContextMenu,
                    poolContextMenu: poolGridContextMenu,
                    osdContextMenu: osdGridContextMenu,
                    workingGrid: '',
                    editMode: 'add',
                    selectRow:  null,
                    selectIndex: 0
                });

                storage.setGraphiteUrl();

                var runner = new Ext.util.TaskRunner();
                var task = runner.newTask({
                    run: function(){
                        var sUniq = '&_uniq=' + Date.now();
                        Ext.getCmp('storageHostImg1').setSrc(storageConstants.griphiteUrl1 + sUniq);
                        Ext.getCmp('storageHostImg2').setSrc(storageConstants.griphiteUrl2 + sUniq);
                    },
                    interval: (storageConstants.chartInterval * 1000)
                });

                storageConstants.task = task;


        this.control({
            "#storageMainContainer": {
                activate: this.onStorageMainContainerActivate
            },
            "#storageHostContainer": {
                show: this.onStorageHostContainerShow,
                hide: this.onStorageHostContainerHide
            },
            "#storageHostButton": {
                click: this.onStorageHostButtonClick
            },
            "#storageMonButton": {
                click: this.onStorageMonButtonClick
            },
            "#storageOsdButton": {
                click: this.onStorageOsdButtonClick
            },
            "#storagePoolButton": {
                click: this.onStoragePoolButtonClick
            },
            "#storagePgButton": {
                click: this.onStoragePgButtonClick
            },
            "#storageUsageButton": {
                click: this.onStorageUsageButtonClick
            },
            "#storagePoolGrid": {
                cellclick: this.onStoragePoolGridpanelCellClick,
                beforeitemcontextmenu: this.onPoolGridpanelBeforeItemContextMenu
            },
            "#storageMonAdd": {
                click: this.onMonAddButtonClick
            },
            "#storageOsdAdd": {
                click: this.onOsdAddButtonClick
            },
            "#storagePoolAdd": {
                click: this.onPoolAddButtonClick
            },
            "#storageMonGrid": {
                beforeitemcontextmenu: this.onMonGridpanelBeforeItemContextMenu
            },
            "#storageOsdGrid": {
                beforeitemcontextmenu: this.onOsdGridpanelBeforeItemContextMenu
            }
        });
    }

});
