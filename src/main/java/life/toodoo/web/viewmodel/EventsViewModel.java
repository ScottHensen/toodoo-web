package life.toodoo.web.viewmodel;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EventsViewModel 
{
	private List<EventViewModel> eventViewModels = new ArrayList<>();
	private Integer eventViewModelsCnt;
	private ErrorModalViewModel modal;
}
