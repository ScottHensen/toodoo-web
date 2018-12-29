package life.toodoo.web.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import life.toodoo.web.command.EventCommand;
import life.toodoo.web.mapper.EventMapper;
import life.toodoo.web.model.Event;
import life.toodoo.web.model.EventDTO;
import life.toodoo.web.model.EventListDTO;
import life.toodoo.web.utility.CustomHttpResponseErrorHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventSvcImpl implements EventSvc 
{
	private static final String URL = "http://localhost:9080/api/v1/events";

	private final EventMapper eventMapper;
	private final RestTemplateBuilder restTemplateBuilder;
	private final RestTemplate restTemplate;
	private final CustomHttpResponseErrorHandler customErrorHandler;
	
	public EventSvcImpl(EventMapper eventMapper, 
						RestTemplateBuilder restTemplateBuilder, 
						CustomHttpResponseErrorHandler customErrorHandler) {
		this.eventMapper = eventMapper;
		this.restTemplateBuilder = restTemplateBuilder;
		this.customErrorHandler = customErrorHandler;
		this.restTemplate = restTemplateBuilder.errorHandler(customErrorHandler).build();
	}
	
	@Override
	public EventSvcResponse getEvents()
	{
		Set<Event> events = new HashSet<>();
		EventSvcResponse response = new EventSvcResponse();

		ResponseEntity<EventListDTO> restResponse =
				restTemplate.exchange(URL, HttpMethod.GET, null, EventListDTO.class);
		
		if ( restResponse.getStatusCode().is2xxSuccessful() )
		{
			response.setSuccess(true);
			response.setEvents(  
						restResponse.getBody().getEvents()
							.stream()
							.map(eventMapper::mapEventDtoToEvent)
							.collect(Collectors.toSet()) );
		}
		else
		{
			String returnMsg = restResponse.getStatusCode().name() 
							 + " error encountered when getting events.";
			log.error( returnMsg );			
			
			response.setMessage(returnMsg);
			response.setSuccess(false);
			response.setEvents(events);
		}
		
		log.debug(response.getEvents().toString());
		response.setCode(restResponse.getStatusCodeValue());
		return response;
	}

	@Override
	public EventSvcResponse getEventById(Long id) 
	{
		EventSvcResponse response = new EventSvcResponse();

		ResponseEntity<EventDTO> restResponse = 
				restTemplate.exchange(URL+"/"+id, HttpMethod.GET, null, EventDTO.class);

		if ( restResponse.getStatusCode().is2xxSuccessful() )
		{
			response.setSuccess(true);
			response.setEvent(eventMapper.mapEventDtoToEvent(restResponse.getBody()));
		}
		else
		{
			String returnMsg = restResponse.getStatusCode().name() 
							 + " error encountered when getting event #" + id.toString() + ".";
			log.error( returnMsg );			

			response.setMessage(returnMsg);
			response.setSuccess(false);
			response.setEvent(new Event());
		}
		
		response.setCode(restResponse.getStatusCodeValue());
		return response;
	}

	@Override
	public EventSvcResponse updateEvent(Event event) 
	{
		EventSvcResponse response = new EventSvcResponse();
		HttpEntity<EventDTO> request = new HttpEntity<>(eventMapper.mapEventToEventDTO(event));

		ResponseEntity<EventDTO> restResponse =
				restTemplate.exchange(URL+"/"+event.getId(), HttpMethod.PUT, request, EventDTO.class);
		
		if ( restResponse.getStatusCode().is2xxSuccessful() ) 
		{
			response.setSuccess(true);
			response.setEvent(eventMapper.mapEventDtoToEvent(restResponse.getBody()));
		}
		else 
		{
			String returnMsg = restResponse.getStatusCode().name() 
					 + " error encountered when updating event #" + event.getId().toString() + ".";
			log.error( returnMsg );			

			response.setMessage(returnMsg);
			response.setSuccess(false);
			response.setEvent(new Event());

		}	
		response.setCode(restResponse.getStatusCodeValue());
		return response;
	}
	
	@Override
	public EventSvcResponse saveEvent(Event event) 
	{
		EventSvcResponse response = new EventSvcResponse();
		HttpEntity<EventDTO> request = new HttpEntity<>(eventMapper.mapEventToEventDTO(event));

		ResponseEntity<EventDTO> restResponse =
				restTemplate.exchange(URL, HttpMethod.POST, request, EventDTO.class);
		
		if ( restResponse.getStatusCode().equals(HttpStatus.CREATED) ) 
		{
			response.setSuccess(true);
			response.setEvent(eventMapper.mapEventDtoToEvent(restResponse.getBody()));
		}
		else 
		{
			String returnMsg = restResponse.getStatusCode().name() 
					 + " error encountered when creating event: " + event.getTitle() + ".";
			log.error( returnMsg );			

			response.setMessage(returnMsg);
			response.setSuccess(false);
			response.setEvent(new Event());

		}	
		response.setCode(restResponse.getStatusCodeValue());
		return response;
	}

	@Override
	public EventSvcResponse deleteEventById(Long id)
	{
		EventSvcResponse response = new EventSvcResponse();
		
		ResponseEntity<EventDTO> restResponse = 
				restTemplate.exchange(URL+"/"+id, HttpMethod.DELETE, null, EventDTO.class);

		if ( restResponse.getStatusCode().is2xxSuccessful() )
		{
			response.setSuccess(true);
			response.setEvent(new Event());
		}
		else
		{
			String returnMsg = restResponse.getStatusCode().name() 
							 + " error encountered when deleting event #" + id.toString() + ".";
			log.error( returnMsg );			

			response.setMessage(returnMsg);
			response.setSuccess(false);
			response.setEvent(new Event());
		}
		
		response.setCode(restResponse.getStatusCodeValue());
		return response;		
	}
}