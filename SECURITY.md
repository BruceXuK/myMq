# 安全配置说明

## 敏感信息处理

当前项目在 `application.yml` 文件中以明文形式存储了邮箱密码等敏感信息，这在生产环境中是不安全的。

### 推荐的安全实践

1. **使用环境变量**
   将敏感信息存储在环境变量中，然后在配置文件中引用：
   ```yaml
   spring:
     mail:
       password: ${MAIL_PASSWORD}
   ```

2. **使用配置中心**
   使用 Spring Cloud Config 或其他配置中心来管理敏感配置。

3. **使用外部密钥管理服务**
   在生产环境中，建议使用如 HashiCorp Vault、AWS Secrets Manager 等专业密钥管理服务。

### 修改步骤

1. 从配置文件中移除明文密码：
   ```yaml
   spring:
     mail:
       # password: xxxxxx  # 删除这行
   ```

2. 设置环境变量：
   ```bash
   export MAIL_PASSWORD=xxxxxx
   ```

3. 在配置文件中引用环境变量：
   ```yaml
   spring:
     mail:
       password: ${MAIL_PASSWORD}
   ```

## SSL配置一致性

当前项目使用 STARTTLS 加密连接，但为了更高的安全性，建议使用 SSL 加密连接。

### 当前配置（STARTTLS）：
```yaml
spring:
  mail:
    host: smtp.163.com
    port: 25
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### 推荐配置（SSL）：
```yaml
spring:
  mail:
    host: smtp.163.com
    port: 465 
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          ssl:
            enable: true
```

### 配置说明

- `port: 25` 改为 `port: 465` - 使用 SSL 端口
- `starttls.enable: true` 改为 `ssl.enable: true` - 使用 SSL 而不是 STARTTLS

这样配置可以提供更安全的加密连接，防止邮件内容在传输过程中被窃取。
