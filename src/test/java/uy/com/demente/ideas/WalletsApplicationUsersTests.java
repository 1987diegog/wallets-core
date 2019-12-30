package uy.com.demente.ideas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uy.com.demente.ideas.wallets.WalletsApplication;
import uy.com.demente.ideas.wallets.business.services.UserService;
import uy.com.demente.ideas.wallets.model.User;
import uy.com.demente.ideas.wallets.model.response.ListUsersDTO;
import uy.com.demente.ideas.wallets.model.response.UserDTO;
import uy.com.demente.ideas.wallets.model.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 1987diegog
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WalletsApplication.class, //
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WalletsApplicationUsersTests {

    private Logger logger = LogManager.getLogger(WalletsApplicationUsersTests.class);

    private static Long idUser;

    @Autowired
    private UserService userService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @LocalServerPort
    public int port;

    public String getRootUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    @Order(1)
    void testCreateOriginUser() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ---------------- [TEST_CREATE_ORIGIN_USER] ---------------- ");
        logger.info(" ----------------------------------------------------------- ");

        UserDTO user = new UserDTO();

        user.setName("Diego");
        user.setLastName("González");
        user.setEmail("testDiegoG@gmail.com");
        user.setCellphone("+59812345678");
        user.setStatus(Status.ENABLED.name());
        user.setUsername("19345487diegog");
        user.setAge(30);


        this.mockMvc.perform(MockMvcRequestBuilders
                .post(getRootUrl() + "/users")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());


        ResponseEntity<UserDTO> response = restTemplate.postForEntity(getRootUrl() + "/users", user, UserDTO.class);
        UserDTO userCreated = response.getBody();

        logger.info("[TEST_CREATE_ORIGIN_USER] - Assigned id: " + userCreated.getIdUser());

        // I save the id of the created object
        idUser = userCreated.getIdUser();

        assertSame(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(userCreated);
        assertEquals("Diego", userCreated.getName());
        assertEquals("19345487diegog", userCreated.getUsername());

    }

    @Test
    public void insertUserStepByStep() throws Exception {

        UserDTO user = new UserDTO();

        user.setName("Diego");
        user.setLastName("González");
        user.setEmail("testDiegoG@gmail.com");
        user.setCellphone("+59812345678");
        user.setStatus(Status.ENABLED.name());
        user.setUsername("19345487diegog");
        user.setAge(30);

//        final MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
//                .post(getRootUrl() + "/users")
//                .content(asJsonString(user))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)).andReturn();


        final ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .post(getRootUrl() + "/users")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        String json = resultActions.andReturn().getResponse().getContentAsString();
        resultActions.andDo(print());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());

        final ObjectMapper mapper = new ObjectMapper();
        UserDTO userCreated = mapper.readValue(json, UserDTO.class);


//        assertNotNull(result.getResponse());
//        assertSame(result.getResponse().getStatus(), HttpStatus.CREATED);
        assertEquals("Diego", userCreated.getName());
        assertEquals("19345487diegog", userCreated.getUsername());

        MockMvcResultMatchers.jsonPath("$.name");
    }

    @Test
    public void insertUser() throws Exception {

        UserDTO user = new UserDTO();

        user.setName("Diego");
        user.setLastName("González");
        user.setEmail("testDiegoG@gmail.com");
        user.setCellphone("+59812345678");
        user.setStatus(Status.ENABLED.name());
        user.setUsername("19345487diegog");
        user.setAge(30);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post(getRootUrl() + "/users")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());

    }

    @Test
    public void getUserById() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get(getRootUrl() + "/users/{id}", 1588)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idUser").value(1588))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cellphone").value("+59899267337"));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    void testCreateDestinationUser() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" -------------- [TEST_CREATE_DESTINATION_USER] ------------- ");
        logger.info(" ----------------------------------------------------------- ");

        UserDTO user = new UserDTO();

        user.setName("Silvia");
        user.setLastName("Narbaiz");
        user.setEmail("testSilviaN@gmail.com");
        user.setCellphone("+59812344578");
        user.setStatus(Status.ENABLED.name());
        user.setUsername("silna2222rbaiz");
        user.setAge(38);

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(getRootUrl() + "/users", user, UserDTO.class);
        UserDTO userCreated = response.getBody();

        logger.info("[TEST_CREATE_DESTINATION_USER] - Assigned id: " + userCreated.getIdUser());

        assertSame(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(userCreated);
        assertEquals("Silvia", userCreated.getName());
        assertEquals("silna2222rbaiz", userCreated.getUsername());
    }

    @Test
    @Order(3)
    void testFindUserById() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ----------------- [TEST_FIND_USER_BY_ID] ------------------ ");
        logger.info(" ----------------------------------------------------------- ");

        logger.info("[TEST_FIND_USER_BY_ID] - id: " + idUser);

        ResponseEntity<UserDTO> response = restTemplate.getForEntity(getRootUrl() + "/users/" + idUser, UserDTO.class);

        UserDTO userFound = response.getBody();
        logger.info("[TEST_FIND_USER_BY_ID] - User found, id: " + userFound.getIdUser());

        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(userFound);
        assertEquals("Diego", userFound.getName());
        assertEquals("19345487diegog", userFound.getUsername());
    }

    @Test
    @Order(4)
    void testUpdateUserById() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ---------------- [TEST_UPDATE_USER_BY_ID] ----------------- ");
        logger.info(" ----------------------------------------------------------- ");

        UserDTO user = new UserDTO();

        logger.info("[TEST_UPDATE_USER_BY_ID] - id: " + idUser);

        user.setIdUser(idUser);
        user.setName("Diego");
        user.setLastName("González");
        user.setEmail("1987diegog@gmail.com");
        user.setStatus(Status.ENABLED.name());
        user.setUsername("19821127diegog");
        user.setCellphone("+59899267337");
        user.setAge(32);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDTO> entity = new HttpEntity<UserDTO>(user, headers);

        ResponseEntity<UserDTO> response = restTemplate //
                .exchange(getRootUrl() + "/users", HttpMethod.PUT, entity, UserDTO.class);

        UserDTO userUpdate = response.getBody();

        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertEquals("Diego", userUpdate.getName());
        assertEquals("19821127diegog", userUpdate.getUsername());
        assertEquals("+59899267337", userUpdate.getCellphone());
        assertSame(32, userUpdate.getAge());
    }

    @Test
    @Order(5)
    void testGetAllUser() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ------------------- [TEST_GET_ALL_USER] ------------------- ");
        logger.info(" ----------------------------------------------------------- ");

        ResponseEntity<ListUsersDTO> response = restTemplate.getForEntity(getRootUrl() + "/users", ListUsersDTO.class);

        ListUsersDTO list = response.getBody();
        List<UserDTO> users = list.getUsers();

        logger.info("[TEST_GET_ALL_USER] - Users size: "
                + ((users != null && users.isEmpty() == false) ? users.size() : " sin usuarios"));

        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(list);
        assertNotNull(users);
    }

    @Test
    @Order(6)
    void testDeleteUserById() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ---------------- [TEST_DELETE_USER_BY_ID] ----------------- ");
        logger.info(" ----------------------------------------------------------- ");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        logger.info("[TEST_DELETE_USER_BY_ID] - id: " + idUser);

        ResponseEntity<String> response = restTemplate //
                .exchange(getRootUrl() + "/users/" + idUser, HttpMethod.DELETE, entity, String.class);

        assertSame(response.getStatusCode(), HttpStatus.OK);
    }
}
