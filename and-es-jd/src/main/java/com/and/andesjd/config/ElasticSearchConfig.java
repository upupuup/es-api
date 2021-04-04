package com.and.andesjd.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/4/4 10:29
 */
@Configurable
public class ElasticSearchConfig {
	@Bean
	public RestHighLevelClient restHighLevelClient() {
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("localhost", 9200, "http")));
		return client;
	}
}
