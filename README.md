## Systematik Catalogue Builder

An [Web Annotation](https://www.w3.org/TR/annotation-model/) data builder for use with the [Trellis Linked Data Platform](https://trellis-ldp.github.io/trellis/apidocs/).

## Dependencies
* A TSV formatted data file exported from the [Chopin](http://www.schneider-mt.de/en/chopin/projekte.html) catalogue software
* a file system directory containing the images referenced in the data file

## Requirements
* [JDK 10](http://jdk.java.net/10/) or higher

### Building
    $ gradle processBuildTools
    $ gradle build

### Test Requirements
* Trellis Application version `0.6.0-SNAPSHOT` published in Maven Local
* a JUnit runner must be used with the VM option `--add-modules jdk.incubator.httpclient`

## API
See [LDPClient](https://github.com/pan-dora/ldp-client/blob/master/src/main/java/cool/pandora/ldpclient/LdpClient.java)