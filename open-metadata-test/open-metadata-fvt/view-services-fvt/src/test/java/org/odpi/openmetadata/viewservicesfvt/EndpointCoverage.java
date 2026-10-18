/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EndpointCoverage records which view service requests the suite has called, and reports the ones it has not.
 * <br>
 * It is registered on the platform itself (the platform scans org.odpi.openmetadata.* for configuration, and
 * this suite's test classes are on its classpath), so it sees each request as Spring routed it: the request
 * mapping's pattern, not the URL the test built.  At the end of the run the patterns recorded are compared with
 * every request mapping the view service controllers declare, and the ones never called are written to
 * {@code build/view-services-fvt-coverage.md}, grouped by controller.
 * <br>
 * With {@code -PviewServicesFvtStrictCoverage} an uncalled request fails the run; without it the gaps are only
 * reported.
 */
@Configuration
public class EndpointCoverage implements WebMvcConfigurer
{
    private static final String VIEW_SERVICE_PACKAGE = "org.odpi.openmetadata.viewservices";
    private static final String STRICT_PROPERTY      = "view.services.fvt.strict.coverage";

    private static final Set<String> calledRequests = ConcurrentHashMap.newKeySet();


    /**
     * Register the recording interceptor.
     *
     * @param registry interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new HandlerInterceptor()
        {
            @Override
            public void afterCompletion(HttpServletRequest  request,
                                        HttpServletResponse response,
                                        Object              handler,
                                        Exception           exception)
            {
                Object pattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

                if ((pattern != null) && (handler instanceof HandlerMethod handlerMethod) &&
                        handlerMethod.getBeanType().getPackageName().startsWith(VIEW_SERVICE_PACKAGE))
                {
                    calledRequests.add(request.getMethod() + " " + pattern);
                }
            }
        });
    }


    /**
     * Compare what was called with what is declared and write the report.
     *
     * @param platformContext the platform's application context
     * @return a failure message if strict coverage is on and requests were not called, otherwise null
     */
    static String writeReport(ApplicationContext platformContext)
    {
        Map<String, Set<String>> uncalledByController = new TreeMap<>();
        int                      declaredCount        = 0;

        for (RequestMappingHandlerMapping mapping : platformContext.getBeansOfType(RequestMappingHandlerMapping.class).values())
        {
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : mapping.getHandlerMethods().entrySet())
            {
                HandlerMethod handlerMethod = entry.getValue();

                if (handlerMethod.getBeanType().getPackageName().startsWith(VIEW_SERVICE_PACKAGE))
                {
                    for (String pattern : entry.getKey().getPatternValues())
                    {
                        for (var method : entry.getKey().getMethodsCondition().getMethods())
                        {
                            declaredCount++;

                            String request = method.name() + " " + pattern;

                            if (! calledRequests.contains(request))
                            {
                                uncalledByController.computeIfAbsent(handlerMethod.getBeanType().getSimpleName(), key -> new TreeSet<>())
                                                    .add(request + "  (" + handlerMethod.getMethod().getName() + ")");
                            }
                        }
                    }
                }
            }
        }

        int uncalledCount = uncalledByController.values().stream().mapToInt(Set::size).sum();

        StringBuilder report = new StringBuilder();

        report.append("# View services FVT coverage\n\n");
        report.append(declaredCount - uncalledCount).append(" of ").append(declaredCount)
              .append(" view service requests were called.\n");

        for (Map.Entry<String, Set<String>> controller : uncalledByController.entrySet())
        {
            report.append("\n## ").append(controller.getKey()).append(" - ").append(controller.getValue().size()).append(" not called\n\n");

            for (String request : controller.getValue())
            {
                report.append("- `").append(request).append("`\n");
            }
        }

        try
        {
            Path reportFile = Path.of("build", "view-services-fvt-coverage.md");

            Files.createDirectories(reportFile.getParent());
            Files.writeString(reportFile, report.toString());

            System.out.println("view-services-fvt: " + (declaredCount - uncalledCount) + " of " + declaredCount +
                                       " view service requests called - report in " + reportFile.toAbsolutePath());
        }
        catch (IOException error)
        {
            System.out.println("view-services-fvt: could not write the coverage report: " + error.getMessage());
            System.out.println(report);
        }

        if (Boolean.getBoolean(STRICT_PROPERTY) && (uncalledCount > 0))
        {
            return uncalledCount + " view service requests were not called by any test - see build/view-services-fvt-coverage.md";
        }

        return null;
    }
}
