/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * BitolMappingResult describes the outcome of cataloguing a Bitol document into open metadata: the unique identifier
 * of the element that represents the document, whether it was created, updated or removed, and any parts of the
 * document that could not be fully represented (for example a team member whose user identity is not known to Egeria,
 * or a data contract that is referenced but not yet catalogued).
 */
public class BitolMappingResult
{
    /**
     * The action taken on the element that represents the document.
     */
    public enum Action
    {
        /**
         * The element was created.
         */
        CREATED,

        /**
         * An existing element was updated.
         */
        UPDATED,

        /**
         * The document is retired and the element was deleted.
         */
        DELETED,

        /**
         * The document could not be catalogued (see the warnings).
         */
        SKIPPED
    }

    private final String       kind;
    private final String       documentId;
    private final String       documentVersion;
    private final String       qualifiedName;
    private       String       elementGUID = null;
    private       Action       action      = Action.SKIPPED;
    private final List<String> warnings    = new ArrayList<>();


    /**
     * Constructor.
     *
     * @param kind kind of document
     * @param documentId identifier from the document
     * @param documentVersion version from the document
     * @param qualifiedName qualified name used for the element that represents the document
     */
    public BitolMappingResult(String kind,
                              String documentId,
                              String documentVersion,
                              String qualifiedName)
    {
        this.kind            = kind;
        this.documentId      = documentId;
        this.documentVersion = documentVersion;
        this.qualifiedName   = qualifiedName;
    }


    /**
     * Return the kind of document.
     *
     * @return DataContract or DataProduct
     */
    public String getKind()
    {
        return kind;
    }


    /**
     * Return the identifier from the document.
     *
     * @return string
     */
    public String getDocumentId()
    {
        return documentId;
    }


    /**
     * Return the version from the document.
     *
     * @return string
     */
    public String getDocumentVersion()
    {
        return documentVersion;
    }


    /**
     * Return the qualified name used for the element that represents the document.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the unique identifier of the element that represents the document.
     *
     * @return guid or null if the document was skipped
     */
    public String getElementGUID()
    {
        return elementGUID;
    }


    /**
     * Set up the unique identifier of the element that represents the document.
     *
     * @param elementGUID guid
     */
    public void setElementGUID(String elementGUID)
    {
        this.elementGUID = elementGUID;
    }


    /**
     * Return the action taken.
     *
     * @return enum
     */
    public Action getAction()
    {
        return action;
    }


    /**
     * Set up the action taken.
     *
     * @param action enum
     */
    public void setAction(Action action)
    {
        this.action = action;
    }


    /**
     * Return the parts of the document that could not be fully represented.
     *
     * @return list of messages (empty if none)
     */
    public List<String> getWarnings()
    {
        return warnings;
    }


    /**
     * Record a part of the document that could not be fully represented.
     *
     * @param warning message
     */
    public void addWarning(String warning)
    {
        warnings.add(warning);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolMappingResult{" +
                "kind='" + kind + '\'' +
                ", documentId='" + documentId + '\'' +
                ", documentVersion='" + documentVersion + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", elementGUID='" + elementGUID + '\'' +
                ", action=" + action +
                ", warnings=" + warnings +
                '}';
    }
}
