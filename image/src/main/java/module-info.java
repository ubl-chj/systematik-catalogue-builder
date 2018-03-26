module org.ubl.image.metadata {
    requires slf4j.api;
    requires metadata.extractor;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.codec;
    requires jackson.annotations;
    exports org.ubl.image.metadata;
    exports org.ubl.image.metadata.templates;
}