package uy.com.demente.ideas;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.web.client.RestTemplate;
import uy.com.demente.ideas.wallets.WalletsApplication;
import uy.com.demente.ideas.wallets.business.services.UserService;
import uy.com.demente.ideas.wallets.model.Status;
import uy.com.demente.ideas.wallets.model.TypeCoin;
import uy.com.demente.ideas.wallets.model.response.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 1987diegog
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WalletsApplication.class, //
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {

    private Logger logger = LogManager.getLogger(ApplicationTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TestRestTemplate restTemplate;

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
        user.setEmail("1987diegog_test@gmail.com");
        user.setCellphone("+59812345678");
        user.setStatus(Status.ENABLED.name());
        user.setUsername("1987diegog_test");
        user.setAge(30);

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(getRootUrl() + "/users", user, UserDTO.class);
        UserDTO userCreated = response.getBody();

        DataForTest.getSingletonInstance().setIdUser(userCreated.getIdUser());
        logger.info("[TEST_CREATE_ORIGIN_USER] - Assigned id: " + userCreated.getIdUser());

        assertSame(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(userCreated);
        assertEquals("Diego", userCreated.getName());
        assertEquals("1987diegog_test", userCreated.getUsername());

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
        user.setEmail("silnarbaiz_test@gmail.com");
        user.setCellphone("+59812344578");
        user.setStatus(Status.ENABLED.name());
        user.setUsername("silnarbaiz_test");
        user.setAge(38);

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(getRootUrl() + "/users", user, UserDTO.class);
        UserDTO userCreated = response.getBody();
        logger.info("[TEST_CREATE_DESTINATION_USER] - Assigned id: " + userCreated.getIdUser());

        assertSame(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(userCreated);
        assertEquals("Silvia", userCreated.getName());
        assertEquals("silnarbaiz_test", userCreated.getUsername());
    }

    @Test
    @Order(3)
    void testCreateOriginWalletForUser() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ----------- [TEST_CREATE_ORIGIN_WALLET_FOR_USER] ---------- ");
        logger.info(" ----------------------------------------------------------- ");

        Long idUser = DataForTest.getSingletonInstance().getIdUser();

        WalletDTO wallet = new WalletDTO();
        wallet.setIdUser(idUser);
        wallet.setBalance(new BigDecimal(8000));
        wallet.setCreatedAt(new Date());
        wallet.setName("MyWalletOrigin");
        wallet.setTypeCoin(TypeCoin.PAGACOIN.name());

        ResponseEntity<WalletDTO> response = restTemplate.postForEntity(getRootUrl() + "/wallets", wallet,
                WalletDTO.class);

        WalletDTO walletCreated = response.getBody();
        DataForTest.getSingletonInstance().setHashOriginWallet(walletCreated.getHash());
        DataForTest.getSingletonInstance().setIdWallet(walletCreated.getIdWallet());
        logger.info("[TEST_CREATE_ORIGIN_WALLET_FOR_USER] - Assigned hash origin wallet: " + walletCreated.getHash());

        assertSame(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(walletCreated);
        assertEquals("MyWalletOrigin", walletCreated.getName());
        assertEquals(walletCreated.getBalance(), new BigDecimal(8000));//

    }

    @Test
    @Order(4)
    void testUpdateWallet() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ------------------ [TEST_UPDATE_WALLET] ------------------- ");
        logger.info(" ----------------------------------------------------------- ");

        Long idWallet = DataForTest.getSingletonInstance().getIdWallet();
        Long idUser = DataForTest.getSingletonInstance().getIdUser();
        logger.info("[TEST_UPDATE_WALLET] - id: " + idWallet);

        WalletDTO wallet = new WalletDTO();
        wallet.setIdWallet(idWallet);
        wallet.setIdUser(idUser);
        wallet.setBalance(new BigDecimal(500));
        wallet.setName("NameUpdate");
        wallet.setTypeCoin(TypeCoin.PAGACOIN.name());

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<WalletDTO> entity = new HttpEntity<WalletDTO>(wallet, headers);

        ResponseEntity<WalletDTO> response = restTemplate //
                .exchange(getRootUrl() + "/wallets", HttpMethod.PUT, entity, WalletDTO.class);

        WalletDTO walletUpdate = response.getBody();

        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertEquals("NameUpdate", walletUpdate.getName());
        assertSame(0, walletUpdate.getBalance().compareTo(new BigDecimal(500)));
    }

    @Test
    @Order(5)
    void testCreateDestinationWalletForUser() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" -------- [TEST_CREATE_DESTINATION_WALLET_FOR_USER] -------- ");
        logger.info(" ----------------------------------------------------------- ");

        WalletDTO wallet = new WalletDTO();
        wallet.setIdUser(2L);
        wallet.setBalance(new BigDecimal(0));
        wallet.setCreatedAt(new Date());
        wallet.setName("MyWalletDestination");
        wallet.setTypeCoin(TypeCoin.PAGACOIN.name());

        ResponseEntity<WalletDTO> response = restTemplate.postForEntity(getRootUrl() + "/wallets", wallet,
                WalletDTO.class);

        WalletDTO walletCreated = response.getBody();
        DataForTest.getSingletonInstance().setHashDestinationWallet(walletCreated.getHash());
        logger.info("[TEST_CREATE_DESTINATION_WALLET_FOR_USER] - Assigned hash destination wallet: "
                + walletCreated.getHash());

        assertSame(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(walletCreated);
        assertEquals("MyWalletDestination", walletCreated.getName());
        assertSame(0, walletCreated.getBalance().compareTo(new BigDecimal(0)));//
    }

    @Test
    @Order(6)
    void testFindWalletsByUser() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ---------------- [TEST_FIND_WALLET_BY_USER] --------------- ");
        logger.info(" ----------------------------------------------------------- ");

        Long idUser = DataForTest.getSingletonInstance().getIdUser();
        logger.info("[TEST_FIND_WALLET_BY_USER] - id: " + idUser);

        ResponseEntity<ListWalletsDTO> response = restTemplate.getForEntity(getRootUrl() //
                + "/users/" + idUser + "/wallets", ListWalletsDTO.class);

        ListWalletsDTO list = response.getBody();
        List<WalletDTO> wallets = list.getWallets();

        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(list);
        assertNotNull(wallets);
    }

    @Test
    @Order(7)
    void testCreateTransfer() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ----------------- [TEST_CREATE_TRANSFER] ------------------ ");
        logger.info(" ----------------------------------------------------------- ");

        TransferDTO transfer = new TransferDTO();
        transfer.setAdminName("TEST_ADMIN");
        transfer.setAmount(new BigDecimal(500));
        transfer.setOriginWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
        transfer.setDestinationWallet(DataForTest.getSingletonInstance().getHashDestinationWallet());
        transfer.setTypeCoin(TypeCoin.PAGACOIN.name());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Session-Token", "token-valido");
        HttpEntity<TransferDTO> entityHeaders = new HttpEntity<>(transfer, headers);
        ResponseEntity<TransferDTO> response = restTemplate.postForEntity(getRootUrl() //
                + "/transfers", entityHeaders, TransferDTO.class);

        TransferDTO transferCreated = response.getBody();
        DataForTest.getSingletonInstance().setIdTransfer(transferCreated.getIdTransfer());
        logger.info("[TEST_CREATE_TRANSFER] - Assigned id: " + transferCreated.getIdTransfer());

        assertSame(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(transferCreated);
        assertEquals("TEST_ADMIN", transferCreated.getAdminName());
        assertSame(0, transferCreated.getAmount().compareTo(new BigDecimal(500)));//
    }

    @Test
    @Order(8)
    void testCreateTransferFailsWalletOriginNotFounds() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" -- [TEST_CREATE_TRANSFER_FAILS_WALLET_ORIGIN_NOT_FOUNDS] -- ");
        logger.info(" ----------------------------------------------------------- ");

        TransferDTO transfer = new TransferDTO();
        transfer.setAdminName("TEST_ADMIN");
        transfer.setAmount(new BigDecimal(900000));
        transfer.setOriginWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
        transfer.setDestinationWallet(DataForTest.getSingletonInstance().getHashDestinationWallet());
        transfer.setTypeCoin(TypeCoin.PAGACOIN.name());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Session-Token", "token-valido");
        HttpEntity<TransferDTO> entityHeaders = new HttpEntity<>(transfer, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity(getRootUrl() //
                + "/transfers", entityHeaders, Object.class);

        if (response.getStatusCode() != HttpStatus.OK) {
//            ErrorMessage message = (ErrorMessage) response.getBody();
//            System.out.println("NO ES OK" + message.getMessage());
        }

        assertSame(response.getStatusCode(), HttpStatus.PAYMENT_REQUIRED);
    }

    @Test
    @Order(9)
    void testCreateTransferFailsInvalidToken() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" -------- [TEST_CREATE_TRANSFER_FAILS_INVALID_TOKEN] ------- ");
        logger.info(" ----------------------------------------------------------- ");

        TransferDTO transfer = new TransferDTO();
        transfer.setAdminName("TEST_ADMIN");
        transfer.setAmount(new BigDecimal(500));
        transfer.setOriginWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
        transfer.setDestinationWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
        transfer.setTypeCoin(TypeCoin.PAGACOIN.name());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Session-Token", "INVALID_SESSION_TOKEN");
        HttpEntity<TransferDTO> entityHeaders = new HttpEntity<>(transfer, headers);
        try {

//            ResponseEntity<Object> response = restTemplate.postForEntity(getRootUrl() //
//                    + "/transfers", entityHeaders, Object.class);

            ResponseEntity<Object> response = restTemplate().postForEntity(getRootUrl() //
                    + "/transfers", entityHeaders, Object.class);


            if (response.getStatusCode() != HttpStatus.OK) {
//            ErrorMessage message = (ErrorMessage) response.getBody();
//            System.out.println("NO ES OK" + message.getMessage());
            }

            assertSame(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        }catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new ErrorHandler());

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);

        restTemplate.setRequestFactory(requestFactory);

        return restTemplate;
    }

    @Test
    @Order(10)
    void testCreateTransferFailsWalletMatches() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ------- [TEST_CREATE_TRANSFER_FAILS_WALLET_MATCHES] ------- ");
        logger.info(" ----------------------------------------------------------- ");

        TransferDTO transfer = new TransferDTO();
        transfer.setAdminName("TEST_ADMIN");
        transfer.setAmount(new BigDecimal(500));
        transfer.setOriginWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
        transfer.setDestinationWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
        transfer.setTypeCoin(TypeCoin.PAGACOIN.name());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Session-Token", "token-valido");
        HttpEntity<TransferDTO> entityHeaders = new HttpEntity<>(transfer, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity(getRootUrl() //
                + "/transfers", entityHeaders, Object.class);

        if (response.getStatusCode() != HttpStatus.OK) {
//            ErrorMessage message = (ErrorMessage) response.getBody();
//            System.out.println("NO ES OK" + message.getMessage());
        }


        assertSame(response.getStatusCode(), HttpStatus.BAD_REQUEST);

    }

    @Test
    @Order(11)
    void testGetAllTransfers() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ------------------ [TEST_GET_TRANSFERS] ------------------- ");
        logger.info(" ----------------------------------------------------------- ");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateFromWhitFormat = sdf.format(new Date());
        String dateToWhitFormat = sdf.format(new Date());

        ResponseEntity<ListTransfersDTO> response = restTemplate.getForEntity(
                getRootUrl() + "/transfers?fromTimestamp=" + dateFromWhitFormat + "&toTimestamp=" + dateToWhitFormat
                        + "&originWallet=" + DataForTest.getSingletonInstance().getHashOriginWallet(),
                ListTransfersDTO.class);

        ListTransfersDTO list = response.getBody();
        List<TransferDTO> transfers = list.getTransfers();

        logger.info("[TEST_GET_TRANSFERS] - Transfers size: "
                + ((transfers != null && transfers.isEmpty() == false) ? transfers.size() : " sin transferencias"));

        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(list);
        assertNotNull(transfers);

        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @Test
    @Order(12)
    void testFindTransferById() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" ---------------- [TEST_FIND_TRANSFER_BY_ID] --------------- ");
        logger.info(" ----------------------------------------------------------- ");

        Long idTransfer = DataForTest.getSingletonInstance().getIdTransfer();
        logger.info("[TEST_FIND_TRANSFER_BY_ID] - id: " + idTransfer);

        ResponseEntity<TransferDTO> response = restTemplate.getForEntity(getRootUrl() + "/transfers/" + idTransfer,
                TransferDTO.class);

        TransferDTO transferFound = response.getBody();

        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(transferFound);
        assertEquals(transferFound.getTypeCoin(), TypeCoin.PAGACOIN.name());
        assertSame(0, transferFound.getAmount().compareTo(new BigDecimal(500))); ////
    }

    @Test
    @Order(13)
    void testUpdateTransferById() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" -------------- [TEST_UPDATE_TRANSFER_BY_ID] --------------- ");
        logger.info(" ----------------------------------------------------------- ");

        TransferDTO transfer = new TransferDTO();

        Long idTransfer = DataForTest.getSingletonInstance().getIdTransfer();
        logger.info("[TEST_UPDATE_TRANSFER_BY_ID] - id: " + idTransfer);

        transfer.setIdTransfer(idTransfer);
        transfer.setAdminName("TEST_1987DIEGOG");
        transfer.setAmount(new BigDecimal(1000));
        transfer.setOriginWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
        transfer.setDestinationWallet(DataForTest.getSingletonInstance().getHashDestinationWallet());
        transfer.setTypeCoin(TypeCoin.PAGACOIN.name());

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TransferDTO> entity = new HttpEntity<TransferDTO>(transfer, headers);

        ResponseEntity<TransferDTO> response = restTemplate //
                .exchange(getRootUrl() + "/transfers", HttpMethod.PUT, entity, TransferDTO.class);

        TransferDTO transferUpdate = response.getBody();

        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(transferUpdate);
        assertEquals("TEST_1987DIEGOG", transferUpdate.getAdminName());
        assertSame(0, transferUpdate.getAmount().compareTo(new BigDecimal(1000)));
    }

    @Test
    @Order(14)
    void testDeleteTransferById() {

        logger.info(" ----------------------------------------------------------- ");
        logger.info(" --------------- [TEST_DELETE_TRANSFER_BY_ID] -------------- ");
        logger.info(" ----------------------------------------------------------- ");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        Long idTransferToDelete = DataForTest.getSingletonInstance().getIdTransfer();
        logger.info("[TEST_DELETE_TRANSFER_BY_ID] - id: " + idTransferToDelete);

        ResponseEntity<String> response = restTemplate //
                .exchange(getRootUrl() + "/transfers/" + idTransferToDelete, HttpMethod.DELETE, entity, String.class);

        assertSame(response.getStatusCode(), HttpStatus.OK);
    }
}
