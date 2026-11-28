/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * TypeCasterGovernanceActionConnectorTest runs the Type Caster Governance Action Service over a mocked governance
 * context and checks the guards it produces.
 */
public class TypeCasterGovernanceActionConnectorTest
{
    private static final String CANDIDATE_TYPE_NAMES = TypeCasterRequestParameter.CANDIDATE_TYPE_NAMES.getName();

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
     * Without candidate types, the guard is the name of the element's type.
     *
     * @throws Exception not expected
     */
    @Test public void testTypeNameIsGuard() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("file-guid",
                                                                                               OpenMetadataType.DATA_FILE.typeName,
                                                                                               OpenMetadataType.ASSET.typeName)));

        this.runService();

        AuditLogMessageDefinition messageDefinition = this.verifyCompletion(CompletionStatus.ACTIONED, OpenMetadataType.DATA_FILE.typeName);

        assertEquals(messageDefinition.getMessageId(), GovernanceActionConnectorsAuditCode.TYPE_CAST.getMessageDefinition().getMessageId());
        assertEquals(messageDefinition.getMessageParams()[2], "file-guid");
        assertEquals(messageDefinition.getMessageParams()[3], OpenMetadataType.DATA_FILE.typeName);
    }


    /**
     * The guard is the first candidate type that the element matches, including through its super types.
     *
     * @throws Exception not expected
     */
    @Test public void testFirstMatchingCandidateIsGuard() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(CANDIDATE_TYPE_NAMES, "GlossaryTerm, Asset, DataFile,"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("file-guid",
                                                                                               OpenMetadataType.DATA_FILE.typeName,
                                                                                               OpenMetadataType.ASSET.typeName)));

        this.runService();

        this.verifyCompletion(CompletionStatus.ACTIONED, OpenMetadataType.ASSET.typeName);
    }


    /**
     * The configuration properties supply the candidate types when there is no request parameter.  They can be a list.
     *
     * @throws Exception not expected
     */
    @Test public void testCandidatesFromConfigurationProperties() throws Exception
    {
        configurationProperties.put(CANDIDATE_TYPE_NAMES, List.of(OpenMetadataType.GLOSSARY_TERM.typeName, OpenMetadataType.ASSET.typeName));

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("term-guid", OpenMetadataType.GLOSSARY_TERM.typeName)));

        this.runService();

        this.verifyCompletion(CompletionStatus.ACTIONED, OpenMetadataType.GLOSSARY_TERM.typeName);
    }


    /**
     * The request parameter overrides the configuration property of the same name.
     *
     * @throws Exception not expected
     */
    @Test public void testRequestParameterOverridesConfigurationProperty() throws Exception
    {
        configurationProperties.put(CANDIDATE_TYPE_NAMES, OpenMetadataType.ASSET.typeName);

        when(governanceContext.getRequestParameters()).thenReturn(Map.of(CANDIDATE_TYPE_NAMES, OpenMetadataType.GLOSSARY_TERM.typeName));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName)));

        this.runService();

        this.verifyCompletion(TypeCasterGuard.TYPE_NOT_LISTED.getCompletionStatus(), TypeCasterGuard.TYPE_NOT_LISTED.getName());
    }


    /**
     * An element that matches none of the candidate types produces the type-not-listed guard, and the completion
     * message names the element, its type and the candidates.
     *
     * @throws Exception not expected
     */
    @Test public void testTypeNotListed() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(CANDIDATE_TYPE_NAMES, "GlossaryTerm,DataFile"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName)));

        this.runService();

        AuditLogMessageDefinition messageDefinition = this.verifyCompletion(TypeCasterGuard.TYPE_NOT_LISTED.getCompletionStatus(),
                                                                            TypeCasterGuard.TYPE_NOT_LISTED.getName());

        assertEquals(messageDefinition.getMessageId(), GovernanceActionConnectorsAuditCode.TYPE_NOT_LISTED.getMessageDefinition().getMessageId());
        assertEquals(messageDefinition.getMessageParams()[1], "asset-guid");
        assertEquals(messageDefinition.getMessageParams()[2], OpenMetadataType.ASSET.typeName);
        assertEquals(messageDefinition.getMessageParams()[3], "[GlossaryTerm, DataFile]");
    }


    /**
     * A blank list of candidate types counts as not set, so the guard is the element's type name.
     *
     * @throws Exception not expected
     */
    @Test public void testBlankCandidatesIgnored() throws Exception
    {
        configurationProperties.put(CANDIDATE_TYPE_NAMES, OpenMetadataType.GLOSSARY_TERM.typeName);

        when(governanceContext.getRequestParameters()).thenReturn(Map.of(CANDIDATE_TYPE_NAMES, " , "));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName)));

        this.runService();

        this.verifyCompletion(CompletionStatus.ACTIONED, OpenMetadataType.ASSET.typeName);
    }


    /**
     * Only one element can be described, since each action target could be of a different type.
     *
     * @throws Exception not expected
     */
    @Test public void testMultipleTargets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName),
                                                                             this.actionTarget("term-guid", OpenMetadataType.GLOSSARY_TERM.typeName)));

        this.runService();

        AuditLogMessageDefinition messageDefinition = this.verifyCompletion(TypeCasterGuard.MULTIPLE_TARGETS.getCompletionStatus(),
                                                                            TypeCasterGuard.MULTIPLE_TARGETS.getName());

        assertEquals(messageDefinition.getMessageParams()[1], "2");
        assertTrue(messageDefinition.getMessageParams()[2].contains("asset-guid"));
        assertTrue(messageDefinition.getMessageParams()[2].contains("term-guid"));
    }


    /**
     * An action target whose element could not be retrieved is ignored.  If it is the only one there is nothing to
     * describe, and alongside another action target it does not count as a second target.
     *
     * @throws Exception not expected
     */
    @Test public void testTargetWithoutElementIgnored() throws Exception
    {
        ActionTargetElement missingTarget = new ActionTargetElement();
        missingTarget.setActionTargetGUID("missing-guid");

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(missingTarget));

        this.runService();

        this.verifyCompletion(TypeCasterGuard.NO_TARGETS.getCompletionStatus(), TypeCasterGuard.NO_TARGETS.getName());

        governanceContext = mock(GovernanceActionContext.class);

        when(governanceContext.getActionTargetElements()).thenReturn(Arrays.asList(missingTarget,
                                                                                   null,
                                                                                   this.actionTarget("asset-guid", OpenMetadataType.ASSET.typeName)));

        this.runService();

        this.verifyCompletion(CompletionStatus.ACTIONED, OpenMetadataType.ASSET.typeName);
    }


    /**
     * Without action targets there is nothing to describe.
     *
     * @throws Exception not expected
     */
    @Test public void testNoTargets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of());

        this.runService();

        this.verifyCompletion(TypeCasterGuard.NO_TARGETS.getCompletionStatus(), TypeCasterGuard.NO_TARGETS.getName());

        governanceContext = mock(GovernanceActionContext.class);

        when(governanceContext.getActionTargetElements()).thenReturn(null);

        this.runService();

        this.verifyCompletion(TypeCasterGuard.NO_TARGETS.getCompletionStatus(), TypeCasterGuard.NO_TARGETS.getName());
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

        TypeCasterGovernanceActionConnector connector = new TypeCasterGovernanceActionConnector();

        connector.initialize("test-instance", connection);
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Check the completion status and guard that the service recorded.
     *
     * @param completionStatus expected completion status
     * @param guard expected guard
     * @return the completion message
     * @throws Exception not expected
     */
    private AuditLogMessageDefinition verifyCompletion(CompletionStatus completionStatus,
                                                       String           guard) throws Exception
    {
        ArgumentCaptor<AuditLogMessageDefinition> captor = ArgumentCaptor.forClass(AuditLogMessageDefinition.class);

        verify(governanceContext).recordCompletionStatus(eq(completionStatus),
                                                         eq(List.of(guard)),
                                                         isNull(),
                                                         isNull(),
                                                         captor.capture());

        return captor.getValue();
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
        actionTarget.setActionTargetName("elementToCast");
        actionTarget.setActionTargetGUID(guid);
        actionTarget.setTargetElement(element);

        return actionTarget;
    }
}
