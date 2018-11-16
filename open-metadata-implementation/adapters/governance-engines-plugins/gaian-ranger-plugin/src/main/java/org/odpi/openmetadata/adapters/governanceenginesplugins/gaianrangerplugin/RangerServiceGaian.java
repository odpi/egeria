/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;
import org.apache.ranger.plugin.service.RangerBaseService;
import org.apache.ranger.plugin.service.ResourceLookupContext;

import java.util.HashMap;
import java.util.List;

public class RangerServiceGaian extends RangerBaseService {
    public HashMap<String, Object> validateConfig() throws Exception {
        // TODO: connect to Gaian resource manager; throw Exception on failure
        return null;
    }
    public List<String> lookupResource(ResourceLookupContext context) throws Exception {
        // TODO: retrieve the resource list from Gaian resource manager using REST API
        return null;
    }
}
