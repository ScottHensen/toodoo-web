package life.toodoo.web.service;

import javax.validation.Valid;

import life.toodoo.web.command.EventCommand;
import life.toodoo.web.model.Event;

public interface EventSvc 
{
	public EventSvcResponse getEvents();

	public EventSvcResponse getEventById(Long id);

	public EventCommand getEventCommandById(Long id);

	public EventSvcResponse updateEvent(Event event);
	
	public EventSvcResponse saveEvent(Event event);
}
