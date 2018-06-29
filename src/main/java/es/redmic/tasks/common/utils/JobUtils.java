package es.redmic.tasks.common.utils;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.JobParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.exception.elasticsearch.ESParseException;

@Component
public class JobUtils {
	
	@Autowired
	ObjectMapper jacksonMapper;
	
	static ObjectMapper jMapper;
	
	@PostConstruct
	private void setUp(){
		jMapper = jacksonMapper;
	}
	
	public static <T> T JobParameters2UserParameters(JobParameter parameter, Class<T> parameterClass) {
		
		try {
			return jMapper.readValue(parameter.getValue().toString(), parameterClass);
		} catch (IOException e) {
			throw new ESParseException(e);
		}
	}
}
