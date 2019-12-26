package uy.com.demente.ideas.wallets.view.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author 1987diegog
 */
@RestController
@RequestMapping("/api/v1/coffees")
@Api(tags = "Coffee")
public class CoffeeResource {

	Logger logger = LogManager.getLogger(CoffeeResource.class);

	@GetMapping()
	@ApiOperation(value = "Make coffee", notes = "Service for make coffee")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "Coffee done correctly"),
			@ApiResponse(code = 418, message = "I'M A TEAPOT") })
	public ResponseEntity<String> makeCoffe() {

		logger.info("[MAKE_COFFEE] - Making coffee...");
		return new ResponseEntity<>("I'm a teapot, I don't make coffee", HttpStatus.I_AM_A_TEAPOT);
	}
}
