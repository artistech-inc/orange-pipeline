/*
 * Copyright 2017 ArtisTech, Inc.
 */
package com.artistech.ee.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author matta
 */
public class JieModelFinder implements PipelineYamlUpdater {

    /**
     * Walk through the yaml configuration and find the joint_ere parameter.
     * From here, find the 'path' value and then search for models. Add all
     * files under the 'models' directory to the available values.
     *
     * @param map
     */
    @Override
    public void updatePipeline(Map map) {
        Map parts = (Map) map.get("parts");
        Map jie = (Map) parts.get("jie");
        ArrayList parameters = (ArrayList) jie.get("parameters");
        ArrayList<String> values = new ArrayList<>();
        for (Object paramObjects : parameters) {
            Map parameter = (Map) paramObjects;
            Map param = (Map) parameter.get("parameter");
            if ("path".equals(param.get("name").toString())) {
                String get = param.get("value").toString();
                Collection<File> listFiles = FileUtils.listFiles(new File(get + File.separator + "models"), null, true);
                for (File file : listFiles) {
                    String val = file.getAbsolutePath().replace(get, "");
                    if (val.startsWith("/")) {
                        val = val.substring(1);
                    }
                    values.add(val);
                }
            }
        }
        for (Object paramObjects : parameters) {
            Map parameter = (Map) paramObjects;
            Map param = (Map) parameter.get("parameter");
            if ("model".equals(param.get("name").toString())) {
                ArrayList get = (ArrayList) param.get("values");
                for (String val : values) {
                    if (!get.contains(val)) {
                        get.add(val);
                    }
                }
            }
        }
    }
}
