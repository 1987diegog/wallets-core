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
import uy.com.demente.ideas.wallets.model.Wallet;
import uy.com.demente.ideas.wallets.model.response.ListUsersDTO;
import uy.com.demente.ideas.wallets.model.response.ListWalletsDTO;
import uy.com.demente.ideas.wallets.model.response.UserDTO;
import uy.com.demente.ideas.wallets.model.response.WalletDTO;
import uy.com.demente.ideas.wallets.view.resources.WalletResource;

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
    private static WalletDTO walletDTO;
    private static long idUser;


    public String getRootUrlUser() {
        return "http://localhost:" + port + "/api/v1/users";
    }

    public String getRootUrlWallet() {
        return "http://localhost:" + port + "/api/v1/wallets";
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
                .post(getRootUrlUser())
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

        WalletDTO createWalletDTO = new WalletDTO();

        createWalletDTO.setIdUser(idUser);
        createWalletDTO.setName("MyWalletTest");
        createWalletDTO.setTypeCoin(TypeCoin.ETHEREUM.name());
        createWalletDTO.setBalance(new BigDecimal(50_000));

        String jsonContent = mapper.writeValueAsString(createWalletDTO);

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_WALLET] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .post(getRootUrlWallet())
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_CREATE_WALLET] - Process results...");

        resultActions.andDo(print());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("MyWalletTest"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.typeCoin").value("ETHEREUM"));

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        // I save the created object
        walletDTO = mapper.readValue(json, WalletDTO.class);

        logger.info("[TEST_CREATE_WALLET] - Assigned id: " + walletDTO.getIdWallet());

        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
        assertNotNull(walletDTO);
        assertEquals("MyWalletTest", walletDTO.getName());
        assertEquals("ETHEREUM", walletDTO.getTypeCoin());
    }

    @Test
    @Order(3)
    void testUpdateWalletById() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" --------------- [TEST_UPDATE_WALLET_BY_ID] ---------------- ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_UPDATE_WALLET_BY_ID] - Updating wallet data, id: " + walletDTO.getIdWallet());

        walletDTO.setName("MyWalletTestUpdated");
        walletDTO.setTypeCoin(TypeCoin.BITCOIN.name());
        walletDTO.setBalance(new BigDecimal(150_000));
        String jsonContent = mapper.writeValueAsString(walletDTO);

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_UPDATE_WALLET_BY_ID] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .put(getRootUrlWallet())
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_UPDATE_WALLET_BY_ID] - Process results...");

        resultActions.andDo(print());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("MyWalletTestUpdated"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(new BigDecimal(150_000)));

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        walletDTO = mapper.readValue(json, WalletDTO.class);

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertNotNull(walletDTO);
        assertEquals("MyWalletTestUpdated", walletDTO.getName());
        assertEquals(walletDTO.getBalance(), new BigDecimal(150_000));//

    }


    @Test
    @Order(4)
    public void getWalletByHash() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" --------------- [TEST_FIND_WALLET_BY_HASH] ---------------- ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_WALLET_BY_HASH] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .get(getRootUrlWallet() + "/{hash}", walletDTO.getHash())
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_WALLET_BY_HASH] - Process results...");

        resultActions.andDo(print());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("MyWalletTestUpdated"));
//        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(new BigDecimal(150_000.0)));

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        walletDTO = mapper.readValue(json, WalletDTO.class);

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertNotNull(walletDTO);
        assertEquals("MyWalletTestUpdated", walletDTO.getName());
//        assertEquals(walletDTO.getBalance(), new BigDecimal(150_000));//
    }


    @Test
    @Order(5)
    public void findAllWallets() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ----------------- [TEST_FIND_ALL_WALLETS] ----------------- ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_ALL_WALLETS] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .get(getRootUrlWallet())
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_FIND_ALL_USERS] - Process results...");

        resultActions.andDo(print());

        MvcResult result = resultActions.andReturn();
        String json = result.getResponse().getContentAsString();
        ListWalletsDTO list = mapper.readValue(json, ListWalletsDTO.class);

        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertNotNull(list);
    }

    @Test
    @Order(6)
    public void deleteWalletById() throws Exception {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" --------------- [TEST_DELETE_WALLET_BY_ID] ---------------- ");
        logger.info(" ----------------------------------------------------------- ");

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_DELETE_WALLET_BY_ID] - Call mock mvc...");

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders
                .delete(getRootUrlWallet() + "/{id}", walletDTO.getIdWallet())
                .accept(MediaType.APPLICATION_JSON));

        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        logger.info("[TEST_DELETE_WALLET_BY_ID] - Process results...");

        resultActions.andDo(print());
        assertEquals(resultActions.andReturn().getResponse().getStatus(), HttpStatus.OK.value());
    }


}


