package net.xzh.oauth.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.xzh.oauth.entity.SysUserRole;
import net.xzh.oauth.repository.SysUserRoleRepository;
import net.xzh.oauth.service.UserRoleService;

/**
 * 
 * @date 2019-02-12
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;


	@Override
	public List<SysUserRole> findByUserId(Integer userId) {
		return sysUserRoleRepository.findByUserId(userId);
	}
}
