package net.xzh.user.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by macro on 2019/8/29.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class User {
    @ApiModelProperty("用户ID")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    
}
