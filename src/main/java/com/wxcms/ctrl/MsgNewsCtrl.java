package com.wxcms.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.core.spring.SpringFreemarkerContextPathUtil;
import com.core.util.PropertiesConfigUtil;
import com.core.util.UploadUtil;
import com.wxcms.domain.MsgBase;
import com.wxcms.domain.MsgNews;
import com.wxcms.service.MsgBaseService;
import com.wxcms.service.MsgNewsService;
/**
 * 
 */

@Controller
@RequestMapping("/msgnews")
public class MsgNewsCtrl{

	@Autowired
	private MsgNewsService entityService;
	
	@Autowired
	private MsgBaseService baseService;
	
	
	@RequestMapping(value = "/getById")
	public ModelAndView getById(String id){
		entityService.getById(id);
		return new ModelAndView();
	}

	@RequestMapping(value = "/list")
	public  ModelAndView list(@ModelAttribute MsgNews searchEntity){
		ModelAndView modelAndView = new ModelAndView("wxcms/msgnewsList");
		List<MsgNews> pageList = entityService.listForPage(searchEntity);
		modelAndView.addObject("pageList", pageList);
		modelAndView.addObject("cur_nav", "news");
		return modelAndView;
	}

	@RequestMapping(value = "/toMerge")
	public ModelAndView toMerge(MsgNews entity){
		ModelAndView mv = new ModelAndView("wxcms/msgnewsMerge");
		mv.addObject("cur_nav", "news");
		
		if(entity.getId() != null){
			MsgNews news = entityService.getById(entity.getId().toString());
			mv.addObject("entity",news);
			mv.addObject("baseEntity", baseService.getById(news.getBaseId().toString()));
		}else{
			mv.addObject("entity", new MsgNews());
			mv.addObject("baseEntity", new MsgBase());
		}
		return mv;
	}

	@RequestMapping(value = "/doMerge")
	public ModelAndView doMerge(HttpServletRequest request,MsgNews entity,@RequestParam(value="imageFile",required=false)MultipartFile file){
		String contextPath = SpringFreemarkerContextPathUtil.getBasePath(request);
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath;
		String realPath = request.getSession().getServletContext().getRealPath("/");
		
		//读取配置文上传件的路径
		if(PropertiesConfigUtil.getProperty("upload.properties","upload.path") != null){
			realPath = PropertiesConfigUtil.getProperty("upload.properties","upload.path").toString();
		}
		
		if(file != null && file.getSize() > 0){
			String tmpPath = UploadUtil.doUpload(realPath,file);//上传文件，上传文件到 /res/upload/ 下
			entity.setPicpath(url + tmpPath);
		}else{
			if(entity.getId() != null){//更新
				entity.setPicpath(entityService.getById(entity.getId().toString()).getPicpath());
			}
		}
		
		if(!StringUtils.isEmpty(entity.getFromurl())){
			String fromUrl = entity.getFromurl();
			if(!fromUrl.startsWith("http://")){
				entity.setFromurl("http://" + fromUrl);
			}
		}else{
			entity.setUrl(url + "/wxweb/msg/newsread.html");//设置微信访问的url
		}
		
		if(entity.getId() != null){//跟新
			entityService.update(entity);
		}else{
			entityService.add(entity);
		}
		return new ModelAndView("redirect:/msgnews/list.html");
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(MsgNews entity){
		entityService.delete(entity);
		return new ModelAndView("redirect:/msgnews/list.html");
	}



}