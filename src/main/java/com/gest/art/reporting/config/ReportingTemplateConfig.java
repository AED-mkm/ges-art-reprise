package com.gest.art.reporting.config;


import com.gest.art.reporting.dto.EReportType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author :  <A HREF="mailto:siguizana08@gmail.com">TRAORE BRAHIMA</A>
 * @version : 1.0
 * @since : 2023/08/10 à 00:16
 */

@Configuration
public class ReportingTemplateConfig {
    private static final String REPORT_ROOT = "/reports/";
    private static final String DUMMY = REPORT_ROOT.concat("dummy.jrxml");
    private static final String FACTURE = REPORT_ROOT.concat("FACTURE.jrxml");
    private static final String BORDEREAU = REPORT_ROOT.concat("BORDEREAU.jrxml");




    /**
     * Building a rest template instance.
     *
     * @return {@link ReportingTemplate}
     */
    @Bean
    public ReportingTemplate configure() {
        Map<String, String> map = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>( EReportType.DUMMY.name(), DUMMY),
        new AbstractMap.SimpleImmutableEntry<>(EReportType.FACTURE.name(), FACTURE),
        new AbstractMap.SimpleImmutableEntry<>(EReportType.BORDEREAU.name(), BORDEREAU)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ReportingTemplate(map);
    }

    /**
     * Return template.
     */
    public static class ReportingTemplate {
        private Map<String, String> templateMap;

        public ReportingTemplate(Map<String, String> templateMap) {
            this.templateMap = templateMap;
        }

        public Map<String, String> getTemplateMap() {
            return templateMap;
        }

        public void setTemplateMap(Map<String, String> templateMap) {
            this.templateMap = templateMap;
        }
    }

}
