package com.example.sampleIntegrationClientApplication;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.converter.SerializingHttpMessageConverter;
import org.springframework.integration.http.dsl.Http;

import com.example.sampleIntegrationClientApplication.model.Person;



@SpringBootApplication
public class PersonClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonClientApplication.class, args);
	}
	

	@Bean
	 public IntegrationFlow inboundGetById() {
		return IntegrationFlows.from(Http.inboundGateway("/getByIdC/{id}")
				  							.requestMapping(m -> m.methods(HttpMethod.GET)
				  							.consumes("application/json").produces("application/json"))
				  							.payloadExpression("#pathVariables.id"))
				  				//.channel("httpGetChannel")
				  				.handle(Http.outboundGateway(m -> "http://localhost:8080/getById/" + m.getPayload())
				  			            .httpMethod(HttpMethod.GET)
				  			            .expectedResponseType(Person.class))
				  				.get();
	  }
	
	@Bean
	public IntegrationFlow inboundAddPerson() {
		return IntegrationFlows.from(
             Http.inboundGateway("/addPersonC")
                     .requestMapping(r -> r.methods(HttpMethod.POST)
                             .consumes("application/json").produces("application/json"))
                     .requestPayloadType(Person.class))
   		     //.channel("httpPostChannel")
             .handle(Http.outboundGateway("http://localhost:8080/addPerson")
            		 .httpMethod(HttpMethod.POST)
            		 .expectedResponseType(Person.class))
             .get();
 }
	
	@Bean
	public IntegrationFlow inBoundUpdatePerson() {
		return IntegrationFlows.from(
	              Http.inboundGateway("/updatePersonC")
	                      .requestMapping(r -> r.methods(HttpMethod.PUT)
	                              .consumes("application/json").produces("application/json"))
	                      .requestPayloadType(Person.class))
	    		  //.channel("httpPutChannel")
	              .handle(Http.outboundGateway("http://localhost:8080/updatePerson")
	             		 .httpMethod(HttpMethod.PUT)
	            		 .expectedResponseType(Person.class))
	              .get();
	}

	
	@Bean
	public IntegrationFlow inBoundDeletePerson() {
		return IntegrationFlows.from(
	              Http.inboundGateway("/deletePersonC/{id}")
	                      .requestMapping(r -> r.methods(HttpMethod.DELETE)
	                              .consumes("application/json").produces("application/json"))
	                      .payloadExpression("#pathVariables.id")
	                      .statusCodeExpression("T(org.springframework.http.HttpStatus).NO_CONTENT"))
	    		 //.channel("httpDelChannel")
	              .handle(Http.outboundGateway(m -> "http://localhost:8080/deletePerson/" + m.getPayload())
	  			            .httpMethod(HttpMethod.DELETE))
	              .get();
	}
	
	@Bean
	 public IntegrationFlow inboundGetAll() {
		
		return IntegrationFlows.from(Http.inboundGateway("/getallC")
				  							.requestMapping(m -> m.methods(HttpMethod.GET)
				  							.consumes("application/json").produces("application/json")))
				  				//.channel("httpGetAllChannel")
				  				.handle(Http.outboundGateway("http://localhost:8080/getall")
				  			            .httpMethod(HttpMethod.GET)
				  			            .expectedResponseType(Person[].class))
				  				.get();
	  }
	
	
	
}
