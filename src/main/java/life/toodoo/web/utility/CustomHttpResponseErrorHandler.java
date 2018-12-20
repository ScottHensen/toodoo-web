package life.toodoo.web.utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomHttpResponseErrorHandler implements ResponseErrorHandler
{
	private List okStatusList;
	public CustomHttpResponseErrorHandler(@Value("${http-status-allowed}") String okStatuses)
	{
		okStatusList = Arrays.stream(okStatuses.split(","))
								.map(HttpStatus::valueOf)
								.collect(Collectors.toList());
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException 
	{
		return ! okStatusList.contains(response.getStatusCode());
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException 
	{
		if ( response.getStatusText() != null ) {
			log.error("http response error: {} {}", response.getStatusCode(), response.getStatusText());
		}
		else {
			log.error("http response error: {}", response.getStatusCode());			
		}
	}
}
