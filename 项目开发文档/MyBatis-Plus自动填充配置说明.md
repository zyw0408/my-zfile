# MyBatis-Plus 自动填充配置说明

## 概述

MyBatis-Plus 提供了自动填充功能，用于在插入或更新数据时自动填充某些字段，如创建时间、更新时间等。本文档详细说明了如何在本项目中配置和使用这一功能。

## 配置步骤

### 1. 实体类配置

在实体类中，使用 `@TableField` 注解的 `fill` 属性来标记需要自动填充的字段：

```java
/**
 * 创建时间
 */
@TableField(value = "create_time", fill = FieldFill.INSERT)
private LocalDateTime create_time;

/**
 * 更新时间
 */
@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
private LocalDateTime update_time;
```

### 2. 自动填充处理器

创建一个类实现 `MetaObjectHandler` 接口，并添加 `@Component` 注解使其成为 Spring 管理的组件：

```java
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间
        this.strictInsertFill(metaObject, "create_time", LocalDateTime.class, LocalDateTime.now());
        
        // 自动填充更新时间
        this.strictInsertFill(metaObject, "update_time", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动更新更新时间
        this.strictUpdateFill(metaObject, "update_time", LocalDateTime.class, LocalDateTime.now());
    }
}
```

### 3. FieldFill 枚举说明

FieldFill 枚举定义了四种填充策略：

- `DEFAULT`: 默认不处理
- `INSERT`: 插入时填充字段
- `UPDATE`: 更新时填充字段
- `INSERT_UPDATE`: 插入和更新时都填充字段

## 使用效果

配置完成后，当执行插入操作时：
- `create_time` 字段会自动填充为当前时间
- `update_time` 字段会自动填充为当前时间

当执行更新操作时：
- `update_time` 字段会自动更新为当前时间

## 注意事项

1. 字段必须声明 `@TableField` 注解，并设置 `fill` 属性来选择填充策略
2. 自动填充处理器需要在 Spring Boot 中声明为 `@Component` 或通过 `@Bean` 注解注册
3. 使用 `strictInsertFill` 或 `strictUpdateFill` 方法可以根据注解 `FieldFill.xxx`、字段名和字段类型来区分填充逻辑
4. 在 `update(T entity, Wrapper<T> updateWrapper)` 时，entity 不能为空，否则自动填充失效
5. 在 `update(Wrapper<T> updateWrapper)` 时不会自动填充，需要手动赋值字段条件