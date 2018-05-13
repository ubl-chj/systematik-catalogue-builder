## Systematik Catalogue Builder

[![Build Status](https://travis-ci.org/ub-leipzig/systematik-catalogue-builder.png?branch=master)](https://travis-ci.org/ub-leipzig/systematik-catalogue-builder)
[![codecov](https://codecov.io/gh/ub-leipzig/systematik-catalogue-builder/branch/master/graph/badge.svg)](https://codecov.io/gh/ub-leipzig/systematik-catalogue-builder)
[![Javadoc](https://javadoc-badge.appspot.com/de.ub-leipzig/scb.creator.svg?label=javadoc)](https://ub-leipzig.github.io/systematik-catalogue-builder/apidocs/)
[![Maven Central](https://img.shields.io/maven-central/v/de.ubleipzig/scb.creator.svg)](https://mvnrepository.com/artifact/de.ubleipzig/scb.creator/0.1.0)


An [Web Annotation](https://www.w3.org/TR/annotation-model/) data builder for use with the [Trellis Linked Data Platform](https://trellis-ldp.github.io/trellis/apidocs/).

## Dependencies
* A TSV formatted data file exported from the [Chopin](http://www.schneider-mt.de/en/chopin/projekte.html) catalogue software
* a file system directory containing the images referenced in the data file

Image Metadata Builder Process:
* See [Image Metadata](https://github.com/ubleipzig/image)
   
This file rather than the binaries will be used for all subsequent annotation builder processes.

## Requirements
* [JDK 10](http://jdk.java.net/10/) or higher

### Building
    $ buildtools/src/install/install-jpms.sh

### Run at CLI
1. Unpack distribution (located in `build/distributions`)
2. customize configuration file (see `lib/scbconfig-test-remote.yml`)
3. copy `lib/scb.creator.sh` to distribution bin folder (this replaces the default gradle exec script)
4. execute `./scb.creator.sh $args`

### Arguments
| Name | Default | Long Name | Description
| ---- | ------- | --------- | -----------
| -b | resources | "builder" | The builder implementation |
| -f | (none)    | "from"    | The start index (this corresponds to the beginning _body_ sequence identifier) for a collection set |
| -t | (none)    | "to"      | The end index (this corresponds to the ending _body_ sequence identifier) for a collection set |
| -c | (none)    | "config"  | The absolute path to the configuration file |

### Configuration File

```yaml
baseUrl: "http://localhost:8000/"
```
| Name | Default | Description |
| ---- | ------- | ----------- |
| baseUrl | (none) | A defined baseUrl for the HTTP/2 proxy HTTP(s) interface for Trellis  |

```yaml
imageServiceBaseUrl: "http://workspaces.ub.uni-leipzig.de:8182/iiif/2/"
```

| Name | Default | Description |
| ---- | ------- | ----------- |
| imageServiceBaseUrl: | (none) | The baseUrl of the IIIF image service  |

```yaml
imageMetadataServiceConfig:
  dimensionManifestFilePath: "https://workspaces.ub.uni-leipzig.de:8445/collection/vp/meta/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49"
  imageSourceDir: "/images"
```
| Name | Default | Description |
| ---- | ------- | ----------- |
| dimensionManifestFilePath: | (none) | A URI or filesystemm path to the image dimension manifest  |
| imageSourceDir: | (none) | An absolute filesystemm path to the image source files  |

See `lib/scbconfig-test-remote.yml` for additional values.

### API
See [WIP]