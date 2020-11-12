(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-cbb92fb4"],{"04fc":function(e,t,a){},"232c":function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticStyle:{"margin-top":"10px",display:"flex","justify-content":"center"}},[a("el-input",{staticStyle:{width:"400px","margin-right":"10px"},attrs:{placeholder:"通过用户名搜索用户...","prefix-icon":"el-icon-search"},nativeOn:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.doSearch(t)}},model:{value:e.keywords,callback:function(t){e.keywords=t},expression:"keywords"}}),a("el-button",{attrs:{icon:"el-icon-search",type:"primary"},on:{click:e.doSearch}},[e._v("搜索")])],1),a("div",{staticStyle:{"margin-top":"10px"}},[a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticStyle:{width:"100%"},attrs:{data:e.users,stripe:"",border:"","element-loading-text":"正在加载...","element-loading-spinner":"el-icon-loading","element-loading-background":"rgba(0, 0, 0, 0.8)"}},[a("el-table-column",{attrs:{type:"selection",width:"55"}}),a("el-table-column",{attrs:{fixed:"",prop:"id",label:"ID",align:"left",width:"50"}}),a("el-table-column",{attrs:{prop:"name",align:"left",label:"姓名",width:"100"}}),a("el-table-column",{attrs:{prop:"phone",label:"电话",width:"150"}}),a("el-table-column",{attrs:{prop:"username",label:"邮箱",width:"150"}}),a("el-table-column",{attrs:{label:"角色"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._l(t.row.roles,(function(t,i){return a("el-tag",{key:i,staticStyle:{"margin-right":"4px"},attrs:{type:"success"}},[e._v(e._s(t.nameZh)+"\n          ")])})),a("el-popover",{attrs:{placement:"right",title:"角色列表",width:"200",trigger:"click"},on:{show:function(a){return e.showPop(t.row)},hide:function(a){return e.hidePop(t.row)}}},[a("el-select",{attrs:{multiple:"",placeholder:"请选择"},model:{value:e.selectedRoles,callback:function(t){e.selectedRoles=t},expression:"selectedRoles"}},e._l(e.allroles,(function(e,t){return a("el-option",{key:t,attrs:{label:e.nameZh,value:e.id}})})),1),a("el-button",{attrs:{slot:"reference",icon:"el-icon-more",type:"text"},slot:"reference"})],1)]}}])}),a("el-table-column",{attrs:{prop:"enabled",width:"160",align:"left",label:"状态"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-switch",{attrs:{"active-text":"启用","active-color":"#13ce66","inactive-color":"#ff4949","inactive-text":"禁用"},on:{change:function(a){return e.enabledChange(t.row)}},model:{value:t.row.enabled,callback:function(a){e.$set(t.row,"enabled",a)},expression:"scope.row.enabled"}})]}}])}),a("el-table-column",{attrs:{prop:"remark",width:"200",align:"left",label:"备注"}}),a("el-table-column",{attrs:{fixed:"right",width:"60",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{staticStyle:{padding:"3px"},attrs:{size:"mini",type:"danger"},on:{click:function(a){return e.deleteUser(t.row)}}},[e._v("删除")])]}}])})],1),a("div",{staticStyle:{display:"flex","justify-content":"flex-end"}},[a("el-pagination",{attrs:{background:"",layout:"sizes, prev, pager, next, jumper, ->, total, slot",total:e.total},on:{"current-change":e.currentChange,"size-change":e.sizeChange}})],1)],1)])},l=[],s=(a("ac6a"),a("7f7f"),{name:"SysUser",data:function(){return{loading:!1,total:0,page:1,keywords:"",size:10,users:[],selectedRoles:[],allroles:[]}},mounted:function(){this.initUsers()},methods:{deleteUser:function(e){var t=this;this.$confirm("此操作将永久删除【"+e.name+"】, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){t.deleteRequest("/system/user/"+e.id).then((function(e){e&&t.initUsers()}))})).catch((function(){t.$message({type:"info",message:"已取消删除"})}))},doSearch:function(){this.initUsers()},hidePop:function(e){var t=this,a=[];Object.assign(a,e.roles);var i=!1;if(a.length!=this.selectedRoles.length)i=!0;else{for(var l=0;l<a.length;l++)for(var s=a[l],n=0;n<this.selectedRoles.length;n++){var r=this.selectedRoles[n];if(s.id==r){a.splice(l,1),l--;break}}0!=a.length&&(i=!0)}if(i){var o="/system/user/role?uid="+e.id;this.selectedRoles.forEach((function(e){o+="&rids="+e})),this.putRequest(o).then((function(e){e&&t.initUsers()}))}},showPop:function(e){var t=this;this.initAllRoles();var a=e.roles;this.selectedRoles=[],a.forEach((function(e){t.selectedRoles.push(e.id)}))},enabledChange:function(e){var t=this;delete e.roles,this.putRequest("/system/user/",e).then((function(e){e&&t.initUsers()}))},initAllRoles:function(){var e=this;this.getRequest("/system/user/roles").then((function(t){t&&(e.allroles=t)}))},sizeChange:function(e){this.size=e,this.initUsers()},currentChange:function(e){this.page=e,this.initUsers()},initUsers:function(){var e=this;this.loading=!0,this.getRequest("/system/user/?keywords="+this.keywords+"&page="+this.page+"&size="+this.size).then((function(t){t&&(e.loading=!1,e.users=t.data,e.total=t.total)}))}}}),n=s,r=a("2877"),o=Object(r["a"])(n,i,l,!1,null,null,null);t["default"]=o.exports},"2ef5":function(e,t,a){"use strict";var i=a("04fc"),l=a.n(i);l.a},"864e":function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",[a("div",{staticStyle:{display:"flex","justify-content":"space-between"}},[a("div",{staticStyle:{display:"inline-flex","margin-left":"8px"}}),a("div",[a("el-input",{staticStyle:{width:"350px","margin-right":"10px"},attrs:{placeholder:"请输入操作记录编号进行搜索，可以直接回车搜索...","prefix-icon":"el-icon-search",clearable:"",disabled:e.showAdvanceSearchView},on:{clear:e.initRecords},nativeOn:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.initRecords(t)}},model:{value:e.keyword,callback:function(t){e.keyword=t},expression:"keyword"}}),a("el-button",{attrs:{icon:"el-icon-search",type:"primary",disabled:e.showAdvanceSearchView},on:{click:e.initRecords}},[e._v("\n          搜索\n        ")]),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.showAdvanceSearchView=!e.showAdvanceSearchView}}},[a("i",{class:e.showAdvanceSearchView?"fa fa-angle-double-up":"fa fa-angle-double-down",attrs:{"aria-hidden":"true"}}),e._v("\n          高级搜索\n        ")])],1)]),a("transition",{attrs:{name:"slide-fade"}},[a("div",{directives:[{name:"show",rawName:"v-show",value:e.showAdvanceSearchView,expression:"showAdvanceSearchView"}],staticStyle:{border:"1px solid #759ad1","border-radius":"5px","box-sizing":"border-box",padding:"5px",margin:"10px 0px"}},[a("el-row",[a("el-col",{attrs:{span:8}},[e._v("\n            类型:\n            "),a("el-select",{staticStyle:{width:"130px"},attrs:{clearable:"",placeholder:"类型",size:"mini"},model:{value:e.searchValue.operate,callback:function(t){e.$set(e.searchValue,"operate",t)},expression:"searchValue.operate"}},e._l(e.types,(function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1)],1),a("el-col",{attrs:{span:8}},[e._v("\n            操作人邮箱:\n            "),a("el-input",{staticStyle:{width:"150px"},attrs:{size:"mini",clearable:"","prefix-icon":"el-icon-edit"},model:{value:e.searchValue.operatorMail,callback:function(t){e.$set(e.searchValue,"operatorMail",t)},expression:"searchValue.operatorMail"}})],1)],1),a("el-row",{staticStyle:{"margin-top":"10px"}},[a("el-col",{attrs:{span:8}},[e._v("\n            操作说明:\n            "),a("el-input",{staticStyle:{width:"200px"},attrs:{size:"mini","prefix-icon":"el-icon-edit",clearable:""},model:{value:e.searchValue.text,callback:function(t){e.$set(e.searchValue,"text",t)},expression:"searchValue.text"}})],1),a("el-col",{attrs:{span:8}},[e._v("\n            申请时间:\n            "),a("el-date-picker",{attrs:{type:"daterange",size:"mini","unlink-panels":"","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:e.searchValue.dateScope,callback:function(t){e.$set(e.searchValue,"dateScope",t)},expression:"searchValue.dateScope"}})],1),a("el-col",{attrs:{span:4,offset:4}},[a("el-button",{attrs:{size:"mini"},on:{click:e.clearSearchValue}},[e._v("重置")]),a("el-button",{attrs:{size:"mini"},on:{click:function(t){e.showAdvanceSearchView=!1}}},[e._v("取消")]),a("el-button",{attrs:{size:"mini",icon:"el-icon-search",type:"primary"},on:{click:e.initRecordsAdv}},[e._v("搜索")])],1)],1)],1)])],1),a("div",{staticStyle:{"margin-top":"10px"}},[a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticStyle:{width:"100%"},attrs:{data:e.records,stripe:"",border:"","element-loading-text":"正在加载...","element-loading-spinner":"el-icon-loading","element-loading-background":"rgba(0, 0, 0, 0.8)"}},[a("el-table-column",{attrs:{fixed:"",prop:"id",label:"操作记录编号",width:"100"}}),a("el-table-column",{attrs:{fixed:"",prop:"operateName",label:"操作类型",width:"100"}}),a("el-table-column",{attrs:{prop:"operatorName",label:"操作人",width:"100"}}),a("el-table-column",{attrs:{prop:"operatorMail",label:"操作人邮箱",width:"150"}}),a("el-table-column",{attrs:{prop:"text","show-overflow-tooltip":!0,label:"操作说明"}}),a("el-table-column",{attrs:{prop:"operateTime",width:"150",align:"left",label:"操作时间"}})],1),a("div",{staticStyle:{display:"flex","justify-content":"flex-end"}},[a("el-pagination",{attrs:{background:"",layout:"sizes, prev, pager, next, jumper, ->, total, slot",total:e.total},on:{"current-change":e.currentChange,"size-change":e.sizeChange}})],1)],1)])},l=[],s={name:"SysLog",data:function(){return{searchValue:{operate:null,operatorMail:null,text:null,dateScope:null},title:"",showAdvanceSearchView:!1,records:[],loading:!1,total:0,page:1,keyword:"",size:10,types:[{id:1,name:"修改资产信息"},{id:2,name:"修改资产状态"},{id:3,name:"资产采购申请"},{id:4,name:"资产借用申请"},{id:5,name:"编辑工单信息"},{id:6,name:"删除工单"},{id:7,name:"取消工单"},{id:8,name:"处理工单"}],record:{id:"",operate:"",operateName:"",operator:"",operatorMail:"",text:"",operateTime:""},type:""}},mounted:function(){this.initRecords()},methods:{sizeChange:function(e){this.size=e,this.type&&"advanced"==this.type?this.initRecordsAdv():this.initRecords()},currentChange:function(e){this.page=e,this.type&&"advanced"==this.type?this.initRecordsAdv():this.initRecords()},initRecords:function(){var e=this;this.type="",this.loading=!0;var t="/record/get/?page="+this.page+"&size="+this.size+"&id="+this.keyword;this.getRequest(t).then((function(t){e.loading=!1,t&&(e.records=t.data,e.total=t.total)}))},initRecordsAdv:function(){var e=this;this.type="advanced",this.loading=!0;var t="/record/get/?page="+this.page+"&size="+this.size;this.searchValue.operate&&(t+="&operate="+this.searchValue.operate),this.searchValue.operatorMail&&(t+="&operatorMail="+this.searchValue.operatorMail),this.searchValue.text&&(t+="&text="+this.searchValue.text),this.searchValue.dateScope&&(t+="&dateScope="+this.searchValue.dateScope),this.getRequest(t).then((function(t){e.loading=!1,t&&(e.records=t.data,e.total=t.total)}))},clearSearchValue:function(){this.searchValue={operate:null,operatorMail:null,text:null,dateScope:null}}}},n=s,r=a("2877"),o=Object(r["a"])(n,i,l,!1,null,"28a5fec6",null);t["default"]=o.exports},d32a:function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{directives:[{name:"loading",rawName:"v-loading",value:e.globalLoading,expression:"globalLoading"}],attrs:{"element-loading-text":"正在添加...","element-loading-spinner":"el-icon-loading","element-loading-background":"rgba(0, 0, 0, 0.8)"}},[a("div",{staticClass:"permissManaTool"},[a("el-input",{attrs:{size:"small",placeholder:"请输入角色英文名"},model:{value:e.role.name,callback:function(t){e.$set(e.role,"name",t)},expression:"role.name"}},[a("template",{slot:"prepend"},[e._v("ROLE_")])],2),a("el-input",{attrs:{size:"small",placeholder:"请输入角色中文名"},nativeOn:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.doAddRole(t)}},model:{value:e.role.nameZh,callback:function(t){e.$set(e.role,"nameZh",t)},expression:"role.nameZh"}}),a("el-button",{attrs:{type:"primary",size:"small",icon:"el-icon-plus"},on:{click:e.doAddRole}},[e._v("添加角色")])],1),a("div",{staticClass:"permissManaMain"},[a("el-collapse",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{"element-loading-text":"正在加载...","element-loading-spinner":"el-icon-loading","element-loading-background":"rgba(0, 0, 0, 0.8)",accordion:""},on:{change:e.change},model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},e._l(e.roles,(function(t,i){return a("el-collapse-item",{key:i,attrs:{title:t.nameZh,name:t.id}},[a("el-card",{staticClass:"box-card"},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("可访问的资源")]),a("el-button",{staticStyle:{float:"right",padding:"3px 0",color:"#ff0000"},attrs:{icon:"el-icon-delete",type:"text"},on:{click:function(a){return e.deleteRole(t)}}})],1),a("div",[a("el-tree",{key:i,ref:"tree",refInFor:!0,attrs:{"show-checkbox":"","node-key":"id","default-checked-keys":e.selectedMenus,data:e.allmenus,props:e.defaultProps}}),a("div",{staticStyle:{display:"flex","justify-content":"flex-end"}},[a("el-button",{on:{click:e.cancelUpdate}},[e._v("取消修改")]),a("el-button",{attrs:{type:"primary"},on:{click:function(a){return e.doUpdate(t.id,i)}}},[e._v("确认修改")])],1)],1)])],1)})),1)],1)])},l=[],s=(a("ac6a"),a("7f7f"),{name:"SysPermission",data:function(){return{role:{name:"",nameZh:""},allmenus:[],activeName:-1,selectedMenus:[],roles:[],loading:!1,globalLoading:!1,defaultProps:{children:"children",label:"name"}}},mounted:function(){this.initRoles()},methods:{deleteRole:function(e){var t=this;this.$confirm("此操作将永久删除【"+e.nameZh+"】角色, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){t.deleteRequest("/system/basic/permiss/role/"+e.id).then((function(e){e&&t.initRoles()}))})).catch((function(){t.$message({type:"info",message:"已取消删除"})}))},doAddRole:function(){var e=this;this.role.name&&this.role.nameZh?(this.globalLoading=!0,this.postRequest("/system/basic/permiss/role",this.role).then((function(t){e.globalLoading=!1,t&&(e.role.name="",e.role.nameZh="",e.initRoles())}))):this.$message.error("数据不可以为空")},cancelUpdate:function(){this.activeName=-1},doUpdate:function(e,t){var a=this,i=this.$refs.tree[t],l=i.getCheckedKeys(!0),s="/system/basic/permiss/?rid="+e;l.forEach((function(e){s+="&mids="+e})),this.putRequest(s).then((function(e){e&&(a.activeName=-1)}))},change:function(e){e&&(this.initAllMenus(),this.initSelectedMenus(e))},initSelectedMenus:function(e){var t=this;this.getRequest("/system/basic/permiss/mids/"+e).then((function(e){e&&(t.selectedMenus=e)}))},initAllMenus:function(){var e=this;this.getRequest("/system/basic/permiss/menus").then((function(t){t&&(e.allmenus=t)}))},initRoles:function(){var e=this;this.loading=!0,this.getRequest("/system/basic/permiss/").then((function(t){e.loading=!1,t&&(e.roles=t)}))}}}),n=s,r=(a("2ef5"),a("2877")),o=Object(r["a"])(n,i,l,!1,null,null,null);t["default"]=o.exports},d66d:function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",[a("div",{staticStyle:{display:"flex","justify-content":"space-between"}},[a("div",[a("el-button",{staticStyle:{display:"inline-flex","margin-left":"8px"},attrs:{type:"success",icon:"el-icon-download"},on:{click:e.exportData}},[e._v("\n          导出数据\n        ")]),a("el-button",{staticStyle:{display:"inline-flex","margin-left":"8px"},attrs:{type:"danger"},on:{click:e.deleteOrder}},[e._v("\n          删除\n        ")])],1),a("div",[a("el-input",{staticStyle:{width:"350px","margin-right":"10px"},attrs:{placeholder:"请输入订单号进行搜索，可以直接回车搜索...","prefix-icon":"el-icon-search",clearable:"",disabled:e.showAdvanceSearchView},on:{clear:e.initPurchaseOrders},nativeOn:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.initPurchaseOrders(t)}},model:{value:e.keyword,callback:function(t){e.keyword=t},expression:"keyword"}}),a("el-button",{attrs:{icon:"el-icon-search",type:"primary",disabled:e.showAdvanceSearchView},on:{click:e.initPurchaseOrders}},[e._v("\n          搜索\n        ")]),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.showAdvanceSearchView=!e.showAdvanceSearchView}}},[a("i",{class:e.showAdvanceSearchView?"fa fa-angle-double-up":"fa fa-angle-double-down",attrs:{"aria-hidden":"true"}}),e._v("\n          高级搜索\n        ")])],1)]),a("transition",{attrs:{name:"slide-fade"}},[a("div",{directives:[{name:"show",rawName:"v-show",value:e.showAdvanceSearchView,expression:"showAdvanceSearchView"}],staticStyle:{border:"1px solid #759ad1","border-radius":"5px","box-sizing":"border-box",padding:"5px",margin:"10px 0px"}},[a("el-row",[a("el-col",{attrs:{span:9}},[e._v("\n            订单名称:\n            "),a("el-input",{staticStyle:{width:"300px"},attrs:{size:"mini","prefix-icon":"el-icon-edit",clearable:""},model:{value:e.searchValue.name,callback:function(t){e.$set(e.searchValue,"name",t)},expression:"searchValue.name"}})],1),a("el-col",{attrs:{span:7}},[e._v("\n            是否有发票:\n            "),a("el-select",{staticStyle:{width:"130px"},attrs:{clearable:"",placeholder:"是否有发票",size:"mini"},model:{value:e.searchValue.hasInvoice,callback:function(t){e.$set(e.searchValue,"hasInvoice",t)},expression:"searchValue.hasInvoice"}},e._l(e.options,(function(e,t){return a("el-option",{key:t,attrs:{label:e.name,value:e.value}})})),1)],1),a("el-col",{attrs:{span:8}},[e._v("\n            创建人邮箱:\n            "),a("el-input",{staticStyle:{width:"250px"},attrs:{size:"mini",clearable:"","prefix-icon":"el-icon-edit"},model:{value:e.searchValue.creatorEmail,callback:function(t){e.$set(e.searchValue,"creatorEmail",t)},expression:"searchValue.creatorEmail"}})],1)],1),a("el-row",{staticStyle:{"margin-top":"10px"}},[a("el-col",{attrs:{span:9}},[e._v("\n            订单总价（元）:\n            "),a("el-input",{staticStyle:{width:"100px"},attrs:{size:"mini","prefix-icon":"el-icon-edit",clearable:""},model:{value:e.searchValue.totalLow,callback:function(t){e.$set(e.searchValue,"totalLow",t)},expression:"searchValue.totalLow"}}),e._v("\n            至:\n            "),a("el-input",{staticStyle:{width:"100px"},attrs:{size:"mini","prefix-icon":"el-icon-edit",clearable:""},model:{value:e.searchValue.totalHigh,callback:function(t){e.$set(e.searchValue,"totalHigh",t)},expression:"searchValue.totalHigh"}})],1),a("el-col",{attrs:{span:9}},[e._v("\n            实际支付（元）:\n            "),a("el-input",{staticStyle:{width:"100px"},attrs:{size:"mini","prefix-icon":"el-icon-edit",clearable:""},model:{value:e.searchValue.payLow,callback:function(t){e.$set(e.searchValue,"payLow",t)},expression:"searchValue.payLow"}}),e._v("\n            至:\n            "),a("el-input",{staticStyle:{width:"100px"},attrs:{size:"mini","prefix-icon":"el-icon-edit",clearable:""},model:{value:e.searchValue.payHigh,callback:function(t){e.$set(e.searchValue,"payHigh",t)},expression:"searchValue.payHigh"}})],1)],1),a("el-row",{staticStyle:{"margin-top":"10px"}},[a("el-col",{attrs:{span:9}},[e._v("\n            购买时间:\n            "),a("el-date-picker",{attrs:{type:"daterange",size:"mini","unlink-panels":"","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:e.searchValue.purchaseDateScope,callback:function(t){e.$set(e.searchValue,"purchaseDateScope",t)},expression:"searchValue.purchaseDateScope"}})],1),a("el-col",{attrs:{span:8}},[e._v("\n            发票时间:\n            "),a("el-date-picker",{attrs:{type:"daterange",size:"mini","unlink-panels":"","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:e.searchValue.invoiceDateScope,callback:function(t){e.$set(e.searchValue,"invoiceDateScope",t)},expression:"searchValue.invoiceDateScope"}})],1),a("el-col",{attrs:{span:4,offset:3}},[a("el-button",{attrs:{size:"mini"},on:{click:e.clearSearchValue}},[e._v("重置")]),a("el-button",{attrs:{size:"mini"},on:{click:function(t){e.showAdvanceSearchView=!1}}},[e._v("取消")]),a("el-button",{attrs:{size:"mini",icon:"el-icon-search",type:"primary"},on:{click:e.initPurchaseOrdersAdv}},[e._v("搜索")])],1)],1)],1)])],1),a("div",{staticStyle:{"margin-top":"10px"}},[a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticStyle:{width:"100%"},attrs:{data:e.purchases,stripe:"",border:"","element-loading-text":"正在加载...","element-loading-spinner":"el-icon-loading","element-loading-background":"rgba(0, 0, 0, 0.8)"},on:{"selection-change":e.handleSelectionChange}},[a("el-table-column",{attrs:{type:"selection",width:"55"}}),a("el-table-column",{attrs:{fixed:"",label:"订单号",align:"left",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{size:"mini"},on:{click:function(a){return e.showDetailView(t.row)}}},[e._v(e._s(t.row.id))])]}}])}),a("el-table-column",{attrs:{fixed:"",prop:"name","show-overflow-tooltip":!0,align:"left",label:"订单名称",width:"80"}}),a("el-table-column",{attrs:{fixed:"",prop:"total",align:"left",label:"订单总价（元）",width:"110"}}),a("el-table-column",{attrs:{prop:"discount",align:"left",label:"订单优惠（元）",width:"110"}}),a("el-table-column",{attrs:{prop:"pay",label:"实际支付（元）",align:"left",width:"110"}}),a("el-table-column",{attrs:{prop:"hasInvoice",width:"95",align:"left",label:"是否有发票"},scopedSlots:e._u([{key:"default",fn:function(t){return[1==t.row.hasInvoice?a("span",[e._v("是")]):a("span",[e._v("否")])]}}])}),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"关联资产编号"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._l(t.row.assetList,(function(t,i){return a("el-tag",{key:i,staticStyle:{"margin-right":"4px"},attrs:{type:"success"}},[e._v(e._s(t.id)+"\n          ")])})),a("el-popover",{attrs:{placement:"right",title:"资产编号列表",width:"200",trigger:"click"},on:{show:function(a){return e.showPop(t.row)},hide:function(a){return e.hidePop(t.row)}}},[a("el-select",{attrs:{multiple:"",filterable:"",placeholder:"请选择"},model:{value:e.selectedAids,callback:function(t){e.selectedAids=t},expression:"selectedAids"}},e._l(e.aids,(function(e,t){return a("el-option",{key:t,attrs:{label:e,value:e}})})),1),a("el-button",{attrs:{slot:"reference",icon:"el-icon-more",type:"text"},slot:"reference"})],1)]}}])}),a("el-table-column",{attrs:{prop:"remark","show-overflow-tooltip":!0,label:"订单备注",width:"150"}}),a("el-table-column",{attrs:{prop:"createTime",width:"100",align:"left",label:"创建日期"}}),a("el-table-column",{attrs:{prop:"updateTime",width:"100",align:"left",label:"更新日期"}}),a("el-table-column",{attrs:{fixed:"right",width:"80",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{on:{click:function(a){return e.showEditView(t.row)}}},[e._v("编辑")])]}}])})],1),a("div",{staticStyle:{display:"flex","justify-content":"flex-end"}},[a("el-pagination",{attrs:{background:"",layout:"sizes, prev, pager, next, jumper, ->, total, slot",total:e.total},on:{"current-change":e.currentChange,"size-change":e.sizeChange}})],1)],1),a("PurchaseOrderEdit",{attrs:{dialogVisible3:e.dialogVisible3,purchase:e.purchase,title:e.title,fileList:e.fileList},on:{close:function(t){e.dialogVisible3=!1}}}),a("PurchaseOrderDetail",{attrs:{dialogVisible2:e.dialogVisible2,purchase:e.purchase,title:e.title,urlList:e.urlList},on:{close:function(t){e.dialogVisible2=!1}}})],1)},l=[],s=(a("ac6a"),a("7f7f"),a("27fb")),n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.title,visible:e.dialogVisible2,"before-close":e.handleClose,width:"80%"},on:{"update:visible":function(t){e.dialogVisible2=t}}},[a("div",[a("el-form",{attrs:{model:e.purchase}},[a("el-row",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"订单编号:",prop:"id"}},[e._v("\n            "+e._s(e.purchase.id)+"\n          ")])],1)],1),a("el-row",[a("el-col",{attrs:{span:20}},[a("el-form-item",{attrs:{label:"订单名称:",prop:"name"}},[e._v("\n            "+e._s(e.purchase.name)+"\n          ")])],1)],1),a("el-row",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"订单总价（元）:",prop:"total"}},[e._v("\n            "+e._s(e.purchase.total)+"\n          ")])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"订单优惠（元）:",prop:"discount"}},[e._v("\n            "+e._s(e.purchase.discount)+"\n          ")])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"实际支付（元）:",prop:"pay"}},[e._v("\n            "+e._s(e.purchase.pay)+"\n          ")])],1)],1),a("el-row",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"购买日期:",prop:"purchaseDate"}},[e._v("\n            "+e._s(e.purchase.purchaseDate)+"\n          ")])],1)],1),a("el-row",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"是否有发票:",prop:"hasInvoice"}},[1==e.purchase.status?a("span",[e._v("是")]):a("span",[e._v("否")])])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"发票日期:",prop:"invoiceDate"}},[e._v("\n            "+e._s(e.purchase.invoiceDate)+"\n          ")])],1)],1),this.purchase.creator?a("el-row",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"创建人姓名:",prop:"creator.name"}},[e._v("\n            "+e._s(e.purchase.creator.name)+"\n          ")])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"创建人邮箱:",prop:"creatorEmail"}},[e._v("\n            "+e._s(e.purchase.creatorEmail)+"\n          ")])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"创建人电话:",prop:"creator.phone"}},[e._v("\n            "+e._s(e.purchase.creator.phone)+"\n          ")])],1)],1):e._e(),this.purchase.updater?a("el-row",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"更新人姓名:",prop:"updater.name"}},[e._v("\n            "+e._s(e.purchase.updater.name)+"\n          ")])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"更新人邮箱:",prop:"updaterEmail"}},[e._v("\n            "+e._s(e.purchase.updaterEmail)+"\n          ")])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"更新人电话:",prop:"updater.phone"}},[e._v("\n            "+e._s(e.purchase.updater.phone)+"\n          ")])],1)],1):e._e(),a("el-row",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"关联资产信息:"}})],1)],1),a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.purchase.assetList,stripe:"",border:"","element-loading-text":"正在加载...","element-loading-spinner":"el-icon-loading","element-loading-background":"rgba(0, 0, 0, 0.8)"}},[a("el-table-column",{attrs:{fixed:"",prop:"id",label:"资产编号",align:"left",width:"100"}}),a("el-table-column",{attrs:{fixed:"",prop:"assetName","show-overflow-tooltip":!0,align:"left",label:"资产名称"}}),a("el-table-column",{attrs:{prop:"statusName",label:"状态",width:"200"},scopedSlots:e._u([{key:"default",fn:function(t){return["2"==t.row.status?a("span",{staticStyle:{color:"#00e079","font-weight":"bold"}},[e._v(e._s(t.row.statusName))]):"3"==t.row.status?a("span",{staticStyle:{color:"#ff4777","font-weight":"bold"}},[e._v(e._s(t.row.statusName))]):"4"==t.row.status?a("span",{staticStyle:{color:"#c0c0c0"}},[e._v(e._s(t.row.statusName))]):a("span",[e._v(e._s(t.row.statusName))])]}}])}),a("el-table-column",{attrs:{prop:"type",align:"left",label:"类型",width:"200"}}),a("el-table-column",{attrs:{prop:"brand",label:"品牌",align:"left",width:"200"}}),a("el-table-column",{attrs:{prop:"price",label:"价格（元）",align:"left",width:"200"}})],1),a("el-row",{staticStyle:{"margin-top":"10px"}},[a("el-col",{attrs:{span:10}},[a("el-form-item",{directives:[{name:"show",rawName:"v-show",value:e.purchase.remark&&""!=e.purchase.remark,expression:"purchase.remark&&purchase.remark!=''"}],attrs:{label:"订单备注:",prop:"remark"}},[e._v("\n            "+e._s(e.purchase.remark)+"\n          ")])],1)],1)],1),a("el-form",[a("el-row",{directives:[{name:"show",rawName:"v-show",value:e.urlList&&e.urlList.length>0,expression:"urlList&&urlList.length>0"}]},[a("el-form-item",{attrs:{label:"资产相关图片:"}})],1),a("el-row",[e._l(e.urlList,(function(t){return[a("el-col",{attrs:{span:3}},[a("el-form-item",[a("el-image",{staticStyle:{width:"130px",height:"130px"},attrs:{src:t,"preview-src-list":e.urlList}})],1)],1)]}))],2)],1)],1),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){return e.$emit("close")}}},[e._v("关 闭")])],1)])},r=[],o={name:"PurchaseOrderDetail",props:["purchase","title","urlList","dialogVisible2"],methods:{handleClose:function(){this.$emit("close")}}},c=o,u=a("2877"),h=Object(u["a"])(c,n,r,!1,null,"e40a6a74",null),d=h.exports,p={name:"PurchaseOrder",data:function(){return{searchValue:{name:null,hasInvoice:null,creatorEmail:null,totalLow:null,totalHigh:null,payLow:null,payHigh:null,purchaseDateScope:null,invoiceDateScope:null},title:"",showAdvanceSearchView:!1,purchases:[],loading:!1,dialogVisible2:!1,dialogVisible3:!1,total:0,page:1,keyword:"",size:10,options:[{name:"是",value:!0},{name:"否",value:!1}],operateList:[],orderIds:[],type:"",fileList:[],urlList:[],purchase:{id:"",name:"",total:"",discount:"",pay:"",purchaseDate:"",hasInvoice:"",invoiceDate:"",remark:"",creatorEmail:"",updaterEmail:"",creator:{},updater:{},createTime:"",updateTime:"",assetList:[],fileList:[]},aids:[],selectedAids:[]}},components:{PurchaseOrderEdit:s["a"],PurchaseOrderDetail:d},mounted:function(){this.initPurchaseOrders()},methods:{exportData:function(){var e="/purchase/export/?1=1";this.type&&"advanced"==this.type?(this.searchValue.name&&(e+="&name="+this.searchValue.name),this.searchValue.hasInvoice&&(e+="&hasInvoice="+this.searchValue.hasInvoice),this.searchValue.creatorEmail&&(e+="&creatorEmail="+this.searchValue.creatorEmail),this.searchValue.totalLow&&(e+="&totalLow="+this.searchValue.totalLow),this.searchValue.totalHigh&&(e+="&totalHigh="+this.searchValue.totalHigh),this.searchValue.payLow&&(e+="&payLow="+this.searchValue.payLow),this.searchValue.payHigh&&(e+="&payHigh="+this.searchValue.payHigh),this.searchValue.purchaseDateScope&&(e+="&purchaseDateScope="+this.searchValue.purchaseDateScope),this.searchValue.invoiceDateScope&&(e+="&invoiceDateScope="+this.searchValue.invoiceDateScope)):e+="&id="+this.keyword,window.open(e,"_parent")},showEditView:function(e){this.title="编辑订单信息",this.purchase=e,this.purchase.fileList&&this.purchase.fileList.length>0?this.fileList=this.purchase.fileList:this.fileList=[],this.dialogVisible3=!0},showDetailView:function(e){if(this.title="订单详情",this.purchase=e,this.purchase&&this.purchase.fileList){this.urlList=[];for(var t=0;t<this.purchase.fileList.length;t++)this.urlList.push(this.purchase.fileList[t].url)}else this.urlList=null;this.dialogVisible2=!0},delete:function(){var e=this;this.$confirm("此操作将永久删除选中的记录, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){e.deleteRequestWithData("/purchase/delete",e.orderIds).then((function(t){t&&(e.orderIds=[],e.initPurchaseOrders())}))})).catch((function(){e.$message({type:"info",message:"已取消删除"})}))},sizeChange:function(e){this.size=e,this.type&&"advanced"==this.type?this.initPurchaseOrdersAdv():this.initPurchaseOrders()},currentChange:function(e){this.page=e,this.type&&"advanced"==this.type?this.initPurchaseOrdersAdv():this.initPurchaseOrders()},initPurchaseOrders:function(){var e=this;this.type="",this.loading=!0;var t="/purchase/get/?page="+this.page+"&size="+this.size+"&poid="+this.keyword;this.getRequest(t).then((function(t){e.loading=!1,t&&(e.purchases=t.data,e.total=t.total)}))},initPurchaseOrdersAdv:function(){var e=this;this.type="advanced",this.loading=!0;var t="/purchase/get/?page="+this.page+"&size="+this.size;this.searchValue.name&&(t+="&name="+this.searchValue.name),this.searchValue.hasInvoice&&(t+="&hasInvoice="+this.searchValue.hasInvoice),this.searchValue.creatorEmail&&(t+="&creatorEmail="+this.searchValue.creatorEmail),this.searchValue.totalLow&&(t+="&totalLow="+this.searchValue.totalLow),this.searchValue.totalHigh&&(t+="&totalHigh="+this.searchValue.totalHigh),this.searchValue.payLow&&(t+="&payLow="+this.searchValue.payLow),this.searchValue.payHigh&&(t+="&payHigh="+this.searchValue.payHigh),this.searchValue.purchaseDateScope&&(t+="&purchaseDateScope="+this.searchValue.purchaseDateScope),this.searchValue.invoiceDateScope&&(t+="&invoiceDateScope="+this.searchValue.invoiceDateScope),this.getRequest(t).then((function(t){e.loading=!1,t&&(e.purchases=t.data,e.total=t.total)}))},handleSelectionChange:function(e){for(var t=0;t<e.length;t++)this.orderIds.push(e[t].id)},clearSearchValue:function(){this.searchValue={name:null,hasInvoice:null,creatorEmail:null,totalLow:null,totalHigh:null,payLow:null,payHigh:null,purchaseDateScope:null,invoiceDateScope:null}},hidePop:function(e){var t=this,a="/purchase/asset?poid="+e.id;this.putRequest(a,this.selectedAids).then((function(e){e&&t.initPurchaseOrdersAdv()}))},showPop:function(e){var t=this;this.initAllAssetIds();var a=e.assetList;this.selectedAids=[],a.forEach((function(e){t.selectedAids.push(e.id)}))},initAllAssetIds:function(){var e=this;this.getRequest("/asset/all").then((function(t){t&&(e.aids=t.obj)}))}}},m=p,f=Object(u["a"])(m,i,l,!1,null,null,null);t["default"]=f.exports},feca:function(e,t,a){var i={"./Home.vue":"bb51","./Login.vue":"a55b","./SignUp.vue":"5c9c","./UserInfo.vue":"ee96","./asset/AssetClean.vue":"e11c","./asset/AssetDashBoard.vue":"c114","./asset/AssetIn.vue":"bb79","./asset/AssetInfo.vue":"9f77","./asset/AssetOut.vue":"2aa2","./asset/AssetRepair.vue":"cf71","./mine/MyApply.vue":"6e6f","./mine/MyOut.vue":"8e88","./mine/MyTask.vue":"096f","./purchase/PurchaseOrder.vue":"d66d","./sys/SysLog.vue":"864e","./sys/SysPermission.vue":"d32a","./sys/SysUser.vue":"232c"};function l(e){var t=s(e);return a(t)}function s(e){if(!a.o(i,e)){var t=new Error("Cannot find module '"+e+"'");throw t.code="MODULE_NOT_FOUND",t}return i[e]}l.keys=function(){return Object.keys(i)},l.resolve=s,e.exports=l,l.id="feca"}}]);
//# sourceMappingURL=chunk-cbb92fb4.5501ad79.js.map