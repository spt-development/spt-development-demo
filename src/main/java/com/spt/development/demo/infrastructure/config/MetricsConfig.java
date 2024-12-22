package com.spt.development.demo.infrastructure.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> commonTags(@Value("${spring.application.name}")
                                                             final String appName,
                                                             final BuildProperties buildProperties) throws UnknownHostException {
        final String hostname = InetAddress.getLocalHost().getHostName();

        // These tags are used by Grafana dashboards such as "JVM Overview", "Spring Boot 3.x Statistics" and "Spring Boot Observability"
        return registry -> registry.config()
                .commonTags("namespace", buildProperties.getGroup())
                .commonTags("application", appName)
                .commonTags("version", buildProperties.getVersion())
                .commonTags("instance", hostname);
    }
}
