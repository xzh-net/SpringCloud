package net.xzh.oauth.service;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import net.xzh.oauth.domain.MyUser;
import net.xzh.oauth.entity.SysPermission;
import net.xzh.oauth.entity.SysUser;
import net.xzh.oauth.entity.SysUserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengJianSheng
 * @date 2019-02-11
 */
@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRoleService userRoleservice;
    
    
    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userService.getByUsername(username);
        if (null == sysUser) {
            log.warn("用户{}不存在", username);
            throw new UsernameNotFoundException(username);
        }
        //获取用户权限
        List<SysPermission> permissionList = permissionService.findByUserId(sysUser.getId());
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            for (SysPermission sysPermission : permissionList) {
                authorityList.add(new SimpleGrantedAuthority(sysPermission.getCode()));
            }
        }
        //获取用户角色
        List<SysUserRole> userRoleList = userRoleservice.findByUserId(sysUser.getId());
        List<SimpleGrantedAuthority> authorityList2 = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoleList)) {
            for (SysUserRole sysUserRole : userRoleList) {
            	authorityList2.add(new SimpleGrantedAuthority(sysUserRole.getRoleId()+""));
            }
        }
        //根据需要放入
        MyUser myUser = new MyUser(sysUser.getUsername(), passwordEncoder.encode(sysUser.getPassword()), authorityList2);
        log.info("登录成功！用户: {}", JSON.toJSONString(myUser));
        return myUser;
    }
}
