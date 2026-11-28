/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


/**
 * Checks that the path name of a file folder is converted to the canonical path name that the folder cataloguer
 * records for the folder's files, whichever way the folder was named when it was catalogued.
 */
public class CanonicalPathNameTest
{
    private static final String LOCAL_MOUNT_POINT     = ".";
    private static final String CANONICAL_MOUNT_POINT = "/deployments";


    @Test
    public void testAbsoluteLocalPathName() throws IOException
    {
        String localPathName = new File("build/landing-area").getCanonicalPath();

        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName(localPathName, LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                     "/deployments/build/landing-area");
    }


    @Test
    public void testRelativeLocalPathName() throws IOException
    {
        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName("build/landing-area", LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                     "/deployments/build/landing-area");
        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName("./build/../build/landing-area", LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                     "/deployments/build/landing-area");
    }


    @Test
    public void testFileURLPathName() throws IOException
    {
        String localPathName = new File("build/landing-area").getCanonicalPath();

        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName("file://" + localPathName, LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                     "/deployments/build/landing-area");
    }


    @Test
    public void testCanonicalPathNameIsUnchanged() throws IOException
    {
        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName("/deployments/build/landing-area", LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                     "/deployments/build/landing-area");
        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName("/deployments", LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                     "/deployments");
    }


