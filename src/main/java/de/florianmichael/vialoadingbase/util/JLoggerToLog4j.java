package de.florianmichael.vialoadingbase.util;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class JLoggerToLog4j extends Logger {
    private final org.apache.logging.log4j.Logger base;

    public JLoggerToLog4j(org.apache.logging.log4j.Logger base) {
        super("logger", null);
        this.base = base;
    }

    @Override
    public void log(LogRecord logRecord) {
        this.log(logRecord.getLevel(), logRecord.getMessage());
    }

    @Override
    public void log(Level level, String var2) {
        if (level == Level.FINE) {
            this.base.debug(var2);
        } else if (level == Level.WARNING) {
            this.base.warn(var2);
        } else if (level == Level.SEVERE) {
            this.base.error(var2);
        } else if (level == Level.INFO) {
            this.base.info(var2);
        } else {
            this.base.trace(var2);
        }
    }

    @Override
    public void log(Level level, String var2, Object var3) {
        if (level == Level.FINE) {
            this.base.debug(var2, var3);
        } else if (level == Level.WARNING) {
            this.base.warn(var2, var3);
        } else if (level == Level.SEVERE) {
            this.base.error(var2, var3);
        } else if (level == Level.INFO) {
            this.base.info(var2, var3);
        } else {
            this.base.trace(var2, var3);
        }
    }

    @Override
    public void log(Level level, String var2, Object[] var3) {
        this.log(level, MessageFormat.format(var2, var3));
    }

    @Override
    public void log(Level level, String var2, Throwable t) {
        if (level == Level.FINE) {
            this.base.debug(var2, t);
        } else if (level == Level.WARNING) {
            this.base.warn(var2, t);
        } else if (level == Level.SEVERE) {
            this.base.error(var2, t);
        } else if (level == Level.INFO) {
            this.base.info(var2, t);
        } else {
            this.base.trace(var2, t);
        }
    }
}
