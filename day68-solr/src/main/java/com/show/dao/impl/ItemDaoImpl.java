package com.show.dao.impl;

import com.show.dao.ItemDao;
import com.show.pojo.Item;
import com.show.pojo.PageMsg;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ItemDaoImpl implements ItemDao {

    @Autowired
    private SolrServer solrServer;

    @Override
    public PageMsg qurryIndex(SolrQuery solrQuery) {
        PageMsg pageMsg = new PageMsg();

        List<Item> itemList = new ArrayList<>();

        try {
            QueryResponse response = solrServer.query(solrQuery);

            SolrDocumentList results = response.getResults();

            long numFound = results.getNumFound();
            pageMsg.setTotalItems(numFound);

            for (SolrDocument result : results) {
                Item item = new Item();

                String id = (String) result.get("id");
                item.setPid(Integer.parseInt(id));

                String product_name = (String) result.get("product_name");

                // 高亮显示
                Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

                Map<String, List<String>> stringListMap = highlighting.get(id);

                List<String> list = stringListMap.get("product_name");

                if (list != null && list.size() > 0) {
                    product_name = list.get(0);
                }

                item.setName(product_name);

                float product_price = (float) result.get("product_price");
                item.setPrice(product_price);

                String product_catalog_name = (String) result.get("product_catalog_name");
                item.setCatalog_name(product_catalog_name);

                String product_picture = (String) result.get("product_picture");
                item.setPicture(product_picture);

                itemList.add(item);
            }

            pageMsg.setItems(itemList);

            return pageMsg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
