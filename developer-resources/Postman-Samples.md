<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Postman Samples

We tend to use [Postman](https://www.getpostman.com) to test the various API endpoints we develop in
Egeria. As such, there are a number of samples we make available for anyone to use for testing or otherwise
becoming familiar with the Egeria APIs.

Egeria by default uses https:// requests with a self-signed certificate. Any PostMan users therefore will need to
go into settings->general and turn off 'SSL certificate verification' or requests will fail.

When developing a new API in Egeria, you may want to make similar samples available to both provide examples
of using the API as well as for basic testing purposes. These should be developed as follows:

1. Wherever possible, re-use the environment variables that are already defined in
    [Egeria.postman_environment.json](../open-metadata-resources/open-metadata-samples/postman-rest-samples/Egeria.postman_environment.json).
    If you need another variable that is not already defined, add it to this environment definition.
    
    This way we have a single environment definition that covers all possible sample configurations.

1. Create a [Postman Collection](https://learning.getpostman.com/docs/postman/collections/intro-to-collections/)
    that includes REST samples for your API. Name it using the convention
    `Egeria-<area>-<operations>` where `<area>` represents the unique area of your API (for
    example the name of an OMAS) and `<operations>` can optionally be used to distinguish between multiple collections
    that may be useful for different purposes (eg. read vs. write operations). Consider adding
    [test scripts](https://learning.getpostman.com/docs/postman/scripts/test-scripts/)
    to your collection to check expected values, if you intend to use them for testing purposes.
    
    Once ready for sharing, export the collection into a file and commit your collection into GitHub
    wherever is most appropriate for the anticipated users of the samples.

1. Create a descriptive entry in [postman-rest-samples/README.md](../open-metadata-resources/open-metadata-samples/postman-rest-samples/README.md)
    under a sub-section of the "Sample Collections" heading, linking to your new collection within GitHub. Use the
    existing samples defined there for guidance: provide a limited introductory description to any pre-requisites for
    your collection, if it needs to be run after some other collection define these in a sequence, etc.
    
    If your description for use requires more than 1-2 simple sentences, consider linking to more detailed instructions
    rather than putting these all into the general README. (See samples where we link out to more information on loading
    Coco Pharmaceuticals samples rather than embedding all of this detail directly in the one README.)
    
1. Within your descriptive entry, link to your collection. Following the other examples, provide a link to the
    raw file so that the link itself can be copy / pasted into Postman (without needing to download the file and
    then import it).

In this way, anyone wanting access to the REST samples of Egeria has a single place from which to find
them, while those working in a particular area of Egeria can still find the appropriate samples for that area
directly within the area of interest.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
