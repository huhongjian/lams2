# lams2
新版本实验室资产管理系统   
![系统展示](https://raw.githubusercontent.com/huhongjian/images/main/img/20210116235512.png)

为实验室资产管理需求开发  
目前支持：  
- 仪表盘
- 资产入库
- 出库借用
- 资产归还
- 资产信息
- 资产报修
- 清退报废
- 我的申请
- 我的任务
- 离退流程
- 订单信息
- 权限管理
- 日志管理
- 用户管理

### 操作手册
#### 资产入库
##### 流程图：  
![资产入库](https://raw.githubusercontent.com/huhongjian/images/main/img/Snipaste_2020-10-08_20-06-17.jpg)
##### 操作说明
点击`新增采购单`发起采购请求  
填写理由  
![新资产采购申请](https://raw.githubusercontent.com/huhongjian/images/main/img/20210116235843.png)  
点击`新增资产`添加资产信息   
![新增资产信息](https://raw.githubusercontent.com/huhongjian/images/main/img/20210117000223.png)
申请之后，点击申请记录id，即可进行审批等操作   
当资产入库成功之后，财务人员可以为资产添加订单信息   
#### 出库借用
##### 流程图  
![资产出库](https://raw.githubusercontent.com/huhongjian/images/main/img/20210117000709.png)  
##### 操作说明
当新资产审批通过，财务做账，确认到库之后，点击入库。此时新资产入库成功，可以借用  
点击`借用资产`发起借用申请  
填写理由和预计归还时间  
![资产借用](https://raw.githubusercontent.com/huhongjian/images/main/img/20210117000900.png)  
选择需要借用的资产  
![选择资产](https://raw.githubusercontent.com/huhongjian/images/main/img/20210117001109.png)  
借用操作发起后，需要管理员审批，通过才算借用成功    
撤回操作，只有自己的申请可以撤回，取消申请  
#### 资产归还
##### 流程图：
![资产归还](https://raw.githubusercontent.com/huhongjian/images/main/img/20210117013509.png)
点击`归还资产`归还资产  
##### 操作说明
![归还资产](https://raw.githubusercontent.com/huhongjian/images/main/img/20210117010653.png)  
选择要归还的资产  
![选择归还资产](https://raw.githubusercontent.com/huhongjian/images/main/img/20210117010742.png)  
确认后，归还申请成功，管理员确定后，审批归还工单  
#### 资产信息
全部资产信息，如果要修改，需要管理员权限  
![资产信息](https://raw.githubusercontent.com/huhongjian/images/main/img/20210117010909.png)  
勾选资产，点击`添加订单信息`，可以为已经入库的资产，添加采购时的订单信息  
![添加订单信息](https://raw.githubusercontent.com/huhongjian/images/main/img/20210117011036.png)  
#### 资产报修
所有用户都可以给资产报修，报修前需要先归还
#### 资产清退
清退资产，清退前需要归还资产，需要管理员权限
#### 我的申请
包含所有当前用户发起的申请，如果有相应的权限的话，可以直接操作
#### 我的任务
所有我有权限审批的申请
#### 离退流程
##### 流程图
![学生离退](https://raw.githubusercontent.com/huhongjian/images/main/img/20201101234604.png)  
##### 操作说明
离开实验室，外出实习，毕业学生发起离退流程，需要归还所有资产才能发起，管理员审批通过之后，账号置为不可用
#### 订单管理
入库时候添加的订单信息可以在这里管理，可以修改关联的资产信息，编辑订单信息
#### 权限管理
管理角色以及角色和菜单的绑定关系
#### 日志管理
所有对资产的操作日志
#### 用户管理
修改用户角色  
设置账号是否可用  
彻底删除账号，为避免误删，只允许删除“不可用”了的账号    
设计上学生离退只会把账号禁用，如果要销号，需要管理员手动删除  
用户个人信息只能由本人在右上角个人中心修改  
### 开发日志
##### 2020.10.06 第一版上线：资产入库，出库，用户管理，个人任务等功能
##### 2020.10.21 第二版上线：仪表盘，资产信息管理，资产报修，资产清退等
##### 2020.11.01 第三版上线：学生离退流程、操作日志、图片上传等
##### 2020.11.09 第四版上线：订单信息管理
##### 2020.12.31 第五版上线：支持一个工单对应多个资产，删除转交，新增归还流程等
