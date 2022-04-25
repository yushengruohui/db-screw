package com.yhh.api;


/**
 * @author yhh 2022-04-25 08:47:20
 **/
public class ScrewProperties {
    private static final String path = "application.properties";
    public static final String fileOutputDir = PropertyUtil.getString(path, "screw.output.dir", "./doc");
    public static final String filename = PropertyUtil.getString(path, "screw.output.filename", "test-db-doc");
    public static final String url = PropertyUtil.getString(path, "spring.datasource.url", "");
    public static final String username = PropertyUtil.getString(path, "spring.datasource.username", "");
    public static final String password = PropertyUtil.getString(path, "spring.datasource.password", "");
    public static final String driverClassName = PropertyUtil.getString(path, "spring.datasource.driver-class-name", "");
}
