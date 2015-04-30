package com.beerbuddy.core.service.impl;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.beerbuddy.core.model.Beer;
import com.beerbuddy.core.model.DefaultUser;
import com.beerbuddy.core.model.UnsecureUserWrapper;
import com.beerbuddy.core.model.User;
import com.beerbuddy.core.model.UserBeerRank;
import com.beerbuddy.core.model.UserProfile;
import com.beerbuddy.core.model.UserWrapper;
import com.beerbuddy.core.repository.BeerRepository;
import com.beerbuddy.core.repository.UserBeerRankRepository;
import com.beerbuddy.core.repository.UserProfileRepository;
import com.beerbuddy.core.repository.UserRepository;

public class DefaultUserServiceTest {

	@Mock
	protected UserRepository repository;
	
	@Mock
	protected UserProfileRepository profileRepository;
	
	@Mock
	protected UserProfile p;
	@Mock
	protected UserBeerRankRepository rankRepository;
	
	@Mock
	protected BeerRepository beerRepository;
	
	protected DefaultUserService service;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		service = new DefaultUserService(repository, profileRepository, rankRepository, beerRepository);
		
		
	}
	
	@Test
	public void createUser_samePasswordEncryptsUniquely() throws Exception {
		
		String username = "123";
		String password = "abc";
		
		User firstUser = service.createUser(username, password);
		User secondUser = service.createUser(username, password);
		
		UnsecureUserWrapper unsecureFirst = new UnsecureUserWrapper(firstUser);
		UnsecureUserWrapper unsecureSecond = new UnsecureUserWrapper(secondUser);
		assertThat(unsecureFirst.getUser(), is(instanceOf(DefaultUser.class)));
		assertThat(unsecureSecond.getUser(), is(instanceOf(DefaultUser.class)));
		
		assertThat(((DefaultUser) unsecureFirst.getUser()).getPassword(), 
				is(not(equalTo(((DefaultUser) unsecureSecond.getUser()).getPassword()))));	
	}
	
	@Test(expected=UserAlreadyExistsException.class)
	public void ExceptionsTest() throws Exception
	{	
		String username = "123";
		String password = "abc";
		
		User firstUser = service.createUser(username, password);
		when(service.userRepository.findByUsername(username)).thenReturn(new DefaultUser());
		User secondUser = service.createUser(username, password);
	}
	
	@Test
	public void setUserProfileTest() throws Exception
	{	
		
		String username = "123";
		String password = "abc";
		
		User u = service.createUser(username, password);
		UserProfile p = new UserProfile();
		User w = service.setUserProfile(u, p);
		assertThat(w, is(instanceOf(UserWrapper.class)));
		assertThat(u, is(instanceOf(User.class)));
		assertThat(u.getUsername(), is(equalTo(w.getUsername())));
	}
	
	@Test
	public void addBeerToRankingsTest()
	{
		Beer b = new Beer();
		when(profileRepository.findOne(anyLong())).thenReturn(p);
		when(beerRepository.findOne(anyLong())).thenReturn(b);
		when(p.getBeerRankings()).thenReturn(new HashSet<UserBeerRank>());
		when(rankRepository.findRankByBeerIdAndUser(any(), anyLong())).thenReturn(null);
		service.addBeerToUserRankings(p, (long)1112223334);
		verify(rankRepository).save(any(UserBeerRank.class));
	
	}
	
	@Test
	public void addBerToRankingBeerAlreadyAdded() throws Exception {
		
		Beer b = new Beer();
		when(profileRepository.findOne(anyLong())).thenReturn(p);
		when(beerRepository.findOne(anyLong())).thenReturn(b);
		when(rankRepository.findRankByBeerIdAndUser(any(), anyLong())).thenReturn(new UserBeerRank());
		service.addBeerToUserRankings(p, 1112223l);
		verify(rankRepository, never()).save(any(UserBeerRank.class));
	}
	
	
}