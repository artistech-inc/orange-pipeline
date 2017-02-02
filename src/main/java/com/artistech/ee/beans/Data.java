/*
 * Copyright 2017 ArtisTech, Inc.
 */
package com.artistech.ee.beans;

import java.io.File;

/**
 * Data bean for orange pipeline.
 *
 * @author matta
 */
public class Data extends DataBase {

    public static final String JIE_DIR = "jie_out";
    public static final String VISUALIZATION_DIR = "viz_out";
    private static final String TEST_LIST = "test_list";

    /**
     * Constructor.
     *
     * @param key
     */
    public Data(String key) {
        super(key);
    }

    /**
     * Get the file of testing files.
     *
     * @return
     */
    public String getTestList() {
        return getPipelineDir() + File.separator + TEST_LIST;
    }

    /**
     * Get the output directory for JIE.
     *
     * @return
     */
    public String getJieOut() {
        return getPipelineDir() + File.separator + JIE_DIR;
    }

    /**
     * Get the output files from JIE.
     *
     * @return
     */
    public String[] getJieOutFiles() {
        File f = new File(getJieOut());
        if (f.exists()) {
            return f.list();
        }
        return new String[]{};
    }

    /**
     * Get the visualization output directory.
     *
     * @return
     */
    public String getVizOut() {
        return getPipelineDir() + File.separator + VISUALIZATION_DIR;
    }

    /**
     * Get the output files from visualization.
     *
     * @return
     */
    public String[] getVizFiles() {
        File f = new File(getVizOut());
        if (f.exists()) {
            return f.list();
        }
        return new String[]{};
    }
}
