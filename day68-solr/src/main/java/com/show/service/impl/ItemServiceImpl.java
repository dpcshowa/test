package com.show.service.impl;

import com.show.dao.ItemDao;
import com.show.pojo.PageMsg;
import com.show.service.ItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;


    @Override
    public PageMsg queryIndex(String queryString, String catalog_name, String price, int curPage, int sort, int rows) {
        SolrQuery query = new SolrQuery();

        if (queryString != null && !"".equals(queryString)) {
            query.setQuery(queryString);
        } else {
            query.setQuery("*:*");
        }

//        query.setFilterQueries(new String[]{"product_price:[120 TO *]"});
        if (price != null && !"".equals(price)) {
            String[] prices = price.split("-");
            query.addFilterQuery("product_price:[" + prices[0] + " TO " + prices[1] + "]");
        }

        if (catalog_name != null && !"".equals(catalog_name)) {
            query.addFilterQuery("product_catalog_name:" + catalog_name);
        }

        if (sort == 1){
            query.setSort("product_price", SolrQuery.ORDER.asc);
        } else if (sort == 2){
            query.setSort("product_price", SolrQuery.ORDER.desc);
        }


        query.setStart((curPage - 1) * rows);
        query.setRows(rows);

//        query.setFields("product_price,product_name");

        query.set("df","product_name");

        // 设置高亮
        query.setHighlight(true);

        query.addHighlightField("product_name");

        query.setHighlightSimplePre("<font color='yellow'>");
        query.setHighlightSimplePost("</font>");


        PageMsg pageMsg = itemDao.qurryIndex(query);

        pageMsg.setCurPage(curPage);

        int totalPage = (int) (pageMsg.getTotalItems() / rows);

        if (pageMsg.getTotalItems() % rows > 0){
           totalPage += 1;
        }

        pageMsg.setTotalPage(totalPage);

        return pageMsg;
    }
}
