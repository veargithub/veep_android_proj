package com.example.veep.vcomm.log;

public class LogToFile extends Vlog.EasyLog  {
	 
	private org.apache.log4j.Logger log4j = null;
	
	protected LogToFile(){}
	
	public LogToFile(org.apache.log4j.Logger log4j) {
		this.log4j = log4j;
	}

    public void trace(Object message) {
        log4j.trace(message);
    }

    public void trace(Object message, Throwable t) {
        log4j.trace(message,t);
    }

    public void debug(Object message) {
        log4j.debug(message);
    }

    public void debug(Object message, Throwable t) {
        log4j.debug(message,t);
    }

    public void info(Object message) {
        log4j.info(message);
    }

    public void info(Object message, Throwable t) {
        log4j.info(message,t);
    }

    public void warn(Object message) {
        log4j.warn(message);
    }

    public void warn(Object message, Throwable t) {
        log4j.warn(message,t);
    }

    public void warn(Throwable t) {
        log4j.warn(t,t);
    }

    public void error(Object message) {
        log4j.error(message);
    }

    public void error(Object message, Throwable t) {
        log4j.error(message,t);
    }

    public void error(Throwable t) {
        log4j.error(t,t);
    }

    public void fatal(Object message) {
        log4j.fatal(message);
    }

    public void fatal(Object message, Throwable t) {
        log4j.fatal(message, t);
    }

    public void fatal(Throwable t) {
        log4j.fatal(t,t);
    }
	
	
}
