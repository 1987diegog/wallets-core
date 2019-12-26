package uy.com.demente.ideas;

/**
 * @author 1987diegog
 */
public class DataForTest {

	private static DataForTest dataTest;

	private Long idUser;
	private Long idUserToDelete;
	private Long idTransfer;
	private String hashOriginWallet;
	private Long idWallet;
	private String hashDestinationWallet;

	private DataForTest() {
	}

	public static DataForTest getSingletonInstance() {
		if (dataTest == null) {
			dataTest = new DataForTest();
		}

		return dataTest;
	}

	public Long getIdTransfer() {
		return idTransfer;
	}

	public void setIdTransfer(Long idTransfer) {
		this.idTransfer = idTransfer;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getIdUserToDelete() {
		return idUserToDelete;
	}

	public void setIdUserToDelete(Long idUserToDelete) {
		this.idUserToDelete = idUserToDelete;
	}

	public String getHashOriginWallet() {
		return hashOriginWallet;
	}

	public void setHashOriginWallet(String hashOriginWallet) {
		this.hashOriginWallet = hashOriginWallet;
	}

	public String getHashDestinationWallet() {
		return hashDestinationWallet;
	}

	public void setHashDestinationWallet(String hashDestinationWallet) {
		this.hashDestinationWallet = hashDestinationWallet;
	}

	public Long getIdWallet() {
		return idWallet;
	}

	public void setIdWallet(Long idWallet) {
		this.idWallet = idWallet;
	}

}
