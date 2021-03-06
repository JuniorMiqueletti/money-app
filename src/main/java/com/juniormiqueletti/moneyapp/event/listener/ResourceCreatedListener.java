package com.juniormiqueletti.moneyapp.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.juniormiqueletti.moneyapp.event.ResourceCreatedEvent;

@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent> {

	@Override
	public void onApplicationEvent(ResourceCreatedEvent resource) {
		HttpServletResponse response = resource.getResponse();
		Long id = resource.getId();

		URI uri = addHeaderLocation(id);

		response.setHeader("Location", uri.toASCIIString());

	}

	private URI addHeaderLocation(Long id) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(id).toUri();
		return uri;
	}

}
