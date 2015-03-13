package com.dnastack.bob.log;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.jboss.logging.Logger;

/**
 * Producer of injectable loggers.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class LoggerProducer {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
