package net.xzh.order.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @date 2019-03-03
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/list")
    public String list(ModelMap modelMap, Authentication authentication) {
        OAuth2Authentication oauth2Authentication = (OAuth2Authentication)authentication;
        modelMap.put("username", oauth2Authentication.getName());
        modelMap.put("authorities", oauth2Authentication.getAuthorities());
        modelMap.put("clientId", oauth2Authentication.getOAuth2Request().getClientId());
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)oauth2Authentication.getDetails();
        modelMap.put("token", details.getTokenValue());
        return "order/list";
    }

}
