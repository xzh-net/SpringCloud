package net.xzh.oauth2.vo;

import java.util.List;

import lombok.Data;
import net.xzh.oauth2.entity.SysUser;

/**
 * @date 2019-02-12
 */
@Data
public class SysUserVO extends SysUser {

    /**
     * 权限列表
     */
    private List<String> authorityList;

}
