package com.zfile.common.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;

/**
 * 事务切面配置类
 */
@Aspect
@Configuration
public class TransactionAspect {

    /**
     * 配置事务切面拦截范围
     * 拦截service包下所有类的所有方法，包括子包中的类
     */
    private static final String POINTCUT_EXPRESSION = "execution(* com.zfile.module..service.*.*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor() {
        // 配置事务规则
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        
        // 配置事务传播特性
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        
        // 对不同的方法设置不同的事务规则
        source.addTransactionalMethod("add*", requiredTx);
        source.addTransactionalMethod("save*", requiredTx);
        source.addTransactionalMethod("insert*", requiredTx);
        source.addTransactionalMethod("create*", requiredTx);
        source.addTransactionalMethod("update*", requiredTx);
        source.addTransactionalMethod("modify*", requiredTx);
        source.addTransactionalMethod("delete*", requiredTx);
        source.addTransactionalMethod("remove*", requiredTx);
        source.addTransactionalMethod("batch*", requiredTx);
        source.addTransactionalMethod("page*", requiredTx);
        source.addTransactionalMethod("list*", requiredTx);
        source.addTransactionalMethod("*", requiredTx); // 默认所有方法都开启事务

        // 创建切面
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(POINTCUT_EXPRESSION);

        // 配置事务拦截器
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        
        // 创建通知器
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }
}