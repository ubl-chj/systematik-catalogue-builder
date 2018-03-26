## Systematik Catalogue Builder

[![Build Status](https://travis-ci.org/ub-leipzig/systematik-catalogue-builder.png?branch=master)](https://travis-ci.org/ub-leipzig/systematik-catalogue-builder)

An [Web Annotation](https://www.w3.org/TR/annotation-model/) data builder for use with the [Trellis Linked Data Platform](https://trellis-ldp.github.io/trellis/apidocs/).

## Dependencies
* A TSV formatted data file exported from the [Chopin](http://www.schneider-mt.de/en/chopin/projekte.html) catalogue software
* a file system directory containing the images referenced in the data file

Image Metadata Builder Process:
* Generate an image dimension manifest with :

```java
/**
@param String imageSourceDir
*/
```

```java
    ImageMetadataGeneratorConfig config = new ImageMetadataGeneratorConfig();
    config.setImageSourceDir(imageSourceDir);
    final ImageMetadataGenerator generator = new ImageMetadataGenerator(config);
    final ImageDimensionManifest dimManifest = generator.build();
```

The dimension manifest looks like this:
```json
{
  "collection" : "/home/christopher/IdeaProjects/systematik-catalogue-builder/image/out/test/resources",
  "images" : [
    {
      "filename" : "00000001.jpg",
      "digest" : "jeUrOCoqaYw/89LmIo3gQlxhipE=",
      "height" : 2130,
      "width" : 1705
    },
    {
      "filename" : "00000002.jpg",
      "digest" : "5t/uKe+X1vTe0fR2YAGYj4OzgiI=",
      "height" : 2130,
      "width" : 1705
    },
    {
      "filename" : "00000003.jpg",
      "digest" : "8caG/XPRwwAZSU3Pldcsl7ZEtoA=",
      "height" : 2130,
      "width" : 1705
    }
  ]
}
``` 
   
This file rather than the binaries will be used for all subsequent annotation builder processes.

## Requirements
* [JDK 10](http://jdk.java.net/10/) or higher

### Building
    $ gradle build

### Test Requirements
* Trellis Application version `0.7.0-SNAPSHOT` 
* a JUnit runner must be used with the VM option `--add-modules jdk.incubator.httpclient`

## API
See [WIP]