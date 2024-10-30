package net.xzh.consul.service;

import java.util.List;

import net.xzh.consul.domain.User;

/**
 * Created 2019/8/29.
 */
public interface UserService {
    void create(User user);

    User getUser(Long id);

    void update(User user);

    void delete(Long id);

    User getByUsername(String username);

    List<User> getUserByIds(List<Long> ids);
}
