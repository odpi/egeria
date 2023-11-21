/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAnnotationStore;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.discovery.properties.DataField;
import org.odpi.openmetadata.frameworks.discovery.properties.DataFieldLink;
import org.odpi.openmetadata.frameworks.discovery.properties.RelatedDataField;

import java.util.List;
import java.util.Map;

/**
 * DiscoveryAnnotationStoreClient provides a client-side implementation of the ODF DiscoveryAnnotationStore
 * that is backed by calls to the Discovery Engine OMAS.
 * An instance of this client is created for each discovery service instance that runs.  This is
 * why the REST client is passed in on the constructor (since creating a new RestTemplate object is
 * very expensive).
 */
public class DiscoveryAnnotationStoreClient extends DiscoveryAnnotationStore
{
    private final DiscoveryEngineClient discoveryEngineClient;    /* Initialized in constructor */


    /**
     * Constructor sets up the key parameters for accessing the annotations store.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param discoveryAnalysisReportClient discovery report that is linked to the annotations.
     * @param discoveryEngineClient client for calling REST APIs
     */
    public DiscoveryAnnotationStoreClient(String                userId,
                                          String                assetGUID,
                                          DiscoveryAnalysisReportClient discoveryAnalysisReportClient,
                                          DiscoveryEngineClient discoveryEngineClient)
    {
        super(userId, assetGUID, discoveryAnalysisReportClient);

        this.discoveryEngineClient = discoveryEngineClient;
    }


