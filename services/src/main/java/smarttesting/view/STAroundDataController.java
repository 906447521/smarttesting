package smarttesting.view;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import smarttesting.service.model.DataResult;

/**
 * @author
 */
@Aspect
@Component
public class STAroundDataController {

    private static final Logger log = LoggerFactory.getLogger(STAroundDataController.class);


    @Pointcut("execution(* smarttesting.action.data.*.*(..))")
    public void proxyAspect() {

    }

    @Around("proxyAspect()")
    public Object doInvoke(ProceedingJoinPoint joinPoint) {
        Object result = null;
        String jobName = joinPoint.getSignature().toLongString();
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("==> {} fail, {}.", new Object[]{jobName, e.getMessage()}, e);
            result = DataResult.errorResult(e.getMessage());
        }
        return result;
    }
}

