# spring cloud alibaba demo 项目

## 创建项目

### 1. 建立 parent 工程

pom.xml 参见工程

### 2. 建立 user-service (provider)和 product-service （consumer）工程

选择 new→module。

使用 cloud toolkit 工具，alibaba java initialize 插件，创建两个项目。并将项目加入到 parent pom 的 module 中。

## 配置中心

需求：

1. 环境
2. 机房
3. 公用配置
4. 私有配置