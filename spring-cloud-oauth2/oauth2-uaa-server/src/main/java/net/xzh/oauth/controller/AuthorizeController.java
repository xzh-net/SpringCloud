package net.xzh.oauth.controller;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 自定义授权确认
 * @Date
 */
@SessionAttributes("authorizationRequest")
@Controller
public class AuthorizeController {

    @RequestMapping("/custom/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("authorize");
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scopes",authorizationRequest.getScope());
        return view;
    }

}
