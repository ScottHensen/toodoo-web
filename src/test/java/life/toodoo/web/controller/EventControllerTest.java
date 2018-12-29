package life.toodoo.web.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import life.toodoo.web.command.EventCommand;
import life.toodoo.web.mapper.EventMapper;
import life.toodoo.web.model.Event;
import life.toodoo.web.service.EventSvc;
import life.toodoo.web.service.EventSvcResponse;

public class EventControllerTest 
{
	@Mock
	EventSvc eventSvc;
	
	MockMvc mockMvc;
	EventController controller;
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		
		controller = new EventController(eventSvc, new EventMapper());
		mockMvc    = MockMvcBuilders
						.standaloneSetup(controller)
						.build();
	}
	
	@Test
	public void testViewEventsList() throws Exception
	{
		// given
		Event event1 = Event.builder().id(1L).title("event1").priority(1).status("In Progress").completePct(BigDecimal.valueOf(50.0)).build();
		Event event2 = Event.builder().id(2L).title("event2").priority(2).status("Not Started").completePct(BigDecimal.valueOf(0.0)).build();
		Set<Event> events = new HashSet<>();
		events.add(event1);
		events.add(event2);
		
		EventSvcResponse response = new EventSvcResponse();
		response.setSuccess(true);
		response.setEvents(events);
		when(eventSvc.getEvents()).thenReturn(response);
		
		// expect
		mockMvc.perform(get("/events/view"))
				.andExpect(status().isOk())
				.andExpect(view().name("/event/viewevents"))
				.andExpect(model().attributeExists("eventsViewModel"))
				.andExpect(model().attribute("eventsViewModel", hasProperty("eventViewModels", hasItem(hasProperty("showUpdateButton", is(true)      )))))
				.andExpect(model().attribute("eventsViewModel", hasProperty("eventViewModels", hasItem(hasProperty("showDeleteButton", is(true)      )))))
				.andExpect(model().attribute("eventsViewModel", hasProperty("eventViewModels", hasItem(hasProperty("message",          is("")        )))))
				.andExpect(model().attribute("eventsViewModel", hasProperty("eventViewModels", hasItem(hasProperty("messageType",      is("default") )))))
				;
		
		verify(eventSvc).getEvents();
	}
	
	@Test
	public void testViewEvent() throws Exception
	{
		// given
		Event event = Event.builder().id(1L).title("event1").priority(1).status("In Progress").completePct(BigDecimal.valueOf(50.0)).build();
		EventSvcResponse response = new EventSvcResponse();
		response.setSuccess(true);
		response.setEvent(event);
		when(eventSvc.getEventById(anyLong())).thenReturn(response);
		
		// expect
		mockMvc.perform(get("/events/1/view"))
				.andExpect(status().isOk())
				.andExpect(view().name("/event/viewevent"))
				.andExpect(model().attributeExists("eventViewModel"))
				.andExpect(model().attribute("eventViewModel", hasProperty("showUpdateButton", is(true))))
				.andExpect(model().attribute("eventViewModel", hasProperty("showDeleteButton", is(true))))
				.andExpect(model().attribute("eventViewModel", hasProperty("message",          is(""))))
				.andExpect(model().attribute("eventViewModel", hasProperty("messageType",      is("default"))))
				;
		
		verify(eventSvc).getEventById(anyLong());
	}
	
	@Test
	public void testViewEventNotFound() throws Exception
	{
		// given
		EventSvcResponse response = new EventSvcResponse();
		response.setSuccess(false);
		response.setCode(404);
		response.setMessage("Event not found");
		response.setEvent(new Event());
		when(eventSvc.getEventById(anyLong())).thenReturn(response);
		
		// expect
		mockMvc.perform(get("/events/99/view"))
				.andExpect(status().isOk())
				.andExpect(view().name("/event/viewevent"))
				.andExpect(model().attribute("eventViewModel", hasProperty("showUpdateButton", is(false))))
				.andExpect(model().attribute("eventViewModel", hasProperty("showDeleteButton", is(false))))
				.andExpect(model().attribute("eventViewModel", hasProperty("message",          is("Event not found"))))
				.andExpect(model().attribute("eventViewModel", hasProperty("messageType",      is("error"))));
		
		verify(eventSvc).getEventById(anyLong());
	}
	
	@Test
	public void testViewEventBadNumberFormat() throws Exception
	{
		// given
		EventSvcResponse response = new EventSvcResponse();
		response.setSuccess(false);
		response.setCode(400);
		response.setMessage("Bad request");
		response.setEvent(new Event());
		when(eventSvc.getEventById(anyLong())).thenReturn(response);
		
		// expect
		mockMvc.perform(get("/events/x/view"))
				.andExpect(status().isBadRequest())
				.andExpect(view().name("400error"));
		
		verifyZeroInteractions(eventSvc);
	}
	
	@Test
	public void testGetFormToUpdateEvent() throws Exception
	{
		// given
		EventSvcResponse response = new EventSvcResponse();
		response.setSuccess(true);
		response.setEvent(new Event());
		when(eventSvc.getEventById(anyLong())).thenReturn(response);

		// expect
		mockMvc.perform( get("/events/1/update"))
			.andExpect(status().isOk())
			.andExpect(view().name("/event/eventform"))
			.andExpect(model().attributeExists("eventViewModel"))
			.andExpect(model().attributeExists("command"));
		
		verify(eventSvc).getEventById(anyLong());
	}
	
	@Test
	public void testPostInvalidFormToUpdateEvent() throws Exception
	{
		// given
		EventSvcResponse response = new EventSvcResponse();
		response.setSuccess(true);
		response.setEvent(new Event());

		when(eventSvc.saveEvent(ArgumentMatchers.any())).thenReturn(response);
		
		// expect
		mockMvc.perform( post("/events") )
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("/event/eventform"));
		
		verifyZeroInteractions(eventSvc);	
	}
	
	@Test
	public void testPostValidFormToUpdateEvent() throws Exception
	{
		// given
		Event event = Event.builder().id(1L).title("event1").priority(1).status("In Progress").completePct(BigDecimal.valueOf(50.0)).build();
		
		EventSvcResponse response = new EventSvcResponse();
		response.setSuccess(true);
		response.setEvent(event);

		when(eventSvc.updateEvent(ArgumentMatchers.any())).thenReturn(response);
		
		// expect
		mockMvc.perform( post("/events")
							.param("id", "1")
							.param("title", "testUpdate")
						)
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/events/1/view"));
		
		verify(eventSvc).updateEvent(ArgumentMatchers.any());
	}
	
	@Test
	public void testPostValidFormToCreateEvent() throws Exception
	{
		// given
		Event event = Event.builder().id(1L).title("event1").priority(1).status("In Progress").completePct(BigDecimal.valueOf(50.0)).build();
		
		EventSvcResponse response = new EventSvcResponse();
		response.setSuccess(true);
		response.setEvent(event);

		when(eventSvc.saveEvent(ArgumentMatchers.any())).thenReturn(response);
		
		// expect
		mockMvc.perform( post("/events")
							.param("title", "testCreate")
						)
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/events/1/view"));
		
		verify(eventSvc).saveEvent(ArgumentMatchers.any());
	}
	
	@Test
	public void testDeleteEvent() throws Exception
	{
		// given
		Event event = Event.builder().id(1L).title("event1").priority(1).status("In Progress").completePct(BigDecimal.valueOf(50.0)).build();
		
		EventSvcResponse response = new EventSvcResponse();
		response.setSuccess(true);
		response.setEvent(event);

		when(eventSvc.deleteEventById(ArgumentMatchers.anyLong())).thenReturn(response);
		
		// expect
		mockMvc.perform( get("/events/1/delete"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/events/view"));
		
		verify(eventSvc).deleteEventById(ArgumentMatchers.anyLong());
	}
	
}
