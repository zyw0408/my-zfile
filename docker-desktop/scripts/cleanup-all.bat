@echo off
REM All Services Data and Log Cleanup Script for Windows
REM This script cleans up data and logs for all services on Windows

echo Starting cleanup of all services data and logs...

REM MySQL Master
if exist "..\mysql-master\data" (
    echo Cleaning MySQL Master data...
    del /q "..\mysql-master\data\*" >nul 2>&1
    for /d %%i in ("..\mysql-master\data\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo MySQL Master data directory does not exist
)

if exist "..\mysql-master\logs" (
    echo Cleaning MySQL Master logs...
    del /q "..\mysql-master\logs\*" >nul 2>&1
    for /d %%i in ("..\mysql-master\logs\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo MySQL Master logs directory does not exist
)

REM MySQL Slave
if exist "..\mysql-slave\data" (
    echo Cleaning MySQL Slave data...
    del /q "..\mysql-slave\data\*" >nul 2>&1
    for /d %%i in ("..\mysql-slave\data\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo MySQL Slave data directory does not exist
)

if exist "..\mysql-slave\logs" (
    echo Cleaning MySQL Slave logs...
    del /q "..\mysql-slave\logs\*" >nul 2>&1
    for /d %%i in ("..\mysql-slave\logs\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo MySQL Slave logs directory does not exist
)

REM Redis Master
if exist "..\redis-master\data" (
    echo Cleaning Redis Master data...
    del /q "..\redis-master\data\*" >nul 2>&1
    for /d %%i in ("..\redis-master\data\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Master data directory does not exist
)

if exist "..\redis-master\logs" (
    echo Cleaning Redis Master logs...
    del /q "..\redis-master\logs\*" >nul 2>&1
    for /d %%i in ("..\redis-master\logs\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Master logs directory does not exist
)

REM Redis Slave
if exist "..\redis-slave\data" (
    echo Cleaning Redis Slave data...
    del /q "..\redis-slave\data\*" >nul 2>&1
    for /d %%i in ("..\redis-slave\data\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Slave data directory does not exist
)

if exist "..\redis-slave\logs" (
    echo Cleaning Redis Slave logs...
    del /q "..\redis-slave\logs\*" >nul 2>&1
    for /d %%i in ("..\redis-slave\logs\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Slave logs directory does not exist
)

REM Redis Sentinel 1
if exist "..\redis-sentinel1\data" (
    echo Cleaning Redis Sentinel 1 data...
    del /q "..\redis-sentinel1\data\*" >nul 2>&1
    for /d %%i in ("..\redis-sentinel1\data\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Sentinel 1 data directory does not exist
)

if exist "..\redis-sentinel1\logs" (
    echo Cleaning Redis Sentinel 1 logs...
    del /q "..\redis-sentinel1\logs\*" >nul 2>&1
    for /d %%i in ("..\redis-sentinel1\logs\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Sentinel 1 logs directory does not exist
)

REM Redis Sentinel 2
if exist "..\redis-sentinel2\data" (
    echo Cleaning Redis Sentinel 2 data...
    del /q "..\redis-sentinel2\data\*" >nul 2>&1
    for /d %%i in ("..\redis-sentinel2\data\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Sentinel 2 data directory does not exist
)

if exist "..\redis-sentinel2\logs" (
    echo Cleaning Redis Sentinel 2 logs...
    del /q "..\redis-sentinel2\logs\*" >nul 2>&1
    for /d %%i in ("..\redis-sentinel2\logs\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Sentinel 2 logs directory does not exist
)

REM Redis Sentinel 3
if exist "..\redis-sentinel3\data" (
    echo Cleaning Redis Sentinel 3 data...
    del /q "..\redis-sentinel3\data\*" >nul 2>&1
    for /d %%i in ("..\redis-sentinel3\data\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Sentinel 3 data directory does not exist
)

if exist "..\redis-sentinel3\logs" (
    echo Cleaning Redis Sentinel 3 logs...
    del /q "..\redis-sentinel3\logs\*" >nul 2>&1
    for /d %%i in ("..\redis-sentinel3\logs\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Redis Sentinel 3 logs directory does not exist
)

REM RocketMQ
if exist "..\rocketmq\store" (
    echo Cleaning RocketMQ store...
    del /q "..\rocketmq\store\*" >nul 2>&1
    for /d %%i in ("..\rocketmq\store\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo RocketMQ store directory does not exist
)

if exist "..\rocketmq\logs" (
    echo Cleaning RocketMQ logs...
    del /q "..\rocketmq\logs\*" >nul 2>&1
    for /d %%i in ("..\rocketmq\logs\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo RocketMQ logs directory does not exist
)

REM Nginx
if exist "..\nginx\logs" (
    echo Cleaning Nginx logs...
    del /q "..\nginx\logs\*" >nul 2>&1
    for /d %%i in ("..\nginx\logs\*") do rmdir /s /q "%%i" >nul 2>&1
) else (
    echo Nginx logs directory does not exist
)

echo All services data and log cleanup completed.
pause