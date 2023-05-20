package com.nowcoder.community;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOptionsBuilders;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTests {
    // 数据来源是mysql，从mysql中取到数据再转存到ES服务器
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    // 有些特殊情况Repository解决不了，还需要调用Template
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SearchOperations operations;

    // 插入数据 每次插入一条数据
    @Test
    public void testInsert() {
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(241));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(242));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(243));
    }

    // 一次插入多条数据
    @Test
    public void testInsertList() {
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101, 0, 100, 0)); // 这个人的数据可能没有100行，全部都能插入ES
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(100, 0, 100, 0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103, 0, 100, 0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(111, 0, 100, 0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(112, 0, 100, 0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(131, 0, 100, 0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(132, 0, 100, 0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(133, 0, 100, 0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(134, 0, 100, 0));
    }

    // 修改数据
    @Test
    public void testUpdate() {
        DiscussPost post = discussPostMapper.selectDiscussPostById(231);
        post.setContent("我是新人，使劲灌水！");
        discussPostRepository.save(post);
    }

    // 删除数据
    @Test
    public void testDelete() {
        // 删一条数据
//        discussPostRepository.deleteById(231);
        // 删所有数据
        discussPostRepository.deleteAll();
    }

    // 搜索
    @Test
    public void testSearchByRepository() {
        // 构造搜索条件，要构造一系列内容：搜索的关键词、排序方式、分页方式、高亮显示
        Query query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m.fields("content", "title").query("互联网寒冬")))
                .withSort(s -> s.field(f -> f.field("type").order(SortOrder.Desc)))
                .withSort(s -> s.field(f -> f.field("score").order(SortOrder.Desc)))
                .withSort(s -> s.field(f -> f.field("createTime").order(SortOrder.Desc)))
                .withPageable(PageRequest.of(0, 10))
//                .withHighlightQuery(new HighlightQuery(new Highlight()))
                .build();
        SearchHits<DiscussPost> searchHits = operations.search(query, DiscussPost.class);

        System.out.println(searchHits.getTotalHits());
        if(searchHits.getTotalHits() == 0) {
            return;
        }
        List<DiscussPost> list = new ArrayList<>();
        for (SearchHit<DiscussPost> searchHit : searchHits) {
            DiscussPost discussPost = searchHit.getContent();

            list.add(discussPost);
        }

        System.out.println(list.size());
        for(DiscussPost post : list) {
            System.out.println(post);
        }

    }
}
