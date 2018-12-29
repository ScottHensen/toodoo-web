package life.toodoo.web.service;

import life.toodoo.web.model.Event;

public interface EventSvc 
{
	public EventSvcResponse getEvents();

	public EventSvcResponse getEventById(Long id);

	public EventSvcResponse updateEvent(Event event);
	
	public EventSvcResponse saveEvent(Event event);
	
	public EventSvcResponse deleteEventById(Long id);
}
