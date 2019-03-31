# Mule CoAP connector - Commons
![Mule-Coap logo](coap-logo.svg)

Mule CoAP connector is an Anypoint Connector implementation of the [RFC7252 - Constrained Application Protocol](http://tools.ietf.org/html/rfc7252). 
With it Mule applications become CoAP capable and can communicate with other CoAP capable devices and services, realising Internet of Things solutions (IoT). 

The connector uses Californium, a Java CoAP implementation. More information about Californium and CoAP can be found at:

* [http://www.eclipse.org/californium/](http://www.eclipse.org/californium/)
* [http://coap.technology/](http://coap.technology/).

This Java component - the Commons component - is one of three parts of the Mule CoAP package.  
The other two being the CoapServer Connector and CoapClient Connector. 
The Commons component contains java-classes that are common to these two connectors. 

# Build using Maven

You need to have a working maven installation to build the Mule CoAP Commons component.
Then simply run the following from the project's root directory:

```sh
$ mvn clean install
```

# Using in Maven Projects

Mule CoAP artifact releases will be published to [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cmule-coap).
Usually the Commons component dependency will added through one of the connector pom's and there is no need to add the 
dependency manually. But if you need for some reason to add it as a library in your projects, add the following dependency
to your `pom.xml` (without the dots):

```xml
  <dependencies>
    ...
    <dependency>
            <groupId>nl.teslanet.mule.transport.coap</groupId>
            <artifactId>mule-coap-commons</artifactId>
            <version>1.1.0</version>
    </dependency>
    ...
  </dependencies>
  ...
```


# Contact

A bug, an idea, an issue? Create an issue on https://github.com/teslanet-nl/mule-coap-commons/issues.

# Contributing

Use issues or pull-requests on your fork.
