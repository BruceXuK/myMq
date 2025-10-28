# RocketMQ 微服务消息处理系统

本项目是一个基于 RocketMQ 的微服务消息处理系统，采用事件驱动架构，实现了异步处理各种业务消息的功能。项目通过 Maven 多模块方式管理，包含用户服务、订单服务、库存服务、邮件服务等多个微服务模块，通过消息队列实现服务间解耦。

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
│   │       ├── config/          # 安全配置类
│   │       │   └── InventorySecurityConfig.java # 库存服务安全配置
│   │       └── InventoryApplication.java # 库存服务主应用类
│   └── src/main/resources/
│       └── application.yml      # 库存服务配置文件
├── email-service/              # 邮件服务模块
│   ├── src/main/java/
│   │   └── com.bruce.mq.email/
│   │       ├── service/         # 邮件发送服务
│   │       │   └── EmailService.java # 邮件服务实现
│   │       ├── rocketmq/        # RocketMQ 配置和消费者服务
│   │       │   ├── EmailMessageListener.java # 邮件消息监听器
│   │       │   └── MessageConsumer.java # 消息消费者
│   │       ├── config/          # 安全配置类
│   │       │   └── EmailSecurityConfig.java # 邮件服务安全配置
│   │       └── EmailServiceApplication.java # 邮件服务主应用类
│   └── src/main/resources/
│       └── application.yml      # 邮件服务配置文件（含邮箱配置）
└── pom.xml                     # 父项目 Maven 配置
```

## 功能特性

1. **业务功能支持**：
   - 用户注册与管理
   - 订单创建与管理
   - 库存查询与扣减
   - 邮件验证码发送
   - 自定义邮件发送
   - 系统维护通知广播
   - 订单超时自动取消

2. **技术栈**：
   - Spring Boot
   - Apache RocketMQ
   - JavaMailSender

3. **架构特点**：
   - 微服务架构设计
   - 多模块Maven项目结构
   - 共享模块减少代码重复
   - 枚举类型提高代码可维护性
   - 事件驱动架构实现服务间解耦
   - RocketMQ 消息队列实现异步处理
   - 延迟消息实现定时任务处理

## 模块间依赖关系

- `shared`：提供公共模型类和枚举，被所有其他模块依赖
- `common`：提供通用异常处理和安全配置，被所有服务模块依赖
- `user-service`：用户服务模块，负责用户注册和管理功能
- `order-service`：订单服务模块，负责订单创建和管理
- `inventory-service`：库存服务模块，负责库存查询和扣减
- `email-service`：邮件服务模块，负责处理各类邮件发送请求

## 安全注意事项

项目当前在配置文件中以明文形式存储了邮箱密码，这在生产环境中是不安全的。

### 推荐的安全实践

请查看 [SECURITY.md](SECURITY.md) 文件了解更多关于敏感信息处理的安全建议，包括使用环境变量、配置中心或外部密钥管理服务等方式来保护敏感信息。

推荐使用环境变量来设置敏感信息，例如：
```yaml
spring:
  mail:
    password: ${MAIL_PASSWORD}
```

并在运行时通过环境变量设置实际值：
```bash
export MAIL_PASSWORD=your_password
```

## API 接口

### 用户服务接口

- `POST /user/register` - 用户注册
- `GET /user/send-code` - 发送验证码到指定邮箱
- `GET /user/check-email` - 检查邮箱是否已被注册
- `POST /user/maintenance-notification` - 发送系统维护通知

### 订单服务接口

- `POST /orders` - 创建订单
- `GET /orders/{id}` - 根据ID获取订单信息

### 库存服务接口

- `POST /inventories/deduct` - 扣减库存
- `GET /inventories/product/{productId}` - 根据商品ID获取库存信息

### 邮件服务接口

邮件服务主要通过消息队列接收请求，不提供直接的REST API接口。

## 消息队列配置说明

### RocketMQ 配置

项目使用 RocketMQ 实现服务间异步通信:

- Name Server: `localhost:9876`
- 不同服务有不同的Producer Group配置
  - 用户服务: `user-service-producer`
  - 订单服务: `order-producer-group`

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

### 邮件配置

邮件服务模块配置为 163 邮箱的连接方式：
- SMTP 服务器: `smtp.163.com`
- 端口: `25`
- 开启STARTTLS加密传输
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
6. 系统管理员可以发送系统维护通知给所有服务
7. 订单服务和库存服务收到维护通知后会通过MQ发送邮件请求给邮件服务
