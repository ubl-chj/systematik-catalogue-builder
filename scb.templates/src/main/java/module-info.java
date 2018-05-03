module de.ubleipzig.scb.templates {
    requires com.fasterxml.jackson.annotation;
    requires de.ubleipzig.iiif.vocabulary;
    exports de.ubleipzig.scb.templates;
    opens de.ubleipzig.scb.templates to com.fasterxml.jackson.databind;
}