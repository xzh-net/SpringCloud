package net.xzh.oauth2.service;

import java.util.List;

import net.xzh.oauth2.entity.SysPermission;

/**
 * @date 2019-02-12
 */
public interface PermissionService {

    List<SysPermission> findByUserId(Integer userId);

}
