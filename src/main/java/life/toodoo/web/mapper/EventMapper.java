package life.toodoo.web.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import life.toodoo.web.command.EventCommand;
import life.toodoo.web.model.Event;
import life.toodoo.web.model.EventDTO;
import life.toodoo.web.viewmodel.ErrorModalViewModel;
import life.toodoo.web.viewmodel.EventViewModel;
import life.toodoo.web.viewmodel.EventsViewModel;
import lombok.extern.slf4j.Slf4j;

@Component
public class EventMapper 
{
	public Event mapEventDtoToEvent(EventDTO eventDTO) 
	{
		Event event = new Event();
		event.setId(eventDTO.getId());
		event.setTitle(eventDTO.getTitle());
		event.setStatus(eventDTO.getStatus());
		event.setPriority(eventDTO.getPriority());
		event.setCompletePct(eventDTO.getCompletePct()); //.doubleValue());
		return event;
	}
	
	public EventViewModel mapEventToEventViewModel(Event event)
	{
		EventViewModel eventViewModel = new EventViewModel();
		eventViewModel.setEvent(event);
		
		if ( event != null ) {
			eventViewModel.setShowCompleteButton( ! event.isComplete() );
		}
		return eventViewModel;
	}
	
	public EventViewModel mapEventToEventViewModel(Event event, int displayOrder)
	{
		EventViewModel eventViewModel = new EventViewModel();
		eventViewModel.setEvent(event);
		
		if ( event != null ) {
			eventViewModel.setShowCompleteButton( ! event.isComplete() );
		}
		eventViewModel.setEventDisplayOrder(displayOrder);
		
		return eventViewModel;
	}

	public EventCommand mapEventToEventCommand(Event event)
	{
		EventCommand eventCommand = new EventCommand();
		
		if ( event != null ) 
		{
			eventCommand.setId(event.getId());
			eventCommand.setTitle(event.getTitle());
			eventCommand.setPriority(event.getPriority());
			eventCommand.setStatus(event.getStatus());
			eventCommand.setCompletePct(event.getCompletePct());
			
		}
		return eventCommand;
	}

	public Event mapEventCommandToEvent(EventCommand eventCommand)
	{
		Event event = new Event();
		
		if ( eventCommand != null ) 
		{
			event.setId(eventCommand.getId());
			event.setTitle(eventCommand.getTitle());
			event.setPriority(eventCommand.getPriority());
			event.setStatus(eventCommand.getStatus());
			event.setCompletePct(eventCommand.getCompletePct());
			
		}
		return event;
	}

	public EventsViewModel mapEventsToEventsViewModel(List<Event> events) 
	{
		EventsViewModel      eventsViewModel = new EventsViewModel();

		List<EventViewModel> eventViewModelList = new ArrayList<>();
		
		for ( int i=0; i < events.size(); i++ ) {
			eventViewModelList.add( mapEventToEventViewModel(events.get(i), i) );
		}
		eventsViewModel.setEventViewModels(eventViewModelList);
		eventsViewModel.setEventViewModelsCnt(eventViewModelList.size());
		eventsViewModel.setModal(new ErrorModalViewModel());
		return eventsViewModel;
	}

	public EventDTO mapEventToEventDTO(Event event) 
	{
		EventDTO eventDTO = new EventDTO();
		
		if ( event != null )
		{
			eventDTO.setId(event.getId());
			eventDTO.setTitle(event.getTitle());
			eventDTO.setPriority(event.getPriority());
			eventDTO.setStatus(event.getStatus());
			eventDTO.setCompletePct(event.getCompletePct());
		}
		return eventDTO;
	}
}