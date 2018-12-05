<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# igc-rest-client-library

A Java client library for connecting to IBM Information Governance Catalog's (IGC's) REST API.

## Requirements

- An IBM Information Server (including IGC) environment
- User credentials to access the REST API of that environment

## Usage

### Connecting

The `IGCRestClient` class provides the entry point to creating a connection to IGC:

```java
import IGCRestClient;

String basicAuth = IGCRestClient.encodeBasicAuth("isadmin", "password");
igcrest = new IGCRestClient("https://myenv.myhost.com:9446", basicAuth);
```

You can either directly provide your own Basic-encoded authentication information, or pass a username and password to the provided static utility function to create this string for you (as in the example above).

Creating an IGCRestClient object will connect to the environment and retrieve basic information, such as whether the workflow is enabled or not in the environment, as well as opening and retaining the cookies for a session.

To cleanly disconnect, simply call the `disconnect()` method on the client:

### Disconnecting

```java
igcrest.disconnect();
```

This will close the active session and logout from the REST API.

### Retrieving assets

Assets can be retrieved using either an Object-based (POJO-based) approach or a generic JSON data manipulation approach.

For the latter, methods will generally return a `JsonNode` object, which can then be traversed using the appropriate Jackson methods.

For the former, you must first register the classes you expect to handle using:

```java
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115.*;

igcrest.registerPOJO(new NamedType(Term.class, "term"));
```

> In this example, we register that the IGC `term` data type should be handled by the (generated) `Term.class` POJO, for v11.5

This registration of POJOs must occur before any usage of the other methods to actually retrieve and process metadata.

Once the type is registered, metadata assets of that type can then be retrieved using functions that generally return `Reference` objects; and these `Reference` objects can be explicitly cast to their more descriptive type (eg. `(Term)` in our example), as needed.

Note that if an asset type is not registered, the minimalistic amount of data for those assets will still be retrieved (as a `Reference` object); you simply won't have access to all of such an assets other properties without first registering a POJO to handle it or using the generic JSON approach described above.

```java
Term term = (Term)igcrest.getAssetById(bigTermRid);
System.out.println("Term '" + term.getName() + " (" + term.getShortDescription() + ")' has the following assigned assets: " + term.getAssignedAssets());
```

You can also dynamically retrieve properties (ie. using a variable as the name of the property) by using the `getPropertyByName` method available on all POJOs:

```java
Reference something = igcrest.getAssetById(rid);
String propertyNameFromSomewhere = "short_description";
if (something.isSimpleType(propertyNameFromSomewhere)) {
    Object value = something.getPropertyByName(propertyNameFromSomewhere);
    System.out.println("Simple property '" + propertyNameFromSomewhere + "' = " + value);
} else if (something.isReference(propertyNameFromSomewhere)) {
    Reference value = (Reference) something.getPropertyByName(propertyNameFromSomewhere);
    System.out.println("Property was a relationship to: " + value.getId());
} else if (something.isReferenceList(propertyNameFromSomewhere)) {
    ReferenceList value = (ReferenceList) something.getPropertyByName(propertyNameFromSomewhere);
    System.out.println("Property was a list of " + value.getPaging().getNumTotal() + " total relationships.");
}
```

If the property does not exist, you'll simply receive back a `null` (and a stacktrace will be dumped in the background).
(So you'd want to add null handling to the above simplified example.)

### Searching for assets

Additional classes have been provided to help simplify searching against IGC as well.

```java
IGCSearchCondition cond1 = new IGCSearchCondition("name", "=", "Street Number");
IGCSearchCondition cond2 = new IGCSearchCondition("name", "=", "City");
IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(cond1);
igcSearchConditionSet.addCondition(cond2);
igcSearchConditionSet.setMatchAnyCondition(true);
IGCSearch igcSearch = new IGCSearch("term", igcSearchConditionSet);
ReferenceList searchResult = igcrest.search(igcSearch);
```

> In this example, a search retrieves any terms whose name is either `Street Number` or `City`

## Included asset types

The client includes POJOs for all asset types (with their properties as class members) that are understood by a vanilla IGC environment. The vast majority of these are code-generated, and included in the package `org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115` for IGC v11.5, and `org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117` for IGC v11.7.

The following POJOs define common characteristics across objects for ease of re-use and generic representation, but are not themselves asset types:

- `Reference` defines the most minimalistic representation of an IGC asset, and is used anywhere an asset is referenced (eg. for relationships). It is the superclass of all asset types.
- `ReferenceList` encapsulates a set of relationships (`Reference` objects) and their paging characteristics (`Paging` object).
- `Paging` encapsulates the details of a page of results (eg. the total number, the URL to the next page of results, etc).
- `MainObject` provides an interim-level superclass for most asset types, including properties common to virtually all assets (eg. `short_description`, `long_description`, etc). While this class itself extends `Reference`, most POJOs (ie. all generated ones) extend this class rather than `Reference` directly.
- `Identity` provides a semantically-meaningful characteristic that can be used for comparison between assets for equality, without relying on ID-level (RID) equivalency.

Finally, one non-generated asset type is defined (the only one that does not extend from `MainObject`):

- `Label` defines the IGC `label` asset type

## Using your own asset types

If your environment includes new objects (ie. via OpenIGC) or adds custom attributes to the native IGC asset types, you will likely want to make use of your own asset types.

The recommended way to do this is to create your own POJOs:

- For OpenIGC assets, create a new class that extends from `MainObject` for each class in your OpenIGC bundle, and add each of your classes' properties as members to each of those POJOs. (See the generated POJOs for examples.)
- For native asset types against which you've defined custom attributes, simply create a new POJO that extends from the appropriate (generated) POJO. Then all you'll need to do is add the custom attribute properties to your new class (all of the native properties will be inherited by extending the generated POJO). For any custom attributes of IGC type `relationship` use a Java type of `ReferenceList`, for any multi-valued custom attributes use a Java type of `ArrayList<String>`, and for any singular values simply use the appropriate type (eg. `String`, `Number`, `Date`, or `Boolean`).
- In either case, to ease reflection-based registration (if you decide to use such an approach), consider adding a `static final String IGC_TYPE_ID = ''` set to the precise type string that IGC uses to refer to assets of this type.

Remember that you'll need to register your own POJO (see "Retrieving assets" above) before the client will make use of it!
