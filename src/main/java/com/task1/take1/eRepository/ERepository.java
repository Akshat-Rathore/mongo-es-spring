package com.task1.take1.eRepository;

import com.task1.take1.eModel.EUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ERepository extends ElasticsearchRepository<EUser,String> {
}
