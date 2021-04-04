package com.and.andesapi;

import com.alibaba.fastjson.JSON;
import com.and.andesapi.pojo.User;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexAction;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.and.andesapi.utils.ESconst.ES_INDEX;

@SpringBootTest
class AndEsApiApplicationTests {
	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@Test
	void contextLoads() {
	}

	/**
	 * 创建索引
	 *
	 * @throws IOException
	 */
	@Test
	void testCreateIndex() throws IOException {
		// 创建索引请求
		CreateIndexRequest request = new CreateIndexRequest(ES_INDEX);
		// 执行请求,请求获得响应
		CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		System.out.println(createIndexResponse);
	}

	/**
	 * 判断索引是否存在
	 *
	 * @throws IOException
	 */
	@Test
	void testExistIndex() throws IOException {
		GetIndexRequest request = new GetIndexRequest(ES_INDEX);
		boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
		System.out.println(exists);
	}

	/**
	 * 删除
	 *
	 * @throws IOException
	 */
	@Test
	void deleteIndex() throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(ES_INDEX);
		AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
		System.out.println(delete.isAcknowledged());
	}

	/**
	 * 添加文档
	 *
	 * @throws IOException
	 */
	@Test
	void testAddDocument() throws IOException {
		User user = new User("狂神说", 3);
		IndexRequest request = new IndexRequest(ES_INDEX);
		// 规则 put /kuang_index/_doc/1
		request.id("1");
		request.timeout(TimeValue.timeValueSeconds(1));
		request.timeout("1s");

		request.source(JSON.toJSONString(user), XContentType.JSON);

		// 客户端发送请求
		IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
		System.out.println(indexResponse.toString());
		System.out.println(indexResponse.status());
	}

	/**
	 * 批量插入数据
	 */
	@Test
	void testBulkRequest() throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		bulkRequest.timeout("10s");

		List<User> userList = new ArrayList<>();
		userList.add(new User("kuangshen1", 3));
		userList.add(new User("kuangshen2", 13));
		userList.add(new User("kuangshen3", 23));
		userList.add(new User("kuangok1", 13));
		userList.add(new User("kuangokok1", 3));
		userList.add(new User("kuangokk1", 23));

		for (int i = 0; i < userList.size(); i++) {
			bulkRequest.add(new IndexRequest(ES_INDEX)
					.source(JSON.toJSONString(userList.get(i)), XContentType.JSON)
			);
		}

		BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		System.out.println(bulk.hasFailures());
	}

	/**
	 * 获取文档，判断是否存在 GET /index/doc/1
	 */
	@Test
	void testIsExists() throws IOException {
		GetRequest getRequest = new GetRequest(ES_INDEX, "1");
		// 不获取返回的 _source的上下文
		getRequest.fetchSourceContext(new FetchSourceContext(false));
		getRequest.storedFields("_none_");
		boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
		System.out.println(exists);
	}

	/**
	 * 获取文档， GET /index/doc/1
	 */
	@Test
	void testGetDocument() throws IOException {
		GetRequest getRequest = new GetRequest(ES_INDEX, "1");
		GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
		System.out.println(getResponse.getSource());
		System.out.println(getResponse.getSourceAsString());
		System.out.println(getResponse);
	}

	/**
	 * 更新文档
	 */
	@Test
	void testUpdateDocument() throws IOException {
		UpdateRequest updateRequest = new UpdateRequest(ES_INDEX, "1");
		updateRequest.timeout("1s");
		User user = new User("狂神说Java", 18);
		updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);
		UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
		System.out.println(updateResponse.status());
	}

	/**
	 * 删除
	 */
	@Test
	void deleteDocument() throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest(ES_INDEX, "1");
		deleteRequest.timeout("1s");
		DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
		System.out.println(deleteResponse.status());
	}

	/**
	 * 查询
	 */
	void testSearch() {
		SearchRequest searchRequest = new SearchRequest(ES_INDEX);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//		QueryBuilder queryBuilder = new QueryBuilder();

	}
}
