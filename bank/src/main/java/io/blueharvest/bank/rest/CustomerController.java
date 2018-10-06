package io.blueharvest.bank.rest;

import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.service.CustomerService;
import io.blueharvest.bank.validation.CustomerValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

import static io.blueharvest.bank.constant.Fields.ID_PARAMETER;
import static io.blueharvest.bank.constant.Messages.INVALID_PARAMETER_ERROR;
import static io.blueharvest.bank.rest.CustomerController.CUSTOMERS_PATH_SEGMENT;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * Controller for customers
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
@Controller
@RequestMapping("/" + CUSTOMERS_PATH_SEGMENT)
public class CustomerController {
    public static final String CUSTOMER_PATH_SEGMENT = "customer";
    public static final String CUSTOMERS_PATH_SEGMENT = "customers";
    public static final String CUSTOMER_ATTRIBUTE_NAME = "customer";
    public static final String CUSTOMERS_ATTRIBUTE_NAME = "customers";

    private CustomerService customerService;
    private CustomerValidator customerValidator;
    private Logger logger;

    @Inject
    public CustomerController(CustomerService customerService, CustomerValidator customerValidator, Logger logger) {
        this.customerService = customerService;
        this.customerValidator = customerValidator;
        this.logger = logger;
    }

    @InitBinder("customer")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(customerValidator);
    }

    @GetMapping
    public String init(Model model) {
        List<Customer> customers = customerService.getAll();
        model.addAttribute(CUSTOMERS_ATTRIBUTE_NAME, customers);
        model.addAttribute(CUSTOMER_ATTRIBUTE_NAME, new Customer());
        return CUSTOMERS_PATH_SEGMENT;
    }

    @GetMapping(path = "/list", name = "getCustomers")
    @ResponseStatus()
    public String getCustomers(Model model) {
        List<Customer> customers = customerService.getAll();
        model.addAttribute(CUSTOMERS_ATTRIBUTE_NAME, customers);
        return CUSTOMERS_PATH_SEGMENT;
    }

//    @GetMapping(path = "/{id}", name = "getCustomer")
//    public ResponseEntity<Customer> getCustomer(@NotNull @PathVariable(ID_PARAMETER) String id) {
//
//        if ((StringUtils.isBlank(id)) || (!StringUtils.isNumeric(id))) {
//            return ResponseEntity.status(SC_BAD_REQUEST).build();
//        }
//
//        return ResponseEntity.ok(customerService.get(Long.parseLong(id)));
//    }

    @PostMapping(name = "createCustomer")
    public String createCustomer(@Valid @ModelAttribute Customer customer, Errors errors, BindingResult bindingResult) {
        if (errors.hasErrors()) {
            return "/" + CUSTOMERS_PATH_SEGMENT;
        }
        customerService.create(customer);
        return "redirect:/" + CUSTOMERS_PATH_SEGMENT;
    }

//    /**
//     * Handler for {@link IllegalArgumentException} that can be thrown in case of invalid parameter is passed
//     *
//     * @param exp
//     * @param response
//     * @throws IOException
//     */
//    @ExceptionHandler
//    private void handleIllegalArgumentException(IllegalArgumentException exp, HttpServletResponse response) throws IOException {
//        String errorMessage = INVALID_PARAMETER_ERROR + ": " + exp.getMessage();
//
//        logger.error(errorMessage, exp);
//        response.sendError(SC_BAD_REQUEST, errorMessage);
//    }

}
