/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.ubleipzig.scb.creator;

import static de.ubleipzig.image.metadata.JsonSerializer.writeToFile;
import static de.ubleipzig.scb.creator.internal.JsonSerializer.serialize;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static jdk.incubator.http.HttpResponse.BodyHandler.asString;

import de.ubleipzig.scb.templates.MapList;
import de.ubleipzig.scb.templates.TemplateMetadataMap;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import org.apache.commons.rdf.jena.JenaRDF;
import org.junit.jupiter.api.Test;

public class MetadataMapBuilderTest {

    private static final JenaRDF rdf = new JenaRDF();
    private static HttpClient client = null;

    private static HttpClient getClient() {
        if (client == null) {
            final ExecutorService exec = Executors.newCachedThreadPool();
            client = HttpClient.newBuilder().executor(exec).build();
        }
        return client;
    }


    private Map<String, String> getImageServiceMap(final InputStream is) {
        //InputStream sparqlResults = MetadataMapBuilderTest.class.getResourceAsStream("/data/imageservices.csv");
        return processInputFile(is);
    }

    private String getImageServiceIRIs() {
        client = getClient();
        final String fusekiURI = "http://workspaces.ub.uni-leipzig.de:3030/fuseki/trellis/query?query=";
        final String query = encode(
                "SELECT ?imageId ?service WHERE {?imageId <http://rdfs.org/sioc/services%23has_service> ?service}",
                UTF_8);
        final String uri = fusekiURI + query;
        try {
            final HttpRequest req = HttpRequest.newBuilder(new URI(uri)).headers(
                    "Content-Type", "application/sparql-query", "Accept", "text/csv").GET().build();
            final HttpResponse<String> response = client.send(req, asString());
            return response.body();
        } catch (InterruptedException | IOException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    void testBuildMetadataMap() {
        final InputStream is = MetadataMapBuilderTest.class.getResourceAsStream("/data/sk2-titles.csv");
        final VorlesungImpl vi = new VorlesungImpl();
        final List<VPMetadata> inputList = vi.processInputFile(is);
        final int loops = 52218;
        final AtomicInteger ai = new AtomicInteger(0);
        final String imageServices = getImageServiceIRIs();
        final InputStream is2 = new ByteArrayInputStream(imageServices.getBytes());
        final Map<String, String> imageServiceMap = getImageServiceMap(is2);
        final List<TemplateMetadataMap> mlist = new ArrayList<>();
        for (int i = 0; i < loops; i++) {
            final TemplateMetadataMap tp = new TemplateMetadataMap();
            final String imageID = String.format("%08d", ai.get());
            if (imageServiceMap.containsKey(imageID)) {
                final String imageServiceIRI = imageServiceMap.get(imageID);
                tp.setImageServiceIRI(imageServiceIRI);
            }
            tp.setImageIndex(ai.get());
            ai.getAndIncrement();
            mlist.add(tp);

        }
        final List<int[]> result = inputList.stream().map(v -> new int[v.getGroupSize()]).collect(Collectors.toList());
        final AtomicInteger ai2 = new AtomicInteger(0);
        final List<TemplateMetadataMap> groupedMaps = new ArrayList<>();
        for (int[] r : result) {
            final Iterable<Integer> it = () -> Arrays.stream(r).iterator();
            ai2.getAndIncrement();
            it.forEach(x -> {
                final TemplateMetadataMap map = new TemplateMetadataMap();
                map.setGroupId(ai2.get());
                groupedMaps.add(map);
            });
        }
        final Iterator<TemplateMetadataMap> i1 = mlist.iterator();
        final Iterator<TemplateMetadataMap> i2 = groupedMaps.iterator();
        final Map<Integer, VPMetadata> vpmap = vi.buildVPMap(inputList);

        while (i1.hasNext() && i2.hasNext()) {
            final TemplateMetadataMap meta = i1.next();
            final TemplateMetadataMap c = i2.next();
            final int groupId = c.getGroupId();
            final Map<String, String> tags = new TreeMap<>();
            final VPMetadata vp = vpmap.get(groupId);
            final Optional<String> t1 = ofNullable(vp.getGroupTag1()).filter(s -> !s.isEmpty());
            t1.ifPresent(s -> tags.put("tag1", s));
            final Optional<String> t2 = ofNullable(vp.getGroupTag2()).filter(s -> !s.isEmpty());
            t2.ifPresent(s -> tags.put("tag2", s));
            final Optional<String> t3 = ofNullable(vp.getGroupTag3()).filter(s -> !s.isEmpty());
            t3.ifPresent(s -> tags.put("tag3", s));
            final Optional<String> t4 = ofNullable(vp.getGroupTag4()).filter(s -> !s.isEmpty());
            t4.ifPresent(s -> tags.put("tag4", s));
            final Optional<String> t5 = ofNullable(vp.getGroupTag5()).filter(s -> !s.isEmpty());
            t5.ifPresent(s -> tags.put("tag5", s));
            final Optional<String> t6 = ofNullable(vp.getGroupTag6()).filter(s -> !s.isEmpty());
            t6.ifPresent(s -> tags.put("tag6", s));
            final Optional<String> t7 = ofNullable(vp.getGroupTag7()).filter(s -> !s.isEmpty());
            t7.ifPresent(s -> tags.put("tag7", s));
            final Optional<String> t8 = ofNullable(vp.getGroupTag8()).filter(s -> !s.isEmpty());
            t8.ifPresent(s -> tags.put("tag8", s));
            meta.setMetadataMap(tags);
            //System.out.println("setting metadata for sequence beginning " + vp.getGroupImageSequenceBegin());
        }
        final MapList out = new MapList();
        out.setMapList(mlist);
        final String json = serialize(out).orElse("");
        writeToFile(json, new File("/tmp/vp-metadata.json"));
    }

    public Map<String, String> processInputFile(final InputStream is) {
        final Map<String, String> item = new HashMap<>();
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            br.lines().forEach(l -> {
                if (l.contains("trellis:data")) {
                    final String[] p = l.split(",", -1);
                    final String key = p[0].substring(31, 39);
                    item.put(key, p[1]);
                }
            });
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

}
