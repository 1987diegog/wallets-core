package uy.com.demente.ideas.wallets.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 1987diegog
 */
public class ListTransfersDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TransferDTO> transfers;

	public ListTransfersDTO() {
		this.transfers = new ArrayList<TransferDTO>();
	}

	public List<TransferDTO> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<TransferDTO> transfers) {
		this.transfers = transfers;
	}
}