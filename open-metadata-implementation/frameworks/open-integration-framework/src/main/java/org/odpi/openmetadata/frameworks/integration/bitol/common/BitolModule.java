/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * BitolModule is a Jackson module that adds the lenient handling needed to read the range of documents found in
 * the wild: for example ODCS v3.0 documents that supply the team as a bare list of members rather than the
 * team object introduced in v3.1.  Register it with an ObjectMapper that reads Bitol documents:
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper(new YAMLFactory()).registerModule(new BitolModule());
 * </pre>
 * BitolDocumentFormatter registers it automatically.  The beans themselves only carry annotations from the
 * jackson-annotations package, so they can be used with any ObjectMapper; without this module the older team
 * form is rejected.
 */
public final class BitolModule extends SimpleModule
{
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor registers the deserializers.
     */
    public BitolModule()
    {
        super("BitolModule");

        addDeserializer(BitolTeam.class, new BitolTeamDeserializer());
    }
}
