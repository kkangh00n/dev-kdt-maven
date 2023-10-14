package org.prgms.order;

import java.text.MessageFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "kdt")
public class OrderProperties implements InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(OrderProperties.class);

    private String version;

    private int minimumOrderAmount;

    private List<String> supportVendors;

    private String description;

    //우선순위 : 환경변수 > properties
    @Value("${JAVA_HOME}")
    private String javaHome;

    public String getVersion() {
        return version;
    }

    public int getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public List<String> getSupportVendors() {
        return supportVendors;
    }

    public String getDescription() {
        return description;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMinimumOrderAmount(int minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public void setSupportVendors(List<String> supportVendors) {
        this.supportVendors = supportVendors;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("version -> {}", version);
        logger.debug("minimumOrderAmount -> {}", minimumOrderAmount);
        logger.debug("supportVendors -> {}", supportVendors);
        logger.debug("description -> {}", description);
        logger.debug("JAVA_HOME -> {}", javaHome);

    }
}
