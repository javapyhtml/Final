package com.icia.rmate.dao;
import com.icia.rmate.dto.Job;
import org.apache.ibatis.annotations.Mapper;
import com.icia.rmate.dao.base.BaseDao;

@Mapper
public interface JobDao extends BaseDao<Job, Long> {
}
