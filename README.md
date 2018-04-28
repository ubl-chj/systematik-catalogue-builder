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
* Generate an image dimension manifest with :

   
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