package com.enicom.nims;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.enicom.nims.login.RSAUtil;
import com.enicom.nims.login.SessionManager;

@Controller
public class HomeController {
	private RSAUtil rsa;

	@Autowired
	public HomeController(RSAUtil rsa) {
		this.rsa = rsa;
	}

	/**
	 * @throws Exception
	 * @Desc Login/Login
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return login(request, response);
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		rsa.generate(request);
		return "single/login";
	}

	/**
	 * @Desc Main Page
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main() {
		return "main/main";
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard() {
		return "single/dashboard";
	}

	/**
	 * @Desc Login/Logout
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		SessionManager.destroy(session);
		return login(request, response);
	}

	/**
	 * @Desc Menu Page
	 */
	@RequestMapping(value = "/{folder}/{page}", method = { RequestMethod.GET, RequestMethod.POST })
	public String goPage(HttpServletRequest request, HttpServletResponse response, @PathVariable String folder,
			@PathVariable String page) {
		String path = String.format("%s/%s", folder, page);
		if (!SessionManager.hasSession(request))
			return main();
		else if (hasPrivileges(request, "/" + path))
			return "main/" + path;
		else
			return "common/error";
	}

	private boolean hasPrivileges(HttpServletRequest request, String path) {
		List<Map<String, Object>> menuList = (List<Map<String, Object>>) request.getSession().getAttribute("MENU_LIST");
		boolean result = menuList.stream().filter(menu -> menu.get("menu_url").toString().equals(path)).count() > 0;
		if (result)
			return true;
		return false;
	}

	/**
	 * @Desc did page
	 */
	@RequestMapping(value = "/did/book", method = RequestMethod.GET)
	public String searchbook() {
		return "did/book";
	}
}
