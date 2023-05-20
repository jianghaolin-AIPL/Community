package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.ElasticsearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    // 当前不是POST请求，是GET，这个keyword就不能用请求体来传，要么用路径后面带问号传，要么用路径中的某一级来传
    // 这里用带问号的方式来传 /search?keyword=xxx
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) {
        // 搜索帖子
        SearchHits<DiscussPost> searchResult =
        elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit()); // 我们封装的page当前页是从1开始，所以要减一

        // 聚合数据
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(searchResult != null) {
            for (SearchHit<DiscussPost> discussPostSearchHit : searchResult) {
                DiscussPost discussPost = discussPostSearchHit.getContent();
                Map<String, Object> map = new HashMap<>();
                // 帖子
                map.put("post", discussPost);
                // 作者
                map.put("user", userService.findUserById(discussPost.getUserId()));
                // 点赞数量
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPost.getId()));

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        // 当返回页面的时候，希望把输入的keyword也设置进去
        model.addAttribute("keyword", keyword);

        // 分页信息
        page.setPath("/search?keyword=" + keyword);
        page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalHits());

        return "/site/search";
    }
}
