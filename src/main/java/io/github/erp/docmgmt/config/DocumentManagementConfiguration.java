package io.github.erp.docmgmt.config;

import io.github.erp.config.AppPropertyFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "erp.document-management")
@PropertySource(value = "classpath:config/erpConfigs.yml", factory = AppPropertyFactory.class)
public class DocumentManagementConfiguration {

    private Storage storage = new Storage();
    private Security security = new Security();
    private Search search = new Search();
    private Integration integration = new Integration();

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public Integration getIntegration() {
        return integration;
    }

    public void setIntegration(Integration integration) {
        this.integration = integration;
    }

    public static class Storage {
        private String type = "filesystem";
        private String basePath = "/var/lib/erp/documents";
        private String maxFileSize = "100MB";
        private String[] allowedTypes = {"pdf", "doc", "docx", "xls", "xlsx", "txt"};

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }

        public String getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(String maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public String[] getAllowedTypes() {
            return allowedTypes;
        }

        public void setAllowedTypes(String[] allowedTypes) {
            this.allowedTypes = allowedTypes;
        }
    }

    public static class Security {
        private Jwt jwt = new Jwt();
        private Clearance clearance = new Clearance();

        public Jwt getJwt() {
            return jwt;
        }

        public void setJwt(Jwt jwt) {
            this.jwt = jwt;
        }

        public Clearance getClearance() {
            return clearance;
        }

        public void setClearance(Clearance clearance) {
            this.clearance = clearance;
        }

        public static class Jwt {
            private String secret;
            private int expiration = 3600;

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public int getExpiration() {
                return expiration;
            }

            public void setExpiration(int expiration) {
                this.expiration = expiration;
            }
        }

        public static class Clearance {
            private boolean required = true;
            private String defaultLevel = "PUBLIC";

            public boolean isRequired() {
                return required;
            }

            public void setRequired(boolean required) {
                this.required = required;
            }

            public String getDefaultLevel() {
                return defaultLevel;
            }

            public void setDefaultLevel(String defaultLevel) {
                this.defaultLevel = defaultLevel;
            }
        }
    }

    public static class Search {
        private Elasticsearch elasticsearch = new Elasticsearch();

        public Elasticsearch getElasticsearch() {
            return elasticsearch;
        }

        public void setElasticsearch(Elasticsearch elasticsearch) {
            this.elasticsearch = elasticsearch;
        }

        public static class Elasticsearch {
            private String hosts;
            private String indexName = "documents";
            private int batchSize = 100;

            public String getHosts() {
                return hosts;
            }

            public void setHosts(String hosts) {
                this.hosts = hosts;
            }

            public String getIndexName() {
                return indexName;
            }

            public void setIndexName(String indexName) {
                this.indexName = indexName;
            }

            public int getBatchSize() {
                return batchSize;
            }

            public void setBatchSize(int batchSize) {
                this.batchSize = batchSize;
            }
        }
    }

    public static class Integration {
        private ErpCore erpCore = new ErpCore();
        private AuditService auditService = new AuditService();

        public ErpCore getErpCore() {
            return erpCore;
        }

        public void setErpCore(ErpCore erpCore) {
            this.erpCore = erpCore;
        }

        public AuditService getAuditService() {
            return auditService;
        }

        public void setAuditService(AuditService auditService) {
            this.auditService = auditService;
        }

        public static class ErpCore {
            private String baseUrl;
            private String timeout = "30s";

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public String getTimeout() {
                return timeout;
            }

            public void setTimeout(String timeout) {
                this.timeout = timeout;
            }
        }

        public static class AuditService {
            private String baseUrl;
            private boolean async = true;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public boolean isAsync() {
                return async;
            }

            public void setAsync(boolean async) {
                this.async = async;
            }
        }
    }
}
