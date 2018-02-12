package com.wxcms.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.core.page.Pagination;
import com.wxcms.domain.AccountFans;
import com.wxcms.service.AccountFansService;

/**
 * 
 */

@Controller
@RequestMapping("/accountfans")
public class AccountFansCtrl{

	@Autowired
	private AccountFansService entityService;

	@RequestMapping(value = "/getById")
	public ModelAndView getById(String id){
		entityService.getById(id);
		return new ModelAndView();
	}

	@RequestMapping(value = "/list")
	public  ModelAndView list(AccountFans searchEntity){
		ModelAndView mv = new ModelAndView("wxcms/paginationEntity");
		return mv;
	}

	@RequestMapping(value = "/paginationEntity")
	public  ModelAndView paginationEntity(AccountFans searchEntity, Pagination<AccountFans> pagination){
		ModelAndView mv = new ModelAndView("wxcms/fansPagination");
		pagination = entityService.paginationEntity(searchEntity,pagination);
		mv.addObject("pagination",pagination);
		mv.addObject("cur_nav","fans");
		return mv;
	}
	
	@RequestMapping(value = "/toMerge")
	public ModelAndView toMerge(AccountFans entity){

		return new ModelAndView();
	}

	@RequestMapping(value = "/merge")
	public ModelAndView doMerge(AccountFans entity){
		entityService.add(entity);
		return new ModelAndView();
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(AccountFans entity){
		entityService.delete(entity);
		return new ModelAndView();
	}



}