/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.discovery.properties.DataField;
import org.odpi.openmetadata.frameworks.discovery.properties.DataFieldLink;
import org.odpi.openmetadata.frameworks.discovery.properties.RelatedDataField;

import java.util.List;
import java.util.Map;

/**
 * The DiscoveryAnnotationStore provides the interface used by a discovery engine to
 * store annotations in the annotation store.  There is one instance of the annotation store
 * for each discovery request.  The userId that made the discovery request is the default user for
 * the annotation store.  This userId may be over-ridden by the discovery engine.
 */
public abstract class DiscoveryAnnotationStore
{
    protected String                       userId;
    protected String                       assetGUID;
    protected DiscoveryAnalysisReportStore discoveryReport;


    /**
     * Constructor sets up the key parameters for accessing the annotations store.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param discoveryReport discovery report that these annotations will be filed against.
     */
    public DiscoveryAnnotationStore(String userId, String assetGUID, DiscoveryAnalysisReportStore discoveryReport)
    {
        this.userId = userId;
        this.assetGUID = assetGUID;
        this.discoveryReport = discoveryReport;
    }


    /**
     * Return the report identifier for this discovery context.  Any new annotations added to tis discovery context
     * will be linked to this report.
     *
     * @return the new discovery report.
     */
    public DiscoveryAnalysisReportStore getDiscoveryReport()
    {
        return discoveryReport;
    }


    /**
     * Return the annotation subtype names.
     *
     * @return list of type names that are subtypes of annotation
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public abstract List<String>  getTypesOfAnnotation() throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Return the annotation subtype names mapped to their descriptions.
     *
     * @return list of type names that are subtypes of annotation
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public abstract Map<String, String> getTypesOfAnnotationWithDescriptions() throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Return the list of Annotations created for the asset by previous runs of the discovery service.
     *
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public abstract List<Annotation>  getPreviousAnnotationsForAsset(int       startingFrom,
                                                                     int       maximumResults) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException;


    /**
     * Return the list of annotations from previous runs of the discovery service that are set to a specific status.
     * If status is null then annotations that have been reviewed, approved and/or actioned are returned from
     * discovery reports that are not waiting or in progress.
     *
     * @param status status value to use on the query
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public abstract List<Annotation>  getPreviousAnnotationsForAsset(AnnotationStatus status,
                                                                     int              startingFrom,
                                                                     int              maximumResults) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException;


    /**
     * Return the current list of annotations for this discovery run.
     *
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public abstract List<Annotation>  getNewAnnotationsForAsset(int       startingFrom,
                                                                int       maximumResults) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Return any annotations attached to this annotation.
     *
     * @param annotationGUID parent annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public abstract List<Annotation>  getExtendedAnnotations(String   annotationGUID,
                                                             int      startingFrom,
                                                             int      maximumResults) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Return a specific annotation stored in the annotation store (previous or new).
     *
     * @param annotationGUID unique identifier of the annotation
     * @return annotation object
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving the annotation from the annotation store.
     */
    public abstract Annotation  getAnnotation(String    annotationGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param annotation annotation object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException the annotation is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem adding the annotation to the annotation store.
     */
    public abstract String  addAnnotationToDiscoveryReport(Annotation annotation) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param parentAnnotationGUID unique identifier of the annotation that this new one is to be attached to
     * @param annotation annotation object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving annotations in the annotation store.
     */
    public abstract String  addAnnotationToAnnotation(String     parentAnnotationGUID,
                                                      Annotation annotation) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Replace the current properties of an annotation.
     *
     * @param annotation new properties
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    public abstract void  updateAnnotation(Annotation annotation) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove an annotation from the annotation store.
     *
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    public abstract void  deleteAnnotation(String   annotationGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Return the list of data fields from previous runs of the discovery service.
     *
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of data fields (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    public abstract List<DataField>  getPreviousDataFieldsForAsset(int              startingFrom,
                                                                   int              maximumResults) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException;


    /**
     * Return the current list of data fields for this discovery run.
     *
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of data fields (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    public abstract List<DataField>  getNewDataFieldsForAsset(int       startingFrom,
                                                              int       maximumResults) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Return any nested data fields attached to this data field.
     *
     * @param parentDataFieldGUID parent data field identifier
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of DataField objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public abstract List<DataField>  getNestedDataFields(String   parentDataFieldGUID,
                                                         int      startingFrom,
                                                         int      maximumResults) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Return any peer data fields attached to this data field.
     *
     * @param dataFieldGUID starting data field identifier
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of DataField objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public abstract List<RelatedDataField>  getLinkedDataFields(String   dataFieldGUID,
                                                                int      startingFrom,
                                                                int      maximumResults) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Return a specific data field stored in the annotation store (previous or new).
     *
     * @param dataFieldGUID unique identifier of the data field
     * @return data field object
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving the data field from the annotation store.
     */
    public abstract DataField  getDataField(String    dataFieldGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;

    /**
     * Add a new data field to the Annotation store linked off of an annotation (typically SchemaAnalysisAnnotation).
     *
     * @param annotationGUID unique identifier of the annotation that the data field is to be linked to
     * @param dataField dataField object
     * @return unique identifier of new data field
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem adding the data field to the Annotation store.
     */
    public abstract String  addDataFieldToDiscoveryReport(String    annotationGUID,
                                                          DataField dataField) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Add a new data field and link it to an existing data field.
     *
     * @param parentDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param dataField data field object
     * @return unique identifier of new data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    public abstract String addDataFieldToDataField(String    parentDataFieldGUID,
                                                   DataField dataField) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Link two exising data fields together in a peer relationship.
     *
     * @param linkFromDataFieldGUID unique identifier of the data field that is at end 1 of the relationship
     * @param relationshipProperties optional properties for the relationship
     * @param linkToDataFieldGUID unique identifier of the data field that is at end 1 of the relationship
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    public abstract void linkDataFields(String        linkFromDataFieldGUID,
                                        DataFieldLink relationshipProperties,
                                        String        linkToDataFieldGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;

    /**
     * Add a new annotation and link it to an existing data field.
     *
     * @param parentDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param annotation data field object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    public abstract String  addAnnotationToDataField(String     parentDataFieldGUID,
                                                     Annotation annotation) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


    /**
     * Replace the current properties of a data field.
     *
     * @param dataField new properties
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the data field in the annotation store.
     */
    public abstract void  updateDataField(DataField dataField) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Remove a data field from the annotation store.
     *
     * @param dataFieldGUID unique identifier of the data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the data field from the annotation store.
     */
    public abstract void  deleteDataField(String   dataFieldGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;
}
