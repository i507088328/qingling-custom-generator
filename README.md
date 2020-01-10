### 介绍
基于mybatis-generator扩展
自定义插件
增加方法:
1. get 根据主键查询
2. add 实体类参数插入
3. update 根据实体类修改
4. logicDelete  根据主键逻辑删除
5. logicBatchDelete 根据主键批量逻辑删除
6. physicalDelete 根据主键物理删除
7. loadPageByMap 根据实体类传参分页查询

### 架构
Maven

### 安装教程
idea或者eclipse导入maven project

### 使用说明
#### 参数配置
1. config.properties配置数据库连接信息
2. Constants类设置好主键名称和数据保存的逻辑标识
3. generatorConfiguration.xml配置tableName表名，domainObjectName实体类名称
#### 生成方式
直接执行MyStart里的main方法

### 作者信息
lishuai
qq：507088328
email:ls_tedu@163.com


