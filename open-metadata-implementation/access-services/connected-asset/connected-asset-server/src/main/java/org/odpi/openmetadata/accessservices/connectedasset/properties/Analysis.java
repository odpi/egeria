/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Analysis returns the Annotations for the connected asset.
 * Annotations are created by Open Discovery Framework (ODF) discovery services.  Each Annotation
 * contains the results of a particular type of analysis.
 *
 * The Analysis class holds a full list of all of the Annotation and offers methods for retrieving
 * different subsets of the Annotations.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Analysis extends PropertyBase
{
    private List<Annotation> allAnnotations = null;


    /**
     * Default Constructor
     */
    public Analysis()
    {
        super();
    }


    /**
     * Copy/clone constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the analysis clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateAnalysis - template for setting up the properties.
     */
    public Analysis(Analysis  templateAnalysis)
    {
        super(templateAnalysis);

        /*
         * Only create a child object if the template is not null.
         */
        if (templateAnalysis != null)
        {
            List<Annotation> templateAllAnnotations = templateAnalysis.getAnnotations();

            if (templateAllAnnotations != null)
            {
                /*
                 * Copy over the annotations ensuring the parent asset is this object's parent, not the template's parent.
                 */
                allAnnotations = new ArrayList<>(templateAnalysis.getAnnotations());
            }
        }
    }


    /**
     * Return an iterator containing all of the annotations for this asset.
     *
     * @return Annotations - list of annotations
     */
    public List<Annotation> getAnnotations()
    {
        return new ArrayList<>(allAnnotations);
    }


    /**
     * Initialize Analysis with a new set of annotations.  This overrides any annotations previously held
     * by Analysis.
     *
     * @param newAnnotations - new annotations
     */
    public void setAnnotations(List<Annotation>   newAnnotations)
    {
        /*
         * A copy of the annotations is taken to be sure the pointers are all at the start.
         */
        allAnnotations = new ArrayList<>(newAnnotations);
    }
}