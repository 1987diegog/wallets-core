package uy.com.demente.ideas.wallets.utils;

import java.util.UUID;

/**
 * @author 1987diegog
 */
public class HashKeyUtil {

	/**
	 * Only after generating 1 billion UUIDs every second for the next 100 years,
	 * the probability of creating just one duplicate would be about 50%. Or, to put
	 * it another way, the probability of one duplicate would be about 50% if every
	 * person on earth owned 600 million UUIDs.
	 * 
	 * @return
	 */
	public static String generateHash() {
		return UUID.randomUUID().toString();
	}
}
