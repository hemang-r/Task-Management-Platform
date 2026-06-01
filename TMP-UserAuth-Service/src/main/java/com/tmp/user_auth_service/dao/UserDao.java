package com.tmp.user_auth_service.dao;

import java.util.UUID;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.tmp.user_auth_service.entity.User;

@Repository
public interface UserDao extends DataTablesRepository<User, UUID>
{

	User findByEmailAndIsActiveTrue(String email);

}