    @Test
    public void testConfiguredMountPoints() throws IOException
    {
        String localMountPoint = new File("build").getCanonicalPath();

        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName(localMountPoint + "/landing-area", localMountPoint, "/shared"),
                     "/shared/landing-area");
    }


    @Test
    public void testPathNameOutsideLocalMountPoint() throws IOException
    {
        String localMountPoint = new File("build").getCanonicalPath();
        String otherPathName   = new File("src/landing-area").getCanonicalPath();

        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName(otherPathName, localMountPoint, "/shared"),
                     otherPathName);
    }


    @Test
    public void testNoPathName() throws IOException
    {
        assertNull(DataSharingHubManagerTargetProcessor.getCanonicalPathName(null, LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT));
        assertNull(DataSharingHubManagerTargetProcessor.getCanonicalPathName("  ", LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT));
    }


    @Test
    public void testNoMountPoints() throws IOException
    {
        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName("build/landing-area", null, CANONICAL_MOUNT_POINT),
                     "build/landing-area");
        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName("build/landing-area", LOCAL_MOUNT_POINT, null),
                     "build/landing-area");
    }


    @Test
    public void testLocalPathName() throws IOException
    {
        String localPathName = new File("build/landing-area").getCanonicalPath();

        assertEquals(DataSharingHubManagerTargetProcessor.getLocalPathName("/deployments/build/landing-area", LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                     localPathName);
        assertEquals(DataSharingHubManagerTargetProcessor.getLocalPathName("/deployments", LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                     new File(LOCAL_MOUNT_POINT).getCanonicalPath());
        assertEquals(DataSharingHubManagerTargetProcessor.getCanonicalPathName(DataSharingHubManagerTargetProcessor.getLocalPathName("/deployments/build/landing-area", LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                                                                               LOCAL_MOUNT_POINT,
                                                                               CANONICAL_MOUNT_POINT),
                     "/deployments/build/landing-area");
    }


    @Test
    public void testLocalPathNameOutsideCanonicalMountPoint() throws IOException
    {
        assertEquals(DataSharingHubManagerTargetProcessor.getLocalPathName("/deployments-old/landing-area", LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT),
                     "/deployments-old/landing-area");
        assertEquals(DataSharingHubManagerTargetProcessor.getLocalPathName("/deployments/landing-area", null, CANONICAL_MOUNT_POINT),
                     "/deployments/landing-area");
        assertNull(DataSharingHubManagerTargetProcessor.getLocalPathName(null, LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT));
    }


    @Test
    public void testDuplicateGroups()
    {
        Map<String, List<String>> duplicateGroups = DataSharingHubManagerTargetProcessor.getDuplicateGroups(List.of("guid-5", "guid-2", "guid-4", "guid-3", "guid-1", "guid-2"),
                                                                                                           List.of("/deployments/a.csv", "/deployments/b.csv", "/deployments/a.csv", "/deployments/c.csv", "/deployments/a.csv", "/deployments/b.csv"));

        assertEquals(duplicateGroups.size(), 1);
        assertEquals(duplicateGroups.get("/deployments/a.csv"), List.of("guid-1", "guid-4", "guid-5"));
    }


    @Test
    public void testNoDuplicateGroups()
    {
        assertTrue(DataSharingHubManagerTargetProcessor.getDuplicateGroups(List.of("guid-1", "guid-2"), List.of("/deployments/a.csv", "/deployments/b.csv")).isEmpty());
        assertTrue(DataSharingHubManagerTargetProcessor.getDuplicateGroups(new ArrayList<>(), new ArrayList<>()).isEmpty());
    }


    @Test
    public void testMountPointsFromDefaults()
    {
        DataSharingHubManagerTargetProcessor.MountPoints defaults = new DataSharingHubManagerTargetProcessor.MountPoints(LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT);

        assertEquals(DataSharingHubManagerTargetProcessor.resolveMountPoints(defaults, null, false, null, null), defaults);
        assertEquals(DataSharingHubManagerTargetProcessor.resolveMountPoints(defaults, Map.of("waitForDirectory", "true"), false, null, null), defaults);
    }


    @Test
    public void testMountPointsFromCatalogTarget()
    {
        DataSharingHubManagerTargetProcessor.MountPoints defaults = new DataSharingHubManagerTargetProcessor.MountPoints(LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT);

        assertEquals(DataSharingHubManagerTargetProcessor.resolveMountPoints(defaults, Map.of("canonicalMountPoint", "/shared"), false, null, null),
                     new DataSharingHubManagerTargetProcessor.MountPoints(LOCAL_MOUNT_POINT, "/shared"));
        assertEquals(DataSharingHubManagerTargetProcessor.resolveMountPoints(defaults, Map.of("localMountPoint", "/mnt/data", "canonicalMountPoint", "/shared"), false, null, null),
                     new DataSharingHubManagerTargetProcessor.MountPoints("/mnt/data", "/shared"));
    }


    @Test
    public void testMountPointsFromFileSystem()
    {
        DataSharingHubManagerTargetProcessor.MountPoints defaults = new DataSharingHubManagerTargetProcessor.MountPoints(LOCAL_MOUNT_POINT, CANONICAL_MOUNT_POINT);

        assertEquals(DataSharingHubManagerTargetProcessor.resolveMountPoints(defaults, Map.of("canonicalMountPoint", "/shared"), true, "/mnt/nas", "/nas"),
                     new DataSharingHubManagerTargetProcessor.MountPoints("/mnt/nas", "/nas"));

        /*
         * A file system that does not set its mount points turns the conversion off, as it does for the cataloguer.
         */
        assertEquals(DataSharingHubManagerTargetProcessor.resolveMountPoints(defaults, null, true, null, null),
                     new DataSharingHubManagerTargetProcessor.MountPoints(null, null));
    }


    private static DataSharingHubManagerTargetProcessor.FileSystemLink link(String fileSystemGUID, boolean uses)
    {
        return new DataSharingHubManagerTargetProcessor.FileSystemLink(fileSystemGUID, uses, "/mnt/" + fileSystemGUID, "/" + fileSystemGUID);
    }


    @Test
    public void testOneFileSystem()
    {
        assertEquals(DataSharingHubManagerTargetProcessor.chooseFileSystem(List.of(link("fs-1", false))).fileSystemGUID(), "fs-1");
        assertEquals(DataSharingHubManagerTargetProcessor.chooseFileSystem(List.of(link("fs-1", true))).fileSystemGUID(), "fs-1");
        assertEquals(DataSharingHubManagerTargetProcessor.chooseFileSystem(List.of(link("fs-1", true), link("fs-1", false), link("fs-1", true))).fileSystemGUID(), "fs-1");
    }


    @Test
    public void testFileSystemsThatOnlyUseTheFolderAreSetAside()
    {
        DataSharingHubManagerTargetProcessor.FileSystemLink chosen = DataSharingHubManagerTargetProcessor.chooseFileSystem(List.of(link("fs-1", true), link("fs-2", false), link("fs-3", true)));

        assertEquals(chosen.fileSystemGUID(), "fs-2");
        assertEquals(chosen.canonicalMountPoint(), "/fs-2");

        /*
         * A file system that is linked as an owner as well as a user is not one that only uses the folder.
         */
        assertNull(DataSharingHubManagerTargetProcessor.chooseFileSystem(List.of(link("fs-1", true), link("fs-1", false), link("fs-2", false))));
    }


    @Test
    public void testAmbiguousFileSystems()
    {
        assertNull(DataSharingHubManagerTargetProcessor.chooseFileSystem(List.of(link("fs-1", false), link("fs-2", false))));
        assertNull(DataSharingHubManagerTargetProcessor.chooseFileSystem(List.of(link("fs-1", true), link("fs-2", true))));
        assertNull(DataSharingHubManagerTargetProcessor.chooseFileSystem(List.of(link("fs-1", true), link("fs-2", false), link("fs-3", false))));
    }
}
