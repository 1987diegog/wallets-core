package uy.com.demente.ideas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uy.com.demente.ideas.wallets.WalletsApplication;
import uy.com.demente.ideas.wallets.model.Status;
import uy.com.demente.ideas.wallets.model.TypeCoin;
import uy.com.demente.ideas.wallets.model.response.ListUsersDTO;
import uy.com.demente.ideas.wallets.model.response.ListWalletsDTO;
import uy.com.demente.ideas.wallets.model.response.UserDTO;
import uy.com.demente.ideas.wallets.model.response.WalletDTO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author 1987diegog
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WalletsApplication.class, //
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WalletsResourceTest {

    private Logger logger = LogManager.getLogger(WalletsResourceTest.class);

    @LocalServerPort
    public int port;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private static Long idUser;
    private static Long idWallet;

    public String getRootUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testCreateUser() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" -------------------- [TEST_CREATE_USER] ------------------- ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_USER] - Creating user data...");

        UserDTO user = new UserDTO();

        user.setName("Diego");
        user.setLastName("Gonzalez");
        user.setEmail("testUserForWallet@gmail.com");
        user.setCellphone("+59812345678");
        user.setStatus(Status.ENABLED.name());
        user.setUsername("testUserForWallet");
        user.setAge(32);

        String jsonContent = mapper.writeValueAsString(user);

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_USER] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .post(getRootUrl() + "/users")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_USER] - Process results...");

        resultActions.andDo(print());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Diego"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("testUserForWallet@gmail.com"));

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        UserDTO userCreated = mapper.readValue(json, UserDTO.class);

        // I save the id of the created object
        idUser = userCreated.getIdUser();
        logger.info("[TEST_CREATE_USER] - Assigned id: " + userCreated.getIdUser());

        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
        assertNotNull(userCreated);
        assertEquals("Diego", userCreated.getName());
        assertEquals("testUserForWallet@gmail.com", userCreated.getEmail());
    }


    @Test
    @Order(2)
    void testCreateWallet() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ------------------ [TEST_CREATE_WALLET] ------------------- ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_WALLET] - Creating wallet data...");

        WalletDTO walletDTO = new WalletDTO();

        walletDTO.setIdUser(idUser);
        walletDTO.setName("MyWalletTest");
        walletDTO.setTypeCoin(TypeCoin.BITCOIN.name());
        walletDTO.setBalance(new BigDecimal(50_000));

        String jsonContent = mapper.writeValueAsString(walletDTO);

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_WALLET] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .post(getRootUrl() + "/wallets")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_WALLET] - Process results...");

        resultActions.andDo(print());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("MyWalletTest"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.typeCoin").value("BITCOIN"));

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        WalletDTO walletCreated = mapper.readValue(json, WalletDTO.class);

        // I save the id of the created object
        idWallet = walletCreated.getIdWallet();
        logger.info("[TEST_CREATE_WALLET] - Assigned id: " + walletCreated.getIdWallet());

        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
        assertNotNull(walletCreated);
        assertEquals("MyWalletTest", walletCreated.getName());
        assertEquals("BITCOIN", walletCreated.getTypeCoin());
    }
