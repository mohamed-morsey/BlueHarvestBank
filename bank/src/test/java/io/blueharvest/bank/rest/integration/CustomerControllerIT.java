package io.blueharvest.bank.rest.integration;

import io.blueharvest.bank.error.BankExceptionHandler;
import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.repository.AccountRepository;
import io.blueharvest.bank.repository.CustomerRepository;
import io.blueharvest.bank.repository.TransactionRepository;
import io.blueharvest.bank.rest.AccountController;
import io.blueharvest.bank.rest.CustomerController;
import io.blueharvest.bank.utils.StandaloneMvcTestViewResolver;
import io.blueharvest.bank.validation.CustomerValidator;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;

import static io.blueharvest.bank.constant.FieldValues.ADDRESS;
import static io.blueharvest.bank.constant.FieldValues.CUSTOMER_ID;
import static io.blueharvest.bank.constant.FieldValues.NAME;
import static io.blueharvest.bank.constant.FieldValues.POSTCODE;
import static io.blueharvest.bank.constant.FieldValues.SURNAME;
import static io.blueharvest.bank.constant.Paths.CUSTOMERS_CONTEXT_PTAH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test class for {@link CustomerController} and {@link AccountController} that tests the whole creation scenario
 *
 * @author Mohamed Morsey
 * Date: 2018-10-11
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIT {

    private static final ParameterizedTypeReference<String> STRING_RESPONSE_TYPE =
            new ParameterizedTypeReference<String>() {
            };

    @Inject
    private CustomerRepository customerRepository;
    @Inject
    private AccountRepository accountRepository;
    @Inject
    private TransactionRepository transactionRepository;
    @Inject
    private TestRestTemplate restTemplate;
    @Inject
    private CustomerController customerController;

    private MockMvc mockMvc;

    private Customer testCustomer;
    private Account testAccount;
    private UriComponentsBuilder builder;

    private SoftAssertions softly = new SoftAssertions();

    @Before
    public void setUp() throws Exception {
        testCustomer = new Customer(CUSTOMER_ID, NAME, SURNAME, ADDRESS, POSTCODE);
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setViewResolvers(new StandaloneMvcTestViewResolver()).setValidator(new CustomerValidator())
                .setControllerAdvice(new BankExceptionHandler()).build();
    }

    @After
    public void teardown() {
        customerRepository.deleteAll();
        accountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    /**
     * Tests the following scenario
     * <ul>
     * <li>Create customer</li>
     * <li>Open account for customer</li>
     * <li>Make sure a transaction is created for the account</li>
     * </ul>
     */
    @Test
    public void testCrudForAnnotations() throws Exception {
        // Prepare the correct URI to contact the controller
        builder = UriComponentsBuilder.fromUriString("/" + CUSTOMERS_CONTEXT_PTAH);
        String uri = builder.toUriString();

        HttpEntity<Customer> customerHttpEntity = new HttpEntity<>(testCustomer);
//        String location = restTemplate.postForObject(uri, testCustomer, String.class);

//        ResponseEntity<String> customerCreateResult =
//                restTemplate.exchange(
//                        uri, POST, customerHttpEntity, String.class);

        this.mockMvc.perform(post("/" + CUSTOMERS_CONTEXT_PTAH)
                .flashAttr("customer", testCustomer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + CUSTOMERS_CONTEXT_PTAH));

//        softly.assertThat(customerCreateResult.getStatusCode()).isEqualTo(HttpStatus.OK);
//        softly.assertThat(customerCreateResult.getBody().getData())
//                .as("Count of annotations before add")
//                .hasSize(0);
        softly.assertAll();

//        // Adding an annotation
//        builder.queryParam(TEXT_BODY_PARAMETER, TEXT_BODY);
//        uri = builder.buildAndExpand(TEXT_CHUNK_ID).toUri();
//
//        ResponseEntity<ResultEnvelope<Annotation>> resultOfPosting =
//                restTemplate.exchange(
//                        uri, POST, new HttpEntity<>(headers), ANNOTATION_RESPONSE_TYPE);
//
//        softly.assertThat(resultOfPosting.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        softly.assertThat(resultOfPosting.getBody().getData().getTextBody()).isEqualTo(TEXT_BODY);
//        softly.assertAll();
//
//        String insertedAnnotationId = resultOfPosting.getBody().getData().getId();
//
//        // Getting annotations after adding one
//        builder.replaceQueryParam(TEXT_BODY_PARAMETER, (Object) null);
//        uri = builder.buildAndExpand(TEXT_CHUNK_ID).toUri();
//
//        customerCreateResult =
//                restTemplate.exchange(
//                        uri, GET, new HttpEntity<>(headers), ANNOTATION_LIST_RESPONSE_TYPE);
//
//        softly.assertThat(customerCreateResult.getStatusCode()).isEqualTo(HttpStatus.OK);
//        softly.assertThat(customerCreateResult.getBody().getData())
//                .as("Count of annotations after add")
//                .hasSize(1);
//        softly.assertAll();
//
//        // Updating the annotation
//        builder.replaceQueryParam(TEXT_BODY_PARAMETER, MODIFIED_TEXT_BODY)
//                .queryParam(ANNOTATION_ID_PARAMETER, insertedAnnotationId);
//        uri = builder.buildAndExpand(TEXT_CHUNK_ID).toUri();
//
//        ResponseEntity<ResultEnvelope<Void>> resultOfPutting =
//                restTemplate.exchange(
//                        uri, PUT, new HttpEntity<>(headers), EMPTY_RESULT_RESPONSE_TYPE);
//
//        softly.assertThat(resultOfPutting.getStatusCode()).isEqualTo(HttpStatus.OK);
//        softly.assertAll();
//
//        // Getting annotations after update
//        builder.replaceQueryParam(TEXT_BODY_PARAMETER, (Object) null)
//                .replaceQueryParam(ANNOTATION_ID_PARAMETER, (Object) null);
//        uri = builder.buildAndExpand(TEXT_CHUNK_ID).toUri();
//
//        customerCreateResult =
//                restTemplate.exchange(
//                        uri, GET, new HttpEntity<>(headers), ANNOTATION_LIST_RESPONSE_TYPE);
//
//        softly.assertThat(customerCreateResult.getStatusCode()).isEqualTo(HttpStatus.OK);
//        softly.assertThat(customerCreateResult.getBody().getData())
//                .as("Count of annotations after update")
//                .hasSize(1);
//        softly.assertThat(customerCreateResult.getBody().getData().get(0).getTextBody())
//                .isEqualTo(MODIFIED_TEXT_BODY);
//        softly.assertAll();
//
//        // Deleting the annotation
//        builder.replaceQueryParam(ANNOTATION_ID_PARAMETER, insertedAnnotationId);
//        uri = builder.buildAndExpand(TEXT_CHUNK_ID).toUri();
//
//        ResponseEntity<Void> resultOfDeleting =
//                restTemplate.exchange(uri, DELETE, new HttpEntity<>(headers), EMPTY_RESPONSE_TYPE);
//
//        softly.assertThat(resultOfDeleting.getStatusCode()).isEqualTo(HttpStatus.OK);
//        softly.assertAll();
//
//        // Getting annotations after delete
//        builder.replaceQueryParam(TEXT_BODY_PARAMETER, (Object) null)
//                .replaceQueryParam(ANNOTATION_ID_PARAMETER, (Object) null);
//        uri = builder.buildAndExpand(TEXT_CHUNK_ID).toUri();
//
//        customerCreateResult =
//                restTemplate.exchange(
//                        uri, GET, new HttpEntity<>(headers), ANNOTATION_LIST_RESPONSE_TYPE);
//
//        softly.assertThat(customerCreateResult.getStatusCode()).isEqualTo(HttpStatus.OK);
//        softly.assertThat(customerCreateResult.getBody().getData())
//                .as("Count of annotations after delete")
//                .hasSize(0);
//        softly.assertAll();
    }
}