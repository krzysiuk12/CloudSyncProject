package pl.edu.agh.iosr.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by ps_krzysztof on 2015-06-09.
 */
@Aspect
@Component
public class LoggerAspect {

    private Logger logger;

    public LoggerAspect() {
        this.logger = Logger.getLogger(LoggerAspect.class);
    }

    @Before("pl.edu.agh.iosr.aspects.pointcuts.PointcutDefinitions.allServicesMethods() || pl.edu.agh.iosr.aspects.pointcuts.PointcutDefinitions.allControllersMethods()")
    public void beforeLogger(JoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();
        builder.append("Before Execution: ");
        builder.append(joinPoint.getTarget().getClass().getSimpleName()).append(" - ");
        builder.append(joinPoint.getSignature().getName()).append(" - ");
        builder.append(Arrays.toString(joinPoint.getArgs()));
        logger.info(builder.toString());
    }

    @After("pl.edu.agh.iosr.aspects.pointcuts.PointcutDefinitions.allServicesMethods() || pl.edu.agh.iosr.aspects.pointcuts.PointcutDefinitions.allControllersMethods()")
    public void afterReturningLogger(JoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();
        builder.append("After execution: ");
        builder.append(joinPoint.getTarget().getClass().getSimpleName()).append(" - ");
        builder.append(joinPoint.getSignature().getName());
        logger.info(builder.toString());
    }

}