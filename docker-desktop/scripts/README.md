# 数据和日志清理脚本

该目录包含用于清理服务数据和日志的脚本。

## 脚本说明

### 1. cleanup-all.sh
- 适用于Linux/Unix/Mac系统的shell脚本
- 清理所有服务的数据和日志目录
- 使用方法:
  ```bash
  ./cleanup-all.sh
  ```

### 2. cleanup-all.bat
- 适用于Windows系统的批处理脚本
- 清理所有服务的数据和日志目录
- 使用方法:
  ```cmd
  cleanup-all.bat
  ```

### 3. schedule-cleanup.sh
- 用于设置定时清理任务的脚本（仅适用于Linux/Unix/Mac）
- 使用方法:
  ```bash
  # 添加定时任务（每天凌晨2点执行清理）
  ./schedule-cleanup.sh add
  
  # 查看当前定时任务
  ./schedule-cleanup.sh list
  
  # 移除定时任务
  ./schedule-cleanup.sh remove
  ```

## 清理内容

脚本将清理以下服务的数据和日志：

1. MySQL Master & Slave
2. Redis Master & Slave
3. Redis Sentinel (1, 2, 3)
4. RocketMQ
5. Nginx

## 注意事项

1. 执行清理脚本将永久删除对应目录下的所有数据，请谨慎操作
2. 在生产环境中使用前，请确保已备份重要数据
3. 定时清理脚本需要cron服务支持（Linux/Unix/Mac）
4. 执行脚本前请确保具有相应目录的读写权限