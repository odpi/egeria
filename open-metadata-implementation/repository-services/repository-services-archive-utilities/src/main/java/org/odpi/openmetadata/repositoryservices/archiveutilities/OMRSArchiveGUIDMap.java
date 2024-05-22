/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.archiveutilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * OMRSArchiveGUIDMap is a utility to create a persisted list of GUIDs used by an archive builder.
 * This helps the archive builder use the same GUIDs each time the archive is built.  GUIDs for
 * new elements are created automatically.
 */
public class OMRSArchiveGUIDMap
{
    private static final Logger log = LoggerFactory.getLogger(OMRSArchiveGUIDMap.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();

    private final String              guidMapFileName;
    private Map<String, String> idToGUIDMap;
    private Map<String, String> usedIdToGUIDMap = new HashMap<>();


    /**
     * Constructor for the GUIDMap.
     *
     * @param guidMapFileName name of the file name where the GUIDs are stashed.
     */
    public OMRSArchiveGUIDMap(String guidMapFileName)
    {
        this.guidMapFileName = guidMapFileName;

        this.loadGUIDs();
    }


    /**
     * Load up the saved GUIds into a new HashMap.
     */
    @SuppressWarnings("unchecked")
    private void loadGUIDs()
    {
        File         idFile = new File(guidMapFileName);

        try
        {
            log.debug("Retrieving Id to GUID Map");

            String idFileContents = FileUtils.readFileToString(idFile, "UTF-8");

            idToGUIDMap = OBJECT_READER.readValue(idFileContents, Map.class);
        }
        catch (Exception   error)
        {
            idToGUIDMap = new HashMap<>();
        }

        log.debug("Id to GUID Map contains: " + idToGUIDMap);
    }


    /**
     * Fix the guid for a particular name.
     *
     * @param id identifier associated with the GUID
     * @param guid fixed guid value
     */
    public  void setGUID(String  id, String  guid)
    {
        idToGUIDMap.put(id, guid);
        usedIdToGUIDMap.put(id, guid);
    }


    /**
     * Retrieve the guid for an element based on its id.
     * If no GUID exists, a new one is created and returned
     *
     * @param id id of element
     * @return guid mapped to id
     */
    public String  getGUID(String id)
    {
        String guid = idToGUIDMap.get(id);

        if (guid == null)
        {
            guid = UUID.randomUUID().toString();
        }

        setGUID(id, guid);

        return guid;
    }


    /**
     * Retrieve the guid for an element based on its id.
     *
     * @param id id of element
     * @return guid mapped to id
     */
    public String  queryGUID(String id)
    {
        String guid = idToGUIDMap.get(id);

        if (guid != null)
        {
            usedIdToGUIDMap.put(id, guid);
        }

        return guid;
    }


    /**
     * Save the map to a file
     */
    public void  saveGUIDs()
    {
        File         idFile = new File(guidMapFileName);

        try
        {
            if (usedIdToGUIDMap.isEmpty())
            {
                log.debug("Deleting id file because map is empty: " + guidMapFileName);

                idFile.delete();
            }
            else
            {
                log.debug("Writing id file " + guidMapFileName);

                String mapContents = OBJECT_WRITER.writeValueAsString(usedIdToGUIDMap);

                FileUtils.writeStringToFile(idFile, mapContents, (String)null,false);
            }
        }
        catch (Exception  exc)
        {
            log.error("Unusable Map Store :(", exc);
        }
    }


    /**
     * Return the size of the map.
     *
     * @return int (zero if the map is null)
     */
    public int getSize()
    {
        if (usedIdToGUIDMap == null)
        {
            return 0;
        }

        return usedIdToGUIDMap.size();
    }
}
