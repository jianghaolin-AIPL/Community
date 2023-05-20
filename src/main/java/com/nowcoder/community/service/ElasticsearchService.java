package com.nowcoder.community.service;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private SearchOperations operations;

    public void saveDiscussPost(DiscussPost discussPost) {
        discussPostRepository.save(discussPost);
    }

    public void deleteDiscussPost(int id) {
        discussPostRepository.deleteById(id);
    }

    public SearchHits<DiscussPost> searchDiscussPost(String keyword, int current, int limit) {
        // 构造搜索条件，要构造一系列内容：搜索的关键词、排序方式、分页方式、高亮显示
        Query query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m.fields("content", "title").query(keyword)))
                .withSort(s -> s.field(f -> f.field("type").order(SortOrder.Desc)))
                .withSort(s -> s.field(f -> f.field("score").order(SortOrder.Desc)))
                .withSort(s -> s.field(f -> f.field("createTime").order(SortOrder.Desc)))
                .withPageable(PageRequest.of(current, limit))
//                .withHighlightQuery(new HighlightQuery(new Highlight()))
                .build();
        SearchHits<DiscussPost> searchHits = operations.search(query, DiscussPost.class);
        return searchHits;
    }
}
