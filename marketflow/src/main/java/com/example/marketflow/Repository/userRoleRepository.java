package com.example.marketflow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketflow.userRoles.UserRoleId;
import com.example.marketflow.userRoles.UserRolesEntity;

@Repository
public interface userRoleRepository extends JpaRepository<UserRolesEntity,UserRoleId> {
    
}
