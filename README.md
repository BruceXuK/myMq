# RocketMQ 微服务消息处理系统

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.13-brightgreen.svg)](#)
[![RocketMQ](https://img.shields.io/badge/RocketMQ-2.2.3-orange.svg)](#)

本项目是一个基于 RocketMQ 的微服务消息处理系统，采用事件驱动架构，实现了异步处理各种业务消息的功能。项目通过 Maven 多模块方式管理，包含用户服务、订单服务、库存服务、邮件服务、日志服务等多个微服务模块，通过消息队列实现服务间解耦。系统具有高可用性、可扩展性和容错能力，适用于生产环境。

## 目录
- [项目介绍](#rocketmq-微服务消息处理系统)
- [项目结构](#项目结构)
- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [架构特点](#架构特点)
- [模块间依赖关系](#模块间依赖关系)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API 接口](#api-接口)
- [消息队列配置说明](#消息队列配置说明)
- [日志服务模块](#日志服务模块)
- [@Log 注解使用说明](#log-注解使用说明)
- [安全注意事项](#安全注意事项)
- [业务流程](#业务流程)
- [贡献](#贡献)
- [许可证](#许可证)

## 项目结构

```
myMq/
├── shared/                     # 共享模块，包含各服务共用的模型类和DTO
│   └── src/main/java/
│       └── com.bruce.mq.shared/
│           ├── email/           # 邮件相关模型类和枚举
│           ├── inventory/model/ # 库存相关模型类
│           ├── order/model/     # 订单相关模型类
│           ├── user/            # 用户相关模型类和DTO
│           │   ├── dto/         # 用户数据传输对象
│           │   ├── model/       # 用户模型类
│           │   └── service/     # 用户服务接口
│           ├── message/         # 消息相关枚举
│           ├── notification/    # 通知相关模型类
│           ├── OrderDTO.java    # 订单DTO
│           └── UserDTO.java     # 用户DTO
├── common/                     # 通用模块，包含异常处理和安全配置
│   └── src/main/java/
│       └── com.bruce.mq.common/
│           ├── exception/       # 通用异常处理
│           │   ├── handler/     # 异常处理器
│           │   └── BusinessException.java # 业务异常类
│           └── security/        # 通用安全配置
│               └── CommonSecurityConfig.java # 安全配置类
├── user-service/               # 用户服务模块
│   ├── src/main/java/
│   │   └── com.bruce.mq.user/
│   │       ├── controller/      # 用户相关REST API
│   │       ├── service/         # 用户业务逻辑实现
│   │       │   └── UserServiceImpl.java # 用户服务实现
│   │       ├── repository/      # 用户数据访问层
│   │       │   └── UserRepository.java # 用户仓储接口
│   │       ├── config/          # 安全配置类
│   │       │   └── UserSecurityConfig.java # 用户服务安全配置
│   │       ├── exception/       # 异常处理类
│   │       │   └── UserGlobalExceptionHandler.java # 用户服务全局异常处理
│   │       ├── rocketmq/        # RocketMQ 生产者
│   │       │   └── MaintenanceNotificationProducer.java # 维护通知生产者
│   │       │   └── PointToPointMessageProducer.java # 点对点消息生产者
│   │       └── UserApplication.java # 用户服务主应用类
│   └── src/main/resources/
│       └── application.yml      # 用户服务配置文件
├── order-service/              # 订单服务模块
│   ├── src/main/java/
│   │   └── com.bruce.mq.order/
│   │       ├── controller/      # 订单相关REST API
│   │       │   └── OrderController.java # 订单控制器
│   │       ├── service/         # 订单业务逻辑实现
│   │       │   ├── OrderService.java # 订单服务接口
│   │       │   └── OrderServiceImpl.java # 订单服务实现
│   │       │   └── MessageService.java # 消息服务接口
│   │       │   └── MessageServiceImpl.java # 消息服务实现
│   │       ├── repository/      # 订单数据访问层
│   │       │   └── OrderRepository.java # 订单仓储接口
│   │       ├── rocketmq/        # RocketMQ 相关监听器
│   │       │   └── MaintenanceNotificationListener.java # 维护通知监听器
│   │       │   └── OrderTimeoutListener.java # 订单超时监听器
│   │       │   └── PointToPointMessageListener.java # 点对点消息监听器
│   │       ├── config/          # 安全配置类
│   │       │   └── OrderSecurityConfig.java # 订单服务安全配置
│   │       └── OrderApplication.java # 订单服务主应用类
│   └── src/main/resources/
│       └── application.yml      # 订单服务配置文件
├── inventory-service/          # 库存服务模块
│   ├── src/main/java/
│   │   └── com.bruce.mq.inventory/
│   │       ├── controller/      # 库存相关REST API
│   │       │   └── InventoryController.java # 库存控制器
│   │       ├── service/         # 库存业务逻辑实现
│   │       │   ├── InventoryService.java # 库存服务接口
│   │       │   └── InventoryServiceImpl.java # 库存服务实现
│   │       ├── repository/      # 库存数据访问层
│   │       │   └── InventoryRepository.java # 库存仓储接口
│   │       ├── rocketmq/        # RocketMQ 配置和消费者服务
│   │       │   └── MessageConsumer.java # 消息消费者
│   │       │   └── MaintenanceNotificationListener.java # 维护通知监听器
│   │       │   └── OrderCancelledListener.java # 订单取消监听器
│   │       ├── config/          # 安全配置类
│   │       │   └── InventorySecurityConfig.java # 库存服务安全配置
│   │       └── InventoryApplication.java # 库存服务主应用类
│   └── src/main/resources/
│       └── application.yml      # 库存服务配置文件
├── email-service/              # 邮件服务模块
│   ├── src/main/java/
│   │   └── com.bruce.mq.email/
│   │       ├── config/          # 配置类
│   │       │   └── MailAdminConfig.java # 邮件管理员配置类
│   │       ├── controller/      # 邮件相关REST API
│   │       │   └── EmailController.java # 邮件控制器
│   │       ├── service/         # 邮件发送服务
│   │       │   └── EmailService.java # 邮件服务实现
│   │       ├── rocketmq/        # RocketMQ 配置和消费者服务
│   │       │   ├── EmailMessageListener.java # 邮件消息监听器
│   │       │   ├── MaintenanceNotificationListener.java # 维护通知邮件监听器
│   │       │   └── MessageConsumer.java # 消息消费者
│   │       ├── config/          # 安全配置类
│   │       │   └── EmailSecurityConfig.java # 邮件服务安全配置
│   │       └── EmailServiceApplication.java # 邮件服务主应用类
│   └── src/main/resources/
│       └── application.yml      # 邮件服务配置文件（含邮箱配置）
├── log-service/                # 日志服务模块
├── .env                        # 环境变量配置文件
└── pom.xml                     # 父项目 Maven 配置
```

## 功能特性

1. **业务功能支持**：
   - 用户注册与管理
   - 订单创建、支付、取消与管理
   - 库存查询与扣减
   - 邮件验证码发送
   - 自定义邮件发送
   - 系统维护通知广播
   - 订单超时自动取消
   - 点对点消息传递（只有一个消费者处理）

2. **技术栈**：
   - Spring Boot 2.6.13
   - Spring Cloud 2021.0.5
   - Apache RocketMQ 2.2.3
   - JavaMailSender
   - Elasticsearch（日志服务）

3. **架构特点**：
   - 微服务架构设计
   - 多模块Maven项目结构
   - 共享模块减少代码重复
   - 枚举类型提高代码可维护性
   - 事件驱动架构实现服务间解耦
   - RocketMQ 消息队列实现异步处理
   - 延迟消息实现定时任务处理
   - 重试机制增强系统稳定性

## 模块间依赖关系

- `shared`：提供公共模型类和枚举，被所有其他模块依赖
- `common`：提供通用异常处理和安全配置，被所有服务模块依赖
- `user-service`：用户服务模块，负责用户注册和管理功能
- `order-service`：订单服务模块，负责订单创建和管理
- `inventory-service`：库存服务模块，负责库存查询和扣减
- `email-service`：邮件服务模块，负责处理各类邮件发送请求

## 环境要求

- Java 8 或更高版本
- Maven 3.6 或更高版本
- RocketMQ 4.9 或更高版本
- Elasticsearch 7.x（用于日志服务）
- 邮件服务器（如 Gmail、163 等）

## 快速开始

### 1. 克隆项目

```bash
git clone <项目地址>
cd myMq
```

### 2. 配置环境

1. 启动 RocketMQ Name Server 和 Broker
2. （可选）启动 Elasticsearch（如果需要使用日志服务）
3. 配置各服务的 `application.yml` 文件或设置环境变量

### 3. 构建项目

```bash
mvn clean install
```

### 4. 启动服务

按以下顺序启动服务：
1. `shared` 模块
2. `common` 模块
3. 各个业务服务（user-service, order-service, inventory-service, email-service, log-service）

```bash
# 示例：启动用户服务
cd user-service
mvn spring-boot:run
```

## 配置说明

系统使用以下配置项，可以通过 `.env` 文件进行配置：

### 邮件服务配置
- `MAIL_HOST`: 邮件服务器主机地址（默认：smtp.gmail.com）
- `MAIL_PORT`: 邮件服务器端口（默认：465）
- `MAIL_USERNAME`: 邮箱用户名（默认：your_email@gmail.com）
- `MAIL_PASSWORD`: 邮箱密码（默认：your_password）
- `ADMIN_EMAIL_ADDRESS`: 系统管理员邮箱地址（默认：your_email_address@qq.com）

### RocketMQ 配置
- `ROCKETMQ_NAMESRV_ADDR`: RocketMQ NameServer 地址（默认：localhost:9876）

### 配置文件加载顺序
1. 系统环境变量
2. `.env` 文件（如果存在）
3. `application.yml` 中的默认值

## API 接口

### 用户服务接口

- `POST /user/register` - 用户注册
- `GET /user/send-code` - 发送验证码到指定邮箱
- `GET /user/check-email` - 检查邮箱是否已被注册
- `POST /user/maintenance-notification` - 发送系统维护通知
- `POST /user/point-to-point-message` - 发送点对点消息

### 订单服务接口

- `POST /orders` - 创建订单
- `GET /orders/{id}` - 根据ID获取订单信息
- `POST /orders/{id}/pay` - 支付订单
- `POST /orders/{id}/cancel` - 取消订单

### 库存服务接口

- `POST /inventories/deduct` - 扣减库存
- `GET /inventories/product/{productId}` - 根据商品ID获取库存信息

### 邮件服务接口

- `POST /email/send-custom` - 发送自定义邮件

## 消息队列配置说明

### RocketMQ 配置

项目使用 RocketMQ 实现服务间异步通信:

- Name Server: `localhost:9876`
- 不同服务有不同的Producer Group配置
  - 用户服务: `user-service-producer`
  - 订单服务: `order-producer-group`
  - 库存服务: `inventory-producer-group`
  - 邮件服务: `email-service-producer`
  - 日志服务: `log-producer-group`

### RocketMQ 延迟消息级别

订单服务支持延迟消息处理，用于订单超时检查:

| Level | Delay Time |
|-------|------------|
| 1     | 1s         |
| 2     | 5s         |
| 3     | 10s        |
| 4     | 30s        |
| 5     | 1m         |
| 6     | 2m         |
| 7     | 3m         |
| 8     | 4m         |
| 9     | 5m         |
| 10    | 6m         |
| 11    | 7m         |
| 12    | 8m         |
| 13    | 9m         |
| 14    | 10m        |
| 15    | 20m        |
| 16    | 30m        |
| 17    | 1h         |
| 18    | 2h         |

### RocketMQ 消息模式

项目支持两种消息模式：

1. **广播模式**：系统维护通知等消息会发送给所有相关服务，每个服务都会处理该消息
2. **点对点模式**：特定任务只需要一个服务实例处理，确保任务不会被重复执行

### 邮件配置

邮件服务模块配置为 163 邮箱的连接方式：
- SMTP 服务器: `smtp.163.com`
- 端口: `465` (SSL加密)
- 用户名: `xxxxxx@163.com`
- 密码: `xxxxxxxxxxx` (仅用于开发环境，请勿在生产环境使用)

## 业务流程

1. 用户注册时，用户服务会发送邮件验证码到用户邮箱
2. 用户填写验证码完成注册
3. 创建订单时，订单服务会：
   - 发送订单确认邮件给用户
   - 发送延迟消息用于超时检查（默认1分钟后检查）
4. 如果订单在1分钟内未支付，则触发订单超时处理：
   - 检查订单状态，如仍为待支付状态则取消订单
   - 发送订单超时取消通知邮件
5. 如果订单在1分钟内完成支付，则：
   - 更新订单状态为已支付
   - 发送消息到库存服务扣减库存
   - 库存扣减完成后，会发送邮件通知给用户
6. 用户或管理员可以取消订单：
   - 更新订单状态为已取消
   - 发送订单取消通知邮件
7. 系统管理员可以发送系统维护通知给所有服务：
   - 各个服务收到通知后会通过MQ发送邮件请求给邮件服务
   - 邮件服务向指定负责人邮箱发送邮件提醒
8. 订单服务和库存服务收到维护通知后会通过MQ发送邮件请求给邮件服务
9. 对于只需要一个消费者处理的任务，可以使用点对点消息模式，确保任务不会被重复执行

# 日志服务模块

## 简介

日志服务模块用于收集各个微服务的日志，并通过RocketMQ将日志传输到Elasticsearch中进行存储和查询。

## 架构设计

```
┌─────────────┐    发送日志消息    ┌──────────────┐    接收日志消息    ┌──────────────────┐
│   服务A     │ ────────────────→ │  RocketMQ    │ ────────────────→ │   日志服务模块   │
└─────────────┘                  └──────────────┘                  └──────────────────┘
                                                                      │
                                                                      │ 存储日志到ES
                                                                      ↓
                                                                ┌──────────────┐
                                                                │ Elasticsearch│
                                                                └──────────────┘
```

## 模块结构

- `log-service`: 日志服务主模块
- `shared`: 共享模块，包含日志消息模型和生产者

## 核心组件

### LogMessage (共享模块)
日志消息模型，用于在服务和日志服务之间传输日志数据。

### LogProducer (共享模块)
日志生产者，用于将日志消息发送到RocketMQ。

### Log (日志服务)
日志实体类，用于在Elasticsearch中存储日志。

### LogMessageListener (日志服务)
RocketMQ消息监听器，接收日志消息并存储到Elasticsearch。

### LogService (日志服务)
日志服务层，处理日志的存储和查询逻辑。

### LogRepository (日志服务)
日志数据访问层，与Elasticsearch交互。

### LogController (日志服务)
REST API控制器，提供日志查询接口。

## 使用方法

1. 在各服务中引入shared模块依赖
2. 配置logback-spring.xml使用MqLogAppender
3. 启动RocketMQ和Elasticsearch
4. 启动日志服务和其他微服务

## API接口

- `GET /logs/service/{service}?startTime=&endTime=` - 根据服务名称和时间范围查询日志
- `GET /logs/level/{level}` - 根据日志级别查询日志
- `GET /logs/trace/{traceId}` - 根据追踪ID查询日志

## 配置说明

### 日志服务配置 (application.yml)
```yaml
server:
  port: 8089

spring:
  application:
    name: log-service
  elasticsearch:
    uris: ${ES_URI:http://localhost:9200}

rocketmq:
  name-server: ${ROCKETMQ_NAMESRV_ADDR:localhost:9876}
  producer:
    group: log-producer-group
```

### 其他服务配置
其他微服务需要配置RocketMQ连接信息:
```yaml
rocketmq:
  name-server: ${ROCKETMQ_NAMESRV_ADDR:localhost:9876}
```

## @Log 注解使用说明

### 简介
@Log 注解是一个自定义注解，用于自动记录方法的访问日志，包括请求参数、路径、IP地址等信息，并通过MQ发送到日志服务存储到Elasticsearch中。

### 使用方法

1. 在需要记录日志的方法上添加 @Log 注解：

```java
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Log("查询用户信息")
    @GetMapping("/info")
    public UserInfo getUserInfo(@RequestParam String userId) {
        // 业务逻辑
        return userInfo;
    }
    
    @Log(value = "创建用户", recordParams = true, recordResult = true)
    @PostMapping("/create")
    public Result createUser(@RequestBody UserCreateRequest request) {
        // 业务逻辑
        return result;
    }
}
```

### 注解参数说明

- `value`: 操作描述信息，默认为空
- `recordParams`: 是否记录请求参数，默认为true
- `recordResult`: 是否记录返回结果，默认为false

### 自动记录的信息

使用 @Log 注解后，系统会自动记录以下信息：
- 请求URL
- 请求方法 (GET, POST等)
- 请求参数
- 请求IP地址
- 执行时间
- User-Agent
- 响应状态码
- 服务名称
- 类名和方法名
- 时间戳

### 注意事项

1. @Log 注解仅适用于Spring管理的Bean中的方法
2. 需要确保服务中正确配置了MQ连接信息
3. 方法必须是通过HTTP请求访问的，才能获取到完整的请求信息

## 贡献

欢迎提交 Issue 或 Pull Request 来帮助改进本项目。

## 许可证

本项目采用 MIT 许可证，详见 [LICENSE](LICENSE) 文件。
