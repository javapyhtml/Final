package com.icia.rmate.service.base;
import java.io.Serializable;
import java.util.List;

import com.icia.rmate.dto.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.icia.rmate.dao.base.BaseDao;
import com.icia.rmate.dao.base.Page;
import com.icia.rmate.parameter.SearchParameter;
public class BaseService<T, ID extends Serializable, Dao extends BaseDao<T, ID>> {
  @Autowired
  protected Dao dao;
  @Transactional(readOnly = true)
  public List<T> getList(SearchParameter searchParameter) {
    return dao.selectList(searchParameter);
  }
  @Transactional(readOnly = true)
  public long getListCount(SearchParameter searchParameter) {
    return dao.selectListCount(searchParameter);
  }
  @Transactional(readOnly = true)
  public Page<T> getPage(SearchParameter searchParameter) {
    List<T> contents = dao.selectList(searchParameter);
    long totalCount = dao.selectListCount(searchParameter);
    return new Page<>(contents, searchParameter.getPageable(), totalCount);
  }
  @Transactional(readOnly = true)
  public Node getOne(ID id) {
    return (Node) dao.selectOne(id);
  }
  @Transactional(readOnly = true)
  public T getOneByParam(SearchParameter searchParameter) {
    return dao.selectOneByParam(searchParameter);
  }
  @Transactional
  public int add(Node t) {
    return dao.insert((T) t);
  }
  @Transactional
  public int mod(T t) {
    return dao.update(t);
  }
  @Transactional
  public int del(ID id) {
    return dao.delete(id);
  }
}
