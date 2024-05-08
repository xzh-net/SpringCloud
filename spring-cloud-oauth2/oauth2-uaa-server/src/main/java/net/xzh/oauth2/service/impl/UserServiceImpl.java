package net.xzh.oauth2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.xzh.oauth2.entity.SysUser;
import net.xzh.oauth2.repository.SysUserRepository;
import net.xzh.oauth2.service.UserService;

/**
 * @date 2019-02-12
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public SysUser getByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }
}
