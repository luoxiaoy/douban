package com.ly.douban.support.logger;

public interface FormatStrategy {

  void log(int priority, String tag, String message);
}
