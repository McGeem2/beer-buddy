package com.beerbuddy.core.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.beerbuddy.core.function.BeerStoreMapperFunction;
import com.beerbuddy.core.model.Beer;
import com.beerbuddy.core.repository.BeerRepository;
import com.beerbuddy.core.service.BeerStoreSyncService;

public class DefaultBeerStoreSyncService implements BeerStoreSyncService,
		BeerStoreMapperFunction {

	private final static String API = "http://ontariobeerapi.ca:80/beers/";

	protected RestTemplate restTemplate;

	protected BeerRepository beerRepository;

	public DefaultBeerStoreSyncService(RestTemplate restTemplate,
			BeerRepository beerRepository) {
		this.restTemplate = restTemplate;
		this.beerRepository = beerRepository;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean sync() {
		ResponseEntity<List> response = restTemplate.getForEntity(API,
				List.class);
		if (response.getStatusCode() == HttpStatus.OK) {

			List<Map<String, Object>> json = response.getBody();
			log.trace("about to sync beers");
			List<Beer> beers = json.stream().map(this)
					.collect(Collectors.toList());
			log.trace("beers have been mapped to model");
			Iterator it = beers.iterator();
			
			while (it.hasNext()) {
				Beer b = (Beer) it.next();
				String imageUrl = b.getImageUrl();
				if (!isImageURLGood(imageUrl)) {
					log.trace("Removed bad image");
					log.trace("Beer: " + b.getName());
					it.remove();
				}
			}

			beers.stream()
					// TODO: FIXME! only grab beers with images
					// takes too long to run.. come back later and multi thread
					// this...
					// .filter(b -> {
					// //if we cannot get a valid image for a beer, we do not
					// want it in our app
					// try {
					// URL url = new URL(b.getImageUrl());
					// url.openStream();
					// } catch(Exception e) {
					// return false;
					// }
					// return true;
					// })
					// sort by name before saving them just for fun so
					// that we do not have to make a fancy db query later
					// laziness +1
					.sorted((a, b) -> a.getName().compareTo(b.getName()))
					.forEach(b -> beerRepository.save(b));

			log.trace("beers have been synced");
		}
		return true;
	}
	
	protected boolean isImageURLGood(String url)
	{
		 HttpURLConnection httpUrlConn;
	        try {
	            httpUrlConn = (HttpURLConnection) new URL(url)
	                    .openConnection();
	
	            httpUrlConn.setRequestMethod("HEAD");
	 
	            // Set timeouts in milliseconds
	            httpUrlConn.setConnectTimeout(30000);
	            httpUrlConn.setReadTimeout(30000);
	 
	            return (httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK);
	        } catch (Exception e) {
	            System.out.println("Error: " + e.getMessage());
	            return false;
	        }
	}
}