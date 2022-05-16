package com.richard.demo.services.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.richard.demo.services.impl.OrderInfoDaoAImpl;
import com.richard.demo.utils.JacksonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/5/26 11:01 AM richard.xu Exp $
 */
@Aspect
@Component
@Slf4j
public class AspectDemo {
    @Autowired
    private OrderInfoDaoAImpl daoA;

    /**
     * 定义一个切入点方法，会拦截含有此注解的方法
     */
    @Pointcut("@annotation(com.richard.demo.services.aspect.Login)")
    private void logPointCut() {
    }

    /**
     * 前置通知：在目标方法执行前调用
     */
    @Before(value = "logPointCut() && @annotation(login)")
    public void check(Login login) {
        log.info("==@Before==checking.........." + login.username());
        daoA.queryOrderList();
    }

    /**
     * 后置通知：在目标方法执行后调用，若目标方法出现异常，则不执行
     */
    @AfterReturning("logPointCut()")
    public void afterReturning() {
        log.info("==@AfterReturning== after returning without errors");
    }

    /**
     * 后置/最终通知：无论目标方法在执行过程中出现一场都会在它之后调用
     */
    @After(value = "logPointCut()")
    public void bye(){
        log.info("==@After==bye bye............");
    }

    /**
     * 异常通知：目标方法抛出异常时执行
     */
    @AfterThrowing("logPointCut()")
    public void afterThrowing() {
        log.info("==@AfterThrowing== after throwing errors");
    }

    /**
     * 环绕通知：灵活自由的在目标方法中切入代码
     */
    @Around("logPointCut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标方法的名称
        String methodName = joinPoint.getSignature().getName();
        // 获取方法传入参数
        Object[] params = joinPoint.getArgs();
        log.info("input param size is {}, params are {}", params.length, JacksonUtil.writeStr(params));
        Login login = getDeclaredAnnotation(joinPoint);
        log.info("==@Around==  ");
        // 执行源方法
        joinPoint.proceed();
        // 模拟进行验证
        if (params != null && params.length > 0 && params[0].equals("Blog Home")) {
            log.info("==@Around== logger auth success, password is " + login.password());
        } else {
            log.info("==@Around== logger auth failed ");
        }

    }

    /**
     * 获取方法中声明的注解
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    public Login getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
        Login annotation = objMethod.getDeclaredAnnotation(Login.class);
        // 返回
        return annotation;
    }

}
