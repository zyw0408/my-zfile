#!/bin/bash

# Scheduled Cleanup Script
# This script sets up a cron job to periodically clean up service data and logs

# Function to add cron job
add_cron_job() {
    # Create a temporary file for crontab
    temp_cron=$(mktemp)
    
    # Backup current crontab
    crontab -l > "$temp_cron" 2>/dev/null
    
    # Add our cleanup job to run daily at 2 AM
    echo "0 2 * * * cd $(pwd) && ./cleanup-all.sh >> cleanup.log 2>&1" >> "$temp_cron"
    
    # Install the new crontab
    crontab "$temp_cron"
    
    # Remove temporary file
    rm "$temp_cron"
    
    echo "Cron job added successfully. Cleanup will run daily at 2 AM."
}

# Function to remove cron job
remove_cron_job() {
    # Create a temporary file for crontab
    temp_cron=$(mktemp)
    
    # Backup current crontab
    crontab -l > "$temp_cron" 2>/dev/null
    
    # Remove our cleanup job
    grep -v "cleanup-all.sh" "$temp_cron" | crontab -
    
    # Remove temporary file
    rm "$temp_cron"
    
    echo "Cron job removed successfully."
}

# Function to list cron jobs
list_cron_jobs() {
    echo "Current cron jobs:"
    crontab -l 2>/dev/null || echo "No cron jobs found."
}

# Main script logic
case "$1" in
    "add")
        add_cron_job
        ;;
    "remove")
        remove_cron_job
        ;;
    "list")
        list_cron_jobs
        ;;
    *)
        echo "Usage: $0 [add|remove|list]"
        echo "  add    - Add scheduled cleanup job (daily at 2 AM)"
        echo "  remove - Remove scheduled cleanup job"
        echo "  list   - List current cron jobs"
        ;;
esac