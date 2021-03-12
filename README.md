[![Build Status](https://travis-ci.org/dat3startcode/dat3-startcode.svg?branch=master)](https://travis-ci.org/dat3startcode/dat3-startcode)

*This project is meant as start code for projects and exercises given in Flow-1+2 (+3 using the security-branch) at http://cphbusiness.dk in the Study Program "AP degree in Computer Science"*

*Projects which are expected to use this start-code are projects that require all, or most of the following technologies:*
 - *JPA and REST*
- *Testing, including database test*
- *Testing, including tests of REST-API's*
- *CI and CONTINUOUS DELIVERY*

### Preconditions
*In order to use this code, you should have a local developer setup + a "matching" droplet on Digital Ocean as described in the 3. semester guidelines* 
# Getting Started

This document explains how to use this code (build, test and deploy), locally with maven, and remotely with maven controlled by Travis
 - [How to use](https://docs.google.com/document/d/1K6s6Tt65bzB8bCSE_NUE8alJrLRNTKCwax3GEm4OjOE/edit?usp=sharing)

## Speciel version
This version of the start code is meant for show casing the swagger / openapi. The following files are extended:
1. `Web Pages folder` has a subfolder now: `openapi`. This folder contains web ressources to show the api documentation. It must be there
2. `index.html` is updated to point to the documentation page
3. `dtos.RenameMeDTO.java` shows how to annotate with `@Schema`
4. `rest.RenameMeRessource.java` shows how to annotate endpoints with `@OpenAPIDefinition` on the class and `@Operation` on each endpoint.
5. `rest.ApplicationConfig.java` has to lines added: `resources.add(OpenApiResource.class); resources.add(AcceptHeaderOpenApiResource.class);`
6. `TestPackages.rest.RenameMeRessourceTest.java` has POST example on rest assured.
7. `pom.xml` has the following dependencies added:
```xml
      <dependency>
            <groupId>io.swagger.core.v3</groupId>
            <artifactId>swagger-annotations</artifactId>
            <version>2.0.9</version>
            <type>jar</type>
        </dependency>
        <dependency>
            <groupId>io.swagger.core.v3</groupId>
            <artifactId>swagger-jaxrs2</artifactId>
            <version>2.0.9</version>
            <type>jar</type>
        </dependency>
        <dependency>
            <groupId>org.apache.clerezza.ext</groupId>
            <artifactId>org.json.simple</artifactId>
            <version>0.4</version>
        </dependency>
        <dependency>
```