package es.redmic.tasks.common.utils;

/*-
 * #%L
 * Tasks
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
