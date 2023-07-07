package com.task1.take1;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.task1.take1.eModel.EUser;
import com.task1.take1.models.User;
import com.task1.take1.repository.UserRepository;
import com.task1.take1.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private UserRepository repo;
    @GetMapping
    public List<String> fetchUser(@RequestBody List<String> users){

        return userService.getCommonUsers(users);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public User retrieve(@PathVariable String id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID: " + id));
    }

//    @PostMapping
//    public ResponseEntity addUser(@RequestBody List<String> users){
//        for(String user:users)
//        userService.saveUser(user);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping
    public ResponseEntity addUserBulk(){
        userService.bulkSave();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/match", produces = "application/json")
    public List<EUser> match(@RequestBody String searchText){
        Logger logger= LoggerFactory.getLogger(UserController.class);
        SearchResponse<EUser> response = userService.getMatch(searchText);
        List<Hit<EUser>> hits = response.hits().hits();
        List<EUser> ret = new ArrayList<EUser>();
        for(Hit<EUser> h:hits){
            ret.add(h.source());
        }
        return ret;
    }
}