# @Log 注解使用说明

## 简介

@Log 注解是一个自定义注解，用于自动记录方法的访问日志，包括请求参数、路径、IP地址等信息，并通过MQ发送到日志服务存储到Elasticsearch中。

## 使用方法

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

## 注解参数说明

- `value`: 操作描述信息，默认为空
- `recordParams`: 是否记录请求参数，默认为true
- `recordResult`: 是否记录返回结果，默认为false

## 自动记录的信息

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

## 注意事项

1. @Log 注解仅适用于Spring管理的Bean中的方法
2. 需要确保服务中正确配置了MQ连接信息
3. 方法必须是通过HTTP请求访问的，才能获取到完整的请求信息

## 配置要求

要使用@Log注解，需要在服务中添加以下配置：

### Maven依赖

确保服务依赖了shared模块：

```xml
<dependency>
    <groupId>com.bruce.mq</groupId>
    <artifactId>shared</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 启用AOP

在Spring Boot主应用类或配置类上添加 @EnableAspectJAutoProxy 注解：

```java
@SpringBootApplication
@EnableAspectJAutoProxy
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

### 配置MQ连接

在 application.yml 中配置RocketMQ连接信息：

```yaml
rocketmq:
  name-server: ${ROCKETMQ_NAMESRV_ADDR:localhost:9876}
```

## 查询日志

日志服务提供了多种查询接口：

- `GET /logs/service/{service}?startTime=&endTime=` - 根据服务名称和时间范围查询日志
- `GET /logs/level/{level}` - 根据日志级别查询日志
- `GET /logs/trace/{traceId}` - 根据追踪ID查询日志
- `GET /logs/ip/{ip}` - 根据请求IP查询日志
- `GET /logs/url?url=` - 根据请求URL查询日志
- `GET /logs/method/{method}` - 根据请求方法查询日志
- `GET /logs/status/{status}` - 根据状态码查询日志

## 示例

以下是一个使用@Log注解的完整示例：

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Log("创建订单")
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }
    
    @Log("查询订单")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @Log(value = "删除订单", recordParams = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
```

当调用这些接口时，系统会自动记录访问日志并发送到日志服务进行存储和分析。