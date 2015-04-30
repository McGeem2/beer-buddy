package com.beerbuddy.web.apprunner;



import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.beerbuddy.core.model.Beer;
import com.beerbuddy.core.service.BeerStoreSyncService;
import com.beerbuddy.web.controller.rest.api.MappingInfo;
import com.beerbuddy.web.controller.ui.model.BeerDTO;
import com.beerbuddy.web.controller.ui.model.NewUserRequest;
import com.beerbuddy.web.listener.BeerStoreSyncListener;

//@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigTest
{
	@Mock
	Environment e;
	
	@Mock
	BeerStoreSyncService bs;
	
	@Mock
	HttpSession session;
	
	@Mock
	Model model;
	
	
	@Mock
	RequestMappingHandlerMapping request;
	
	@Mock
	HandlerMethod m;
	
	Beer newBeer = new Beer();
	Beer newBeer2 = new Beer();
	
	
	
	
	@Before
	public void startup()
	{
		newBeer.setAbv("5.5");
		newBeer.setBeerStoreId(12);
		newBeer.setBrewer("Molson");
		newBeer.setCategory("Craft");
		newBeer.setCountry("Canada, eh?");
		newBeer.setId((long)1112223334);
		newBeer.setImageUrl("Image");
		newBeer.setName("BuddyLight");
		newBeer.setOnSale(true);
		newBeer.setType("Lager");
		
		newBeer2.setAbv("4.0");
		newBeer2.setBeerStoreId(15);
		newBeer2.setBrewer("Marks");
		newBeer2.setCategory("Craft");
		newBeer2.setCountry("'Murica");
		newBeer2.setId((long)2345);
		newBeer2.setImageUrl("Image");
		newBeer2.setName("MillyLight");
		newBeer2.setOnSale(true);
		newBeer2.setType("Lager");
		
	}
	
	@Test
	public void BeerStoreSyncListenerTest()
	{
		
		
		MockitoAnnotations.initMocks(this);
		
		when(e.getProperty(anyString(), anyString())).thenReturn("true");
		when(bs.sync()).thenReturn(true);
		BeerStoreSyncListener b = new BeerStoreSyncListener(e, bs);
		
		b.runSync();
		
		verify(e).getProperty(anyString(), anyString());
		verify(bs).sync();
	}
	
	@Test
	public void BeerDTOTest()
	{
		BeerDTO dto = new BeerDTO();
		dto.setAbv(newBeer.getAbv());
		dto.setBrewer(newBeer.getBrewer());
		dto.setCategory(newBeer.getCategory());
		dto.setCountry(newBeer.getCountry());
		dto.setId(newBeer.getId());
		dto.setImageUrl(newBeer.getImageUrl());
		dto.setName(newBeer.getName());
		dto.setOnSale(newBeer.isOnSale());
		dto.setType(newBeer.getType());
		
		assertThat(dto.getAbv(), is(equalTo(newBeer.getAbv())));
		assertThat(dto.getBrewer(), is(equalTo(newBeer.getBrewer())));
		assertThat(dto.getCategory(), is(equalTo(newBeer.getCategory())));
		assertThat(dto.getCountry(), is(equalTo(newBeer.getCountry())));
		assertThat(dto.getId(), is(equalTo(newBeer.getId())));
		assertThat(dto.getImageUrl(), is(equalTo(newBeer.getImageUrl())));
		assertThat(dto.getName(), is(equalTo(newBeer.getName())));
		assertThat(dto.isOnSale(), is(equalTo(newBeer.isOnSale())));
		assertThat(dto.getType(), is(equalTo(newBeer.getType())));	
	}
	
	@Test
	public void NewUserRequestTest()
	{
		NewUserRequest r = new NewUserRequest();
		String email= "bigdrinker@yahoo.com";
		String Name = "Emile";
		String password = "Password1";
		String Username = "Parzival";
		r.setEmail(email);
		r.setName(Name);
		r.setPassword(password);
		r.setUsername(Username);
		assertThat(r.getEmail(),is(equalTo(email)));
		assertThat(r.getName(),is(equalTo(Name)));
		assertThat(r.getPassword(),is(equalTo(password)));
		assertThat(r.getUsername(),is(equalTo(Username)));
	}
	
	@Test
	public void MappingInfoTests()
	{
		MappingInfo mi = new MappingInfo();
		Set<String> pattern = new HashSet<String>();
		Set<RequestMethod> rm = new HashSet<RequestMethod>();
		Set<NameValueExpression<String>> headers = new HashSet<NameValueExpression<String>>();
		Set<NameValueExpression<String>> parameters = new HashSet<NameValueExpression<String>>();
		Set<org.springframework.http.MediaType> media = new HashSet<org.springframework.http.MediaType>();
		String beanMethod = "beanMethod";
		String beanSimple = "Simple";
		String description = "This method sucks";
		String returnType = "boolean";
		
		mi.setBeanMethodName(beanMethod);
		mi.setBeanSimpleName(beanSimple);
		mi.setDescription(description);
		mi.setHeaders(headers);
		mi.setMethods(rm);
		mi.setParams(parameters);
		mi.setPatterns(pattern);
		mi.setProduces(media);
		mi.setReturnType(returnType);
		
		assertThat(mi.getBeanMethodName(), is(equalTo(beanMethod)));
		assertThat(mi.getBeanSimpleName(), is(equalTo(beanSimple)));
		assertThat(mi.getDescription(), is(equalTo(description)));
		assertThat(mi.getHeaders(), is(equalTo(headers)));
		assertThat(mi.getMethods(), is(equalTo(rm)));
		assertThat(mi.getParams(), is(equalTo(parameters)));
		assertThat(mi.getPatterns(), is(equalTo(pattern)));
		assertThat(mi.getProduces(), is(equalTo(media)));
		assertThat(mi.getReturnType(), is(equalTo(returnType)));
		
	}		
}