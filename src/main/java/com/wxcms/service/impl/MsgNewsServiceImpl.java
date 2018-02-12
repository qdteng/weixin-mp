package com.wxcms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.page.Pagination;
import com.wxapi.process.MsgType;
import com.wxcms.domain.MsgBase;
import com.wxcms.domain.MsgNews;
import com.wxcms.domain.MsgNewsVO;
import com.wxcms.mapper.MsgBaseDao;
import com.wxcms.mapper.MsgNewsDao;
import com.wxcms.service.MsgNewsService;


@Service
public class MsgNewsServiceImpl implements MsgNewsService{

	@Autowired
	private MsgBaseDao baseDao;
	
	@Autowired
	private MsgNewsDao entityDao;

	public MsgNews getById(String id){
		return entityDao.getById(id);
	}

	public List<MsgNews> listForPage(MsgNews searchEntity){
		return entityDao.listForPage(searchEntity);
	}
	
	public List<MsgNewsVO> pageWebNewsList(MsgNews searchEntity,Pagination<MsgNews> page){
		List<MsgNews> list = entityDao.pageWebNewsList(searchEntity,page);
		List<MsgNewsVO> pageList = new ArrayList<MsgNewsVO>();
		for(MsgNews msg : list){
			if(pageList.size() == 0){
				MsgNewsVO vo = new MsgNewsVO();
				vo.setCreateTimeStr(msg.getCreateTimeStr());
				vo.getMsgNewsList().add(msg);
				pageList.add(vo);
			}else{
				MsgNewsVO tmpMsgNewsVO = pageList.get(pageList.size() - 1);
				if(tmpMsgNewsVO.getCreateTimeStr().equals(msg.getCreateTimeStr())){
					tmpMsgNewsVO.getMsgNewsList().add(msg);
				}else{
					MsgNewsVO vo = new MsgNewsVO();
					vo.setCreateTimeStr(msg.getCreateTimeStr());
					vo.getMsgNewsList().add(msg);
					pageList.add(vo);
				}
			}
		}
		return pageList;
	}

	
	public void add(MsgNews entity){
		
		MsgBase base = new MsgBase();
		base.setInputcode(entity.getInputcode());
		base.setCreatetime(new Date());
		base.setMsgtype(MsgType.News.toString());
		baseDao.add(base);
		
		entity.setBaseId(base.getId());
		entityDao.add(entity);
		
		if(StringUtils.isEmpty(entity.getFromurl())){
			entity.setUrl(entity.getUrl()+"?id="+entity.getId());
		}else{
			entity.setUrl("");
		}
		
		entityDao.updateUrl(entity);
	}

	public void update(MsgNews entity){
		MsgBase base = baseDao.getById(entity.getBaseId().toString());
		base.setInputcode(entity.getInputcode());
		baseDao.updateInputcode(base);
		
		if(StringUtils.isEmpty(entity.getFromurl())){
			entity.setUrl(entity.getUrl()+"?id="+entity.getId());
		}else{
			entity.setUrl("");
		}
		
		entityDao.update(entity);
	}

	public void delete(MsgNews entity){
		MsgBase base = new MsgBase();
		base.setId(entity.getBaseId());
		entityDao.delete(entity);
		baseDao.delete(entity);
	}

	public List<MsgNews> getRandomMsg(String inputCode,Integer num){
		return entityDao.getRandomMsgByContent(inputCode,num);
	}

}
