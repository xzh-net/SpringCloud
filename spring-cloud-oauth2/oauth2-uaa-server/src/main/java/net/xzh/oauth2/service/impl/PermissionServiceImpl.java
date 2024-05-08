package net.xzh.oauth2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import net.xzh.oauth2.entity.SysPermission;
import net.xzh.oauth2.entity.SysRolePermission;
import net.xzh.oauth2.entity.SysUserRole;
import net.xzh.oauth2.repository.SysPermissionRepository;
import net.xzh.oauth2.repository.SysRolePermissionRepository;
import net.xzh.oauth2.repository.SysUserRoleRepository;
import net.xzh.oauth2.service.PermissionService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @date 2019-02-12
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;
    @Autowired
    private SysRolePermissionRepository sysRolePermissionRepository;
    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @Override
    public List<SysPermission> findByUserId(Integer userId) {
        List<SysUserRole> sysUserRoleList = sysUserRoleRepository.findByUserId(userId);
        if (CollectionUtils.isEmpty(sysUserRoleList)) {
            return null;
        }
        List<Integer> roleIdList = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        List<SysRolePermission> rolePermissionList = sysRolePermissionRepository.findByRoleIds(roleIdList);
        if (CollectionUtils.isEmpty(rolePermissionList)) {
            return null;
        }
        List<Integer> permissionIdList = rolePermissionList.stream().map(SysRolePermission::getPermissionId).distinct().collect(Collectors.toList());
        List<SysPermission> sysPermissionList = sysPermissionRepository.findByIds(permissionIdList);
        if (CollectionUtils.isEmpty(sysPermissionList)) {
            return null;
        }
        return sysPermissionList;
    }
}
