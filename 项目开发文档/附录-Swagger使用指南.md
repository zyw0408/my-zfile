# Swagger/Knife4j 使用指南

> 完整的API文档配置和使用说明

## 📚 目录
- [什么是Swagger](#什么是swagger)
- [为什么选择Knife4j](#为什么选择knife4j)
- [配置说明](#配置说明)
- [注解使用](#注解使用)
- [在线测试](#在线测试)
- [常见问题](#常见问题)

---

## 什么是Swagger

Swagger是一个API文档规范，可以：
- ✅ 自动生成API文档
- ✅ 在线测试API接口
- ✅ 生成客户端SDK
- ✅ 实时更新，无需手动维护

## 为什么选择Knife4j

Knife4j是Swagger的增强版，提供：
- 🎨 更美观的UI界面
- 🚀 更好的性能
- 🔍 更强大的搜索功能
- 📱 更好的移动端适配
- 🌐 中文界面支持

---

## 配置说明

### 1. Maven依赖

已在 `pom.xml` 中配置：
```xml
<!-- Knife4j (Swagger增强版) -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.5.0</version>
</dependency>
```

### 2. application.yml配置

```yaml
# Knife4j配置
knife4j:
  enable: true  # 是否启用Knife4j增强功能
  setting:
    language: zh_cn  # 中文界面
    swagger-model-name: 实体类列表  # 模型列表名称
```

### 3. Swagger配置类

**文件**: `common/config/SwaggerConfig.java`

```java
package com.noname.background.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 基本信息
                .info(new Info()
                        .title("云盘系统 API 文档")
                        .version("1.0.0")
                        .description("基于Spring Boot 3 + Vue 3的在线云盘系统\n\n" +
                                "## 主要功能\n" +
                                "- 用户认证（登录、注册、Token刷新）\n" +
                                "- 文件管理（上传、下载、删除、重命名）\n" +
                                "- 文件分享（生成分享链接、访问控制）\n" +
                                "- 用户管理（个人信息、存储空间）\n" +
                                "- 管理后台（用户管理、系统设置）")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com")
                                .url("https://github.com/yourusername/cloud-disk"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                // 安全认证配置
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Token",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("请在下方输入JWT Token\n\n" +
                                                "格式：Bearer {token}\n\n" +
                                                "获取方式：调用登录接口后从响应中获取accessToken")));
    }
}
```

---

## 注解使用

### 1. Controller类注解

```java
@Tag(name = "认证管理", description = "用户登录、注册、Token刷新等接口")
@RestController
@RequestMapping("/auth")
public class AuthController {
    // ...
}
```

**@Tag注解说明**：
- `name`: 模块名称（在文档中显示的分组名）
- `description`: 模块描述（详细说明）

### 2. 接口方法注解

```java
@Operation(
    summary = "用户登录",
    description = "通过用户名和密码登录系统，返回JWT Token和用户信息"
)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "登录成功"),
    @ApiResponse(responseCode = "400", description = "用户名或密码错误"),
    @ApiResponse(responseCode = "500", description = "系统错误")
})
@PostMapping("/login")
public Result<LoginResponse> login(
    @Parameter(description = "登录请求参数", required = true)
    @Valid @RequestBody LoginRequest request) {
    return Result.success(authService.login(request));
}
```

**@Operation注解说明**：
- `summary`: 接口简短说明
- `description`: 接口详细说明（支持Markdown）

**@Parameter注解说明**：
- `description`: 参数说明
- `required`: 是否必填
- `example`: 示例值

### 3. 请求/响应DTO注解

```java
@Data
@Schema(description = "登录请求参数")
public class LoginRequest {
    
    @Schema(description = "用户名", example = "admin", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @Schema(description = "密码", example = "admin123", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
```

```java
@Data
@Schema(description = "登录响应数据")
public class LoginResponse {
    
    @Schema(description = "访问令牌，有效期2小时")
    private String accessToken;
    
    @Schema(description = "刷新令牌，有效期7天")
    private String refreshToken;
    
    @Schema(description = "令牌过期时间（毫秒）", example = "7200000")
    private Long expiresIn;
    
    @Schema(description = "用户信息")
    private UserInfo userInfo;
}
```

### 4. 完整示例

**文件管理Controller示例**：
```java
@Tag(name = "文件管理", description = "文件上传、下载、删除、重命名等操作")
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "获取文件列表", description = "分页查询用户的文件列表，支持排序和筛选")
    @Parameters({
        @Parameter(name = "parentId", description = "父文件夹ID，0表示根目录", example = "0"),
        @Parameter(name = "pageNum", description = "页码", example = "1"),
        @Parameter(name = "pageSize", description = "每页数量", example = "20"),
        @Parameter(name = "sortBy", description = "排序字段：name, size, time", example = "time"),
        @Parameter(name = "sortOrder", description = "排序方式：asc, desc", example = "desc")
    })
    @GetMapping
    public Result<PageResult<FileResponse>> listFiles(
            @RequestParam(defaultValue = "0") Long parentId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        return Result.success(fileService.listFiles(parentId, pageNum, pageSize, sortBy, sortOrder));
    }

    @Operation(summary = "上传文件", description = "上传单个文件到指定文件夹")
    @PostMapping("/upload")
    public Result<FileResponse> uploadFile(
            @Parameter(description = "文件对象", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "父文件夹ID", example = "0")
            @RequestParam(defaultValue = "0") Long parentId) {
        return Result.success(fileService.uploadFile(file, parentId));
    }

    @Operation(summary = "删除文件", description = "删除指定文件（移入回收站）")
    @DeleteMapping("/{fileId}")
    public Result<Void> deleteFile(
            @Parameter(description = "文件ID", required = true, example = "1")
            @PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return Result.success();
    }
}
```

---

## 在线测试

### 1. 访问Swagger文档

启动项目后，访问以下地址：

**Knife4j增强版**（推荐）：
```
http://localhost:8080/api/doc.html
```

**Swagger原生UI**：
```
http://localhost:8080/api/swagger-ui/index.html
```

**OpenAPI JSON**：
```
http://localhost:8080/api/v3/api-docs
```

### 2. 测试无需认证的接口

以登录接口为例：

1. 访问 `http://localhost:8080/api/doc.html`
2. 找到"认证管理"模块
3. 展开"用户登录"接口
4. 点击"调试"或"试一下"
5. 填写请求参数：
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
6. 点击"发送"
7. 查看响应结果，复制 `accessToken`

### 3. 测试需要认证的接口

1. 点击右上角的"🔓 Authorize"按钮
2. 在弹出框中输入：
   ```
   Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOi...
   ```
   （注意：`Bearer ` + 空格 + token）
3. 点击"Authorize"
4. 关闭弹窗
5. 现在所有接口请求都会自动带上Token
6. 测试任意需要认证的接口

### 4. Knife4j特色功能

#### 4.1 离线文档
- 点击"文档管理" → "离线文档"
- 可以下载Markdown或HTML格式的文档

#### 4.2 全局参数
- 点击"文档管理" → "全局参数设置"
- 可以设置所有请求都带上的参数（如Token）

#### 4.3 接口搜索
- 在页面顶部的搜索框输入关键词
- 快速定位到目标接口

#### 4.4 个性化设置
- 点击右上角"个性化设置"
- 可以调整界面主题、字体大小等

---

## 常见问题

### Q1: Swagger UI打不开？

**检查项**：
1. 项目是否正常启动？
2. 端口是否正确？（默认8080）
3. context-path是否配置？（默认/api）
4. 依赖是否正确导入？

**解决方案**：
```bash
# 查看依赖
mvn dependency:tree | grep knife4j

# 查看启动日志
# 应该有类似输出：
# Knife4j enabled: true
# Swagger-UI: http://localhost:8080/api/swagger-ui/index.html
```

### Q2: 接口不显示在文档中？

**原因**：
1. Controller类没有 `@RestController` 注解
2. 没有扫描到该包
3. 接口路径配置错误

**解决方案**：
```java
// 检查主启动类
@SpringBootApplication
@MapperScan("com.noname.background.module.*.mapper")  // 确保扫描范围正确
public class BackgroundApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackgroundApplication.class, args);
    }
}
```

### Q3: 如何隐藏某些接口？

**方式1**：使用 `@Hidden` 注解
```java
@Hidden  // 该接口不会显示在文档中
@GetMapping("/internal")
public Result<String> internalApi() {
    return Result.success("internal");
}
```

**方式2**：配置扫描路径
```java
@Bean
public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/api/**")
            .pathsToExclude("/api/internal/**")  // 排除内部接口
            .build();
}
```

### Q4: 如何配置多个分组？

```java
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户端API")
                .pathsToMatch("/api/auth/**", "/api/files/**", "/api/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("管理端API")
                .pathsToMatch("/api/admin/**")
                .build();
    }
}
```

### Q5: Token认证不生效？

**检查项**：
1. Token格式是否正确？（`Bearer ` + token）
2. Token是否过期？
3. SecurityConfig是否正确配置？

**调试方法**：
```java
// 在JWT过滤器中添加日志
log.debug("Token from header: {}", token);
log.debug("Token valid: {}", jwtUtil.validateToken(token));
```

### Q6: 文件上传接口如何测试？

**在Swagger UI中**：
1. 找到文件上传接口
2. 点击"试一下"
3. 点击"选择文件"按钮
4. 选择本地文件
5. 点击"执行"

**注意**：
- 确保文件大小不超过配置的限制（默认100MB）
- 确保文件类型被允许

### Q7: 如何导出API文档？

**方式1**：Knife4j离线文档
1. 访问 `http://localhost:8080/api/doc.html`
2. 点击"文档管理" → "离线文档"
3. 选择格式（Markdown/HTML）
4. 点击"下载"

**方式2**：导出OpenAPI JSON
```bash
curl http://localhost:8080/api/v3/api-docs > api-docs.json
```

**方式3**：使用Swagger Codegen生成客户端
```bash
# 安装swagger-codegen
npm install -g @openapitools/openapi-generator-cli

# 生成TypeScript客户端
openapi-generator-cli generate \
  -i http://localhost:8080/api/v3/api-docs \
  -g typescript-axios \
  -o ./client
```

---

## 最佳实践

### 1. 注解规范

✅ **推荐写法**：
```java
@Tag(name = "文件管理", description = "文件的增删改查操作")
@Operation(summary = "上传文件", description = "支持单文件上传，最大100MB")
@Parameter(description = "文件对象", required = true)
```

❌ **不推荐写法**：
```java
@Tag(name = "file")  // 太简略
@Operation(summary = "upload")  // 英文不直观
// 缺少参数说明
```

### 2. 响应示例

在DTO中添加示例值：
```java
@Data
@Schema(description = "文件信息")
public class FileResponse {
    @Schema(description = "文件ID", example = "1")
    private Long id;
    
    @Schema(description = "文件名", example = "我的文档.pdf")
    private String fileName;
    
    @Schema(description = "文件大小（字节）", example = "1024000")
    private Long fileSize;
    
    @Schema(description = "创建时间", example = "2025-01-01 10:00:00")
    private String createTime;
}
```

### 3. 错误码文档化

在统一响应类中添加说明：
```java
@Data
@Schema(description = "统一响应对象")
public class Result<T> {
    
    @Schema(description = "响应码：200-成功，400-业务错误，500-系统错误", example = "200")
    private Integer code;
    
    @Schema(description = "响应消息", example = "success")
    private String message;
    
    @Schema(description = "响应数据")
    private T data;
}
```

### 4. 分组管理

按业务模块合理分组：
```java
@Tag(name = "01-认证管理")  // 使用数字排序
@Tag(name = "02-文件管理")
@Tag(name = "03-用户管理")
@Tag(name = "04-分享管理")
@Tag(name = "99-系统管理")
```

---

## 生产环境配置

### 禁用Swagger（可选）

在生产环境建议禁用Swagger：

```yaml
# application-prod.yml
knife4j:
  enable: false  # 生产环境关闭
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```

### 访问控制

如果保留Swagger，建议添加访问控制：

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/doc.html", "/api/swagger-ui/**", "/api/v3/api-docs/**")
            .hasRole("ADMIN")  // 只允许管理员访问
            .anyRequest().authenticated()
        );
        return http.build();
    }
}
```

---

## 总结

通过Swagger/Knife4j，我们实现了：

✅ **零配置**：只需添加注解，自动生成文档  
✅ **实时更新**：代码改动，文档自动同步  
✅ **在线测试**：无需Postman，直接在文档中测试  
✅ **美观易用**：Knife4j提供了更好的用户体验  
✅ **支持导出**：可以导出离线文档  

**下一步**：
- 为所有Controller添加Swagger注解
- 定期检查文档完整性
- 在团队中推广使用

---

*最后更新: 2025-01-01*

