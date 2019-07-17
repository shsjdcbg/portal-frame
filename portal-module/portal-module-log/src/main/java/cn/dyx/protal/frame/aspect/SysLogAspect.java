package cn.dyx.protal.frame.aspect;

import cn.dyx.portal.frame.util.IpUtils;
import cn.dyx.portal.frame.util.WebUtils;
import cn.dyx.protal.frame.annotation.SysLog;
import cn.dyx.protal.frame.dto.SysLogDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Description：日志切面
 *
 * @author dyx
 * @date 2019/7/16 17:03
 */
@Component
@Aspect
public class SysLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
    private long startTime;

    @Pointcut("@annotation(cn.dyx.protal.frame.annotation.SysLog)")
    public void methodAspect() {
    }

    @Before("methodAspect()")
    public void before(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
    }

    @Around("methodAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SysLogDTO sysLogDTO = createSysLogDTO(signature, endTime - startTime);
        sysLogDTO.setExecuteTime(new Timestamp(endTime));
        logger.info(sysLogDTO.toString());
        return result;
    }

    @AfterThrowing(pointcut = "methodAspect()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SysLogDTO sysLogDTO = createSysLogDTO(signature, endTime - startTime);
        sysLogDTO.setExecuteTime(new Timestamp(endTime));
        logger.info(sysLogDTO.toString());
    }

    private SysLogDTO createSysLogDTO(MethodSignature signature, long costTime) {
        SysLog sysLog = signature.getMethod().getDeclaredAnnotation(SysLog.class);
        SysLogDTO sysLogDTO = new SysLogDTO();
        sysLogDTO.setDescription(sysLog.description());
        sysLogDTO.setExecuteClass(signature.getDeclaringTypeName());
        sysLogDTO.setExecuteMethod(signature.getName());
        sysLogDTO.setExecuteCostTime(costTime);
        sysLogDTO.setExecuteIp(IpUtils.getIP(WebUtils.getServletRequest()));
        return sysLogDTO;
    }
}
