package com.task1.take1;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.task1.take1.eModel.EUser;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class ERestClient {
    RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200)).build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    // And create the API client
    ElasticsearchClient client = new ElasticsearchClient(transport);

    String searchText = "Tom";

    SearchResponse<EUser> response = client.search(s -> s
                    .index("moviez")
                    .query(q -> q
                            .match(t -> t
                                    .field("primaryName")
                                    .query(searchText)
                            )
                    ),
            EUser.class
    );

    TotalHits total = response.hits().total();
    boolean isExactResult = total.relation() == TotalHitsRelation.Eq;


    public ERestClient() throws IOException {
    }
}
