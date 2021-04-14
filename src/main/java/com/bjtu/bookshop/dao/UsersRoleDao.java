package com.bjtu.bookshop.dao;

import com.bjtu.bookshop.entity.UsersRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRoleDao extends JpaRepository<UsersRole, Integer> {
    UsersRole findByUidAndRid(Integer uid, Integer rid);

    List<UsersRole> findByUid(Integer uid);

    List<UsersRole> findByRid(Integer rid);

    void deleteByUidAndRid(Integer uid, Integer rid);

    void deleteByUid(Integer uid);

    void deleteByRid(Integer rid);
}
