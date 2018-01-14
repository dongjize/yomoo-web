package com.yomoo.yomooweb.service;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2018-01-14
 * @Time: 15:50
 */
public abstract class BaseSolrService {

    @Value("${spring.data.solr.host}")
    private String url;

    public HttpSolrClient getSolrClient(String coreName) {
        return new HttpSolrClient(url + "/" + coreName);
    }
}
