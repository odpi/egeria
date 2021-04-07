<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Simple helm chart for Kubernetes deployment of Egeria

This is a simple deployment of Egeria which will just deploy a basic Egeria environment
which is ready for you to experiment with. Specifically it sets up

  - A single egeria platform which hosts
    - An egeria metadata server with a persistent graph store
    - A new server to support the UI
  - A UI to allow browsing of types, instances & servers
  - Apache Kafka & Zookeeper

It does not provide access to our lab notebooks, the Polymer based UI, nor is it preloaded with any data.

This chart may also be useful to understand how to deploy Egeria within kubernetes. In future we anticipate providing
an [operator](https://github.com/odpi/egeria-k8s-operator) which will be more flexible

## Prerequisites

In order to use the labs, you'll first need to have the following installed:

  - A Kubernetes cluster at 1.15 or above
  - the `kubectl` tool in your path
  - [Helm](https://github.com/helm/helm/releases) 3.0 or above

No configuration of the chart is required to use defaults, but information is provided below

## Installation

From one directory level above the location of this README, run the following:

```bash
helm dep update egeria-base
helm install egeria egeria-base
```

THE INSTALL WILL TAKE SEVERAL MINUTES

This is because it is not only creating the required
objects in Kubernetes to run the platforms, but also is configuring egeria itself - which involves waiting
for everything to startup before configuring Egeria via REST API calls.

Once installed the configured server is set to start automatically, storage is persisted, and so if your pod gets moved/restarted, egeria should come back automatically with the same data as before.

### Additional Install Configuration

This section is optional - skip over if you're happy with defaults - a good idea to begin with.

In a helm chart the configuration that has been externalised by the chart writer is specified in the `values.yaml` file which you can find in this directory. However rather than edit this file directly, it's recommended you create an additional file with the required overrides.

As an example, in `values.yaml` we see a value 'serverName' which is set to mds1. If I want to override this I could do
```bash
helm install --set-string egeria.serverName=myserver
```
However this can get tedious with multiple values to override, and you need to know the correct types to use.

Instead it may be easier to create an additional file. For example let's create a file in my home directory `~/egeria.yaml` containing:
```
egeria:
  serverName: metadataserver
  viewServerName: presentationview
```
We can then install the chart with:
```bash
helm install -f ~/egeria.yaml egeria egeria-base
```

Up to now the installation has been called `egeria` but we could call it something else ie `metadataserver`
```bash
helm install -f ~/egeria.yaml metadataserver egeria-base
```

This is known as a `release` in Helm, and we can have multiple installed currently.

To list these use
```bash
helm list
```
and to delete both names we've experimented with so far:
```bash
helm delete metadataserver
helm delete egeria
```

Refer to the comments in `values.yaml` for further information on what can be configured - this includes:
- server, organization, cohort names
- storage options - k8s storage class/size
- Egeria version
- Kubernetes service setup (see below also)
- roles & accounts
- timeouts
- names & repositories for docker images used by the chart
- Kafka specific configuration (setup in the Bitnami chart we use)

### Using additional connectors

If you have additional Egeria connectors that are needed in your deployment, you should soon be able to include the following when deploying the chart

```bash
helm install --include-dir libs=~/libs egeria egeria-base
```
Where in this example ~/libs is the directory including the additional connectors you wish to use.

However the support for this is awaiting a [helm PR](https://github.com/helm/helm/pull/8841), so in the meantime please copy files directly into a 'libs' directory within the chart instead.

For example
```bash
mkdir egeria-base/libs
cp ~/libs/*jar egeria-base/libs
```

These files will be copied into a kubernetes config map, which is then made available as a mounted volume to the runtime image of egeria & added to the class loading path as `/extlib`. You still need to configure the egeria server(s) appropriately
## Accessing Egeria 

When this chart is installed, an initialization job is run to configure the egeria metadata server and UI.

For example looking at kubernetes jobs will show something like:
```bash
$ kubectl get pods                                                                                      [17:27:11]
NAME                                        READY   STATUS    RESTARTS   AGE
egeria-base-config-crhrv                    1/1     Running   0          4m16s
egeria-base-platform-0                      1/1     Running   0          4m16s
egeria-base-presentation-699669cfd4-9swjb   1/1     Running   0          4m16s
egeria-kafka-0                              1/1     Running   2          4m16s
egeria-zookeeper-0                          1/1     Running   0          4m16s
```

You should wait until that first 'egeria-base-config' job completes, it will then disappear from the list.
This job issues REST calls against the egeria serves to configure them for this simple environment (see scripts directory).

The script will not run again, since we will have now configured the servers with persistent storage, and for the platform
to autostart our servers. So even if a pod is removed and restarted, the egeria platform and servers should return in the same state.

We now have egeria running within a Kubernetes cluster, but by default no services are exposed externally - they are all of type `ClusterIP` - we can see these with

```bash
$ kubectl get services                                                                                                                                                                                                                                [12:38:16]
NAME                        TYPE           CLUSTER-IP       EXTERNAL-IP                         PORT(S)                      AGE
egeria-kafka                ClusterIP      172.21.42.105    <none>                              9092/TCP                     33m
egeria-kafka-headless       ClusterIP      None             <none>                              9092/TCP,9093/TCP            33m
egeria-platform             ClusterIP      172.21.40.79     <none>                              9443/TCP                     33m
egeria-presentation         ClusterIP      172.21.107.242   <none>                              8091/TCP                     33m
egeria-zookeeper            ClusterIP      172.21.126.56    <none>                              2181/TCP,2888/TCP,3888/TCP   33m
egeria-zookeeper-headless   ClusterIP      None             <none>                              2181/TCP,2888/TCP,3888/TCP   33m
```

The `egeria-presentation` service is very useful to expose as this provides a useful UI where you can explore
types and instances. Also `egeria-platform` is the service for egeria itself. In production you might want this only exposed very carefully to other systems - and not other users or the internet, but for experimenting with egeria let's assume you do.

How these are exposed can be somewhat dependent on the specific kubernetes environment you are using.

As an example, when running RedHat OpenShift in IBM Cloud, you can expose these services via a LoadBalancer using

```
kubectl expose service/egeria-presentation --type=LoadBalancer --port=8091 --target-port=8091 --name pres  
kubectl expose service/egeria-platform --type=LoadBalancer --port=9443 --target-port=9443 --name platform   
```

If I run these, and then look again at my services I see I now have 2 additional entries (modified for obfuscation):
```
platform                    LoadBalancer   172.21.218.241   3bc644c3-eu-gb.lb.appdomain.cloud   9443:30640/TCP               25h
pres                        LoadBalancer   172.21.18.10     42b0c7f5-eu-gb.lb.appdomain.cloud   8091:30311/TCP               22h
```

So I can access them at their respective hosts. Note that where hosts are allocated dynamically, it can take up to an hour or more for DNS caches to refresh. Waiting up to 5 minutes then refreshing a local cache has proven sufficient for me, but your experience may differ.

In the example above, the Egeria UI can be accessed at `https://42b0c7f5-eu-gb.lb.appdomain.cloud:8091/org/` . Replace the hostname accordingly, and also the `org` is the organization name from the `Values.yaml` file we referred to above

Once logged in, you should be able to login using our demo userid/password of `garygeeke/admin` & start browsing types and instances within Egeria.

You can also issue REST API calls against egeria using a base URL for the platform of `https://3bc644c3-eu-gb.lb.appdomain.cloud` - Other material
covers these REST API calls in more detail, but a simple api doc is available at `https://3bc644c3-eu-gb.lb.appdomain.cloud/swagger-ui.html`
## Cleaning up / removing Egeria

To delete the deployment, simply run this for Helm3:

```bash
$ helm delete egeria
```

In addition, the egeria chart makes use of persistent storage. To remove these use
```bash
$ kubectl get pvc
NAME                                        STATUS   VOLUME                                     CAPACITY   ACCESS MODES   STORAGECLASS                 AGE
data-egeria-kafka-0                         Bound    pvc-d31e0012-8568-4e6f-ae5d-94832ebcd92d   10Gi       RWO            ibmc-vpc-block-10iops-tier   48m
data-egeria-zookeeper-0                     Bound    pvc-90739fce-dce0-422b-8738-a38293d8fdfb   10Gi       RWO            ibmc-vpc-block-10iops-tier   48m
egeria-egeria-data-egeria-base-platform-0   Bound    pvc-197ba47e-a6d5-4f35-a7d8-7b7ec1ed1df3   10Gi       RWO            ibmc-vpc-block-10iops-tier   48m
```
Identify those associated with egeria - which should be obvious from the name and then delete with
```bash
kubectl delete pvc <id>
```

See the section on Configuration for more details

## Feedback & Future

See Egeria on [GitHub](https://github.com/odpi/egeria) for more reference material, our Egeria mailing lists on [lists.lfaidata](https://lists.lfaidata.foundation/groups), or our slack channels by joining/singing up at https://slack.lfai.foundation . We'd very much like to help & discuss how we can improve, and ideally how you can help!

This helm chart offers a basic configuration only - we have also started work at https://github.com/odpi/egeria-k8s-operator & would be delighted if you would 
like to join there too :-)



License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
