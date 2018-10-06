package io.blueharvest.bank.rest;

import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.service.CustomerService;
import io.blueharvest.bank.validation.CustomerValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

import static io.blueharvest.bank.constant.Fields.ID_PARAMETER;
import static io.blueharvest.bank.rest.CustomerController.CUSTOMERS_PATH_SEGMENT;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * Controller for bank
 *
 * @author Mohamed Morsey
 * Date: 2018-08-29
 **/
@Controller
@RequestMapping("/" + CUSTOMERS_PATH_SEGMENT)
public class CustomerController {
    public static final String CUSTOMERS_PATH_SEGMENT = "customers";


    private CustomerService customerService;
    private CustomerValidator customerValidator;
    private Logger logger;

    @Inject
    public CustomerController(CustomerService customerService, CustomerValidator customerValidator, Logger logger){
        this.customerService = customerService;
        this.customerValidator = customerValidator;
        this.logger = logger;
    }

    @InitBinder("customer")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(customerValidator);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, name = "getCustomers")
    @ResponseStatus()
    public ResponseEntity<List<Customer>> getCustomers(){
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE}, name = "getCustomer")
    public ResponseEntity<Customer> getCustomer(@NotNull @PathVariable(ID_PARAMETER) String id){

        if((StringUtils.isBlank(id)) || (!StringUtils.isNumeric(id))){
            return ResponseEntity.status(SC_BAD_REQUEST).build();
        }

        return ResponseEntity.ok(customerService.get(Long.parseLong(id)));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}, name = "createCustomer")
    public ResponseEntity createCustomer(@Validated @RequestBody final Customer customer){
        customerService.create(customer);
        return ResponseEntity.ok().build();
    }

//    /**
//     * Handler for {@link IllegalArgumentException} that can be thrown in case of invalid file format parameter
//     *
//     * @param exp
//     * @param response
//     * @throws IOException
//     */
//    @ExceptionHandler
//    private void handleIllegalArgumentException(IllegalArgumentException exp, HttpServletResponse response) throws IOException {
//        String errorMessage = INVALID_FILE_FORMAT_ERROR + ": " + exp.getMessage();
//
//        logger.error(errorMessage, exp);
//        response.sendError(SC_BAD_REQUEST, errorMessage);
//    }

}
