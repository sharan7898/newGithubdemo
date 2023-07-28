package com.swayaan.nysf.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Role;
import org.springframework.data.domain.Pageable;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {
//public interface RoleRepository extends CrudRepository<Role, Integer> {

	public List<Role> findAllByOrderByNameAsc();

	public Long countById(Integer id);

	@Modifying
	@Query("UPDATE Role r SET r.enabled = ?2 WHERE r.id = ?1")
	@Transactional
	public void updateEnabledStatus(Integer id, boolean enabled);

	public Role findByName(String name);

	@Query("SELECT r FROM  Role r WHERE r.judge=?1")
	public List<Role> findByJudge(boolean judge);

	public Boolean existsByName(String name);

	public Set<Role> findAllById(Integer id);

	// Admin filters starts here
	public Page<Role> findAllByNameContaining(String name, Pageable pageable);
	// Admin filters ends here

}
