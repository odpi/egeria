/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * TypeDetectorGovernanceActionConnectorTest runs the Type Detector Governance Action Service over a mocked governance
 * context and checks the guards it produces.
 */
public class TypeDetectorGovernanceActionConnectorTest
{
    private static final String ACCEPTABLE_TYPE_NAMES = TypeDetectorRequestParameter.ACCEPTABLE_TYPE_NAMES.getName();

    private GovernanceActionContext governanceContext;
    private Map<String, Object>     configurationProperties;


    /**
     * A mocked governance context with no request parameters or action targets.
     */
    @BeforeMethod public void setUpContext()
    {
        governanceContext       = mock(GovernanceActionContext.class);
        configurationProperties = new HashMap<>();
    }


    /**
     * An element whose type is listed is acceptable.
     *
     * @throws Exception not expected
     */
    @Test public void testExactTypeDetected() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ACCEPTABLE_TYPE_NAMES, "GlossaryTerm, Asset,"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName)));

        this.runService();

        this.verifyCompletion(TypeDetectorGuard.TYPE_DETECTED);
    }


    /**
     * An element whose super type is listed is acceptable.
     *
     * @throws Exception not expected
     */
    @Test public void testSuperTypeDetected() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ACCEPTABLE_TYPE_NAMES, OpenMetadataType.ASSET.typeName));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("file-guid",
                                                                                               OpenMetadataType.DATA_FILE.typeName,
                                                                                               OpenMetadataType.ASSET.typeName)));

        this.runService();

        this.verifyCompletion(TypeDetectorGuard.TYPE_DETECTED);
    }


    /**
     * The configuration properties supply the acceptable types when there is no request parameter.  They can be a list.
     *
     * @throws Exception not expected
     */
    @Test public void testTypesFromConfigurationProperties() throws Exception
    {
        configurationProperties.put(ACCEPTABLE_TYPE_NAMES, List.of(OpenMetadataType.GLOSSARY_TERM.typeName, OpenMetadataType.ASSET.typeName));

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName)));

        this.runService();

        this.verifyCompletion(TypeDetectorGuard.TYPE_DETECTED);
    }


    /**
     * The request parameter overrides the configuration property of the same name.
     *
     * @throws Exception not expected
     */
    @Test public void testRequestParameterOverridesConfigurationProperty() throws Exception
    {
        configurationProperties.put(ACCEPTABLE_TYPE_NAMES, OpenMetadataType.ASSET.typeName);

        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ACCEPTABLE_TYPE_NAMES, OpenMetadataType.GLOSSARY_TERM.typeName));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName)));

        this.runService();

        this.verifyCompletion(TypeDetectorGuard.TYPE_NOT_DETECTED);
    }


    /**
     * Every action target must be acceptable since they are all passed on to the next step of a process.
     * The elements that are not acceptable are reported in the completion message.
     *
     * @throws Exception not expected
     */
    @Test public void testOneUnacceptableTarget() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ACCEPTABLE_TYPE_NAMES, OpenMetadataType.ASSET.typeName));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName),
                                                                             this.actionTarget("term-guid", OpenMetadataType.GLOSSARY_TERM.typeName)));

        this.runService();

        ArgumentCaptor<AuditLogMessageDefinition> captor = ArgumentCaptor.forClass(AuditLogMessageDefinition.class);

        verify(governanceContext).recordCompletionStatus(eq(CompletionStatus.ACTIONED),
                                                         eq(List.of(TypeDetectorGuard.TYPE_NOT_DETECTED.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         captor.capture());

        String[] messageParameters = captor.getValue().getMessageParams();

        assertTrue(messageParameters[1].contains("term-guid (GlossaryTerm)"));
        assertFalse(messageParameters[1].contains("asset-guid"));
    }


    /**
     * An action target whose element could not be retrieved is not acceptable.
     *
     * @throws Exception not expected
     */
    @Test public void testTargetWithoutElement() throws Exception
    {
        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetGUID("missing-guid");

        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ACCEPTABLE_TYPE_NAMES, OpenMetadataType.ASSET.typeName));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(actionTarget));

        this.runService();

        this.verifyCompletion(TypeDetectorGuard.TYPE_NOT_DETECTED);
    }


    /**
     * The service cannot check the action targets without a list of acceptable types.  A blank list counts as missing.
     *
     * @throws Exception not expected
     */
    @Test public void testMissingAcceptableTypeNames() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(ACCEPTABLE_TYPE_NAMES, " , "));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName)));

        this.runService();

        this.verifyCompletion(TypeDetectorGuard.MISSING_ACCEPTABLE_TYPE_NAMES);
    }


    /**
     * Without action targets there is nothing to check.
     *
     * @throws Exception not expected
     */
    @Test public void testNoTargets() throws Exception
    {
        configurationProperties.put(ACCEPTABLE_TYPE_NAMES, OpenMetadataType.ASSET.typeName);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of());

        this.runService();

        this.verifyCompletion(TypeDetectorGuard.NO_TARGETS);
    }


    /**
     * Run the service with the test's configuration properties.
     *
     * @throws Exception not expected
     */
    private void runService() throws Exception
    {
        Connection connection = new Connection();
        connection.setConfigurationProperties(configurationProperties);

        TypeDetectorGovernanceActionConnector connector = new TypeDetectorGovernanceActionConnector();

        connector.initialize("test-instance", connection);
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Check the completion status and guard that the service recorded.
     *
     * @param guard expected guard
     * @throws Exception not expected
     */
    private void verifyCompletion(TypeDetectorGuard guard) throws Exception
    {
        verify(governanceContext).recordCompletionStatus(eq(guard.getCompletionStatus()),
                                                         eq(List.of(guard.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         nullable(AuditLogMessageDefinition.class));
    }


    /**
     * Return an action target for an element of the requested type.
     *
     * @param guid unique identifier of the element
     * @param typeName type of the element
     * @param superTypeNames names of the super types of the element
     * @return action target
     */
    private ActionTargetElement actionTarget(String    guid,
                                             String    typeName,
                                             String... superTypeNames)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(typeName);
        elementType.setSuperTypeNames(List.of(superTypeNames));

        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(guid);
        element.setType(elementType);

        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetName("elementToCheck");
        actionTarget.setActionTargetGUID(guid);
        actionTarget.setTargetElement(element);

        return actionTarget;
    }
}
