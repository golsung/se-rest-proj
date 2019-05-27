package org.insang.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.insang.domain.Member;
import org.insang.repository.MemberRepository;
import org.insang.service.Memberservice;
import org.insang.web.DemoController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoControllerTests2 { 
	
  @Test
  public void createNewMember() {
	  TestRestTemplate rt = new TestRestTemplate();
	  Member m = new Member();
	  m.setName("insang");
	  m.setEmail("insang@hansung.ac.kr");
	  m.setScore(30);
	  ResponseEntity<Member> mem = rt.postForEntity("http://localhost:8080/member", m, Member.class);
	  assertEquals(HttpStatus.CREATED, mem.getStatusCode());
	  assertEquals("insang", mem.getBody().getName());
	  assertEquals("insang@hansung.ac.kr", mem.getBody().getEmail());
	  assertEquals(30, mem.getBody().getScore());
  }
  
  @Test
  public void deleteMember() {
	  TestRestTemplate rt = new TestRestTemplate();
	  Member m = new Member();
	  m.setName("insang");
	  m.setEmail("insang@hansung.ac.kr");
	  m.setScore(30);
	  ResponseEntity<Member> mem = rt.postForEntity("http://localhost:8080/member", m, Member.class);

	  ResponseEntity<Void> re = rt.exchange(mem.getHeaders().getLocation(), HttpMethod.DELETE, null, Void.class);
	  
	  assertEquals(HttpStatus.OK, re.getStatusCode());
	 
	  
  }
  @Test
  public void getAllMembers() {
	  TestRestTemplate rt = new TestRestTemplate();
	  Member m1 = new Member();
	  m1.setName("insang1");
	  m1.setEmail("insang1@hansung.ac.kr");
	  m1.setScore(30);
	  ResponseEntity<Member> member1 = rt.postForEntity("http://localhost:8080/member", m1, Member.class);
	  Member m2 = new Member();
	  m2.setName("insang2");
	  m2.setEmail("insang2@hansung.ac.kr");
	  m2.setScore(40);
	  ResponseEntity<Member> member2 = rt.postForEntity("http://localhost:8080/member", m2, Member.class);
	  
	  ResponseEntity<List<Member>> response = rt.exchange("http://localhost:8080/members", HttpMethod.GET, null, 
		  new ParameterizedTypeReference<List<Member>>(){});
	  assertEquals(HttpStatus.OK, response.getStatusCode());
	  List<Member> members = response.getBody();
	  assertEquals(2, members.size());
	  assertEquals("insang1", members.get(0).getName());
	  assertEquals("insang2", members.get(1).getName());

  }
}
