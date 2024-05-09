package net.xzh.oauth.service;

import java.util.List;

import net.xzh.oauth.entity.SysPermission;

/**
 * @author ChengJianSheng
 * @date 2019-02-12
 */
public interface PermissionService {

    List<SysPermission> findByUserId(Integer userId);

}
