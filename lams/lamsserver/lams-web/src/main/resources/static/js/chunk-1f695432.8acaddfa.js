(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-1f695432"],{"096f":function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",[a("div",{staticStyle:{display:"flex","justify-content":"space-between"}},[e._m(0),a("div",[a("el-input",{staticStyle:{width:"350px","margin-right":"10px"},attrs:{placeholder:"请输入申请单编号进行搜索，可以直接回车搜索...","prefix-icon":"el-icon-search",clearable:"",disabled:e.showAdvanceSearchView},on:{clear:e.initOrders},nativeOn:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.initOrders(t)}},model:{value:e.keyword,callback:function(t){e.keyword=t},expression:"keyword"}}),a("el-button",{attrs:{icon:"el-icon-search",type:"primary",disabled:e.showAdvanceSearchView},on:{click:e.initOrders}},[e._v("\n          搜索\n        ")]),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.showAdvanceSearchView=!e.showAdvanceSearchView}}},[a("i",{class:e.showAdvanceSearchView?"fa fa-angle-double-up":"fa fa-angle-double-down",attrs:{"aria-hidden":"true"}}),e._v("\n          高级搜索\n        ")])],1)]),a("transition",{attrs:{name:"slide-fade"}},[a("div",{directives:[{name:"show",rawName:"v-show",value:e.showAdvanceSearchView,expression:"showAdvanceSearchView"}],staticStyle:{border:"1px solid #759ad1","border-radius":"5px","box-sizing":"border-box",padding:"5px",margin:"10px 0px"}},[a("el-row",[a("el-col",{attrs:{span:4}},[e._v("\n            状态:\n            "),a("el-select",{staticStyle:{width:"130px"},attrs:{clearable:"",placeholder:"状态",size:"mini"},model:{value:e.searchValue.status,callback:function(t){e.$set(e.searchValue,"status",t)},expression:"searchValue.status"}},e._l(e.statuses,(function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1)],1),a("el-col",{attrs:{span:6}},[e._v("\n            申请人邮箱:\n            "),a("el-input",{staticStyle:{width:"200px"},attrs:{size:"mini",clearable:"","prefix-icon":"el-icon-edit"},model:{value:e.searchValue.userEmail,callback:function(t){e.$set(e.searchValue,"userEmail",t)},expression:"searchValue.userEmail"}})],1),a("el-col",{attrs:{span:10}},[e._v("\n            申请时间:\n            "),a("el-date-picker",{attrs:{type:"daterange",size:"mini","unlink-panels":"","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:e.searchValue.dateScope,callback:function(t){e.$set(e.searchValue,"dateScope",t)},expression:"searchValue.dateScope"}})],1),a("el-col",{attrs:{span:4}},[a("el-button",{attrs:{size:"mini"},on:{click:e.clearSearchValue}},[e._v("重置")]),a("el-button",{attrs:{size:"mini"},on:{click:function(t){e.showAdvanceSearchView=!1}}},[e._v("取消")]),a("el-button",{attrs:{size:"mini",icon:"el-icon-search",type:"primary"},on:{click:e.initOrdersAdv}},[e._v("搜索")])],1)],1)],1)])],1),a("Mine",{attrs:{orders:e.orders,total:e.total,page:e.page,size:e.size,loading:e.loading},on:{currentSize:e.sizeChange,currentPage:e.currentChange}}),a("OrderDetail",{attrs:{dialogVisible7:e.dialogVisible7,order:e.order,title:e.title,operateList:e.operateList},on:{close:function(t){e.dialogVisible7=!1}}})],1)},s=[function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("p",{staticStyle:{display:"inline-flex","margin-left":"8px"}})])}],r=a("b9d6"),n=(a("4ca5"),a("42f5")),l={name:"MyTask",data:function(){return{searchValue:{status:null,userEmail:null,dateScope:null},title:"",showAdvanceSearchView:!1,orders:[],loading:!1,dialogVisible7:!1,total:0,page:1,keyword:"",size:10,operateList:[],order:{id:"",category:"",categoryName:"",status:"",statusName:"",expireTime:"",reason:"测试",userEmail:"admin",user:{id:"",name:"",phone:"",username:"",enabled:"",remark:""},createTime:"",updateTime:"",assetList:[]},types:[],statuses:[{id:1,name:"申请采购"},{id:2,name:"审批通过"},{id:3,name:"已入库"},{id:4,name:"申请借用"},{id:5,name:"已借出"},{id:6,name:"审批未通过（采购）"},{id:7,name:"已关闭"},{id:8,name:"审批未通过（借用）"},{id:9,name:"离退申请"},{id:10,name:"归还中"},{id:11,name:"已归还"},{id:12,name:"审批未通过（归还）"}],type:"",fileList:[]}},components:{Mine:n["a"],OrderDetail:r["a"]},mounted:function(){this.initOrders(),this.initTypes()},methods:{emptyOrder:function(){this.order={id:"",category:"",categoryName:"",status:"",statusName:"",expireTime:"",reason:"测试",userEmail:"admin",user:{id:"",name:"",phone:"",username:""},createTime:"",updateTime:"",assetList:[]}},showDetailView:function(e){this.title="申请单详情",this.order=e,this.dialogVisible7=!0},getOperateList:function(e){var t=this;this.getRequest("/order/task/getOperateList?id="+e.id).then((function(a){a&&(t.operateList=a.obj,t.showDetailView(e))}))},sizeChange:function(e){this.size=e,this.initOrders()},currentChange:function(e){this.page=e,this.initOrdersAdv()},initOrders:function(){var e=this;this.type="",this.loading=!0;var t="/order/task/getMyTask/?page="+this.page+"&size="+this.size+"&oid="+this.keyword;this.getRequest(t).then((function(t){e.loading=!1,t&&(e.orders=t.data,e.total=t.total)}))},initOrdersAdv:function(){var e=this;this.type="advanced",this.loading=!0;var t="/order/task/getMyTask/?page="+this.page+"&size="+this.size;this.searchValue.status&&(t+="&status="+this.searchValue.status),this.searchValue.userEmail&&(t+="&userEmail="+this.searchValue.userEmail),this.searchValue.dateScope&&(t+="&dateScope="+this.searchValue.dateScope),this.getRequest(t).then((function(t){e.loading=!1,t&&(e.orders=t.data,e.total=t.total)}))},clearSearchValue:function(){this.searchValue={status:null,userEmail:null,dateScope:null}},initTypes:function(){var e=this,t="/asset/types/get";this.getRequest(t).then((function(t){t&&(e.types=t.obj)}))}}},o=l,c=a("2877"),d=Object(c["a"])(o,i,s,!1,null,null,null);t["default"]=d.exports},"42f5":function(e,t,a){"use strict";var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticStyle:{"margin-top":"10px"}},[a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticStyle:{width:"100%"},attrs:{data:e.orders,stripe:"",border:"","element-loading-text":"正在加载...","element-loading-spinner":"el-icon-loading","element-loading-background":"rgba(0, 0, 0, 0.8)"}},[a("el-table-column",{attrs:{type:"selection",width:"55"}}),a("el-table-column",{attrs:{fixed:"",label:"申请单编号",align:"left",width:"90"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{size:"mini"},on:{click:function(a){return e.getOperateList(t.row)}}},[e._v(e._s(t.row.id)+"\n          ")])]}}])}),a("el-table-column",{attrs:{prop:"categoryName",align:"left",label:"流程",width:"90"}}),a("el-table-column",{attrs:{prop:"statusName",width:"90",label:"状态"},scopedSlots:e._u([{key:"default",fn:function(t){return["2"==t.row.status||"3"==t.row.status||"5"==t.row.status||"11"==t.row.status?a("span",{staticStyle:{color:"#00e079","font-weight":"bold"}},[e._v(e._s(t.row.statusName))]):"6"==t.row.status||"8"==t.row.status||"12"==t.row.status?a("span",{staticStyle:{color:"#ff4777","font-weight":"bold"}},[e._v(e._s(t.row.statusName))]):"7"==t.row.status?a("span",{staticStyle:{color:"#c0c0c0"}},[e._v(e._s(t.row.statusName))]):a("span",[e._v(e._s(t.row.statusName))])]}}])}),a("el-table-column",{attrs:{prop:"expireTime",width:"100",align:"left",label:"预计归还时间"}}),a("el-table-column",{attrs:{prop:"reason","show-overflow-tooltip":!0,label:"理由"}}),a("el-table-column",{attrs:{prop:"user.name",width:"95",align:"left",label:"申请人"}}),a("el-table-column",{attrs:{prop:"user.username",width:"150",align:"left",label:"申请人邮箱"}}),a("el-table-column",{attrs:{prop:"user.phone",width:"100",label:"申请人电话"}}),a("el-table-column",{attrs:{prop:"createTime",align:"left",label:"申请时间",width:"180"}}),a("el-table-column",{attrs:{prop:"updateTime",align:"left",label:"更新时间",width:"180"}})],1),a("div",{staticStyle:{display:"flex","justify-content":"flex-end"}},[a("el-pagination",{attrs:{background:"",layout:"sizes, prev, pager, next, jumper, ->, total, slot",total:e.total},on:{"current-change":e.currentChange,"size-change":e.sizeChange}})],1)],1)])},s=[],r={name:"Mine",props:["orders","total","page","size","loading","isOut"],methods:{sizeChange:function(e){this.$emit("currentSize",e)},currentChange:function(e){this.$emit("currentPage",e)},getOperateList:function(e){this.$parent.getOperateList(e)}}},n=r,l=a("2877"),o=Object(l["a"])(n,i,s,!1,null,"2e7c8b5a",null);t["a"]=o.exports},"6e6f":function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",[a("div",{staticStyle:{display:"flex","justify-content":"space-between"}},[e._m(0),a("div",[a("el-input",{staticStyle:{width:"350px","margin-right":"10px"},attrs:{placeholder:"请输入申请单编号进行搜索，可以直接回车搜索...","prefix-icon":"el-icon-search",clearable:"",disabled:e.showAdvanceSearchView},on:{clear:e.initOrders},nativeOn:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.initOrders(t)}},model:{value:e.keyword,callback:function(t){e.keyword=t},expression:"keyword"}}),a("el-button",{attrs:{icon:"el-icon-search",type:"primary",disabled:e.showAdvanceSearchView},on:{click:e.initOrders}},[e._v("\n          搜索\n        ")]),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.showAdvanceSearchView=!e.showAdvanceSearchView}}},[a("i",{class:e.showAdvanceSearchView?"fa fa-angle-double-up":"fa fa-angle-double-down",attrs:{"aria-hidden":"true"}}),e._v("\n          高级搜索\n        ")])],1)]),a("transition",{attrs:{name:"slide-fade"}},[a("div",{directives:[{name:"show",rawName:"v-show",value:e.showAdvanceSearchView,expression:"showAdvanceSearchView"}],staticStyle:{border:"1px solid #759ad1","border-radius":"5px","box-sizing":"border-box",padding:"5px",margin:"10px 0px"}},[a("el-row",[a("el-col",{attrs:{span:4}},[e._v("\n            状态:\n            "),a("el-select",{staticStyle:{width:"130px"},attrs:{clearable:"",placeholder:"状态",size:"mini"},model:{value:e.searchValue.status,callback:function(t){e.$set(e.searchValue,"status",t)},expression:"searchValue.status"}},e._l(e.statuses,(function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1)],1),a("el-col",{attrs:{span:6}},[e._v("\n            申请人邮箱:\n            "),a("el-input",{staticStyle:{width:"200px"},attrs:{size:"mini",clearable:"","prefix-icon":"el-icon-edit"},model:{value:e.searchValue.userEmail,callback:function(t){e.$set(e.searchValue,"userEmail",t)},expression:"searchValue.userEmail"}})],1),a("el-col",{attrs:{span:10}},[e._v("\n            申请时间:\n            "),a("el-date-picker",{attrs:{type:"daterange",size:"mini","unlink-panels":"","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:e.searchValue.dateScope,callback:function(t){e.$set(e.searchValue,"dateScope",t)},expression:"searchValue.dateScope"}})],1),a("el-col",{attrs:{span:4}},[a("el-button",{attrs:{size:"mini"},on:{click:e.clearSearchValue}},[e._v("重置")]),a("el-button",{attrs:{size:"mini"},on:{click:function(t){e.showAdvanceSearchView=!1}}},[e._v("取消")]),a("el-button",{attrs:{size:"mini",icon:"el-icon-search",type:"primary"},on:{click:e.initOrdersAdv}},[e._v("搜索")])],1)],1)],1)])],1),a("Mine",{attrs:{orders:e.orders,total:e.total,page:e.page,size:e.size,loading:e.loading},on:{currentSize:e.sizeChange,currentPage:e.currentChange}}),a("OrderDetail",{attrs:{dialogVisible7:e.dialogVisible7,order:e.order,title:e.title,operateList:e.operateList},on:{close:function(t){e.dialogVisible7=!1}}})],1)},s=[function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("p",{staticStyle:{display:"inline-flex","margin-left":"8px"}})])}],r=a("b9d6"),n=a("42f5"),l={name:"MyApply",data:function(){return{searchValue:{status:null,userEmail:null,dateScope:null},title:"",showAdvanceSearchView:!1,orders:[],loading:!1,dialogVisible7:!1,total:0,page:1,keyword:"",size:10,operateList:[],order:{id:"",category:"",categoryName:"",status:"",statusName:"",expireTime:"",reason:"测试",userEmail:"admin",user:{id:"",name:"",phone:"",username:"",enabled:"",remark:""},createTime:"",updateTime:"",asset:{id:"",status:"",statusName:"",brand:"华为",type:"手机",price:"4000",adv:{}}},types:[],statuses:[{id:1,name:"申请采购"},{id:2,name:"审批通过"},{id:3,name:"已入库"},{id:4,name:"申请借用"},{id:5,name:"已借出"},{id:6,name:"审批未通过（采购）"},{id:7,name:"已关闭"},{id:8,name:"审批未通过（借用）"},{id:9,name:"离退申请"},{id:10,name:"归还中"},{id:11,name:"已归还"},{id:12,name:"审批未通过（归还）"}],type:"",fileList:[]}},components:{Mine:n["a"],OrderDetail:r["a"]},mounted:function(){this.initOrders(),this.initTypes()},methods:{emptyOrder:function(){this.order={id:"",category:"",categoryName:"",status:"",statusName:"",expireTime:"",reason:"测试",userEmail:"admin",user:{id:"",name:"",phone:"",username:""},createTime:"",updateTime:"",assetList:[]}},showDetailView:function(e){this.title="申请单详情",this.order=e,this.dialogVisible7=!0},getOperateList:function(e){var t=this;this.getRequest("/order/task/getOperateList?id="+e.id).then((function(a){a&&(t.operateList=a.obj,t.showDetailView(e))}))},sizeChange:function(e){this.size=e,this.initOrders()},currentChange:function(e){this.page=e,this.initOrdersAdv()},initOrders:function(){var e=this;this.type="",this.loading=!0;var t="/order/task/getMyApply/?page="+this.page+"&size="+this.size+"&oid="+this.keyword;this.getRequest(t).then((function(t){e.loading=!1,t&&(e.orders=t.data,e.total=t.total)}))},initOrdersAdv:function(){var e=this;this.type="advanced",this.loading=!0;var t="/order/task/getMyApply/?page="+this.page+"&size="+this.size;this.searchValue.status&&(t+="&status="+this.searchValue.status),this.searchValue.userEmail&&(t+="&userEmail="+this.searchValue.userEmail),this.searchValue.dateScope&&(t+="&dateScope="+this.searchValue.dateScope),this.getRequest(t).then((function(t){e.loading=!1,t&&(e.orders=t.data,e.total=t.total)}))},clearSearchValue:function(){this.searchValue={status:null,userEmail:null,beginDateScope:null}},initTypes:function(){var e=this,t="/asset/types/get";this.getRequest(t).then((function(t){t&&(e.types=t.obj)}))}}},o=l,c=a("2877"),d=Object(c["a"])(o,i,s,!1,null,null,null);t["default"]=d.exports},"8e88":function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",[a("div",{staticStyle:{display:"flex","justify-content":"space-between"}},[a("div",[a("el-button",{staticStyle:{display:"inline-flex","margin-left":"8px"},attrs:{type:"primary",icon:"el-icon-plus"},on:{click:e.showAddView}},[e._v("\n          发起离退流程\n        ")])],1),a("div",[a("el-input",{staticStyle:{width:"350px","margin-right":"10px"},attrs:{placeholder:"请输入申请单编号进行搜索，可以直接回车搜索...","prefix-icon":"el-icon-search",clearable:"",disabled:e.showAdvanceSearchView},on:{clear:e.initOrders},nativeOn:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.initOrders(t)}},model:{value:e.keyword,callback:function(t){e.keyword=t},expression:"keyword"}}),a("el-button",{attrs:{icon:"el-icon-search",type:"primary",disabled:e.showAdvanceSearchView},on:{click:e.initOrders}},[e._v("\n          搜索\n        ")]),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.showAdvanceSearchView=!e.showAdvanceSearchView}}},[a("i",{class:e.showAdvanceSearchView?"fa fa-angle-double-up":"fa fa-angle-double-down",attrs:{"aria-hidden":"true"}}),e._v("\n          高级搜索\n        ")])],1)]),a("transition",{attrs:{name:"slide-fade"}},[a("div",{directives:[{name:"show",rawName:"v-show",value:e.showAdvanceSearchView,expression:"showAdvanceSearchView"}],staticStyle:{border:"1px solid #759ad1","border-radius":"5px","box-sizing":"border-box",padding:"5px",margin:"10px 0px"}},[a("el-row",[a("el-col",{attrs:{span:7}},[e._v("\n            状态:\n            "),a("el-select",{staticStyle:{width:"130px"},attrs:{clearable:"",placeholder:"状态",size:"mini"},model:{value:e.searchValue.status,callback:function(t){e.$set(e.searchValue,"status",t)},expression:"searchValue.status"}},e._l(e.statuses,(function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1)],1),a("el-col",{attrs:{span:9}},[e._v("\n            理由:\n            "),a("el-input",{staticStyle:{width:"350px"},attrs:{size:"mini","prefix-icon":"el-icon-edit",clearable:""},model:{value:e.searchValue.reason,callback:function(t){e.$set(e.searchValue,"reason",t)},expression:"searchValue.reason"}})],1)],1),a("el-row",{staticStyle:{"margin-top":"10px"}},[a("el-col",{attrs:{span:7}},[e._v("\n            申请人邮箱:\n            "),a("el-input",{staticStyle:{width:"200px"},attrs:{size:"mini",clearable:"","prefix-icon":"el-icon-edit"},model:{value:e.searchValue.userEmail,callback:function(t){e.$set(e.searchValue,"userEmail",t)},expression:"searchValue.userEmail"}})],1),a("el-col",{attrs:{span:8}},[e._v("\n            申请时间:\n            "),a("el-date-picker",{attrs:{type:"daterange",size:"mini","unlink-panels":"","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:e.searchValue.dateScope,callback:function(t){e.$set(e.searchValue,"dateScope",t)},expression:"searchValue.dateScope"}})],1),a("el-col",{attrs:{span:5,offset:4}},[a("el-button",{attrs:{size:"mini"},on:{click:e.clearSearchValue}},[e._v("重置")]),a("el-button",{attrs:{size:"mini"},on:{click:function(t){e.showAdvanceSearchView=!1}}},[e._v("取消")]),a("el-button",{attrs:{size:"mini",icon:"el-icon-search",type:"primary"},on:{click:e.initOrdersAdv}},[e._v("搜索")])],1)],1)],1)])],1),a("Mine",{attrs:{orders:e.orders,total:e.total,page:e.page,size:e.size,loading:e.loading,isOut:e.isOut},on:{currentSize:e.sizeChange,currentPage:e.currentChange}}),a("el-dialog",{attrs:{title:e.title,visible:e.dialogVisible2,width:"80%"},on:{"update:visible":function(t){e.dialogVisible2=t}}},[a("div",[a("el-form",{attrs:{model:e.order}},[a("el-row",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"申请单号:",prop:"id"}},[e._v("\n              "+e._s(e.order.id)+"\n            ")])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"流程:",prop:"categoryName"}},[e._v("\n              "+e._s(e.order.categoryName)+"\n            ")])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"状态:",prop:"statusName"}},[e._v("\n              "+e._s(e.order.statusName)+"\n            ")])],1)],1),a("el-row",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"电话:",prop:"user.phone"}},[e._v("\n              "+e._s(e.order.user.phone)+"\n            ")])],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"邮件:",prop:"user.username"}},[e._v("\n              "+e._s(e.order.user.username)+"\n            ")])],1)],1),a("el-row",[a("el-col",{attrs:{span:10}},[a("el-form-item",{directives:[{name:"show",rawName:"v-show",value:e.order.reason&&""!=e.order.reason,expression:"order.reason&&order.reason!=''"}],attrs:{label:"理由:",prop:"reason"}},[e._v("\n              "+e._s(e.order.reason)+"\n            ")])],1)],1),a("el-row",[a("el-col",{attrs:{span:10}},[a("el-form-item",{directives:[{name:"show",rawName:"v-show",value:e.order.createTime,expression:"order.createTime"}],attrs:{label:"创建时间:",prop:"createTime"}},[e._v("\n              "+e._s(e.order.createTime)+"\n            ")])],1)],1)],1)],1),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e._l(e.operateList,(function(t){return[a("el-button",{attrs:{type:"primary"},on:{click:function(a){return e.checkAndHandle(t.operateType)}}},[e._v(e._s(t.operate))])]})),a("el-button",{on:{click:function(t){e.dialogVisible2=!1}}},[e._v("取 消")])],2)]),a("el-dialog",{attrs:{title:e.title,visible:e.dialogVisible,width:"80%"},on:{"update:visible":function(t){e.dialogVisible=t}}},[a("div",[a("el-form",{ref:"orderForm",attrs:{model:e.order}},[a("el-row",[a("el-col",{attrs:{span:20}},[a("el-form-item",{attrs:{label:"理由:",prop:"reason"}},[a("el-input",{attrs:{size:"mini",type:"textarea",rows:2,placeholder:"请输入申请理由"},model:{value:e.order.reason,callback:function(t){e.$set(e.order,"reason",t)},expression:"order.reason"}})],1)],1)],1)],1)],1),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.dialogVisible=!1}}},[e._v("取 消")]),a("el-button",{attrs:{type:"primary"},on:{click:e.doStudentOut}},[e._v("确 定")])],1)])],1)},s=[],r=a("42f5"),n={name:"MyOut",data:function(){return{isOut:!0,loading:!1,searchValue:{status:null,reason:null,userEmail:null,dateScope:null},title:"",orders:[],showAdvanceSearchView:!1,dialogVisible:!1,dialogVisible2:!1,total:0,page:1,keyword:"",size:10,statuses:[{id:9,name:"申请离退"},{id:2,name:"审批通过"},{id:6,name:"审批未通过"},{id:7,name:"已关闭"}],candidateBranches:{},operateList:[],order:{id:"",category:"",categoryName:"",status:"",statusName:"",expireTime:"",reason:"",userEmail:"",user:{id:"",name:"",phone:"",username:""},createTime:"",updateTime:"",assetList:[]},taskHandleDto:{id:null,operateType:null}}},components:{Mine:r["a"]},mounted:function(){this.initOrders()},methods:{emptyOrder:function(){this.order={id:"",category:"",categoryName:"",status:"",statusName:"",expireTime:"",reason:"",userEmail:"",user:{id:"",name:"",phone:"",username:"",enabled:"",remark:""},createTime:"",updateTime:"",assetList:[]}},showDetailView:function(e){this.title="离退单详情",this.order=e,this.dialogVisible2=!0},getOperateList:function(e){var t=this;this.getRequest("/order/task/getOperateList?id="+e.id).then((function(a){a&&(t.operateList=a.obj,t.showDetailView(e))}))},sizeChange:function(e){this.size=e,this.initOrders("advanced")},currentChange:function(e){this.page=e,this.initOrders("advanced")},showAddView:function(){this.emptyOrder(),this.title="离退流程申请",this.dialogVisible=!0},checkAndHandle:function(e){this.taskHandleDto.operateType=e,"7"==e?this.cancel():this.handle()},handle:function(){var e=this;this.taskHandleDto.id=this.order.id,this.postRequest("/order/task/handleTask",this.taskHandleDto).then((function(t){t&&(e.dialogVisible2=!1,e.initOrders())}))},cancel:function(){var e=this;this.taskHandleDto.id=this.order.id,this.postRequest("/order/task/cancel",this.taskHandleDto).then((function(t){t&&(e.visible=!1,e.dialogVisible2=!1,e.initOrders())}))},doStudentOut:function(){var e=this;this.postRequest("/stuOut/add",this.order).then((function(t){t&&(e.dialogVisible=!1,e.initOrders())}))},initOrders:function(){var e=this;this.type="",this.loading=!0;var t="/stuOut/get/?page="+this.page+"&size="+this.size+"&oid="+this.keyword;this.getRequest(t).then((function(t){e.loading=!1,t&&(e.orders=t.data,e.total=t.total)}))},initOrdersAdv:function(){var e=this;this.type="advanced",this.loading=!0;var t="/stuOut/get/?page="+this.page+"&size="+this.size;this.searchValue.status&&(t+="&status="+this.searchValue.status),this.searchValue.reason&&(t+="&reason="+this.searchValue.reason),this.searchValue.userEmail&&(t+="&userEmail="+this.searchValue.userEmail),this.searchValue.dateScope&&(t+="&dateScope="+this.searchValue.dateScope),this.getRequest(t).then((function(t){e.loading=!1,t&&(e.orders=t.data,e.total=t.total)}))},clearSearchValue:function(){this.searchValue={status:null,reason:null,userEmail:null,dateScope:null}}}},l=n,o=a("2877"),c=Object(o["a"])(l,i,s,!1,null,null,null);t["default"]=c.exports}}]);
//# sourceMappingURL=chunk-1f695432.8acaddfa.js.map