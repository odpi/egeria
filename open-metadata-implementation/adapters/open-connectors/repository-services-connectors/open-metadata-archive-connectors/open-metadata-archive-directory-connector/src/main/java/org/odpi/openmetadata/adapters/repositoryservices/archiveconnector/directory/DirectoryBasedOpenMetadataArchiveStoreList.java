/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.RepositoryElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.CollectionDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DirectoryBasedOpenMetadataArchiveStoreList<T extends RepositoryElementHeader> implements List<T>
{
    private Class<T>                               elementClass;
    private DirectoryBasedOpenMetadataArchiveStore archiveStore;
    private List<String>                           directoryNames;
    private AuditLog                               auditLog;


    DirectoryBasedOpenMetadataArchiveStoreList(Class<T>                               elementClass,
                                               DirectoryBasedOpenMetadataArchiveStore archiveStore,
                                               List<String>                           directoryNames,
                                               AuditLog                               auditLog)
    {
        this.elementClass = elementClass;
        this.archiveStore = archiveStore;
        this.directoryNames = directoryNames;
        this.auditLog = auditLog;
    }


    @Override
    public int size()
    {
        int size = 0;

        for (String directoryName : directoryNames)
        {
            File file = new File(directoryName);

            File[] files = file.listFiles();

            if (files != null)
            {
                size = size + files.length;
            }
        }

        return size;
    }


    @Override
    public boolean isEmpty()
    {
        return (this.size() == 0);
    }


    @Override
    public boolean contains(Object objectToCompare)
    {
        final String methodName = "compare";

        if (objectToCompare == null)
        {
            return false;
        }

        if (elementClass.isInstance(objectToCompare))
        {
            if (objectToCompare instanceof PrimitiveDef)
            {
                PrimitiveDef typeToCompare = (PrimitiveDef)objectToCompare;
                PrimitiveDef retrievedElement = (PrimitiveDef)archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                                       typeToCompare.getVersion(),
                                                                                       methodName);

                return (retrievedElement != null);
            }
            else if (objectToCompare instanceof CollectionDef)
            {
                CollectionDef typeToCompare = (CollectionDef)objectToCompare;
                CollectionDef retrievedElement = (CollectionDef)archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                                         typeToCompare.getVersion(),
                                                                                         methodName);

                return (retrievedElement != null);
            }
            else if (objectToCompare instanceof EnumDef)
            {
                EnumDef typeToCompare = (EnumDef)objectToCompare;
                EnumDef retrievedElement = (EnumDef)archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                             typeToCompare.getVersion(),
                                                                             methodName);

                return (retrievedElement != null);
            }
            else if (objectToCompare instanceof TypeDefPatch)
            {
                TypeDefPatch typeToCompare = (TypeDefPatch)objectToCompare;
                TypeDefPatch retrievedElement = (TypeDefPatch)archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                                       typeToCompare.getApplyToVersion(),
                                                                                       methodName);

                return (retrievedElement != null);
            }
            else if (objectToCompare instanceof EntityDef)
            {
                EntityDef typeToCompare = (EntityDef)objectToCompare;
                EntityDef retrievedElement = (EntityDef)archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                                 typeToCompare.getVersion(),
                                                                                 methodName);

                return (retrievedElement != null);
            }
            else if (objectToCompare instanceof RelationshipDef)
            {
                RelationshipDef typeToCompare = (RelationshipDef)objectToCompare;
                RelationshipDef retrievedElement = (RelationshipDef)archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                                             typeToCompare.getVersion(),
                                                                                             methodName);

                return (retrievedElement != null);
            }
            else if (objectToCompare instanceof ClassificationDef)
            {
                ClassificationDef typeToCompare = (ClassificationDef) objectToCompare;
                ClassificationDef retrievedElement = (ClassificationDef) archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                                                  typeToCompare.getVersion(),
                                                                                                  methodName);

                return (retrievedElement != null);
            }
            else if (objectToCompare instanceof EntityDetail)
            {
                EntityDetail typeToCompare = (EntityDetail) objectToCompare;
                EntityDetail retrievedElement = (EntityDetail) archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                                        typeToCompare.getVersion(),
                                                                                        methodName);

                return (retrievedElement != null);
            }
            else if (objectToCompare instanceof Relationship)
            {
                Relationship typeToCompare = (Relationship) objectToCompare;
                Relationship retrievedElement = (Relationship) archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                                        typeToCompare.getVersion(),
                                                                                        methodName);

                return (retrievedElement != null);
            }
            else if (objectToCompare instanceof ClassificationEntityExtension)
            {
                ClassificationEntityExtension typeToCompare = (ClassificationEntityExtension) objectToCompare;
                ClassificationEntityExtension retrievedElement = (ClassificationEntityExtension) archiveStore.readElement(archiveStore.getFileName(typeToCompare),
                                                                                                                          typeToCompare.getClassification().getVersion(),
                                                                                                                          methodName);

                return (retrievedElement != null);
            }
        }

        return false;
    }


    @Override
    public Iterator<T> iterator()
    {
        List<File> fileList = new ArrayList<>();

        for (String directoryName : directoryNames)
        {
            File file = new File(directoryName);

            File[] files = file.listFiles();

            if (files != null)
            {
                fileList.addAll(Arrays.asList(files));
            }
        }

        if (fileList.isEmpty())
        {
            fileList = null;
        }

        return new DirectoryBasedOpenMetadataArchiveStoreIterator<T>(archiveStore, fileList, auditLog);
    }


    @Override
    public Object[] toArray()
    {
        return new Object[0];
    }


    @Override
    public <T1> T1[] toArray(T1[] a)
    {
        return null;
    }


    @Override
    public boolean add(T t)
    {
        return false;
    }


    @Override
    public boolean remove(Object o)
    {
        return false;
    }


    @Override
    public boolean containsAll(Collection<?> c)
    {
        return false;
    }


    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        return false;
    }


    @Override
    public boolean addAll(int index, Collection<? extends T> c)
    {
        return false;
    }


    @Override
    public boolean removeAll(Collection<?> c)
    {
        return false;
    }


    @Override
    public boolean retainAll(Collection<?> c)
    {
        return false;
    }


    @Override
    public void clear()
    {

    }


    @Override
    public T get(int index)
    {
        return null;
    }


    @Override
    public T set(int index, T element)
    {
        return null;
    }


    @Override
    public void add(int index, T element)
    {

    }


    @Override
    public T remove(int index)
    {
        return null;
    }


    @Override
    public int indexOf(Object o)
    {
        return 0;
    }


    @Override
    public int lastIndexOf(Object o)
    {
        return 0;
    }


    @Override
    public ListIterator<T> listIterator()
    {
        return null;
    }


    @Override
    public ListIterator<T> listIterator(int index)
    {
        return null;
    }


    @Override
    public List<T> subList(int fromIndex, int toIndex)
    {
        return null;
    }
}
