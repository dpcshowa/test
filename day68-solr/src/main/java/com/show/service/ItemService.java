package com.show.service;

import com.show.pojo.PageMsg;

public interface ItemService {

    PageMsg queryIndex(String queryString, String catalog_name, String price, int curPage, int sort, int rows);
}
