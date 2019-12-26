package uy.com.demente.ideas;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uy.com.demente.ideas.business.services.UserService;
import uy.com.demente.ideas.dto.ListTransfersDTO;
import uy.com.demente.ideas.dto.ListUsersDTO;
import uy.com.demente.ideas.dto.ListWalletsDTO;
import uy.com.demente.ideas.dto.TransferDTO;
import uy.com.demente.ideas.dto.UserDTO;
import uy.com.demente.ideas.dto.WalletDTO;
import uy.com.demente.ideas.model.Status;
import uy.com.demente.ideas.model.TypesCoins;

/**
 * @author 1987diegog
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PagaCoinApplication.class, //
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PagaCoinApplicationTests {

	Logger logger = LogManager.getLogger(PagaCoinApplicationTests.class);

	@Autowired
	public UserService userService;

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port + "/api/v1";
	}

	@Test
	@Order(1)
	public void testCreateOriginUser() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ---------------- [TEST_CREATE_ORIGIN_USER] ---------------- ");
		logger.info(" ----------------------------------------------------------- ");

		UserDTO user = new UserDTO();

		user.setName("Diego");
		user.setLastName("González");
		user.setEmail("1987diegog@gmail.com");
		user.setCellphone("+59812345678");
		user.setStatus(Status.ENABLED.name());
		user.setUsername("19345487diegog");
		user.setAge(30);

		ResponseEntity<UserDTO> response = restTemplate.postForEntity(getRootUrl() + "/users", user, UserDTO.class);
		UserDTO userCreated = response.getBody();

		DataForTest.getSingletonInstance().setIdUser(userCreated.getIdUser());
		logger.info("[TEST_CREATE_ORIGIN_USER] - Assigned id: " + userCreated.getIdUser());

		assertTrue(response.getStatusCode() == HttpStatus.CREATED);
		assertNotNull(userCreated);
		assertTrue(userCreated.getName().equals("Diego"));
		assertTrue(userCreated.getUsername().equals("19345487diegog"));

	}

	@Test
	@Order(2)
	public void testCreateDestinationUser() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" -------------- [TEST_CREATE_DESTINATION_USER] ------------- ");
		logger.info(" ----------------------------------------------------------- ");

		UserDTO user = new UserDTO();

		user.setName("Silvia");
		user.setLastName("Narbaiz");
		user.setEmail("silnarbaiz@gmail.com");
		user.setCellphone("+59812344578");
		user.setStatus(Status.ENABLED.name());
		user.setUsername("silna2222rbaiz");
		user.setAge(38);

		ResponseEntity<UserDTO> response = restTemplate.postForEntity(getRootUrl() + "/users", user, UserDTO.class);
		UserDTO userCreated = response.getBody();
		logger.info("[TEST_CREATE_DESTINATION_USER] - Assigned id: " + userCreated.getIdUser());

		assertTrue(response.getStatusCode() == HttpStatus.CREATED);
		assertNotNull(userCreated);
		assertTrue(userCreated.getName().equals("Silvia"));
		assertTrue(userCreated.getUsername().equals("silna2222rbaiz"));
	}

	@Test
	@Order(3)
	public void testFindUserById() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ----------------- [TEST_FIND_USER_BY_ID] ------------------ ");
		logger.info(" ----------------------------------------------------------- ");

		Long idUser = DataForTest.getSingletonInstance().getIdUser();
		logger.info("[TEST_FIND_USER_BY_ID] - id: " + idUser);

		ResponseEntity<UserDTO> response = restTemplate.getForEntity(getRootUrl() + "/users/" + idUser, UserDTO.class);

		UserDTO userFound = response.getBody();
		logger.info("[TEST_FIND_USER_BY_ID] - User found, id: " + userFound.getIdUser());

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertNotNull(userFound);
		assertTrue(userFound.getName().equals("Diego"));
		assertTrue(userFound.getUsername().equals("19345487diegog"));
	}

	@Test
	@Order(4)
	public void testUpdateUserById() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ---------------- [TEST_UPDATE_USER_BY_ID] ----------------- ");
		logger.info(" ----------------------------------------------------------- ");

		UserDTO user = new UserDTO();

		Long idUser = DataForTest.getSingletonInstance().getIdUser();
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

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertTrue(userUpdate.getName().equals("Diego"));
		assertTrue(userUpdate.getUsername().equals("19821127diegog"));
		assertTrue(userUpdate.getCellphone().equals("+59899267337"));
		assertTrue(userUpdate.getAge() == 32);
	}

	@Test
	@Order(5)
	public void testCreateUserToDelete() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" --------------- [TEST_CREATE_USER_TO_DELETE] -------------- ");
		logger.info(" ----------------------------------------------------------- ");

		UserDTO user = new UserDTO();

		user.setName("User");
		user.setLastName("toDelete");
		user.setEmail("userToDelete@gmail.com");
		user.setCellphone("+59898744578");
		user.setStatus(Status.DISABLE.name());
		user.setUsername("userToDelete");
		user.setAge(99);

		ResponseEntity<UserDTO> response = restTemplate.postForEntity(getRootUrl() + "/users", user, UserDTO.class);
		UserDTO userToDelete = response.getBody();
		DataForTest.getSingletonInstance().setIdUserToDelete(userToDelete.getIdUser());
		logger.info("[TEST_CREATE_USER_TO_DELETE] - Assigned id: " + userToDelete.getIdUser());

		assertTrue(response.getStatusCode() == HttpStatus.CREATED);
		assertNotNull(userToDelete);
		assertTrue(userToDelete.getName().equals("User"));
		assertTrue(userToDelete.getUsername().equals("userToDelete"));//
	}

	@Test
	@Order(6)
	public void testDeleteUserById() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ---------------- [TEST_DELETE_USER_BY_ID] ----------------- ");
		logger.info(" ----------------------------------------------------------- ");

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		Long idUserToDelete = DataForTest.getSingletonInstance().getIdUserToDelete();
		logger.info("[TEST_DELETE_USER_BY_ID] - id: " + idUserToDelete);

		ResponseEntity<String> response = restTemplate //
				.exchange(getRootUrl() + "/users/" + idUserToDelete, HttpMethod.DELETE, entity, String.class);

		assertTrue(response.getStatusCode() == HttpStatus.OK);
	}

	@Test
	@Order(7)
	public void testGetAllUser() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ------------------- [TEST_GET_ALL_USER] ------------------- ");
		logger.info(" ----------------------------------------------------------- ");

		ResponseEntity<ListUsersDTO> response = restTemplate.getForEntity(getRootUrl() + "/users", ListUsersDTO.class);

		ListUsersDTO list = response.getBody();
		List<UserDTO> users = list.getUsers();

		logger.info("[TEST_GET_ALL_USER] - Users size: "
				+ ((users != null && users.isEmpty() == false) ? users.size() : " sin usuarios"));

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertNotNull(list);
		assertNotNull(users);

	}

	@Test
	@Order(8)
	public void testCreateOriginWalletForUser() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ----------- [TEST_CREATE_ORIGIN_WALLET_FOR_USER] ---------- ");
		logger.info(" ----------------------------------------------------------- ");

		Long idUser = DataForTest.getSingletonInstance().getIdUser();

		WalletDTO wallet = new WalletDTO();
		wallet.setIdUser(idUser);
		wallet.setBalance(new BigDecimal(8000));
		wallet.setCreated(new Date());
		wallet.setName("MyWalletOrigin");
		wallet.setTypeCoin(TypesCoins.PAGACOIN.name());

		ResponseEntity<WalletDTO> response = restTemplate.postForEntity(getRootUrl() + "/wallets", wallet,
				WalletDTO.class);

		WalletDTO walletCreated = response.getBody();
		DataForTest.getSingletonInstance().setHashOriginWallet(walletCreated.getHash());
		DataForTest.getSingletonInstance().setIdWallet(walletCreated.getIdWallet());
		logger.info("[TEST_CREATE_ORIGIN_WALLET_FOR_USER] - Assigned hash origin wallet: " + walletCreated.getHash());

		assertTrue(response.getStatusCode() == HttpStatus.CREATED);
		assertNotNull(walletCreated);
		assertTrue(walletCreated.getName().equals("MyWalletOrigin"));
		assertTrue(walletCreated.getBalance().equals(new BigDecimal(8000)));//

	}

	@Test
	@Order(9)
	public void testUpdateWallet() {

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
		wallet.setTypeCoin(TypesCoins.PAGACOIN.name());

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<WalletDTO> entity = new HttpEntity<WalletDTO>(wallet, headers);

		ResponseEntity<WalletDTO> response = restTemplate //
				.exchange(getRootUrl() + "/wallets", HttpMethod.PUT, entity, WalletDTO.class);

		WalletDTO walletUpdate = response.getBody();

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertTrue(walletUpdate.getName().equals("NameUpdate"));
		assertTrue(walletUpdate.getBalance().compareTo(new BigDecimal(500)) == 0);
	}

	@Test
	@Order(10)
	public void testCreateDestinationWalletForUser() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" -------- [TEST_CREATE_DESTINATION_WALLET_FOR_USER] -------- ");
		logger.info(" ----------------------------------------------------------- ");

		WalletDTO wallet = new WalletDTO();
		wallet.setIdUser(2L);
		wallet.setBalance(new BigDecimal(0));
		wallet.setCreated(new Date());
		wallet.setName("MyWalletDestination");
		wallet.setTypeCoin(TypesCoins.PAGACOIN.name());

		ResponseEntity<WalletDTO> response = restTemplate.postForEntity(getRootUrl() + "/wallets", wallet,
				WalletDTO.class);

		WalletDTO walletCreated = response.getBody();
		DataForTest.getSingletonInstance().setHashDestinationWallet(walletCreated.getHash());
		logger.info("[TEST_CREATE_DESTINATION_WALLET_FOR_USER] - Assigned hash destination wallet: "
				+ walletCreated.getHash());

		assertTrue(response.getStatusCode() == HttpStatus.CREATED);
		assertNotNull(walletCreated);
		assertTrue(walletCreated.getName().equals("MyWalletDestination"));
		assertTrue(walletCreated.getBalance().compareTo(new BigDecimal(0)) == 0);//
	}

	@Test
	@Order(11)
	public void testFindWalletsByUser() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ---------------- [TEST_FIND_WALLET_BY_USER] --------------- ");
		logger.info(" ----------------------------------------------------------- ");

		Long idUser = DataForTest.getSingletonInstance().getIdUser();
		logger.info("[TEST_FIND_WALLET_BY_USER] - id: " + idUser);

		ResponseEntity<ListWalletsDTO> response = restTemplate.getForEntity(getRootUrl() //
				+ "/users/" + idUser + "/wallets", ListWalletsDTO.class);

		ListWalletsDTO list = response.getBody();
		List<WalletDTO> wallets = list.getWallets();

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertNotNull(list);
		assertNotNull(wallets);
	}

	@Test
	@Order(12)
	public void testCreateTransfer() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ----------------- [TEST_CREATE_TRANSFER] ------------------ ");
		logger.info(" ----------------------------------------------------------- ");

		TransferDTO transfer = new TransferDTO();
		transfer.setAdminName("TEST_ADMIN");
		transfer.setAmount(new BigDecimal(500));
		transfer.setOriginWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
		transfer.setDestinationWallet(DataForTest.getSingletonInstance().getHashDestinationWallet());
		transfer.setTypeCoin(TypesCoins.PAGACOIN.name());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Session-Token", "token-valido");
		HttpEntity<TransferDTO> entityHeaders = new HttpEntity<>(transfer, headers);
		ResponseEntity<TransferDTO> response = restTemplate.postForEntity(getRootUrl() //
				+ "/transfers", entityHeaders, TransferDTO.class);

		TransferDTO transferCreated = response.getBody();
		DataForTest.getSingletonInstance().setIdTransfer(transferCreated.getIdTransfer());
		logger.info("[TEST_CREATE_TRANSFER] - Assigned id: " + transferCreated.getIdTransfer());

		assertTrue(response.getStatusCode() == HttpStatus.CREATED);
		assertNotNull(transferCreated);
		assertTrue(transferCreated.getAdminName().equals("TEST_ADMIN"));
		assertTrue(transferCreated.getAmount().compareTo(new BigDecimal(500)) == 0);//
	}

	@Test
	@Order(13)
	public void testCreateTransferFailsWalletOriginNotFounds() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" -- [TEST_CREATE_TRANSFER_FAILS_WALLET_ORIGIN_NOT_FOUNDS] -- ");
		logger.info(" ----------------------------------------------------------- ");

		TransferDTO transfer = new TransferDTO();
		transfer.setAdminName("TEST_ADMIN");
		transfer.setAmount(new BigDecimal(900000));
		transfer.setOriginWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
		transfer.setDestinationWallet(DataForTest.getSingletonInstance().getHashDestinationWallet());
		transfer.setTypeCoin(TypesCoins.PAGACOIN.name());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Session-Token", "token-valido");
		HttpEntity<TransferDTO> entityHeaders = new HttpEntity<>(transfer, headers);
		ResponseEntity<TransferDTO> response = restTemplate.postForEntity(getRootUrl() //
				+ "/transfers", entityHeaders, TransferDTO.class);

		TransferDTO transferCreated = response.getBody();

		assertTrue(response.getStatusCode() == HttpStatus.PAYMENT_REQUIRED);
		assertTrue(transferCreated == null);
	}

	@Test
	@Order(14)
	public void testCreateTransferFailsInvalidToken() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" -------- [TEST_CREATE_TRANSFER_FAILS_INVALID_TOKEN] ------- ");
		logger.info(" ----------------------------------------------------------- ");

		TransferDTO transfer = new TransferDTO();
		transfer.setAdminName("TEST_ADMIN");
		transfer.setAmount(new BigDecimal(500));
		transfer.setOriginWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
		transfer.setDestinationWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
		transfer.setTypeCoin(TypesCoins.PAGACOIN.name());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Session-Token", "INVALID_SESSION_TOKEN");
		HttpEntity<TransferDTO> entityHeaders = new HttpEntity<>(transfer, headers);
		ResponseEntity<TransferDTO> response = restTemplate.postForEntity(getRootUrl() //
				+ "/transfers", entityHeaders, TransferDTO.class);

		TransferDTO transferCreated = response.getBody();

		assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
		assertTrue(transferCreated == null);
	}

	@Test
	@Order(15)
	public void testCreateTransferFailsWalletMatches() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ------- [TEST_CREATE_TRANSFER_FAILS_WALLET_MATCHES] ------- ");
		logger.info(" ----------------------------------------------------------- ");

		TransferDTO transfer = new TransferDTO();
		transfer.setAdminName("TEST_ADMIN");
		transfer.setAmount(new BigDecimal(500));
		transfer.setOriginWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
		transfer.setDestinationWallet(DataForTest.getSingletonInstance().getHashOriginWallet());
		transfer.setTypeCoin(TypesCoins.PAGACOIN.name());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Session-Token", "token-valido");
		HttpEntity<TransferDTO> entityHeaders = new HttpEntity<>(transfer, headers);
		ResponseEntity<TransferDTO> response = restTemplate.postForEntity(getRootUrl() //
				+ "/transfers", entityHeaders, TransferDTO.class);

		TransferDTO transferCreated = response.getBody();
//		logger.info("[TEST_CREATE_TRANSFER_FAILS_WALLET_MATCHES] - Assigned id: " + transferCreated.getIdTransfer());

		assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
		assertTrue(transferCreated == null);

	}

	@Test
	@Order(16)
	public void testGetAllTransfers() {

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

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertNotNull(list);
		assertNotNull(transfers);

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertNotNull(response.getBody());
	}

	@Test
	@Order(17)
	public void testFindTransferById() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" ---------------- [TEST_FIND_TRANSFER_BY_ID] --------------- ");
		logger.info(" ----------------------------------------------------------- ");

		Long idTransfer = DataForTest.getSingletonInstance().getIdTransfer();
		logger.info("[TEST_FIND_TRANSFER_BY_ID] - id: " + idTransfer);

		ResponseEntity<TransferDTO> response = restTemplate.getForEntity(getRootUrl() + "/transfers/" + idTransfer,
				TransferDTO.class);

		TransferDTO transferFound = response.getBody();

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertNotNull(transferFound);
		assertTrue(transferFound.getTypeCoin().equals(TypesCoins.PAGACOIN.name()));
		assertTrue(transferFound.getAmount().compareTo(new BigDecimal(500)) == 0); ////
	}

	@Test
	@Order(18)
	public void testUpdateTransferById() {

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
		transfer.setTypeCoin(TypesCoins.PAGACOIN.name());

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<TransferDTO> entity = new HttpEntity<TransferDTO>(transfer, headers);

		ResponseEntity<TransferDTO> response = restTemplate //
				.exchange(getRootUrl() + "/transfers", HttpMethod.PUT, entity, TransferDTO.class);

		TransferDTO transferUpdate = response.getBody();

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		assertNotNull(transferUpdate);
		assertTrue(transferUpdate.getAdminName().equals("TEST_1987DIEGOG"));
		assertTrue(transferUpdate.getAmount().compareTo(new BigDecimal(1000)) == 0);
	}

	@Test
	@Order(19)
	public void testDeleteTransferById() {

		logger.info(" ----------------------------------------------------------- ");
		logger.info(" --------------- [TEST_DELETE_TRANSFER_BY_ID] -------------- ");
		logger.info(" ----------------------------------------------------------- ");

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		Long idTransferToDelete = DataForTest.getSingletonInstance().getIdTransfer();
		logger.info("[TEST_DELETE_TRANSFER_BY_ID] - id: " + idTransferToDelete);

		ResponseEntity<String> response = restTemplate //
				.exchange(getRootUrl() + "/transfers/" + idTransferToDelete, HttpMethod.DELETE, entity, String.class);

		assertTrue(response.getStatusCode() == HttpStatus.OK);
	}
}
