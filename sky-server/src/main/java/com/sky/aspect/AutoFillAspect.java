package com.sky.aspect;


import com.sky.anatation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 定义一个切点，切点表达式为 `execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.anatation.AutoFill)`
     * 匹配带有 @AutoFill 注解的 com.sky.mapper 包下的所有类的所有方法
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.anatation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * 执行方法之前，填充公共字段
     * @param joinPoint 切点
     */
    @Before(value = "autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("公共字段填充。。。");
        // 获取拦截方法上的数据库操作类型
        MethodSignature methodSignature =  (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        // 被拦截方法的实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];

        // 公共属性获取
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 公共属性赋值
        if (OperationType.INSERT.equals(operationType)) {
            // 新增
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime =  entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser =  entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser =  entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);

            } catch (Exception e) {
                log.error("新增-公共属性赋值失败", e);
            }
        } else if (OperationType.UPDATE.equals(operationType)) {
            // 修改
            try {
                Method setUpdateTime =  entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser =  entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("修改-公共属性赋值失败", e);
            }
        }

    }
}
