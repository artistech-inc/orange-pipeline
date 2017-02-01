/*
 * Copyright 2017 ArtisTech, Inc.
 */
package com.artistech.ee.beans;

import java.io.File;

/**
 *
 * @author matta
 */
public class Data extends DataBase {

    public static final String JIE_DIR = "jie_out";
    public static final String VISUALIZATION_DIR = "viz_out";
    private static final String TEST_LIST = "test_list";

    public Data(String key) {
        super(key);
    }

    public String getTestList() {
        return getPipelineDir() + File.separator + TEST_LIST;
    }

    public String getJieOut() {
        return getPipelineDir() + File.separator + JIE_DIR;
    }

    public String[] getJieOutFiles() {
        File f = new File(getJieOut());
        if (f.exists()) {
            return f.list();
        }
        return new String[]{};
    }

    public String getVizOut() {
        return getPipelineDir() + File.separator + VISUALIZATION_DIR;
    }

    public String[] getVizFiles() {
        File f = new File(getVizOut());
        if (f.exists()) {
            return f.list();
        }
        return new String[]{};
    }
}
