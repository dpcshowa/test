package com.show.dao;

import com.show.pojo.PageMsg;
import org.apache.solr.client.solrj.SolrQuery;

public interface ItemDao {

    PageMsg qurryIndex(SolrQuery solrQuery);
}
