package com.and.andesjd.service;

import com.alibaba.fastjson.JSON;
import com.and.andesjd.pojo.Content;
import com.and.andesjd.utils.HtmlParseUtil;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/4/4 14:11
 */
@Service
public class ContentService {
	@Autowired
	private RestHighLevelClient restHighLevelClient;

	/**
	 * 搜索京东的商品保存到es中
	 * @param keywords
	 * @return
	 * @throws Exception
	 */
	public Boolean parseContent(String keywords) throws Exception {
		List<Content> contents = new HtmlParseUtil().parseJD(keywords);
		BulkRequest bulkRequest = new BulkRequest();
		bulkRequest.timeout("2m");

		for (int i = 0; i < contents.size(); i++) {
			bulkRequest.add(
					new IndexRequest("jd_goods")
					.source(JSON.toJSONString(contents.get(i)), XContentType.JSON)
			);
		}

		BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		return !bulk.hasFailures();
	}

	/**
	 * 分页搜索
	 * @param keywords
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> searchPage(String keywords, int pageNo, int pageSize) throws IOException {
		if (pageNo < 0) {
			pageNo = 1;
		}

		// 条件搜索
		SearchRequest searchRequest = new SearchRequest("jd_goods");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

		// 分页
		sourceBuilder.from(pageNo);
		sourceBuilder.size(pageSize);

		// 精准匹配
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keywords);
		sourceBuilder.query(termQueryBuilder);
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("title");
		// 第一个高亮
//		highlightBuilder.requireFieldMatch(false);
		highlightBuilder.preTags("<span style='color:red'>");
		highlightBuilder.postTags("</span>");
		sourceBuilder.highlighter(highlightBuilder);

		// 执行搜索
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

		// 解析结果
		List<Map<String, Object>> list = new ArrayList<>();
		for (SearchHit hit : searchResponse.getHits().getHits()) {
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			HighlightField title = highlightFields.get("title");
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();

			if (title != null) {
				Text[] fragments = title.fragments();
				String newTitle = "";
				for (Text text : fragments) {
					newTitle += text;
				}
				sourceAsMap.put("title", newTitle);
			}
			list.add(sourceAsMap);
		}
		return list;
	}
}
