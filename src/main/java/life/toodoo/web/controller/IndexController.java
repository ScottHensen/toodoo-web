package life.toodoo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import life.toodoo.web.service.EventSvc;

@Controller
public class IndexController 
{
	private final EventSvc eventSvc;

	public IndexController(EventSvc eventSvc) {
		this.eventSvc = eventSvc;
	}
	
	@GetMapping( { "", "/" } )
	public String getIndexPage(Model model)
	{
		return "index";
	}
}
