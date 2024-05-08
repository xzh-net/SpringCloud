package net.xzh.oauth2.service;

import net.xzh.oauth2.entity.SysUser;

/**
 * @date 2019-02-12
 */
public interface UserService {

    SysUser getByUsername(String username);
}
