package com.campus.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Logger class for system-wide logging
 */
public class Logger {
    private static Logger instance;
    private List<String> logs;
    private DateTimeFormatter formatter;
    private LogLevel currentLogLevel;
    
    private Logger() {
        this.logs = new ArrayList<>();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.currentLogLevel = LogLevel.INFO;
    }
    
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
    
    public void log(String message) {
        log(LogLevel.INFO, message);
    }
    
    public void log(LogLevel level, String message) {
        if (level.ordinal() >= currentLogLevel.ordinal()) {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = String.format("[%s] %s: %s", timestamp, level, message);
            logs.add(logEntry);
            System.out.println(logEntry); // Also print to console
        }
    }
    
    public void debug(String message) { log(LogLevel.DEBUG, message); }
    public void info(String message) { log(LogLevel.INFO, message); }
    public void warn(String message) { log(LogLevel.WARN, message); }
    public void error(String message) { log(LogLevel.ERROR, message); }
    
    public List<String> getLogs() { return new ArrayList<>(logs); }
    public void clearLogs() { logs.clear(); }
    public void setLogLevel(LogLevel level) { this.currentLogLevel = level; }
    
    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }
}