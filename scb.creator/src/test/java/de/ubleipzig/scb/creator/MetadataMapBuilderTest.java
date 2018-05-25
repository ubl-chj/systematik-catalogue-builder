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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.rdf.jena.JenaRDF;
import org.apache.jena.arq.atlas.json.JSON;
import org.apache.jena.arq.atlas.json.JsonArray;
import org.apache.jena.arq.atlas.json.JsonObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MetadataMapBuilderTest {

    private static final JenaRDF rdf = new JenaRDF();
    static HttpClient client = null;
    static ExecutorService exec;

    public static HttpClient getClient() {
        if (client == null) {
            exec = Executors.newCachedThreadPool();
            client = HttpClient.newBuilder().executor(exec).build();
        }
        return client;
    }

    private String getImageServiceIRI(String image) {
        client = getClient();
        final String fusekiURI = "http://workspaces.ub.uni-leipzig.de:3030/fuseki/trellis/query?query=";
        final String query = encode("SELECT ?o WHERE {<trellis:data/collection/vp/res/" + image
                + ".tif> <http://rdfs.org/sioc/services%23has_service> ?o}", UTF_8);
        final String uri = fusekiURI + query;
        try {
            HttpRequest req = HttpRequest.newBuilder(new URI(uri)).headers(
                    "Content-Type", "application/sparql-query", "Accept", "application/json").GET().build();
            HttpResponse<String> response = client.send(req, asString());
            JsonObject json = JSON.parse(response.body());
            System.out.println(json.toString());
            JsonArray bindings = json.get("results").getAsObject().get("bindings").getAsArray();
            if (bindings.size() > 0 ) {
                return bindings.get(0).getAsObject().get("o").getAsObject().get("value").toString();
            } else {
                return null;
            }
        } catch (InterruptedException | IOException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Disabled
    @Test
    void testBuildMetadataMap() {
        List<VPMetadata> inputList = new ArrayList<>();
        InputStream is = MetadataMapBuilderTest.class.getResourceAsStream("/data/sk2-titles.csv");
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            inputList = br.lines().map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final int LOOPS = 52218;
        final AtomicInteger ai = new AtomicInteger(0);
        final List<TemplateMetadataMap> mlist = new ArrayList<>();
        for (int i = 0; i < LOOPS; i++) {
            final TemplateMetadataMap tp = new TemplateMetadataMap();
            String imageID = String.format("%08d", ai.get());
            Optional<String> imageServiceIRI = ofNullable(getImageServiceIRI(imageID));
            imageServiceIRI.ifPresent(s -> tp.setImageServiceIRI(s.replace("\"", "")));
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
        final Map<Integer, VPMetadata> vpmap = buildVPMap(inputList);

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
            meta.setMetadataMap(tags);
            //System.out.println("setting metadata for sequence beginning " + vp.getGroupImageSequenceBegin());
        }
        final MapList out = new MapList();
        out.setMapList(mlist);
        String json = serialize(out).orElse("");
        writeToFile(json, new File("/tmp/vp-metadata.json"));
    }

    private Function<String, VPMetadata> mapToItem = (line) -> {
        final String[] p = line.split("\t", -1);
        final VPMetadata item = new VPMetadata();
        item.setGroupNumber(Integer.parseInt(p[0]));
        item.setGroupTag1(p[1]);
        item.setGroupTag2(p[2]);
        item.setGroupTag3(p[3]);
        item.setGroupTag4(p[4]);
        item.setGroupTag5(p[5]);
        item.setGroupTag6(p[6]);
        item.setGroupTag7(p[7]);
        item.setGroupImageSequenceBegin(Integer.parseInt(p[8]));
        item.setGroupSize(Integer.parseInt(p[9]));
        return item;
    };

    /**
     * buildVPMap.
     *
     * @param inputList a list of {@link VPMetadata}
     * @return {@link Map}
     */
    public Map<Integer, VPMetadata> buildVPMap(final List<VPMetadata> inputList) {
        final Map<Integer, VPMetadata> vpmap = new HashMap<>();
        inputList.forEach(vp -> {
            final int gid = vp.getGroupNumber();
            vpmap.put(gid, vp);
        });
        return vpmap;
    }
}
