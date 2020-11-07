# lams2
新版本实验室管理系统   
![系统展示](https://raw.githubusercontent.com/huhongjian/images/main/img/20201101232530.png)

为实验室资产管理需求开发  
目前支持：  
- 仪表盘
- 资产入库
- 出库借用
- 资产信息
- 资产报修
- 清退报废
- 我的申请
- 我的任务
- 离退流程
- 权限管理
- 日志管理
- 用户管理

### 操作手册
#### 资产入库
##### 流程图：  
![资产入库](https://raw.githubusercontent.com/huhongjian/images/main/img/Snipaste_2020-10-08_20-06-17.jpg)
##### 操作说明
点击新增资产发起采购请求  
填写详细信息  
![新资产采购申请](https://raw.githubusercontent.com/huhongjian/images/main/img/20201101232937.png)  
申请之后，点击申请记录id，即可进行审批等操作   
#### 出库借用
##### 流程图  
![资产出库](https://raw.githubusercontent.com/huhongjian/images/main/img/20201008204610.png)  
##### 操作说明
当新资产审批通过，财务做账，确认到库之后，点击入库。此时新资产入库成功，可以借用  
点击“已入库”，“审批拒绝”（他人申请借用未通过，可以继续借用）或“已关闭”（他人借用申请取消，可以继续借用）资产，会出现借用按钮，发起借用流程  
![资产借用](https://raw.githubusercontent.com/huhongjian/images/main/img/20201101233609.png)  
借用操作发起后，需要管理员审批，通过才算借用成功  
对于自己在用的资产，可以转交给其他用户，注意：必须要填写正确的邮箱  
归还操作，资产重新入库  
撤回操作，只有自己的申请可以撤回，取消申请  
#### 资产信息
全部资产信息，需要管理员权限
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
##### 2020.10.6第一版上线
##### 2020.10.21 第二版上线
##### 2020.11.1 第三版上线
