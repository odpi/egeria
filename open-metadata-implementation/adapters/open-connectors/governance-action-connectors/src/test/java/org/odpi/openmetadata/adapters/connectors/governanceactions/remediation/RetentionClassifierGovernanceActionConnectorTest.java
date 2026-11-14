/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.RetentionBasis;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

/**
 * RetentionClassifierGovernanceActionConnectorTest runs the Retention Classifier Governance Action Service over a
 * mocked governance context and checks the Retention classification it sets and the guards it produces.
 */
public class RetentionClassifierGovernanceActionConnectorTest
{
    private static final String ASSET_GUID   = "asset-guid";
    private static final String STEWARD_GUID = "steward-guid";

    private static final long ONE_DAY   = 86400000L;
    private static final long ONE_YEAR  = 31556952000L;

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private GovernanceActionContext governanceContext;
    private OpenMetadataStore       openMetadataStore;
    private Map<String, Object>     configurationProperties;


    /**
     * A mocked governance context with no request parameters or action targets.
     *
     * @throws Exception not expected
     */
    @BeforeMethod public void setUpContext() throws Exception
    {
        governanceContext = mock(GovernanceActionContext.class);
        openMetadataStore = mock(OpenMetadataStore.class);

        when(governanceContext.getOpenMetadataStore()).thenReturn(openMetadataStore);

        configurationProperties = new HashMap<>();
    }


