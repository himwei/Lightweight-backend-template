# 后端

🚀 Spring Boot 3 + Sa-Token RBAC 快速开发模板 (v2.0)

> **基于 Spring Boot 3.0 + JDK 17 + Sa-Token + MyBatis-Plus + Knife4j + Docker 的轻量级后台管理系统脚手架。**

---

# 📚 第一部分：技术栈核心知识点速查 (Cheat Sheet)

### 1. Sa-Token (权限认证)
*   **核心类**: `StpUtil`
*   **登录/注销**: `StpUtil.login(id)`, `StpUtil.logout()`
*   **鉴权注解**:
    *   `@SaCheckRole("admin")`
    *   `@SaCheckPermission("user:add")`
*   **自动续期**: 依靠 `active-timeout` 配置，无操作过期，有操作自动续命。

### 2. MyBatis-Plus (ORM)
*   **LambdaQuery**: `new LambdaQueryWrapper<User>().eq(User::getUsername, "admin")`
*   **自动填充**: `createTime`/`updateTime` 插入更新时自动维护，无需手动 set。

### 3. JDK 17+ 新特性 (推荐写法)
*   **不可变列表**: `List.of(1L, 2L)` (替代 `Arrays.asList` 或 `Collections.singletonList`)。
*   **文本块**: 使用 `"""` 拼接 SQL 或 JSON。
*   **Record**: `public record UserDto(String name) {}`。

### 4. 常用工具
*   **Hutool**: `StrUtil`, `BeanUtil`, `DateUtil`。
*   **Knife4j**: `@Tag`(类), `@Operation`(方法), `@Schema`(实体)。

---

## 🛠️ 第二部分：后端模板开发备忘录 (Project Guide)

### 1. 环境与版本
*   **JDK**: 17
*   **Spring Boot**: 3.0.2
*   **Sa-Token**: 1.39.0 (**Core/Starter/Redis插件版本严格一致**)
*   **Knife4j**: 4.4.0
*   **MySQL**: 8.0+
*   **Redis**: 5.0+ (必须配置，否则 Sa-Token 报错)

### 2. 安全与加密 (新增 🔥)
本项目采用了 **MD5 + 动态盐值 (Salt)** 的加密方式。

*   **盐值配置**: 在 `application.yml` 中修改 `project.security.salt`。
*   **工具类**: `utils/PasswordUtils.java` (Spring 容器管理)。
*   **使用方式**:
    ```java
    @Resource
    private PasswordUtils passwordUtils;
    
    // 加密 (注册/改密)
    String encryptPwd = passwordUtils.encrypt("123456");
    
    // 校验 (登录)
    if (!passwordUtils.matches(inputPwd, dbPwd)) {
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
    }
    ```

### 3. 角色管理 (新增 🔥)
*   **拒绝魔法值**: 不要直接写 `roleId = 2L`。
*   **枚举管理**: 使用 `model/enums/RoleEnum.java`。
*   **分配角色示例**:
    ```java
    // 给用户分配 "普通用户" 角色 (使用 List.of 创建不可变列表)
    sysRoleService.assignRoles(userId, List.of(RoleEnum.USER.getId()));
    ```

### 4. ID 精度与序列化
*   **问题**: 数据库 ID (19位) 传给前端会丢失精度。
*   **解决**: 已配置 `JacksonConfig`，后端出参时自动将 `Long` 转为 `String`。
*   **注意**: 后端入参接收 `Long` 即可，Spring 会自动处理 String->Long 的转换。

### 5. 开发规范示例
```java
@Tag(name = "示例模块")
@RestController
@RequestMapping("/demo")
public class DemoController {

    @PostMapping("/do")
    @SaCheckRole("admin") // 1. 鉴权
    @Log(title = "示例模块", businessType = "执行操作") // 2. 日志
    public BaseResponse<String> doSomething(@RequestBody @Valid DemoDTO dto) { // 3. 校验
        return ResultUtils.success("ok");
    }
}
```

---

## 🐳 第三部分：Docker 部署指南 (新增 🔥)

本项目已内置 `Dockerfile`，支持一键容器化部署。

### 1. 准备工作
在 `pom.xml` 中确认已添加 `<finalName>app</finalName>`，确保打包出的文件名固定。

### 2. 构建流程

**Step 1: Maven 打包**
```bash
mvn clean package -DskipTests
```
*(成功后会在 target 目录下生成 app.jar)*

**Step 2: 构建镜像**
```bash
# 注意最后有个点 .
docker build -t test-backend:v1 .
```

**Step 3: 运行容器**
```bash
docker run -d \
  -p 8123:8123 \
  --name my-backend \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/test-template-backend-app?..." \
  -e SPRING_DATA_REDIS_HOST="host.docker.internal" \
  test-backend:v1
```
*(注意：如果连接宿主机数据库，Host 请使用 host.docker.internal 或实际 IP，不要用 127.0.0.1)*

---

## 📂 目录结构索引

*   `annotation/Log` - 日志注解
*   `aspect/LogAspect` - 日志切面
*   `config/JacksonConfig` - **Long转String配置**
*   `config/SaTokenConfig` - 拦截器/放行配置
*   `model/enums/RoleEnum` - **角色枚举**
*   `utils/PasswordUtils` - **密码加密工具**
*   `handler/MyMetaObjectHandler` - 自动填充时间
*   `Dockerfile` - 容器构建文件

---

### 💡 常见避雷
1.  **注册报错？** 检查 `SaTokenConfig` 是否放行了 `/user/register`。
2.  **Redis乱码？** 检查 pom 是否用了 `sa-token-redis-jackson`。
3.  **前端代码没 Token？** 前端拦截器记得加 header: `satoken`。






