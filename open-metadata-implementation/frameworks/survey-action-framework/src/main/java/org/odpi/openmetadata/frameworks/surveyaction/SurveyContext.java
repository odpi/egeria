/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.surveyaction.ffdc.SAFAuditCode;
import org.odpi.openmetadata.frameworks.surveyaction.ffdc.SAFErrorCode;

import java.util.Map;
import java.util.Objects;


/**
 * SurveyContext provides a survey action service with access to information about
 * the survey request along with access to the open metadata repository interfaces.
 */
public class SurveyContext
{
    private final String                  userId;
    private final String                  assetGUID;
    private final Map<String, String>     requestParameters;
    private final SurveyAssetStore        assetStore;
    private final AnnotationStore         annotationStore;
    private final SurveyOpenMetadataStore openMetadataStore;
    private final String                  surveyActionServiceName;
    private final String                  requesterUserId;
    private final AuditLog                auditLog;
    private final FileClassifier          fileClassifier;
    private       boolean                 isActive = true;


    /**
     * Constructor sets up the key parameters for using the context.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the survey action service
     * @param assetStore survey asset store for the survey action service
     * @param annotationStore annotation store for the survey action service
     * @param openMetadataStore generic metadata API from the Governance Action Framework (GAF)
     * @param surveyActionServiceName name of the running service
     * @param requesterUserId original user requesting this governance service
     * @param auditLog logging destination
     */
    public SurveyContext(String                     userId,
                         String                     assetGUID,
                         Map<String, String>        requestParameters,
                         SurveyAssetStore           assetStore,
                         AnnotationStore            annotationStore,
                         SurveyOpenMetadataStore    openMetadataStore,
                         String                     surveyActionServiceName,
                         String                     requesterUserId,
                         AuditLog                   auditLog)
    {
        this.userId                  = userId;
        this.assetGUID               = assetGUID;
        this.requestParameters       = requestParameters;
        this.assetStore              = assetStore;
        this.annotationStore         = annotationStore;
        this.openMetadataStore       = openMetadataStore;
        this.surveyActionServiceName = surveyActionServiceName;
        this.requesterUserId         = requesterUserId;
        this.auditLog                = auditLog;

        this.fileClassifier          = new FileClassifier(openMetadataStore);
    }


    /**
     * Return the unique identifier of the asset being discovered.
     *
     * @return string guid
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public String getAssetGUID() throws ConnectorCheckedException
    {
        final String methodName = "getAssetGUID";

        validateIsActive(methodName);

        return assetGUID;
    }


    /**
     * Return the properties that hold the parameters used to drive the survey action service's analysis.
     *
     * @return AdditionalProperties object storing the analysis parameters
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public Map<String, String> getRequestParameters() throws ConnectorCheckedException
    {
        final String methodName = "getRequestParameters";

        validateIsActive(methodName);

        return requestParameters;
    }


    /**
     * Return the requester user identifier.
     *
     * @return userId
     */
    public String getRequesterUserId()
    {
        return requesterUserId;
    }


    /**
     * Return the asset store for the survey action service.  This is able to provide a connector to the asset
     * configured with the properties of the asset from a property server.
     *
     * @return asset store
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public SurveyAssetStore getAssetStore() throws ConnectorCheckedException
    {
        final String methodName = "getAssetStore";

        validateIsActive(methodName);

        return assetStore;
    }


    /**
     * Return the annotation store for the survey action service.  This is where the annotations are stored and
     * retrieved from.
     *
     * @return annotation store
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public AnnotationStore getAnnotationStore() throws ConnectorCheckedException
    {
        final String methodName = "getAnnotationStore";

        validateIsActive(methodName);

        return annotationStore;
    }


    /**
     * Return a generic interface for accessing and updating open metadata elements, classifications and relationships.
     *
     * @return open metadata store
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public SurveyOpenMetadataStore getOpenMetadataStore() throws ConnectorCheckedException
    {
        final String methodName = "getOpenMetadataStore";

        validateIsActive(methodName);

        return openMetadataStore;
    }


    /**
     * Return the file classifier that retrieves file reference data from the open metadata repositories.
     *
     * @return file classifier
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public FileClassifier getFileClassifier() throws ConnectorCheckedException
    {
        final String methodName = "getFileClassifier";

        validateIsActive(methodName);

        return fileClassifier;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        isActive = false;
    }


    /**
     * Verify that the connector is still active.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException exception thrown if no longer active
     */
    private void validateIsActive(String methodName) throws ConnectorCheckedException
    {
        if (! isActive)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    SAFAuditCode.DISCONNECT_DETECTED.getMessageDefinition(surveyActionServiceName));
            }

            throw new ConnectorCheckedException(SAFErrorCode.DISCONNECT_DETECTED.getMessageDefinition(surveyActionServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SurveyContext{" +
                "userId='" + userId + '\'' +
                ", assetGUID='" + assetGUID + '\'' +
                ", requestParameters=" + requestParameters +
                ", assetStore=" + assetStore +
                ", annotationStore=" + annotationStore +
                ", openMetadataStore=" + openMetadataStore +
                '}';
    }

    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        SurveyContext that = (SurveyContext) objectToCompare;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(assetGUID, that.assetGUID) &&
                Objects.equals(requestParameters, that.requestParameters) &&
                Objects.equals(assetStore, that.assetStore) &&
                Objects.equals(annotationStore, that.annotationStore) &&
                Objects.equals(openMetadataStore, that.openMetadataStore);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(userId, assetGUID, requestParameters, assetStore, annotationStore, openMetadataStore);
    }
}
