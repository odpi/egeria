/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.SpineAttribute;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test Synonym mapper
 */
public class TestClassificationFactory
{
    public static final String DESC = " ssss";
    @Mock
    private OMRSAPIHelper oMRSAPIHelper;
    @Mock
    private OMRSRepositoryHelper omrsRepositoryHelper;

    @BeforeMethod

    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testClassificationFactory()
    {
//        when( oMRSAPIHelper.getOMRSRepositoryHelper()).thenReturn(omrsRepositoryHelper);
//        when( oMRSAPIHelper.getServiceName()).thenReturn("source");
//        when( omrsRepositoryHelper.isTypeOf(anyString(),anyString(),anyString())).thenReturn(true);
//
//
//        // set the mock omrs in to the rest file.
//
//        ClassificationFactory factory = new ClassificationFactory(oMRSAPIHelper);
//        org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification omrsClassification =new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification();
//        String name = "SpineAttribute";
//        SpineAttribute spineAttribute =  (SpineAttribute )factory.getOMASClassification(name,omrsClassification);
//       int i=0;
    }
}
