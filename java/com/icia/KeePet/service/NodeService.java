package com.icia.rmate.service;
import com.icia.rmate.dto.NodeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icia.rmate.dao.NodeDao;
import com.icia.rmate.service.base.BaseService;
@Transactional(readOnly = true)
@Service
public class NodeService extends BaseService<NodeDTO, Long, NodeDao> {
}
