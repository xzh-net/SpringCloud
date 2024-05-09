package net.xzh.oauth.service;

import net.xzh.oauth.entity.SysUser;

/**
 * 用户信息
 * @date 2019-02-12
 */
public interface UserService {

    SysUser getByUsername(String username);
}
