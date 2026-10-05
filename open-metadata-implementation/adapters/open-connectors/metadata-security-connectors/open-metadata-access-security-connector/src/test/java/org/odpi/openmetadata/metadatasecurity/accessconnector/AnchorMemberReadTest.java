/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.accessconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.users.AccessOperation;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

/**
 * An element with no zones of its own is decided on its anchor's zones.  When the user may not read the anchor,
 * an explicit read is refused naming the anchor, but a search result is filtered out - the same as for an element
 * refused on its own zones.  The anchor branch used to refuse search results as well, so every member of a
 * secured anchor a search came across was recorded as an unauthorized access attempt.
 */
public class AnchorMemberReadTest
{
    private static final String UNAUTHORIZED_ANCHOR_ACCESS = "OPEN-METADATA-SECURITY-403-007";
    private static final String FILTERED_ELEMENT           = "OPEN-METADATA-SECURITY-500-002";


    /**
     * A connector whose zones never grant access, standing in for an anchor in a zone the user cannot read.
     *
     * @return connector
     */
    private OpenMetadataAccessSecurityConnector getRefusingConnector()
    {
        OpenMetadataAccessSecurityConnector connector = new OpenMetadataAccessSecurityConnector()
        {
            @Override
            protected boolean validateZoneAccess(String               userId,
                                                 List<Classification> classifications,
                                                 AccessOperation      operation,
                                                 List<String>         maintainers,
                                                 OMRSRepositoryHelper repositoryHelper,
                                                 String               serviceName,
                                                 String               methodName)
            {
                return false;
            }
        };

        connector.setAuditLog(mock(AuditLog.class));

        return connector;
    }


    private EntityDetail getEntity(String guid, String typeName)
    {
        EntityDetail entity = new EntityDetail();
        InstanceType type   = new InstanceType();

        type.setTypeDefName(typeName);
        entity.setGUID(guid);
        entity.setType(type);

        return entity;
    }


    @Test
    public void testExplicitReadIsRefusedNamingTheAnchor()
    {
        EntityDetail anchor = this.getEntity("anchor-guid", "DigitalProductFamily");
        EntityDetail member = this.getEntity("member-guid", "Collection");

        UserNotAuthorizedException error = expectThrows(UserNotAuthorizedException.class,
                                                        () -> this.getRefusingConnector().validateUserForAnchorMemberRead("generalnpa",
                                                                                                                         anchor,
                                                                                                                         member,
                                                                                                                         true,
                                                                                                                         mock(OMRSRepositoryHelper.class),
                                                                                                                         "testService",
                                                                                                                         "getMetadataElementByGUID"));

        assertTrue(error.getMessage().contains(UNAUTHORIZED_ANCHOR_ACCESS), error.getMessage());
        assertTrue(error.getMessage().contains("on DigitalProductFamily anchor element anchor-guid"), error.getMessage());
    }


    @Test
    public void testSearchResultIsFiltered()
    {
        EntityDetail anchor = this.getEntity("anchor-guid", "DigitalProductFamily");
        EntityDetail member = this.getEntity("member-guid", "Collection");

        UserNotAuthorizedException error = expectThrows(UserNotAuthorizedException.class,
                                                        () -> this.getRefusingConnector().validateUserForAnchorMemberRead("generalnpa",
                                                                                                                         anchor,
                                                                                                                         member,
                                                                                                                         false,
                                                                                                                         mock(OMRSRepositoryHelper.class),
                                                                                                                         "testService",
                                                                                                                         "findMetadataElements"));

        assertTrue(error.getMessage().contains(FILTERED_ELEMENT), error.getMessage());
        assertTrue(error.getMessage().contains("member-guid"), error.getMessage());
    }
}
