package com.wxcms.ctrl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
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
import com.wxapi.process.WxMemoryCacheClient;
import com.wxcms.domain.Account;
import com.wxcms.mapper.AccountDao;
import com.wxcms.mapper.MsgNewsDao;

/**
 * 
 */

@Controller
@RequestMapping("/wxcms")
public class WxCmsCtrl {

	@Autowired
	AccountDao accountDao;
	
	@Autowired
	MsgNewsDao msgNewsDao;
	
	@RequestMapping(value = "/urltoken")
	public ModelAndView urltoken(String save,HttpServletRequest request){
		
		if (null==request.getSession().getAttribute("pass")){   return new ModelAndView("index");}
		ModelAndView mv = new ModelAndView("wxcms/urltoken");
		List<Account> accounts = accountDao.listForPage(null);
		if(!CollectionUtils.isEmpty(accounts)){
			mv.addObject("account",accounts.get(0));
		}else{
			mv.addObject("account",new Account());
		}
		mv.addObject("cur_nav", "urltoken");
		if(save != null){
			mv.addObject("successflag",true);
		}
		return mv;
	}
	
	@RequestMapping(value = "/getUrl")
	public ModelAndView getUrl(HttpServletRequest request ,@ModelAttribute Account account){
		String path = SpringFreemarkerContextPathUtil.getBasePath(request);
		String url = request.getScheme() + "://" + request.getServerName() + path + "/wxapi/" + account.getAccount()+"/message.html";
		
		if(account.getId() == null){//新增
			account.setUrl(url);
			account.setToken(UUID.randomUUID().toString().replace("-", ""));
			account.setCreatetime(new Date());
			accountDao.add(account);
		}else{//更新
			Account tmpAccount = accountDao.getById(account.getId().toString());
			tmpAccount.setUrl(url);
			tmpAccount.setAccount(account.getAccount());
			tmpAccount.setAppid(account.getAppid());
			tmpAccount.setAppsecret(account.getAppsecret());
			tmpAccount.setMsgcount(account.getMsgcount());
			accountDao.update(tmpAccount);
		}
		WxMemoryCacheClient.addMpAccount(account);
		return new ModelAndView("redirect:/wxcms/urltoken.html?save=true");
	}
	
	@RequestMapping(value = "/ckeditorImage")
	public void ckeditorImage(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="imgFile",required=false)MultipartFile file){
		String contextPath = SpringFreemarkerContextPathUtil.getBasePath(request);
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath;
		String realPath = request.getSession().getServletContext().getRealPath("/");
		
		//读取配置文上传件的路径
		if(PropertiesConfigUtil.getProperty("upload.properties","upload.path") != null){
			realPath = PropertiesConfigUtil.getProperty("upload.properties","upload.path").toString();
		}
		
		JSONObject obj = new JSONObject();
		if(file != null && file.getSize() > 0){
			String tmpPath = UploadUtil.doUpload(realPath,file);//上传文件，上传文件到 /res/upload/ 下
			obj.put("error", 0);
			obj.put("url", url + tmpPath);
		}
		
		try {
			response.getWriter().write(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//上传永久素材，这里以图文消息为例子
	@RequestMapping(value = "/toUploadMaterial")
	public  ModelAndView toUploadMaterial(String[] newIds){
		ModelAndView mv = new ModelAndView("wxcms/materialUpload");
		mv.addObject("cur_nav", "material");
		return mv;
	}
	
	//到生成二维码页面
	@RequestMapping(value = "/qrcode")
	public ModelAndView qrcode(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("wxcms/qrcode");
		mv.addObject("cur_nav", "qrcode");
		return mv;
	}
	
	//发送消息页面
	@RequestMapping(value = "/sendMsg")
	public ModelAndView sendMsg(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("wxcms/sendmsg");
		mv.addObject("cur_nav", "sendmsg");
		
		return mv;
	}
	
	//通过interceptor处理OAuth认证
	@RequestMapping(value = "/oauthInterceptor")
	public ModelAndView oauthInterceptor(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("wxcms/oauthInterceptor");
		mv.addObject("cur_nav", "oauthInterceptor");
		
		return mv;
	}
	
	//jssdk
	@RequestMapping(value = "/jssdk")
	public ModelAndView jssdk(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("wxcms/jssdk");
		mv.addObject("cur_nav", "jssdk");
		
		return mv;
	}
	
	
}
