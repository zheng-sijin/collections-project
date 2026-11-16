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

**当前完成第 1～2 项基础、第 3 项的 Lombok 入门及第 4 项的 MySQL、MyBatis-Plus 入门，并添加了 `/hello`、B 站链接解析和播放源接口。** 后续按学习进度增加其他工具；Redis 等外部服务到对应阶段再准备。

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

现在已有 `com.zhengsijin.collections.controller.HelloController` 作为第一个 Spring MVC 接口。播放源接口使用 Service 处理校验与业务逻辑，使用 MyBatis-Plus Mapper 访问 MySQL。当前程序需要本机 MySQL 和 `COLLECTIONS_DB_PASSWORD` 环境变量才能正常使用播放源接口。

`VideoSource.java` 使用 Lombok 的 `@Data`：编译时自动生成 getter、setter、`toString()`、`equals()` 和 `hashCode()`。这些方法会出现在编译后的 `.class` 文件里，因此源码中不用重复手写。

## 用 Postman 测试 B 站链接解析

1. 启动应用，在 Postman 中新建请求，方法选 **POST**，地址填 `http://localhost:8080/api/videos/parse-link`。
2. 在 **Body → raw** 中选择 **JSON**，输入：

   ```json
   {"url":"https://www.bilibili.com/video/BV1AD421K7Ha/?p=2"}
   ```

3. 点击 **Send**。预期状态是 `200 OK`，响应为 `{"bvid":"BV1AD421K7Ha","page":2}`。

也可以把 `url` 改为单独的 `BV1AD421K7Ha`，此时 `page` 为 `1`。输入其他网站地址或 `p=0` 时，预期状态为 `400 Bad Request`。本接口只解析输入文字，不访问 B 站，也不保存视频信息。

## 用 Postman 测试播放源数据库接口

1. 在 MySQL 客户端执行 `SOURCE D:/IdeaProjects/collections-project/sql/init.sql;`，创建数据库与 `video_source` 表。
2. 在 IDEA 的 `CollectionsApplication` 运行配置中设置环境变量 `COLLECTIONS_DB_PASSWORD`，值为本机 MySQL root 密码；不要勾选“存储为项目文件”。重新启动程序。
3. 新建 POST 请求 `http://localhost:8080/api/videos/sources`，Body 选择 raw JSON：

   ```json
   {"title":"测试视频","episodeNumber":1,"lineName":"线路一","playType":"MP4","playUrl":"https://example.com/test.mp4"}
   ```

4. 预期返回 HTTP 201，响应中含数据库生成的 `id`。用这个 ID 发送 GET `http://localhost:8080/api/videos/sources/{id}/play`，预期取回同一条记录；不存在的 ID 返回 HTTP 404。

`playType` 目前仅接受 `MP4` 或 `HLS`；这里只保存链接，不保证第三方视频地址能播放。上面的 `example.com` 地址仅用于测试数据写入。
