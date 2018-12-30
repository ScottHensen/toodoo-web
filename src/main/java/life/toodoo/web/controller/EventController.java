package life.toodoo.web.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import life.toodoo.web.command.EventCommand;
import life.toodoo.web.mapper.EventMapper;
import life.toodoo.web.model.Event;
import life.toodoo.web.service.EventSvc;
import life.toodoo.web.service.EventSvcResponse;
import life.toodoo.web.viewmodel.EventViewModel;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class EventController 
{
	private final EventSvc eventSvc;
	private final EventMapper eventMapper;
	
	public EventController(EventSvc eventSvc, EventMapper eventMapper) {
		this.eventSvc = eventSvc;
		this.eventMapper = eventMapper;
	}
	
	private static final String EVENTS_VIEW        = "/event/viewevents";
	private static final String EVENTS_VIEW_MODEL  = "eventsViewModel";
	
	private static final String EVENT_VIEW         = "/event/viewevent";
	private static final String EVENT_VIEW_MODEL   = "eventViewModel";
	
	private static final String EVENT_FORM         = "/event/eventform";
	private static final String EVENT_FORM_MODEL   = "command";
	
	@ExceptionHandler( NumberFormatException.class )
	@ResponseStatus( HttpStatus.BAD_REQUEST )
	public ModelAndView handleNumberFormatError(Exception e)
	{
		log.error("Number Format Exception.  ", e.getMessage());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("400error");
		modelAndView.addObject("exception", e);
		return modelAndView;
		
	}
	
	@GetMapping( "/events/view" )
	public String viewEvents( Model model )
	{
		EventSvcResponse eventSvcResponse = eventSvc.getEvents();

		List<Event> events = 
				eventSvcResponse.getEvents()
					.stream()
					.sorted( (e1,e2) -> e1.getPriority().compareTo(e2.getPriority()) )
					.collect( Collectors.partitioningBy( e -> e.getCompletePct().equals(new BigDecimal("1.00")) ) )
					.values()
						.stream()
						.flatMap(Collection::stream)
						.collect(Collectors.toList());

		model.addAttribute(EVENTS_VIEW_MODEL, eventMapper.mapEventsToEventsViewModel(events));
		
		return EVENTS_VIEW;
	}
	
	@GetMapping( "/events/{id}/view" )
	public String viewEvent( @PathVariable String id, Model model )
	{
		EventViewModel eventViewModel = new EventViewModel();
		EventSvcResponse eventSvcResponse = eventSvc.getEventById(Long.valueOf(id));
		
		if ( eventSvcResponse.isSuccess() ) {
			eventViewModel = eventMapper.mapEventToEventViewModel(eventSvcResponse.getEvent());
		}
		else {
			eventViewModel.setError(eventSvcResponse.getMessage());
		}
		
		model.addAttribute(EVENT_VIEW_MODEL, eventViewModel);
		
		return EVENT_VIEW;
	}
	
	@GetMapping( "/events/new" )
	public String getCreateEventForm( Model model )
	{
		EventViewModel eventViewModel = new EventViewModel();
		eventViewModel.setEvent(new Event());
		model.addAttribute(EVENT_VIEW_MODEL, eventViewModel); 
		model.addAttribute(EVENT_FORM_MODEL, new EventCommand());
		
		return EVENT_FORM;
	}
	
	@GetMapping( "/events/{id}/update" )
	public String getUpdateEventForm( @PathVariable String id, Model model )
	{
		EventViewModel eventViewModel = new EventViewModel();
		EventSvcResponse eventSvcResponse = eventSvc.getEventById(Long.valueOf(id));
		
		if ( ! eventSvcResponse.isSuccess() ) {
			eventViewModel.setError(eventSvcResponse.getMessage());
		}

		eventViewModel.setEvent(eventSvcResponse.getEvent());
		
		EventCommand eventCommand =  
				eventMapper.mapEventToEventCommand(	eventSvcResponse.getEvent()) ;
		
		model.addAttribute(EVENT_VIEW_MODEL, eventViewModel); 
		model.addAttribute(EVENT_FORM_MODEL, eventCommand);
		
		return EVENT_FORM;
	}
	
	@PostMapping( "/events" )
	public String updateEvent( @Valid @ModelAttribute( "command" ) EventCommand command,
								BindingResult bindingResult )
	{
		if ( bindingResult.hasErrors() )
		{
    		bindingResult.getAllErrors().forEach( 
    				objectError -> log.error("objectError {}", objectError.toString())
    		);		
    		
    		return EVENT_FORM;
		}
		
		EventSvcResponse eventSvcResponse = new EventSvcResponse();
		
		if ( command.getId() == null ) {
			eventSvcResponse = eventSvc.saveEvent(eventMapper.mapEventCommandToEvent(command));
		}
		else {
			eventSvcResponse = eventSvc.updateEvent(eventMapper.mapEventCommandToEvent(command));
		}
		
		return "redirect:/events/" + eventSvcResponse.getEvent().getId() + "/view";
	}
	
	
	@GetMapping( "/events/{id}/delete" )
	public String deleteEvent( @PathVariable String id, Model model )
	{
		EventViewModel eventViewModel = new EventViewModel();
		EventSvcResponse eventSvcResponse = eventSvc.deleteEventById(Long.valueOf(id));
		
		//TODO: this is dumb. display error msg on the page that posted this request.
		if ( ! eventSvcResponse.isSuccess() ) {
			eventViewModel.setError(eventSvcResponse.getMessage());
		}
		
		return "redirect:/events/view";

	}
}
