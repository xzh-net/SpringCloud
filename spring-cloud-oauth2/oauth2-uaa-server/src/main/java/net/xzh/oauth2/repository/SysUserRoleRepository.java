package net.xzh.oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.xzh.oauth2.entity.SysUserRole;

import java.util.List;

/**
 * @date 2019-02-12
 */
public interface SysUserRoleRepository extends JpaSpecificationExecutor<SysUserRole>, JpaRepository<SysUserRole, Integer> {

    List<SysUserRole> findByUserId(Integer userId);
}
