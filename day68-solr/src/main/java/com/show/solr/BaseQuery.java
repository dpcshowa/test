package com.show.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class BaseQuery {

    @Test
    public void query() throws Exception {

        SolrQuery query = new SolrQuery();

        query.setQuery("五件套");

//        query.setFilterQueries(new String[]{"product_price:[120 TO *]"});
        query.addFilterQuery("product_price:[120 TO *]");
        query.addFilterQuery("product_catalog_name:时尚卫浴");

        query.setSort("product_price", SolrQuery.ORDER.desc);

        query.setStart(0);
        query.setRows(3);

//        query.setFields("product_price,product_name");

        query.set("df","product_name");

        // 设置高亮
        query.setHighlight(true);

        query.addHighlightField("product_name");

        query.setHighlightSimplePre("<font color='yellow'>");
        query.setHighlightSimplePost("</font>");

        queryForItem(query);
    }

    private void queryForItem(SolrQuery query) throws Exception {
        String url = "http://localhost:8080/solr/item";

        SolrServer solrServer = new HttpSolrServer(url);

        QueryResponse response = solrServer.query(query);

        SolrDocumentList results = response.getResults();

        long numFound = results.getNumFound();
        System.out.println("总记录数为: " + numFound);

        for (SolrDocument result : results) {

            String id = (String) result.get("id");
            System.out.println("文档域id: " + id);

            String product_name = (String) result.get("product_name");

            // 高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

            Map<String, List<String>> stringListMap = highlighting.get(id);

            List<String> list = stringListMap.get("product_name");

            if (list != null && list.size() > 0){
                product_name = list.get(0);
            }

            System.out.println("商品名称: " + product_name);

            float product_price = (float) result.get("product_price");
            System.out.println("商品价格: " + product_price);

            String product_catalog_name = (String) result.get("product_catalog_name");
            System.out.println("商品类别: " + product_catalog_name);

            String product_picture = (String) result.get("product_picture");
            System.out.println("商品图片: " + product_picture);
        }
    }
}
