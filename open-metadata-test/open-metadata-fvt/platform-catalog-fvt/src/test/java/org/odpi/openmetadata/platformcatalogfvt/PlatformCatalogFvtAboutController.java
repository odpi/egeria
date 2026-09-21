/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.odpi.openmetadata.platformservices.properties.PublicProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Serves the two endpoints that describe the platform itself: {@code /api/about} and
 * {@code /api/public/app/info}.
 * <br>
 * On a real platform both belong to the {@code user-authn} module - {@code AboutController} and
 * {@code PublicController} - and this class does exactly what those do: return the Spring Boot
 * {@code BuildProperties} bean, and return the platform's name, description and organization from
 * application.properties.  They are here because {@code user-authn} is excluded from this suite's
 * classpath: it wires in a login and token filter chain that puts {@code anyRequest().authenticated()} in
 * front of the whole platform, which a hermetic suite with no user directory cannot satisfy and which would
 * make every test about authentication rather than about cataloguing.
 * <br>
 * <b>Why the suite has to supply them at all.</b>  Both are called by {@code getPlatformReport()}, which is
 * the first thing the cataloguer's {@code refresh()} does, and neither call is guarded.  A 404 from either
 * one propagates out of the report, {@code refresh()}'s catch-all swallows it, and nothing whatsoever is
 * catalogued - not the platform, not its servers, not its users.  The only trace is a single
 * {@code OMAG-CONNECTORS-0001} audit record.
 * <br>
 * That is worth stating plainly, because it is a property of the connector rather than of this suite: the
 * OMAG Server Platform Cataloguer cannot catalog a platform that is not running {@code user-authn}, or that
 * is running it without Spring Boot build information behind it.  See the README.
 * <br>
 * Nothing here is invented.  The build properties come from
 * {@code src/test/resources/META-INF/build-info.properties} by way of Spring Boot's own bean, and the
 * public properties are read from the same application.properties settings the real controller reads.
 */
@RestController
public class PlatformCatalogFvtAboutController
{
    @Autowired(required = false)
    BuildProperties buildProperties;

    @Value("${platform.name: }")
    String platformName;

    @Value("${platform.description: }")
    String platformDescription;

    @Value("${platform.organization.name: }")
    String platformOrganizationName;


    /**
     * Return the build properties of this platform.
     *
     * @return build properties
     */
    @GetMapping("/api/about")
    public BuildProperties getBuildProperties()
    {
        return buildProperties;
    }


    /**
     * Return the platform's own name, description and organization - the values the cataloguer turns into
     * the platform element's display name, description, qualified name and resource name.
     *
     * @return public properties
     */
    @GetMapping("/api/public/app/info")
    public PublicProperties getApplicationInformation()
    {
        PublicProperties publicProperties = new PublicProperties();

        publicProperties.setDisplayName(platformName);
        publicProperties.setDescription(platformDescription);
        publicProperties.setOrganizationName(platformOrganizationName);

        return publicProperties;
    }
}
