/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * The paging contract for a find: an empty list means the page was filtered empty and the caller should keep
 * paging; null means there are no more elements.  findBeans used to turn a filtered page (for example, one holding
 * only an archived Memento twin when forLineage is false) into null, so a caller following the contract stopped
 * before reaching the matching elements on later pages.
 */
public class FindBeansFilteredPageTest
{
    /**
     * Default constructor
     */
    public FindBeansFilteredPageTest()
    {
    }


    /**
     * The repository retrieved elements for the page but filtered them all out: the result is an empty list.
     *
     * @throws Exception problem in the handler
     */
    @Test
    public void testFilteredPageIsEmptyNotNull() throws Exception
    {
        List<Object> results = findBeans(new ArrayList<>());

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }


    /**
     * The repository has no more elements: the result is null.
     *
     * @throws Exception problem in the handler
     */
    @Test
    public void testNoMoreElementsIsNull() throws Exception
    {
        assertNull(findBeans(null));
    }


    private static List<Object> findBeans(List<EntityDetail> retrievedEntities) throws Exception
    {
        RepositoryHandler repositoryHandler = Mockito.mock(RepositoryHandler.class, (InvocationOnMock invocation) ->
        {
            if ("findEntities".equals(invocation.getMethod().getName()))
            {
                return retrievedEntities;
            }

            return List.class.isAssignableFrom(invocation.getMethod().getReturnType()) ? null : Mockito.RETURNS_DEFAULTS.answer(invocation);
        });

        OpenMetadataAPIGenericHandler<Object> handler = new OpenMetadataAPIGenericHandler<>(null,
                                                                                            Object.class,
                                                                                            "testService",
                                                                                            "testServer",
                                                                                            Mockito.mock(InvalidParameterHandler.class),
                                                                                            repositoryHandler,
                                                                                            Mockito.mock(OMRSRepositoryHelper.class),
                                                                                            "localServerUser",
                                                                                            Mockito.mock(OpenMetadataServerSecurityVerifier.class),
                                                                                            Mockito.mock(AuditLog.class));

        return handler.findBeans("testUser",
                                 "Topic",
                                 null,
                                 false,
                                 null,
                                 null,
                                 null,
                                 null,
                                 null,
                                 null,
                                 false,
                                 false,
                                 0,
                                 1,
                                 null,
                                 "test");
    }
}
