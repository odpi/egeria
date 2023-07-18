/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.server.config;

import org.odpi.openmetadata.http.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * This class provides configuration bean for customizing the SSL environment used by java.net.ssl and Tomcat server ssl.
 */
@Configuration
public class InitializingBeanConfig {
    public static final String PREFIX = "SSL configuration started";

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String OPTION_STRICT_SSL_IS_SET_TO_FALSE_INVALID_CERTIFICATES_WILL_BE_ACCEPTED_FOR_CONNECTION =
            "Option strict.ssl is set to false! Invalid certificates will be accepted for connection!";
    public static final String JAVAX_NET_SSL_TRUST_STORE = "javax.net.ssl.trustStore";
    public static final String SERVER_SSL_TRUST_STORE = "server.ssl.trust-store";
    public static final String SERVER_SSL_TRUST_STORE_PASSWORD = "server.ssl.trust-store-password";
    public static final String JAVAX_NET_SSL_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";

    private final Environment env;

    @Value("${strict.ssl:true}")
    Boolean strictSSL;

    @Value("${server.ssl.enabled:true}")
    Boolean serverSSL;

    public InitializingBeanConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public InitializingBean getInitialize() {
        return () ->
        {
            log.info("{} working directory: {}", PREFIX, System.getProperty("user.dir"));

            if (!strictSSL) {
                log.warn("{} :: {}", PREFIX , OPTION_STRICT_SSL_IS_SET_TO_FALSE_INVALID_CERTIFICATES_WILL_BE_ACCEPTED_FOR_CONNECTION);
                HttpHelper.noStrictSSL();
            }

            if (serverSSL && System.getProperty(JAVAX_NET_SSL_TRUST_STORE) == null) {
                log.warn("{} Java trust store '{}' is null - this is needed by Tomcat - using '{}'", PREFIX, JAVAX_NET_SSL_TRUST_STORE, SERVER_SSL_TRUST_STORE);

                /*
                 * load the 'javax.net.ssl.trustStore' and 'javax.net.ssl.trustStorePassword' from application.properties.
                 * Note, these variables should only used for mutual SSL.  This function is provided for backward compatibility.
                 * Also note that there is an NPE if the java variables are set to null.
                 */
                if (env.getProperty(SERVER_SSL_TRUST_STORE) != null) {
                    System.setProperty(JAVAX_NET_SSL_TRUST_STORE, env.getProperty(SERVER_SSL_TRUST_STORE));
                }
                if (env.getProperty(SERVER_SSL_TRUST_STORE_PASSWORD) != null) {
                    System.setProperty(JAVAX_NET_SSL_TRUST_STORE_PASSWORD, env.getProperty(SERVER_SSL_TRUST_STORE_PASSWORD));
                }
            }
        };

    }
}
