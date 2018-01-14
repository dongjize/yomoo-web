package com.yomoo.yomooweb.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-14
 * @Time: 14:34
 */

@Configuration
@EnableSolrRepositories(basePackages = {"com.yomoo.yomooweb"}, multicoreSupport = true)
public class SolrConfig {
    @Value("${spring.data.solr.host}")
    private String url;

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient(url);
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        SolrTemplate solrTemplate = new SolrTemplate(solrClient());
//		solrTemplate.setSolrConverter(solrConverter);
        return solrTemplate;
    }

}
