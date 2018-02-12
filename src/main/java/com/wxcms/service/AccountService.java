package com.wxcms.service;

import java.util.List;

import com.wxcms.domain.Account;


public interface AccountService {

	public Account getById(String id);
	
	public Account getByAccount(String account);
	
	public List<Account> listForPage(Account searchEntity);

	public void add(Account entity);

	public void update(Account entity);

	public void delete(Account entity);



}