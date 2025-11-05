#!/bin/bash

# All Services Data and Log Cleanup Script
# This script cleans up data and logs for all services

echo "Starting cleanup of all services data and logs..."

# MySQL Master
if [ -d "../mysql-master/data" ]; then
    echo "Cleaning MySQL Master data..."
    rm -rf ../mysql-master/data/*
else
    echo "MySQL Master data directory does not exist"
fi

if [ -d "../mysql-master/logs" ]; then
    echo "Cleaning MySQL Master logs..."
    rm -rf ../mysql-master/logs/*
else
    echo "MySQL Master logs directory does not exist"
fi

# MySQL Slave
if [ -d "../mysql-slave/data" ]; then
    echo "Cleaning MySQL Slave data..."
    rm -rf ../mysql-slave/data/*
else
    echo "MySQL Slave data directory does not exist"
fi

if [ -d "../mysql-slave/logs" ]; then
    echo "Cleaning MySQL Slave logs..."
    rm -rf ../mysql-slave/logs/*
else
    echo "MySQL Slave logs directory does not exist"
fi

# Redis Master
if [ -d "../redis-master/data" ]; then
    echo "Cleaning Redis Master data..."
    rm -rf ../redis-master/data/*
else
    echo "Redis Master data directory does not exist"
fi

if [ -d "../redis-master/logs" ]; then
    echo "Cleaning Redis Master logs..."
    rm -rf ../redis-master/logs/*
else
    echo "Redis Master logs directory does not exist"
fi

# Redis Slave
if [ -d "../redis-slave/data" ]; then
    echo "Cleaning Redis Slave data..."
    rm -rf ../redis-slave/data/*
else
    echo "Redis Slave data directory does not exist"
fi

if [ -d "../redis-slave/logs" ]; then
    echo "Cleaning Redis Slave logs..."
    rm -rf ../redis-slave/logs/*
else
    echo "Redis Slave logs directory does not exist"
fi

# Redis Sentinel 1
if [ -d "../redis-sentinel1/data" ]; then
    echo "Cleaning Redis Sentinel 1 data..."
    rm -rf ../redis-sentinel1/data/*
else
    echo "Redis Sentinel 1 data directory does not exist"
fi

if [ -d "../redis-sentinel1/logs" ]; then
    echo "Cleaning Redis Sentinel 1 logs..."
    rm -rf ../redis-sentinel1/logs/*
else
    echo "Redis Sentinel 1 logs directory does not exist"
fi

# Redis Sentinel 2
if [ -d "../redis-sentinel2/data" ]; then
    echo "Cleaning Redis Sentinel 2 data..."
    rm -rf ../redis-sentinel2/data/*
else
    echo "Redis Sentinel 2 data directory does not exist"
fi

if [ -d "../redis-sentinel2/logs" ]; then
    echo "Cleaning Redis Sentinel 2 logs..."
    rm -rf ../redis-sentinel2/logs/*
else
    echo "Redis Sentinel 2 logs directory does not exist"
fi

# Redis Sentinel 3
if [ -d "../redis-sentinel3/data" ]; then
    echo "Cleaning Redis Sentinel 3 data..."
    rm -rf ../redis-sentinel3/data/*
else
    echo "Redis Sentinel 3 data directory does not exist"
fi

if [ -d "../redis-sentinel3/logs" ]; then
    echo "Cleaning Redis Sentinel 3 logs..."
    rm -rf ../redis-sentinel3/logs/*
else
    echo "Redis Sentinel 3 logs directory does not exist"
fi

# RocketMQ
if [ -d "../rocketmq/store" ]; then
    echo "Cleaning RocketMQ store..."
    rm -rf ../rocketmq/store/*
else
    echo "RocketMQ store directory does not exist"
fi

if [ -d "../rocketmq/logs" ]; then
    echo "Cleaning RocketMQ logs..."
    rm -rf ../rocketmq/logs/*
else
    echo "RocketMQ logs directory does not exist"
fi

# Nginx
if [ -d "../nginx/logs" ]; then
    echo "Cleaning Nginx logs..."
    rm -rf ../nginx/logs/*
else
    echo "Nginx logs directory does not exist"
fi

echo "All services data and log cleanup completed."