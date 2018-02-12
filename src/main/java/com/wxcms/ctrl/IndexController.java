package com.wxcms.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping()
public class IndexController {
	@RequestMapping(value = "/index")
	public ModelAndView index(@RequestParam(required=false) String pass,HttpServletRequest request){
		
		if (StringUtils.isNotBlank(pass)&&pass.equals("111111")){
			request.getSession().setAttribute("pass", pass);
			return new ModelAndView("redirect:/wxcms/urltoken.html");
		}
		
		return new ModelAndView("index");
	}
}