    /**
     * Return the annotation subtype names.
     *
     * @return list of type names that are subtypes of annotation
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String>  getTypesOfAnnotation() throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        return discoveryEngineClient.getTypesOfAnnotation(userId);
    }


    /**
     * Return the annotation subtype names mapped to their descriptions.
     *
     * @return map of type names that are subtypes of annotation to their descriptions
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public Map<String, String> getTypesOfAnnotationWithDescriptions() throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return discoveryEngineClient.getTypesOfAnnotationWithDescriptions(userId);
    }


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
    @Override
    public  List<Annotation> getPreviousAnnotationsForAsset(int       startingFrom,
                                                            int       maximumResults) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return discoveryEngineClient.getAnnotationsForAssetByStatus(userId, assetGUID, null, startingFrom, maximumResults);
    }


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
    @Override
    public  List<Annotation>  getPreviousAnnotationsForAsset(AnnotationStatus status,
                                                             int              startingFrom,
                                                             int              maximumResults) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return discoveryEngineClient.getAnnotationsForAssetByStatus(userId, assetGUID, status, startingFrom, maximumResults);
    }


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
    @Override
    public  List<Annotation>  getNewAnnotationsForAsset(int startingFrom,
                                                        int maximumResults) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String   methodName = "getNewAnnotationsForAsset";

        return discoveryEngineClient.getDiscoveryReportAnnotations(userId, discoveryReport.getDiscoveryReportGUID(), startingFrom, maximumResults, methodName);
    }


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
    @Override
    public  List<Annotation>  getExtendedAnnotations(String   annotationGUID,
                                                     int      startingFrom,
                                                     int      maximumResults) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return discoveryEngineClient.getExtendedAnnotations(userId, annotationGUID, startingFrom, maximumResults);
    }


    /**
     * Return a specific annotation stored in the annotation store (previous or new).
     *
     * @param annotationGUID unique identifier of the annotation
     * @return annotation object
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving the annotation from the annotation store.
     */
    @Override
    public  Annotation  getAnnotation(String    annotationGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return discoveryEngineClient.getAnnotation(userId, annotationGUID);
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param annotation annotation object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException the annotation is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem adding the annotation to the annotation store.
     */
    @Override
    public  String  addAnnotationToDiscoveryReport(Annotation annotation) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return discoveryEngineClient.addAnnotationToDiscoveryReport(userId, discoveryReport.getDiscoveryReportGUID(), annotation);
    }


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
    @Override
    public String addAnnotationToAnnotation(String     parentAnnotationGUID,
                                            Annotation annotation) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return discoveryEngineClient.addAnnotationToAnnotation(userId, parentAnnotationGUID, annotation);
    }


    /**
     * Replace the current properties of an annotation.
     *
     * @param annotation new properties
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    @Override
    public  void  updateAnnotation(Annotation annotation) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        discoveryEngineClient.updateAnnotation(userId, annotation);
    }


    /**
     * Remove an annotation from the annotation store.
     *
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    @Override
    public  void  deleteAnnotation(String annotationGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        discoveryEngineClient.deleteAnnotation(userId, annotationGUID);
    }


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
    @Override
    public  List<DataField>  getPreviousDataFieldsForAsset(int startingFrom,
                                                           int maximumResults) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return discoveryEngineClient.getPreviousDataFieldsForAsset(userId, discoveryReport.getDiscoveryReportGUID(), startingFrom, maximumResults);
    }


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
    @Override
    public  List<DataField>  getNewDataFieldsForAsset(int       startingFrom,
                                                      int       maximumResults) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return discoveryEngineClient.getNewDataFieldsForAsset(userId, discoveryReport.getDiscoveryReportGUID(), startingFrom, maximumResults);
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param parentDataFieldGUID parent data field identifier
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of data fields that can be returned.
     *
     * @return list of DataField objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    @Override
    public  List<DataField>  getNestedDataFields(String   parentDataFieldGUID,
                                                 int      startingFrom,
                                                 int      maximumResults) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return discoveryEngineClient.getNestedDataFields(userId, parentDataFieldGUID, startingFrom, maximumResults);
    }


    /**
     * Return any peer data fields attached to this data field.
     *
     * @param dataFieldGUID starting data field identifier
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of data fields that can be returned.
     *
     * @return list of DataField objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public  List<RelatedDataField>  getLinkedDataFields(String   dataFieldGUID,
                                                        int      startingFrom,
                                                        int      maximumResults) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return discoveryEngineClient.getLinkedDataFields(userId, dataFieldGUID, startingFrom, maximumResults);
    }


    /**
     * Return a specific data field stored in the annotation store (previous or new).
     *
     * @param dataFieldGUID unique identifier of the data field
     * @return data field object
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving the data field from the annotation store.
     */
    @Override
    public  DataField  getDataField(String    dataFieldGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return discoveryEngineClient.getDataField(userId, dataFieldGUID);
    }


    /**
     * Add a new data field to the Annotation store linked off of an annotation (typically SchemaAnalysisAnnotation).
     *
     * @param annotationGUID unique identifier of the annotation that the data field is to be linked to
     * @param dataField dataField object
     * @return unique identifier of new dataField
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    @Override
    public  String  addDataFieldToDiscoveryReport(String    annotationGUID,
                                                  DataField dataField) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return discoveryEngineClient.addDataFieldToDiscoveryReport(userId, annotationGUID, dataField);
    }


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
    @Override
    public  String  addDataFieldToDataField(String    parentDataFieldGUID,
                                            DataField dataField) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return discoveryEngineClient.addDataFieldToDataField(userId, parentDataFieldGUID, dataField);
    }


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
    @Override
    public void linkDataFields(String        linkFromDataFieldGUID,
                               DataFieldLink relationshipProperties,
                               String        linkToDataFieldGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        discoveryEngineClient.linkDataFields(userId, linkFromDataFieldGUID, relationshipProperties, linkToDataFieldGUID);
    }


    /**
     * Add a new annotation and link it to an existing data field.
     *
     * @param parentDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param annotation data field object
     * @return unique identifier of annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    @Override
    public  String  addAnnotationToDataField(String     parentDataFieldGUID,
                                             Annotation annotation) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return discoveryEngineClient.addAnnotationToDataField(userId, parentDataFieldGUID, annotation);

    }


    /**
     * Replace the current properties of a data field.
     *
     * @param dataField new properties
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the data field in the annotation store.
     */
    @Override
    public void updateDataField(DataField dataField) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        discoveryEngineClient.updateDataField(userId, dataField);
    }


    /**
     * Remove a data field from the annotation store.
     *
     * @param dataFieldGUID unique identifier of the data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the data field from the annotation store.
     */
    @Override
    public void deleteDataField(String   dataFieldGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        discoveryEngineClient.deleteDataField(userId, dataFieldGUID);
    }
}
