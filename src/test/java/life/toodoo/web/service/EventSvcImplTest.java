package life.toodoo.web.service;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import life.toodoo.web.mapper.EventMapper;
import life.toodoo.web.model.Event;
import life.toodoo.web.model.EventDTO;
import life.toodoo.web.model.EventListDTO;
import life.toodoo.web.utility.CustomHttpResponseErrorHandler;

public class EventSvcImplTest {

	private final String URL = "http://foo";
	
	@Mock
	RestTemplate restTemplate;
	
	@Mock
	EventMapper eventMapper;
	
	@Mock
	CustomHttpResponseErrorHandler customErrorHandler;

	EventSvc svc;
	
	RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
	

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		svc = new EventSvcImpl(eventMapper, restTemplateBuilder, customErrorHandler);
	}

	
	@Test
	public void testGetEvents() throws Exception
	{
		// Given
		EventDTO eventDTO1 = new EventDTO();
		eventDTO1.setId(1L);
		EventDTO eventDTO2 = new EventDTO();
		eventDTO2.setId(2L);
		
		List<EventDTO> eventDTOs = new ArrayList<>();
		eventDTOs.add(eventDTO1);
		eventDTOs.add(eventDTO2);
		
		EventListDTO eventListDTO = new EventListDTO();
		eventListDTO.setEvents(eventDTOs);
		
		ResponseEntity<EventListDTO> responseEntity = new ResponseEntity<EventListDTO>(eventListDTO, HttpStatus.OK);
		
		when(restTemplate.exchange(eq(URL), eq(HttpMethod.GET), eq(null), eq(EventListDTO.class))).thenReturn(responseEntity);
		
		// When
		EventSvcResponse svcResponse = svc.getEvents();
		
		// Then 
		Assertions.assertThat(svcResponse.getSuccess()).isEqualTo(true);
	}
	
//TODO: 
//	@Test
//	public void testGetEventByIdNotFound() throws Exception
//	{
//	}
//	
//	@Test
//	public void testGetEventByIdBadNumberFormat() throws Exception
//	{
//	}
	

}
