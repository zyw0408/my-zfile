#!/bin/bash

# All Services Data and Log Cleanup Script
# This script cleans up data and logs for all services

echo "Starting cleanup of all services data and logs..."

# MySQL Master
if [ -d "../mysql-master/data" ]; then
    echo "Cleaning MySQL Master data..."
    rm -rf ../mysql-master/data/*
fi

if [ -d "../mysql-master/logs" ]; then
    echo "Cleaning MySQL Master logs..."
    rm -rf ../mysql-master/logs/*
fi

# MySQL Slave
if [ -d "../mysql-slave/data" ]; then
    echo "Cleaning MySQL Slave data..."
    rm -rf ../mysql-slave/data/*
fi

if [ -d "../mysql-slave/logs" ]; then
    echo "Cleaning MySQL Slave logs..."
    rm -rf ../mysql-slave/logs/*
fi

# Redis Master
if [ -d "../redis-master/data" ]; then
    echo "Cleaning Redis Master data..."
    rm -rf ../redis-master/data/*
fi

if [ -d "../redis-master/logs" ]; then
    echo "Cleaning Redis Master logs..."
    rm -rf ../redis-master/logs/*
fi

# Redis Slave
if [ -d "../redis-slave/data" ]; then
    echo "Cleaning Redis Slave data..."
    rm -rf ../redis-slave/data/*
fi

if [ -d "../redis-slave/logs" ]; then
    echo "Cleaning Redis Slave logs..."
    rm -rf ../redis-slave/logs/*
fi

# Redis Sentinel 1
if [ -d "../redis-sentinel1/data" ]; then
    echo "Cleaning Redis Sentinel 1 data..."
    rm -rf ../redis-sentinel1/data/*
fi

if [ -d "../redis-sentinel1/logs" ]; then
    echo "Cleaning Redis Sentinel 1 logs..."
    rm -rf ../redis-sentinel1/logs/*
fi

# Redis Sentinel 2
if [ -d "../redis-sentinel2/data" ]; then
    echo "Cleaning Redis Sentinel 2 data..."
    rm -rf ../redis-sentinel2/data/*
fi

if [ -d "../redis-sentinel2/logs" ]; then
    echo "Cleaning Redis Sentinel 2 logs..."
    rm -rf ../redis-sentinel2/logs/*
fi

# Redis Sentinel 3
if [ -d "../redis-sentinel3/data" ]; then
    echo "Cleaning Redis Sentinel 3 data..."
    rm -rf ../redis-sentinel3/data/*
fi

if [ -d "../redis-sentinel3/logs" ]; then
    echo "Cleaning Redis Sentinel 3 logs..."
    rm -rf ../redis-sentinel3/logs/*
fi

# RocketMQ
if [ -d "../rocketmq/store" ]; then
    echo "Cleaning RocketMQ store..."
    rm -rf ../rocketmq/store/*
fi

if [ -d "../rocketmq/logs" ]; then
    echo "Cleaning RocketMQ logs..."
    rm -rf ../rocketmq/logs/*
fi

# Nginx
if [ -d "../nginx/logs" ]; then
    echo "Cleaning Nginx logs..."
    rm -rf ../nginx/logs/*
fi

echo "All services data and log cleanup completed."