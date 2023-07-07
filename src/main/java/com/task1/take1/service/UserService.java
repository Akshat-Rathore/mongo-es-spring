package com.task1.take1.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.task1.take1.UserController;
import com.task1.take1.eModel.EUser;
import com.task1.take1.eRepository.ERepository;
import com.task1.take1.models.User;
import com.task1.take1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ScriptType;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

 class Result{
    public String _id;
    public String nconst;
    public String primaryName;
    public String birthYear;
    public String deathYear;
    public String primaryProfession;
    public String knownForTitles;
}

 class Root{
    public String page;
    public String next;
    public int entries;
    public ArrayList<Result> results;
}


//class Root{
//    public boolean adult;
//    public String backdrop_path;
//    public ArrayList<Integer> genre_ids;
//    public int id;
//    public String original_language;
//    public String original_title;
//    public String overview;
//    public double popularity;
//    public String poster_path;
//    public String release_date;
//    public String title;
//    public boolean video;
//    public double vote_average;
//    public int vote_count;
//    public String character;
//    public String credit_id;
//    public int order;
//    public int actorId;
//}

@AllArgsConstructor
@Service
public class UserService {

    private final ERepository eRepo;
    private final UserRepository repo;
    RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200)).build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    // And create the API client
    ElasticsearchClient client = new ElasticsearchClient(transport);
        String decorateItem(List<String> item,Logger logger){
        String str="";
        for(String i:item){
            str+="\""+i+"\",";
        }
        str = str.substring(0, str.length() - 1);
        return str;
    }
    public List<String> getCommonUsers(List<String> users) {
        SearchResponse<EUser> u1 = null,u2=null;
        try {
            u1 = client.search(s -> s
                            .index("moviez").size(1000)
                            .query(q -> q
                                    .match(t -> t
                                            .field("primaryName")
                                                .query(users.get(0))
                                    )
                            ),
                    EUser.class
            );
            u2 = client.search(s -> s
                            .index("moviez").size(1000)
                            .query(q -> q
                                    .match(t -> t
                                            .field("primaryName")
                                            .query(users.get(1))
                                    )
                            ),
                    EUser.class
            );

        } catch (IOException e) {
            return null;
        }
        Logger logger= LoggerFactory.getLogger(UserController.class);
        List<Hit<EUser>> hit1 = u1.hits().hits(),hit2=u2.hits().hits();
        List<String> item1=new ArrayList<String>(),item2=new ArrayList<String>();;
        for(Hit<EUser> h:hit1){
            item1=h.source().getKnownForTitles();
        }
        for(Hit<EUser> h:hit2){
            item2=h.source().getKnownForTitles();
        }
        SearchResponse<EUser> a=null;
        try{
            RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
            builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                    return httpClientBuilder;
                }
            });
            RestClient restClient = builder.build();
            String index = "moviez";
            String endpoint = "/" + index + "/_search";
            String item1Str=decorateItem(item1,logger),item2Str=decorateItem(item2,logger);
//            logger.info(item1Str);
//            logger.info(item2Str);
            String query = "{\n" +
                    "  \"size\": 0,\n" +
                    "  \"aggs\":{\n" +
                    "    \"common_strings\":{\n" +
                    "      \"terms\":{\n" +
                    "        \"script\": {\n" +
                    "          \"source\": \"def array1=params.array1;def array2=params.array2;def common=[];for (string in array1){if(array2.contains(string)){common.add(string)}}return common\",\n" +
                    "          \"params\": {\n" +
                    "            \"array1\":["+item1Str+"],\n" +
                    "            \"array2\":["+item2Str+"]\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"size\": 10\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
//            logger.info(query);
//            SearchResponse<List<String>> a = client.search();
            Request request = new Request("POST", endpoint);
            request.setJsonEntity(query);
            RequestOptions requestOptions = RequestOptions.DEFAULT;
            Response response = restClient.performRequest(request);
//            logger.info(response.toString());
            String responseBody = EntityUtils.toString(response.getEntity());
//            logger.info(responseBody);
            restClient.close();
        }catch (IOException e){item1.add(e.toString());return item1;}
        item1.retainAll(item2);
        return item1;
    }

    private RestTemplate restTemplate = new RestTemplate();

    public SearchResponse<EUser> getMatch(String searchText) {
        SearchResponse<EUser> response = null;
        try {
            response = client.search(s -> s
                            .index("moviez")
                            .size(1000)
                            .query(q -> q
                                    .queryString(t -> t
                                            .defaultField("primaryName")
                                            .query(searchText)
                                    )
                            ),
                    EUser.class
            );
        } catch (IOException e) {
            return null;
        }
        return response;
    }


    public void bulkSave(){
        int max_page=10;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key","208ee6f2b8msha7df4e1ad2fe2c1p199566jsn199035d53e6c");
        headers.set("X-RapidAPI-Host","moviesdatabase.p.rapidapi.com");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        for(int i =1;i<max_page;i++){
            String url = "https://moviesdatabase.p.rapidapi.com/actors?page=";
            url+=Integer.toString(i)+"&limit=50";
            ResponseEntity<Root> response = restTemplate.exchange(url, HttpMethod.GET, entity, Root.class);
            if(response.getBody().results==null) {
//                System.out.println("here");
                break;
            }
//            System.out.println(i);
            for(Result res:response.getBody().results){
                List<String> titles = Arrays.asList(res.knownForTitles.split("\\s*,\\s*"));
                User newUser = new User(res.nconst,res.primaryName,res.birthYear,res.deathYear,res.primaryProfession,titles);
                repo.insert(newUser);
//                System.out.println(newUser);
                EUser newEuser = new EUser(res.nconst,res.primaryName,res.birthYear,res.deathYear,res.primaryProfession,titles);
                eRepo.save(newEuser);
//                System.out.println(newEuser);

            }
        }
    }
    public void saveUser(String user) {
        return;
//        user = StringUtils.capitalize(user);
//        System.out.println(user);
//        String url = "https://actor-movie-api1.p.rapidapi.com/getid/"+URLEncoder.encode(user, StandardCharsets.UTF_8)+"?apiKey=62ffac58c57333a136053150eaa1b587";
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("X-RapidAPI-Key","208ee6f2b8msha7df4e1ad2fe2c1p199566jsn199035d53e6c");
//        headers.set("X-RapidAPI-Host","actor-movie-api1.p.rapidapi.com");
//        HttpEntity<String> entity = new HttpEntity<String>(headers);
//        System.out.println("koko dayo");
//        ResponseEntity<Root[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Root[].class);
//        List<String> movies=new ArrayList<String>();
//        System.out.println(response.getBody());
//        System.out.println(response.getBody().getClass());
//        for(Root i:response.getBody()){
//            movies.add(i.title);
//        }
//        String finalUser = user;
//        repo.findUserByActor(user).ifPresentOrElse(s->{
//            System.out.println(s.getActor()+" already exists");
//        },()->{
//            User newUser = new User(finalUser,movies);
//            repo.insert(newUser);
//            EUser eUser = new EUser(finalUser,movies);
//            eRepo.save(eUser);
//        });
    }
}
