package com.wxcms.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.core.page.Pagination;
import com.core.util.HttpRequestDeviceUtils;
import com.wxapi.process.WxMemoryCacheClient;
import com.wxcms.domain.MsgNews;
import com.wxcms.domain.MsgNewsVO;
import com.wxcms.service.MsgNewsService;

/**
 * 手机微信页面
 */
@Controller
@RequestMapping("/wxweb")
public class WxWebCtrl {
	
	@Autowired
	private MsgNewsService msgNewsService;
	
	@RequestMapping(value = "/msg/newsread")
	public ModelAndView newsread(HttpServletRequest request,String id){
		ModelAndView mv = new ModelAndView("wxweb/mobileNewsRead");
		MsgNews news = msgNewsService.getById(id);
		mv.addObject("news",news);
		
		if(!HttpRequestDeviceUtils.isMobileDevice(request)){
			mv.setViewName("wxweb/pcNewsRead");
		}
		return mv;
	}
	
	@RequestMapping(value = "/msg/newsList")
	public ModelAndView pageWebNewsList(HttpServletRequest request,MsgNews searchEntity,Pagination<MsgNews> page){
		ModelAndView mv = new ModelAndView("wxweb/mobileNewsList");
		List<MsgNewsVO> pageList = msgNewsService.pageWebNewsList(searchEntity,page);
		mv.addObject("pageList", pageList);
		return mv;
	}
	
	@RequestMapping(value = "/jssdk")
	public ModelAndView jssdk(HttpServletRequest request,String api){
		ModelAndView mv = new ModelAndView("wxweb/jssdk");
		if(!StringUtils.isBlank(api)){
			mv.addObject("api", api);
		}
		return mv;
	}
	
	@RequestMapping(value = "/sendmsg")
	public ModelAndView sendmsg(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("wxweb/sendmsg");
		//拦截器已经处理了缓存,这里直接取
		String openid = WxMemoryCacheClient.getOpenid(request.getSession().getId());
		mv.addObject("openid", openid);
		return mv;
	}
	
}

