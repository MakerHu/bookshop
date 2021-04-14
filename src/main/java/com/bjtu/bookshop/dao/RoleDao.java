package com.bjtu.bookshop.dao;

import com.bjtu.bookshop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
