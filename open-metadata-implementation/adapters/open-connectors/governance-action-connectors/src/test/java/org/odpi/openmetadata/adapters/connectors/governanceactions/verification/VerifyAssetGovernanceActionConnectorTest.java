/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

/**
 * VerifyAssetGovernanceActionConnectorTest runs the Verify Asset Governance Action Service over a mocked
 * governance context and checks the guards it produces.
 */
public class VerifyAssetGovernanceActionConnectorTest
{
    private static final String ASSET_GUID = "asset-guid";

    private GovernanceActionContext governanceContext;


    /**
     * A mocked governance context with no action targets.
     */
    @BeforeMethod public void setUpContext()
    {
        governanceContext = mock(GovernanceActionContext.class);
    }


    /**
     * An asset with zones, an owner and an origin passes each check.
     *
     * @throws Exception not expected
     */
    @Test public void testAllAssigned() throws Exception
    {
        OpenMetadataElement asset = this.element(ASSET_GUID,
                                                 OpenMetadataType.ASSET.typeName,
                                                 OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                 OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                 OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(asset)));

        this.runService();

        this.verifyChecks(VerifyAssetGuard.ZONES_ASSIGNED, VerifyAssetGuard.OWNER_ASSIGNED, VerifyAssetGuard.ORIGIN_ASSIGNED);
    }


    /**
     * An asset without classifications fails each check.
     *
     * @throws Exception not expected
     */
    @Test public void testNoneAssigned() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(this.element(ASSET_GUID, OpenMetadataType.ASSET.typeName))));

        this.runService();

        this.verifyChecks(VerifyAssetGuard.NO_ZONES, VerifyAssetGuard.NO_OWNER, VerifyAssetGuard.NO_ORIGIN);
    }


    /**
     * Each check is reported separately, and classifications that are not checked are ignored.
     *
     * @throws Exception not expected
     */
    @Test public void testSomeAssigned() throws Exception
    {
        OpenMetadataElement asset = this.element(ASSET_GUID,
                                                 OpenMetadataType.ASSET.typeName,
                                                 OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                 OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName);

        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(asset)));

        this.runService();

        this.verifyChecks(VerifyAssetGuard.NO_ZONES, VerifyAssetGuard.OWNER_ASSIGNED, VerifyAssetGuard.NO_ORIGIN);
    }


    /**
     * An action target that is not an asset is rejected.
     *
     * @throws Exception not expected
     */
    @Test public void testTargetNotAsset() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(this.element("term-guid", OpenMetadataType.GLOSSARY_TERM.typeName))));

        this.runService();

        this.verifyCompletion(CompletionStatus.INVALID, VerifyAssetGuard.TARGET_NOT_ASSET.getName());
    }


    /**
     * No action targets, whether the list is missing or empty, means there is no asset to verify.
     *
     * @throws Exception not expected
     */
    @Test public void testNoTargets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(null);

        this.runService();

        when(governanceContext.getActionTargetElements()).thenReturn(List.of());

        this.runService();

        verify(governanceContext, times(2)).recordCompletionStatus(eq(CompletionStatus.INVALID),
                                                                   eq(List.of(VerifyAssetGuard.NO_TARGETS_DETECTED.getName())),
                                                                   isNull(),
                                                                   isNull());
    }


    /**
     * More than one action target is not supported.
     *
     * @throws Exception not expected
     */
    @Test public void testMultipleTargets() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(this.element(ASSET_GUID, OpenMetadataType.ASSET.typeName)),
                                                                             this.actionTarget(this.element("second-asset-guid", OpenMetadataType.ASSET.typeName))));

        this.runService();

        this.verifyCompletion(CompletionStatus.INVALID, VerifyAssetGuard.MULTIPLE_TARGETS_DETECTED.getName());
    }


    /**
     * A failure is reported with the verification-failed guard.
     *
     * @throws Exception not expected
     */
    @Test public void testVerificationFailed() throws Exception
    {
        when(governanceContext.getActionTargetElements()).thenReturn(List.of(this.actionTarget(this.element(ASSET_GUID, OpenMetadataType.ASSET.typeName))));

        doThrow(new PropertyServerException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition("test", "test", "repository unavailable"),
                                            "test",
                                            "test"))
                .when(governanceContext).recordCompletionStatus(eq(CompletionStatus.ACTIONED), anyList(), isNull(), isNull());

        assertThrows(ConnectorCheckedException.class, this::runService);

        verify(governanceContext).recordCompletionStatus(eq(CompletionStatus.FAILED),
                                                         eq(List.of(VerifyAssetGuard.VERIFICATION_FAILED.getName())),
                                                         isNull(),
                                                         isNull(),
                                                         anyString());
    }


    /**
     * Run the service.
     *
     * @throws Exception not expected
     */
    private void runService() throws Exception
    {
        VerifyAssetGovernanceActionConnector connector = new VerifyAssetGovernanceActionConnector();

        connector.initialize("test-instance", new Connection());
        connector.setGovernanceContext(governanceContext);
        connector.start();
    }


    /**
     * Check that the service completed with the guards for the zones, owner and origin checks, and that each of
     * these guards is published with the completion status that the service uses.
     *
     * @param zonesGuard expected guard for the zones check
     * @param ownerGuard expected guard for the owner check
     * @param originGuard expected guard for the origin check
     * @throws Exception not expected
     */
    private void verifyChecks(VerifyAssetGuard zonesGuard,
                              VerifyAssetGuard ownerGuard,
                              VerifyAssetGuard originGuard) throws Exception
    {
        List<String> guards = new ArrayList<>();

        for (VerifyAssetGuard guard : List.of(zonesGuard, ownerGuard, originGuard))
        {
            assertEquals(guard.getCompletionStatus(), CompletionStatus.ACTIONED, guard.getName());
            guards.add(guard.getName());
        }

        verify(governanceContext).recordCompletionStatus(eq(CompletionStatus.ACTIONED), eq(guards), isNull(), isNull());
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
                                                         isNull());
    }


    /**
     * Return an element of the requested type with the requested classifications.
     *
     * @param guid unique identifier of the element
     * @param typeName type of the element
     * @param classificationNames names of the classifications attached to the element
     * @return element
     */
    private OpenMetadataElement element(String    guid,
                                        String    typeName,
                                        String... classificationNames)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(typeName);
        elementType.setSuperTypeNames(List.of(OpenMetadataType.REFERENCEABLE.typeName, OpenMetadataType.OPEN_METADATA_ROOT.typeName));

        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(guid);
        element.setType(elementType);

        if (classificationNames.length > 0)
        {
            List<AttachedClassification> classifications = new ArrayList<>();

            for (String classificationName : classificationNames)
            {
                ElementType classificationType = new ElementType();
                classificationType.setTypeName(classificationName);

                AttachedClassification classification = new AttachedClassification();
                classification.setClassificationName(classificationName);
                classification.setType(classificationType);

                classifications.add(classification);
            }

            element.setClassifications(classifications);
        }

        return element;
    }


    /**
     * Return an action target.
     *
     * @param element element to verify
     * @return action target
     */
    private ActionTargetElement actionTarget(OpenMetadataElement element)
    {
        ActionTargetElement actionTarget = new ActionTargetElement();
        actionTarget.setActionTargetName("newAsset");
        actionTarget.setActionTargetGUID(element.getElementGUID());
        actionTarget.setTargetElement(element);

        return actionTarget;
    }
}
