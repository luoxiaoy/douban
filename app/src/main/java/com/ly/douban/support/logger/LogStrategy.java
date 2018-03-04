package com.ly.douban.support.logger;

public interface LogStrategy {

  void log(int priority, String tag, String message);
}
