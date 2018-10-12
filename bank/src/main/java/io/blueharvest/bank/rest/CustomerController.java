package io.blueharvest.bank.rest;

import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.service.CustomerService;
import io.blueharvest.bank.validation.CustomerValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.List;

import static io.blueharvest.bank.constant.Paths.CUSTOMERS_CONTEXT_PTAH;
import static io.blueharvest.bank.constant.Paths.LIST_CONTEXT_PATH;

/**
 * Controller for {@link Customer}s
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
@Controller
@RequestMapping("/" + CUSTOMERS_CONTEXT_PTAH)
public class CustomerController {
    static final String CUSTOMER_ATTRIBUTE_NAME = "customer";
    public static final String CUSTOMERS_ATTRIBUTE_NAME = "customers";

    private CustomerService customerService;
    private CustomerValidator customerValidator;

    @Inject
    public CustomerController(CustomerService customerService, CustomerValidator customerValidator) {
        this.customerService = customerService;
        this.customerValidator = customerValidator;
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
        return "/" + CUSTOMERS_CONTEXT_PTAH;
    }

    /**
     * Lists all clients in the system
     *
     * @param model
     * @return
     */
    @GetMapping(path = "/" + LIST_CONTEXT_PATH)
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.getAll();
        model.addAttribute(CUSTOMERS_ATTRIBUTE_NAME, customers);
        return "/" + CUSTOMERS_CONTEXT_PTAH;
    }

    /**
     * Creates a new customer and adds it to the system
     *
     * @param customer      The customer to be created
     * @param errors
     * @return
     */
    @PostMapping(name = "createCustomer")
    public String createCustomer(@Validated @ModelAttribute @RequestBody Customer customer, Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldErrors().get(0).toString());
        }
        customerService.create(customer);
        return "redirect:/" + CUSTOMERS_CONTEXT_PTAH;
    }

}
