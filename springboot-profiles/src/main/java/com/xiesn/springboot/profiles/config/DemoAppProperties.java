package com.xiesn.springboot.profiles.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 薄配置绑定，只证明「最终生效值」；校验与 record 留给 #10。
 */
@ConfigurationProperties(prefix = "demo")
public class DemoAppProperties {

    private String envName = "default";
    private String message = "";
    private Feature feature = new Feature();
    private String datasourceLabel = "none";
    private String importHint = "not-imported";
    private String multidocFlag = "base";

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public String getDatasourceLabel() {
        return datasourceLabel;
    }

    public void setDatasourceLabel(String datasourceLabel) {
        this.datasourceLabel = datasourceLabel;
    }

    public String getImportHint() {
        return importHint;
    }

    public void setImportHint(String importHint) {
        this.importHint = importHint;
    }

    public String getMultidocFlag() {
        return multidocFlag;
    }

    public void setMultidocFlag(String multidocFlag) {
        this.multidocFlag = multidocFlag;
    }

    /**
     * 功能开关嵌套配置。
     */
    public static class Feature {
        private boolean debug;

        public boolean isDebug() {
            return debug;
        }

        public void setDebug(boolean debug) {
            this.debug = debug;
        }
    }
}