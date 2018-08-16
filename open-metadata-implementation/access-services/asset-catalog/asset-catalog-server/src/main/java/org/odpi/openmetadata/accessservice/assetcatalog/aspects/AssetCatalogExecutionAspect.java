/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.odpi.openmetadata.accessservice.assetcatalog.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AssetCatalogExecutionAspect {

    private static final Logger logger = LoggerFactory.getLogger(AssetCatalogExecutionAspect.class);

    @Around("execution(* (@LogExecution *).*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        logger.info(Constants.ASSET_CATALOG_OMAS + " The method " + joinPoint.getSignature().getName() + " executed in " + executionTime + " ms");
        return proceed;
    }
}
