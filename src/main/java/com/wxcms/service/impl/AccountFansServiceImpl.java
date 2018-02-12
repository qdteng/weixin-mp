package com.wxcms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.page.Pagination;
import com.wxcms.domain.AccountFans;
import com.wxcms.mapper.AccountFansDao;
import com.wxcms.service.AccountFansService;


@Service
public class AccountFansServiceImpl implements AccountFansService{

	@Autowired
	private AccountFansDao entityDao;

	public AccountFans getById(String id){
		return entityDao.getById(id);
	}

	public AccountFans getByOpenId(String openId){
		return entityDao.getByOpenId(openId);
	}
	
	public List<AccountFans> list(AccountFans searchEntity){
		return entityDao.list(searchEntity);
	}

	public Pagination<AccountFans> paginationEntity(AccountFans searchEntity,Pagination<AccountFans> pagination){
		Integer totalItemsCount = entityDao.getTotalItemsCount(searchEntity);
		List<AccountFans> items = entityDao.paginationEntity(searchEntity,pagination);
		pagination.setTotalItemsCount(totalItemsCount);
		pagination.setItems(items);
		return pagination;
	}
	
	public AccountFans getLastOpenId(){
		return entityDao.getLastOpenId();
	}
	
	public void sync(AccountFans searchEntity){
		AccountFans lastFans = entityDao.getLastOpenId();
		String lastOpenId = "";
		if(lastFans != null){
			lastOpenId = lastFans.getOpenId();
		}
		
		
	}

	public void add(AccountFans entity){
		entityDao.add(entity);
	}

	public void update(AccountFans entity){
		entityDao.update(entity);
	}

	public void delete(AccountFans entity){
		entityDao.delete(entity);
	}

	public void deleteByOpenId(String openId){
		entityDao.deleteByOpenId(openId);
	}

}