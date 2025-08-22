package io.github.erp.context.assets;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "io.github.erp.context.assets.service",
    "io.github.erp.context.assets.acl",
    "io.github.erp.context.assets.events",
    "io.github.erp.context.assets.repository",
    "io.github.erp.context.assets.web",
    "io.github.erp.context.assets.domain"
})
public class AssetManagementContextConfiguration {
}
