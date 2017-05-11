package pt.ulisboa.tecnico.cmu.tg14;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jackson.annotate.JsonCreator;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringexampleApplicationTests {
	@Autowired
	WebApplicationContext context;


	private MockMvc mockMvc;
	@Before
	public void setup() throws Exception {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	/*
	* TODO
	* - roll back db
	* */
	@Test
	public void contextLoads() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("testUser");


		mockMvc.perform(MockMvcRequestBuilders.put("/api/user/create")
				.content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"));
	}

}
