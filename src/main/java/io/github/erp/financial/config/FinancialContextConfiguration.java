package io.github.erp.financial.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    basePackages = "io.github.erp.repository",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*\\.search\\..*"
    )
)
@EntityScan(basePackages = {
    "io.github.erp.financial.domain",
    "io.github.erp.domain"
})
@ComponentScan(basePackages = {
    "io.github.erp.financial.service",
    "io.github.erp.financial.web.rest",
    "io.github.erp.financial.events",
    "io.github.erp.service",
    "io.github.erp.erp"
})
public class FinancialContextConfiguration {
}
