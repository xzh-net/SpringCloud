package net.xzh.oauth.service;

import java.util.List;

import net.xzh.oauth.entity.SysUserRole;

/**
 * 用户角色
 * @date 2019-02-12
 */
public interface UserRoleService {

    List<SysUserRole> findByUserId(Integer userId);

}