    /**
     * The values from the request parameters are set on the new asset, along with the steward.
     *
     * @throws Exception not expected
     */
    @Test public void testValuesFromRequestParameters() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(RetentionClassifierRequestParameter.STATUS_IDENTIFIER.getName(), "2",
                                                                         RetentionClassifierRequestParameter.BASIS_IDENTIFIER.getName(), "3",
                                                                         RetentionClassifierRequestParameter.RETENTION_TIME_TO_ARCHIVE.getName(), Long.toString(ONE_DAY),
                                                                         RetentionClassifierRequestParameter.RETENTION_TIME_TO_DELETE.getName(), Long.toString(ONE_DAY * 2)));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID), this.steward()));

        long start = new Date().getTime();

        this.runService();

        long end = new Date().getTime();

        NewElementProperties properties = this.classificationProperties();

        assertEquals(this.intProperty(OpenMetadataProperty.STATUS_IDENTIFIER.name, properties), 2);
        assertEquals(this.intProperty(OpenMetadataProperty.RETENTION_BASIS.name, properties), 3);
        assertEquals(this.stringProperty(OpenMetadataProperty.STEWARD.name, properties), STEWARD_GUID);
        assertEquals(this.stringProperty(OpenMetadataProperty.STEWARD_TYPE_NAME.name, properties), OpenMetadataType.PERSON.typeName);
        assertEquals(this.stringProperty(OpenMetadataProperty.STEWARD_PROPERTY_NAME.name, properties), OpenMetadataProperty.GUID.name);
        this.assertDateBetween(OpenMetadataProperty.ARCHIVE_AFTER.name, properties, start + ONE_DAY, end + ONE_DAY);
        this.assertDateBetween(OpenMetadataProperty.DELETE_AFTER.name, properties, start + (ONE_DAY * 2), end + (ONE_DAY * 2));

        this.verifyCompletion(CompletionStatus.ACTIONED, RetentionClassifierGuard.CLASSIFICATION_ASSIGNED.getName());
    }


    /**
     * The configuration properties supply the values that the request parameters do not override.  Values from the
     * configuration properties may be numbers or strings, and blank request parameters are ignored.
     *
     * @throws Exception not expected
     */
    @Test public void testRequestParametersOverrideConfigurationProperties() throws Exception
    {
        configurationProperties.put(RetentionClassifierRequestParameter.STATUS_IDENTIFIER.getName(), 1);
        configurationProperties.put(RetentionClassifierRequestParameter.BASIS_IDENTIFIER.getName(), "4");
        configurationProperties.put(RetentionClassifierRequestParameter.RETENTION_TIME_TO_ARCHIVE.getName(), ONE_DAY * 3);
        configurationProperties.put(RetentionClassifierRequestParameter.RETENTION_TIME_TO_DELETE.getName(), ONE_DAY * 4);

        when(governanceContext.getRequestParameters()).thenReturn(Map.of(RetentionClassifierRequestParameter.BASIS_IDENTIFIER.getName(), "5",
                                                                         RetentionClassifierRequestParameter.RETENTION_TIME_TO_DELETE.getName(), " "));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID), this.steward()));

        long start = new Date().getTime();

        this.runService();

        long end = new Date().getTime();

        NewElementProperties properties = this.classificationProperties();

        assertEquals(this.intProperty(OpenMetadataProperty.STATUS_IDENTIFIER.name, properties), 1);
        assertEquals(this.intProperty(OpenMetadataProperty.RETENTION_BASIS.name, properties), 5);
        this.assertDateBetween(OpenMetadataProperty.ARCHIVE_AFTER.name, properties, start + (ONE_DAY * 3), end + (ONE_DAY * 3));
        this.assertDateBetween(OpenMetadataProperty.DELETE_AFTER.name, properties, start + (ONE_DAY * 4), end + (ONE_DAY * 4));

        this.verifyCompletion(CompletionStatus.ACTIONED, RetentionClassifierGuard.CLASSIFICATION_ASSIGNED.getName());
    }


    /**
     * Without request parameters or configuration properties, the default values are used.
     *
     * @throws Exception not expected
     */
    @Test public void testDefaultValues() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.steward(), this.newAsset(ASSET_GUID)));

        long start = new Date().getTime();

        this.runService();

        long end = new Date().getTime();

        NewElementProperties properties = this.classificationProperties();

        assertEquals(this.intProperty(OpenMetadataProperty.STATUS_IDENTIFIER.name, properties), StatusIdentifier.VALIDATED.getOrdinal());
        assertEquals(this.intProperty(OpenMetadataProperty.RETENTION_BASIS.name, properties), RetentionBasis.REGULATED_LIFETIME.getOrdinal());
        this.assertDateBetween(OpenMetadataProperty.ARCHIVE_AFTER.name, properties, start + (ONE_YEAR * 5), end + (ONE_YEAR * 5));
        this.assertDateBetween(OpenMetadataProperty.DELETE_AFTER.name, properties, start + (ONE_YEAR * 15), end + (ONE_YEAR * 15));

        this.verifyCompletion(CompletionStatus.ACTIONED, RetentionClassifierGuard.CLASSIFICATION_ASSIGNED.getName());
    }


    /**
     * Only assets supplied as newAsset action targets are classified.
     *
     * @throws Exception not expected
     */
    @Test public void testOnlyNewAssetsClassified() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID),
                                                                             this.newAsset("second-asset-guid"),
                                                                             this.actionTarget("newAsset", "term-guid", OpenMetadataType.GLOSSARY_TERM.typeName),
                                                                             this.actionTarget("sourceFile", "source-guid", OpenMetadataType.ASSET.typeName),
                                                                             this.steward()));

        this.runService();

        verify(openMetadataStore).classifyMetadataElementInStore(eq(ASSET_GUID), eq(OpenMetadataType.RETENTION_CLASSIFICATION.typeName), any(), any(NewElementProperties.class));
        verify(openMetadataStore).classifyMetadataElementInStore(eq("second-asset-guid"), eq(OpenMetadataType.RETENTION_CLASSIFICATION.typeName), any(), any(NewElementProperties.class));
        verify(openMetadataStore, times(2)).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));

        this.verifyCompletion(CompletionStatus.ACTIONED, RetentionClassifierGuard.CLASSIFICATION_ASSIGNED.getName());
    }


    /**
     * Without a steward nothing is changed and the service ends as invalid.
     *
     * @throws Exception not expected
     */
    @Test public void testMissingSteward() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID)));

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.INVALID, RetentionClassifierGuard.MISSING_STEWARD.getName());
    }


    /**
     * Without action targets there is nothing to classify.
     *
     * @throws Exception not expected
     */
    @Test public void testNoTargets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of());

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.INVALID, RetentionClassifierGuard.NO_TARGETS.getName());
    }


    /**
     * A steward on its own, or with elements that are not new assets, does not mean an asset was classified.
     *
     * @throws Exception not expected
     */
    @Test public void testNoNewAssets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.steward(),
                                                                             this.actionTarget("sourceFile", "source-guid", OpenMetadataType.ASSET.typeName)));

        this.runService();

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyCompletion(CompletionStatus.INVALID, RetentionClassifierGuard.NO_TARGETS.getName());
    }


    /**
     * A value that is not a whole number is reported as a failure before any asset is changed.
     *
     * @throws Exception not expected
     */
    @Test public void testInvalidValue() throws Exception
    {
        when(governanceContext.getRequestParameters()).thenReturn(Map.of(RetentionClassifierRequestParameter.RETENTION_TIME_TO_ARCHIVE.getName(), "five years"));
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID), this.steward()));

        assertThrows(ConnectorCheckedException.class, this::runService);

        verify(openMetadataStore, never()).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));
        this.verifyFailure();
    }


    /**
     * A failure to classify an asset is reported with the classification-failed guard.
     *
     * @throws Exception not expected
     */
    @Test public void testClassificationFailure() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.newAsset(ASSET_GUID), this.steward()));

        doThrow(new PropertyServerException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition("test", "test", "repository unavailable"),
                                            "test",
                                            "test"))
                .when(openMetadataStore).classifyMetadataElementInStore(anyString(), anyString(), any(), any(NewElementProperties.class));

        assertThrows(ConnectorCheckedException.class, this::runService);

        this.verifyFailure();
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

        RetentionClassifierGovernanceActionConnector connector = new RetentionClassifierGovernanceActionConnector();

        connector.initialize("test-instance", connection);
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Return the properties of the single Retention classification that was set.
     *
     * @return classification properties
     * @throws Exception not expected
     */
    private NewElementProperties classificationProperties() throws Exception
    {
        ArgumentCaptor<NewElementProperties> captor = ArgumentCaptor.forClass(NewElementProperties.class);

        verify(openMetadataStore).classifyMetadataElementInStore(eq(ASSET_GUID),
                                                                 eq(OpenMetadataType.RETENTION_CLASSIFICATION.typeName),
                                                                 any(),
                                                                 captor.capture());
        return captor.getValue();
    }


    /**
     * Check the completion status and guard that the service recorded.
     *
     * @param completionStatus expected completion status
     * @param guard expected guard
     * @throws Exception not expected
     */
    private void verifyCompletion(CompletionStatus completionStatus,
                                  String           guard) throws Exception
    {
        verify(governanceContext).recordCompletionStatus(eq(completionStatus),
                                                         eq(List.of(guard)),
                                                         isNull(),
                                                         isNull(),
                                                         nullable(AuditLogMessageDefinition.class));
    }


    /**
     * Check that the service recorded the classification-failed guard.
     *
     * @throws Exception not expected
     */
    private void verifyFailure() throws Exception
    {
        verify(governanceContext).recordCompletionStatus(eq(CompletionStatus.FAILED),
                                                         eq(List.of(RetentionClassifierGuard.CLASSIFICATION_FAILED.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         anyString());
    }


    /**
     * Check that a date property falls within the expected range.
     *
     * @param propertyName name of the property
     * @param properties properties to look in
     * @param earliest earliest expected time
     * @param latest latest expected time
     */
    private void assertDateBetween(String               propertyName,
                                   NewElementProperties properties,
                                   long                 earliest,
                                   long                 latest)
    {
        Date date = propertyHelper.getDateProperty("test", propertyName, properties, "test");

        assertTrue((date != null) && (date.getTime() >= earliest) && (date.getTime() <= latest),
                   propertyName + " is " + date);
    }


    /**
     * Return an int property.
     *
     * @param propertyName name of the property
     * @param properties properties to look in
     * @return value
     */
    private int intProperty(String               propertyName,
                            NewElementProperties properties)
    {
        return propertyHelper.getIntProperty("test", propertyName, properties, "test");
    }


    /**
     * Return a string property.
     *
     * @param propertyName name of the property
     * @param properties properties to look in
     * @return value or null
     */
    private String stringProperty(String               propertyName,
                                  NewElementProperties properties)
    {
        return propertyHelper.getStringProperty("test", propertyName, properties, "test");
    }


    /**
     * Return a newAsset action target for an asset.
     *
     * @param guid unique identifier of the asset
     * @return action target
     */
    private ActionTargetElement newAsset(String guid)
    {
        return this.actionTarget("newAsset", guid, OpenMetadataType.ASSET.typeName);
    }


    /**
     * Return the steward action target.
     *
     * @return action target
     */
    private ActionTargetElement steward()
    {
        return this.actionTarget("steward", STEWARD_GUID, OpenMetadataType.PERSON.typeName);
    }


    /**
     * Return an action target for an element of the requested type.
     *
     * @param actionTargetName name of the action target
     * @param guid unique identifier of the element
     * @param typeName type of the element - Asset, GlossaryTerm and Person elements are given their supertypes
     * @return action target
     */
    private ActionTargetElement actionTarget(String actionTargetName,
                                             String guid,
                                             String typeName)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(typeName);

        if (OpenMetadataType.PERSON.typeName.equals(typeName))
        {
            elementType.setSuperTypeNames(List.of(OpenMetadataType.ACTOR_PROFILE.typeName,
                                                  OpenMetadataType.ACTOR.typeName,
                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                  OpenMetadataType.OPEN_METADATA_ROOT.typeName));
        }
        else
        {
            elementType.setSuperTypeNames(List.of(OpenMetadataType.REFERENCEABLE.typeName, OpenMetadataType.OPEN_METADATA_ROOT.typeName));
        }

        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(guid);
        element.setType(elementType);

        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetName(actionTargetName);
        actionTarget.setActionTargetGUID(guid);
        actionTarget.setTargetElement(element);

        return actionTarget;
    }
}
