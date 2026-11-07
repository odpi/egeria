/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.display;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DisplayDataContainerProperties represents a nested grouping of fields within a report, form or query.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DisplayDataContainerProperties extends SchemaAttributeProperties
{
    private String renderer          = null;
    private String placementKind     = null;
    private String span              = null;
    private String presentationStyle = null;
    private String markdownContent   = null;


    /**
     * Default constructor
     */
    public DisplayDataContainerProperties()
    {
        super();
        super.typeName = OpenMetadataType.DISPLAY_DATA_CONTAINER.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DisplayDataContainerProperties(DisplayDataContainerProperties template)
    {
        super(template);

        if (template != null)
        {
            renderer          = template.getRenderer();
            placementKind     = template.getPlacementKind();
            span              = template.getSpan();
            presentationStyle = template.getPresentationStyle();
            markdownContent   = template.getMarkdownContent();
        }
    }


    /**
     * Return the type of renderer used to display this container.
     *
     * @return string
     */
    public String getRenderer()
    {
        return renderer;
    }


    /**
     * Set up the type of renderer used to display this container.
     *
     * @param renderer string
     */
    public void setRenderer(String renderer)
    {
        this.renderer = renderer;
    }


    /**
     * Return how the element is placed in the display.
     *
     * @return string
     */
    public String getPlacementKind()
    {
        return placementKind;
    }


    /**
     * Set up how the element is placed in the display.
     *
     * @param placementKind string
     */
    public void setPlacementKind(String placementKind)
    {
        this.placementKind = placementKind;
    }


    /**
     * Return the number of columns/cells the element spans.
     *
     * @return string
     */
    public String getSpan()
    {
        return span;
    }


    /**
     * Set up the number of columns/cells the element spans.
     *
     * @param span string
     */
    public void setSpan(String span)
    {
        this.span = span;
    }


    /**
     * Return the style of presentation used to display this element.
     *
     * @return string
     */
    public String getPresentationStyle()
    {
        return presentationStyle;
    }


    /**
     * Set up the style of presentation used to display this element.
     *
     * @param presentationStyle string
     */
    public void setPresentationStyle(String presentationStyle)
    {
        this.presentationStyle = presentationStyle;
    }


    /**
     * Return the Markdown content of the element.
     *
     * @return string
     */
    public String getMarkdownContent()
    {
        return markdownContent;
    }


    /**
     * Set up the Markdown content of the element.
     *
     * @param markdownContent string
     */
    public void setMarkdownContent(String markdownContent)
    {
        this.markdownContent = markdownContent;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DisplayDataContainerProperties{" +
                "renderer='" + renderer + '\'' +
                ", placementKind='" + placementKind + '\'' +
                ", span='" + span + '\'' +
                ", presentationStyle='" + presentationStyle + '\'' +
                ", markdownContent='" + markdownContent + '\'' +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        DisplayDataContainerProperties that = (DisplayDataContainerProperties) objectToCompare;
        return Objects.equals(renderer, that.renderer) &&
                Objects.equals(placementKind, that.placementKind) &&
                Objects.equals(span, that.span) &&
                Objects.equals(presentationStyle, that.presentationStyle) &&
                Objects.equals(markdownContent, that.markdownContent);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), renderer, placementKind, span, presentationStyle, markdownContent);
    }
}
