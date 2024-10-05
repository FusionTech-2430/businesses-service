package co.allconnected.fussiontech.businessesservice;

import co.allconnected.fussiontech.businessesservice.controllers.BusinessController;
import co.allconnected.fussiontech.businessesservice.dtos.BusinessResponseDto;
import co.allconnected.fussiontech.businessesservice.services.BusinessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(BusinessController.class)
class BusinessesServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BusinessService businessService;

	// Method to test get all businesses /api/v1/businesses
	@Test
	void getAllBusinessTest() throws Exception {
		// Arrange
		BusinessResponseDto business1 = new BusinessResponseDto(UUID.randomUUID(), "Business 1", Arrays.asList(UUID.randomUUID()), "owner1", "logo1");
		BusinessResponseDto business2 = new BusinessResponseDto(UUID.randomUUID(), "Business 2", Arrays.asList(UUID.randomUUID()), "owner2", "logo2");

		when(businessService.getAllBusinesses()).thenReturn(Arrays.asList(business1, business2));

		// Act & Assert
		mockMvc.perform(get("/api/v1/businesses")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name").value("Business 1"))
				.andExpect(jsonPath("$[1].name").value("Business 2"));
	}
}
