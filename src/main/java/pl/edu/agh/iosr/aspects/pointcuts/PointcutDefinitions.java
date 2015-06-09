package pl.edu.agh.iosr.aspects.pointcuts;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by ps_krzysztof on 2015-06-09.
 */
@Aspect
public class PointcutDefinitions {

    @Pointcut("within(pl.edu.agh.iosr.services.implementation.*))")
    public void allServicesMethods() {}

    @Pointcut("within(pl.edu.agh.iosr.controllers.*))")
    public void allControllersMethods() {}

}