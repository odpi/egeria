/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * DeleteOptions ignores unknown properties, so a caller that misspells the cascade flag gets a delete that does
 * not cascade and no error.  "cascadeDelete" is the spelling pyegeria used, and it is accepted as an alias.
 */
public class DeleteOptionsAliasTest
{
    @Test
    public void cascadeDeleteIsAcceptedForCascadedDelete() throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();

        assertTrue(objectMapper.readValue("{\"cascadedDelete\":true}", DeleteOptions.class).getCascadedDelete());
        assertTrue(objectMapper.readValue("{\"cascadeDelete\":true}", DeleteOptions.class).getCascadedDelete());
    }
}
