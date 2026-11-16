# collections-project 学习项目

这是参照 `D:\szsk\szmg-bms-boot` 的技术栈搭建的**单模块学习项目**。不复制它的模块划分和业务代码，框架与工具会按学习进度逐项加入。

后续开发按 [agent.md](agent.md) 的逐步确认流程进行；项目进展记录在 [docs/开发记录.md](docs/开发记录.md)。

**想亲手从零搭一遍：先看 [从零搭建.md](从零搭建.md)。** 当前文件夹是对照答案；教程会带你在 `D:\IdeaProjects\collections-project-practice` 从空目录创建一份。

## 参考项目用了什么

以下从参考项目的 `pom.xml`、各子工程 `pom.xml` 和 `szmg-admin/src/main/resources/application*.yml` 核对而来。配置中的某些工具只在特定业务中使用，学习时不必全部接入。

| 学习顺序 | 框架或工具 | 主要用途 | 参考项目中的线索 |
| --- | --- | --- | --- |
| 1 | JDK 17、Maven、Spring Boot 3.5.9 | 编译、依赖管理、启动应用 | 根目录 `pom.xml` |
| 2 | Spring MVC、Undertow | 接收 HTTP 请求 | `szmg-common/szmg-common-web/pom.xml` |
| 3 | Lombok、Hutool、MapStruct Plus | 减少样板代码、常用工具、对象转换 | 根目录及 `szmg-common-core/pom.xml` |
| 4 | MySQL、MyBatis-Plus、dynamic-datasource | 数据访问、多数据源 | `szmg-common-mybatis/pom.xml`、`application-local.yml` |
| 5 | Sa-Token | 登录、权限和令牌 | `szmg-common-satoken/pom.xml`、`application.yml` |
| 6 | Redis、Redisson、Lock4j | 缓存、分布式锁 | `szmg-common-redis/pom.xml`、`application-local.yml` |
| 7 | springdoc OpenAPI、Spring Boot Admin | 接口文档、运行监控 | `szmg-common-doc/pom.xml`、`szmg-admin/pom.xml` |
| 8 | Kafka、Spring Modulith、SnailJob、Warm-Flow | 消息与模块事件、任务、工作流 | 对应业务模块和公共模块的 `pom.xml` |
| 按需 | WebSocket、OSS、短信、微信支付等 | 特定业务集成 | 对应公共模块的 `pom.xml` |

**当前完成第 1～2 项基础，并添加了 `/hello` 和 B 站链接解析接口。** 以后每学一项，再增加那一项的依赖、配置和最小练习；数据库、Redis 等外部服务到对应阶段再准备。

## 第 1 步：认识目录

```text
collections-project/
├─ pom.xml                                  Maven、Spring Boot、Spring MVC、Undertow
├─ src/main/java/com/zhengsijin/collections/
│  ├─ CollectionsApplication.java              程序入口
│  ├─ controller/HelloController.java       第一个 HTTP 接口
│  ├─ controller/VideoLinkController.java   B 站链接解析接口
│  └─ video/                              链接解析与结果类型
└─ src/main/resources/
   └─ application.yml                      应用名和端口
```

这里的 `pom.xml` 没有 `<modules>`，所以它就是一个单模块项目。`spring-boot-starter-web` 引入 Spring MVC；排除默认 Tomcat 后加入 Undertow，和参考项目的 Web 服务器一致。

## 第 2 步：用 IntelliJ IDEA 打开

1. 选择 **File → Open**，打开 `D:\IdeaProjects\collections-project`。
2. 在 **File → Project Structure → Project** 中，把 Project SDK 设为 **JDK 17**。
3. 等待 IDEA 导入 Maven 依赖。如果没有自动导入，右键 `pom.xml`，选择 **Add as Maven Project**。

## 第 3 步：编译和启动

在 PowerShell 中执行：

```powershell
cd D:\IdeaProjects\collections-project
mvn clean package
mvn spring-boot:run
```

看到 `Undertow started on port 8080` 和 `Started CollectionsApplication` 表示启动成功。浏览器打开 `http://localhost:8080/` 会显示 404，因为没有配置根路径接口；访问 `http://localhost:8080/hello?name=小明` 可以看到 JSON 问候语。用 `Ctrl+C` 停止服务。

也可以在 IDEA 中右键 `CollectionsApplication.java`，点击 **Run 'CollectionsApplication'**。请确认运行配置使用 JDK 17；某些电脑的 PowerShell `java -version` 可能仍指向 Java 8。

## 第 4 步：读懂这次搭建的三个文件

- `pom.xml`：Maven 读取它来下载依赖并打包；Spring Boot 父 POM 统一管理大部分依赖版本。
- `CollectionsApplication.java`：`main` 方法启动 Spring Boot；`@SpringBootApplication` 会扫描 `com.zhengsijin.collections` 及其子包。
- `application.yml`：设置应用名和 HTTP 端口。当前没有数据库地址、账号或密钥。

现在已有 `com.zhengsijin.collections.controller.HelloController` 作为第一个 Spring MVC 接口。后续可逐步学习 Service、请求参数、返回值和异常处理。当前不需要数据库、Redis 或其他外部服务即可启动。

## 用 Postman 测试 B 站链接解析

1. 启动应用，在 Postman 中新建请求，方法选 **POST**，地址填 `http://localhost:8080/api/videos/parse-link`。
2. 在 **Body → raw** 中选择 **JSON**，输入：

   ```json
   {"url":"https://www.bilibili.com/video/BV1AD421K7Ha/?p=2"}
   ```

3. 点击 **Send**。预期状态是 `200 OK`，响应为 `{"bvid":"BV1AD421K7Ha","page":2}`。

也可以把 `url` 改为单独的 `BV1AD421K7Ha`，此时 `page` 为 `1`。输入其他网站地址或 `p=0` 时，预期状态为 `400 Bad Request`。本接口只解析输入文字，不访问 B 站，也不保存视频信息。
