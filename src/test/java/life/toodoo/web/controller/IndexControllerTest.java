package life.toodoo.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import life.toodoo.web.model.Event;
import life.toodoo.web.service.EventSvc;

public class IndexControllerTest 
{
	@Mock
	Model model;
	
	@Mock
	EventSvc eventSvc;
	
	MockMvc mockMvc;
	IndexController controller;
	
	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		
		controller = new IndexController(eventSvc);
		mockMvc    = MockMvcBuilders
						.standaloneSetup(controller)
						.build();
	}

	@Test
	public void testGetIndexPageMvc() throws Exception 
	{
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}

//	@Test
//	public void testGetIndexPage() throws Exception
//	{
//		// given
//		List<Event> events = new ArrayList<>();
//		Event event1 = Event.builder().title("event1").priority(1).status("Complete").completePct(BigDecimal.valueOf(100.0)).build();
//		Event event2 = Event.builder().title("event2").priority(2).status("In Progress").completePct(BigDecimal.valueOf(50.0)).build();
//		events.add(event1);
//		events.add(event2);
//		
//		when(eventSvc.getEvents()).thenReturn(events);
//		
//		ArgumentCaptor<List<Event>> argumentCaptor = ArgumentCaptor.forClass(List.class);
//		
//		// when 
//		String viewName = controller.getIndexPage(model);
//		
//		// then
//		assertThat(viewName).isEqualTo("index");
//		verify(eventSvc, times(1)).getEvents();
//		verify(model, times(1)).addAttribute(eq("events"), argumentCaptor.capture());
//		List<Event> eventsInModel = argumentCaptor.getValue();
//		assertThat(eventsInModel.size()).isEqualTo(2);
//	}
}
