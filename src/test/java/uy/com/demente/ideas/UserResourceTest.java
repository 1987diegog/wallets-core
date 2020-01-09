package uy.com.demente.ideas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import uy.com.demente.ideas.wallets.model.response.ListUsersDTO;
import uy.com.demente.ideas.wallets.model.response.ListWalletsDTO;
import uy.com.demente.ideas.wallets.model.response.UserDTO;
import uy.com.demente.ideas.wallets.model.Status;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author 1987diegog
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WalletsApplication.class, //
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

    private Logger logger = LogManager.getLogger(UserResourceTest.class);

    @LocalServerPort
    public int port;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private static UserDTO userDTO;

    public String getRootUrlUser() {
        return "http://localhost:" + port + "/api/v1/users";
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testCreateOriginUser() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ---------------- [TEST_CREATE_ORIGIN_USER] ---------------- ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_ORIGIN_USER] - Creating user data...");

        UserDTO user = new UserDTO();

        user.setName("Diego");
        user.setLastName("Gonzalez");
        user.setEmail("testUserOrigin@gmail.com");
        user.setCellphone("+59812345678");
        user.setStatus(Status.ENABLED.name());
        user.setUsername("diegogTestOrigin");
        user.setAge(32);

        String jsonContent = mapper.writeValueAsString(user);

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_ORIGIN_USER] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .post(getRootUrlUser())
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_ORIGIN_USER] - Process results...");

        resultActions.andDo(print());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Diego"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("testUserOrigin@gmail.com"));

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        // I save the created object
        userDTO = mapper.readValue(json, UserDTO.class);

        logger.info("[TEST_CREATE_ORIGIN_USER] - Assigned id: " + userDTO.getIdUser());

        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
        assertNotNull(userDTO);
        assertEquals("Diego", userDTO.getName());
        assertEquals("testUserOrigin@gmail.com", userDTO.getEmail());
    }

    @Test
    @Order(2)
    void testUpdateUserById() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ---------------- [TEST_UPDATE_USER_BY_ID] ----------------- ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_UPDATE_USER_BY_ID] - Updating user data, id: " + userDTO.getIdUser());

        userDTO.setName("Diego Andres");
        userDTO.setLastName("Gonzalez Durand");
        userDTO.setEmail("1987diegogTestUpdate@gmail.com");
        userDTO.setStatus(Status.DISABLE.name());
        userDTO.setUsername("1987diegogTestUpdate");
        userDTO.setCellphone("+59899267337");
        userDTO.setAge(30);

        String jsonContent = mapper.writeValueAsString(userDTO);

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_UPDATE_USER_BY_ID] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .put(getRootUrlUser())
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_UPDATE_USER_BY_ID] - Process results...");

        resultActions.andDo(print());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Diego Andres"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("1987diegogTestUpdate@gmail.com"));

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        userDTO = mapper.readValue(json, UserDTO.class);

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertNotNull(userDTO);
        assertEquals("Diego Andres", userDTO.getName());
        assertEquals("1987diegogTestUpdate@gmail.com", userDTO.getEmail());

    }

    @Test
    @Order(3)
    public void getUserById() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ----------------- [TEST_FIND_USER_BY_ID] ------------------ ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_USER_BY_ID] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .get(getRootUrlUser() + "/{id}", userDTO.getIdUser())
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_USER_BY_ID] - Process results...");

        resultActions.andDo(print());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Diego Andres"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("1987diegogTestUpdate@gmail.com"));

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        UserDTO user = mapper.readValue(json, UserDTO.class);

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertNotNull(user);
        assertEquals("Diego Andres", user.getName());
        assertEquals("1987diegogTestUpdate@gmail.com", user.getEmail());
    }



    @Test
    @Order(4)
    public void findAllUsers() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ------------------ [TEST_FIND_ALL_USERS] ------------------ ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_ALL_USERS] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .get(getRootUrlUser())
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_ALL_USERS] - Process results...");

        resultActions.andDo(print());

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        ListUsersDTO list = mapper.readValue(json, ListUsersDTO.class);

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertNotNull(list);
    }

    @Test
    @Order(5)
    public void findWalletsByUser() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" --------------- [TEST_FIND_WALLETS_BY_USER] --------------- ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_WALLETS_BY_USER] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .get(getRootUrlUser() + "/{id}/wallets", userDTO.getIdUser())
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_WALLETS_BY_USER] - Process results...");

        resultActions.andDo(print());

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        ListWalletsDTO list = mapper.readValue(json, ListWalletsDTO.class);

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertNotNull(list);
    }

    @Test
    @Order(6)
    public void deleteUserById() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ----------------- [TEST_DELETE_USER_BY_ID] ------------------ ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_DELETE_USER_BY_ID] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .delete(getRootUrlUser() + "/{id}", userDTO.getIdUser())
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_DELETE_USER_BY_ID] - Process results...");

        resultActions.andDo(print());
        assertEquals(resultActions.andReturn().getResponse().getStatus(), HttpStatus.OK.value());
    }
}


