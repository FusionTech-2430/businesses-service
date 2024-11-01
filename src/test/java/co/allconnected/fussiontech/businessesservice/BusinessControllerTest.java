package co.allconnected.fussiontech.businessesservice;

import co.allconnected.fussiontech.businessesservice.controllers.BusinessController;
import co.allconnected.fussiontech.businessesservice.dtos.BusinessResponseDto;
import co.allconnected.fussiontech.businessesservice.services.BusinessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class BusinessControllerTest {

	@InjectMocks
	private BusinessController businessController;

	@Mock
	private BusinessService businessService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getAllBusinessesTest() {
		// Arrange
		UUID businessId1 = UUID.randomUUID();
		UUID businessId2 = UUID.randomUUID();

		BusinessResponseDto business1 = new BusinessResponseDto(
				businessId1,
				"Business 1",
				Arrays.asList(UUID.randomUUID()),
				"owner1",
				"logo1"
		);

		BusinessResponseDto business2 = new BusinessResponseDto(
				businessId2,
				"Business 2",
				Arrays.asList(UUID.randomUUID()),
				"owner2",
				"logo2"
		);

		List<BusinessResponseDto> expectedBusinesses = Arrays.asList(business1, business2);
		when(businessService.getAllBusinesses()).thenReturn(expectedBusinesses);

		// Act
		ResponseEntity<?> response = businessController.getAllBusinesses();

		// Assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBusinesses, response.getBody());

		@SuppressWarnings("unchecked")
		List<BusinessResponseDto> actualBusinesses = (List<BusinessResponseDto>) response.getBody();
		assertEquals(2, actualBusinesses.size());
		assertEquals("Business 1", actualBusinesses.get(0).getName());
		assertEquals("Business 2", actualBusinesses.get(1).getName());
	}
}