package com.beerbuddy.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beerbuddy.core.model.UserBeerRank;

public interface UserBeerRankRepository extends JpaRepository<UserBeerRank, Long> {
	
	@Query("select r from UserBeerRank r "
			+ " where r.user.user.username = :username "
			+ " order by r.rank asc")
	public Page<UserBeerRank> findByUser(@Param("username") String username, Pageable page);
	
	@Query("select r from UserBeerRank r" 
			+ " where r.beer.id = :beerId"
			+ " and r.user.user.username = :username")
	public UserBeerRank findRankByBeerIdAndUser(@Param("username") String username,@Param("beerId") Long beerId);
}