//
//    @Test
//    @Order(2)
//    void testUpdateUserById() throws Exception {
//
//        logger.info(" ----------------------------------------------------------- ");
//        logger.info(" ---------------- [TEST_UPDATE_USER_BY_ID] ----------------- ");
//        logger.info(" ----------------------------------------------------------- ");
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_UPDATE_USER_BY_ID] - Updating user data, id: " + idUser);
//
//        UserDTO user = new UserDTO();
//
//        user.setIdUser(idUser);
//        user.setName("Diego Andres");
//        user.setLastName("Gonzalez Durand");
//        user.setEmail("1987diegogTestUpdate@gmail.com");
//        user.setStatus(Status.DISABLE.name());
//        user.setUsername("1987diegogTestUpdate");
//        user.setCellphone("+59899267337");
//        user.setAge(30);
//
//        String jsonContent = mapper.writeValueAsString(user);
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_UPDATE_USER_BY_ID] - Call mock mvc...");
//
//        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
//                .put(getRootUrl() + "/users")
//                .content(jsonContent)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON));
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_UPDATE_USER_BY_ID] - Process results...");
//
//        resultActions.andDo(print());
//        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Diego Andres"));
//        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("1987diegogTestUpdate@gmail.com"));
//
//        MvcResult result = resultActions.andReturn();
//        String json = result.getResponse().getContentAsString();
//        UserDTO userUpdated = mapper.readValue(json, UserDTO.class);
//
//        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
//        assertNotNull(userUpdated);
//        assertEquals("Diego Andres", userUpdated.getName());
//        assertEquals("1987diegogTestUpdate@gmail.com", userUpdated.getEmail());
//
//    }
//
//    @Test
//    @Order(3)
//    public void getUserById() throws Exception {
//
//        logger.info(" ----------------------------------------------------------- ");
//        logger.info(" ----------------- [TEST_FIND_USER_BY_ID] ------------------ ");
//        logger.info(" ----------------------------------------------------------- ");
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_FIND_USER_BY_ID] - Call mock mvc...");
//
//        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
//                .get(getRootUrl() + "/users/{id}", idUser)
//                .accept(MediaType.APPLICATION_JSON));
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_FIND_USER_BY_ID] - Process results...");
//
//        resultActions.andDo(print());
//        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Diego Andres"));
//        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("1987diegogTestUpdate@gmail.com"));
//
//        MvcResult result = resultActions.andReturn();
//        String json = result.getResponse().getContentAsString();
//        UserDTO user = mapper.readValue(json, UserDTO.class);
//
//        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
//        assertNotNull(user);
//        assertEquals("Diego Andres", user.getName());
//        assertEquals("1987diegogTestUpdate@gmail.com", user.getEmail());
//    }
//
//
//
//    @Test
//    @Order(4)
//    public void findAllUsers() throws Exception {
//
//        logger.info(" ----------------------------------------------------------- ");
//        logger.info(" ------------------ [TEST_FIND_ALL_USERS] ------------------ ");
//        logger.info(" ----------------------------------------------------------- ");
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_FIND_ALL_USERS] - Call mock mvc...");
//
//        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
//                .get(getRootUrl() + "/users")
//                .accept(MediaType.APPLICATION_JSON));
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_FIND_ALL_USERS] - Process results...");
//
//        resultActions.andDo(print());
//
//        MvcResult result = resultActions.andReturn();
//        String json = result.getResponse().getContentAsString();
//        ListUsersDTO list = mapper.readValue(json, ListUsersDTO.class);
//
//        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
//        assertNotNull(list);
//    }
//
//    @Test
//    @Order(5)
//    public void findWalletsByUser() throws Exception {
//
//        logger.info(" ----------------------------------------------------------- ");
//        logger.info(" --------------- [TEST_FIND_WALLETS_BY_USER] --------------- ");
//        logger.info(" ----------------------------------------------------------- ");
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_FIND_WALLETS_BY_USER] - Call mock mvc...");
//
//        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
//                .get(getRootUrl() + "/users/{id}/wallets", idUser)
//                .accept(MediaType.APPLICATION_JSON));
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_FIND_WALLETS_BY_USER] - Process results...");
//
//        resultActions.andDo(print());
//
//        MvcResult result = resultActions.andReturn();
//        String json = result.getResponse().getContentAsString();
//        ListWalletsDTO list = mapper.readValue(json, ListWalletsDTO.class);
//
//        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
//        assertNotNull(list);
//    }
//
//    @Test
//    @Order(6)
//    public void deleteUserById() throws Exception {
//
//        logger.info(" ----------------------------------------------------------- ");
//        logger.info(" ----------------- [TEST_DELETE_USER_BY_ID] ------------------ ");
//        logger.info(" ----------------------------------------------------------- ");
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_DELETE_USER_BY_ID] - Call mock mvc...");
//
//        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
//                .delete(getRootUrl() + "/users/{id}", idUser)
//                .accept(MediaType.APPLICATION_JSON));
//
//        /////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////
//
//        logger.info("[TEST_DELETE_USER_BY_ID] - Process results...");
//
//        resultActions.andDo(print());
//        assertEquals(resultActions.andReturn().getResponse().getStatus(), HttpStatus.OK.value());
//    }
}


