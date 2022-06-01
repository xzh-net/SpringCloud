package net.xzh.user.service;

import java.util.List;

import net.xzh.user.domain.User;

/**
 * Created by macro on 2019/8/29.
 */
public interface UserService {
    void create(User user);

    User getUser(Long id);

    void update(User user);

    void delete(Long id);

    User getByUsername(String username);

    List<User> getUserByIds(List<Long> ids);
}
