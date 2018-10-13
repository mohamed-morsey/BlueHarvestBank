package io.blueharvest.bank.rest;

import io.blueharvest.bank.dto.CustomerDto;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.service.CustomerService;
import io.blueharvest.bank.validation.CustomerValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    public static final String CUSTOMER_DTO_ATTRIBUTE_NAME = "customerDto";
    public static final String CUSTOMERS_ATTRIBUTE_NAME = "customers";

    private CustomerService customerService;
    private CustomerValidator customerValidator;

    private ModelMapper mapper = new ModelMapper(); // Mapper for converting between entities and DTOs

    @Inject
    public CustomerController(CustomerService customerService, CustomerValidator customerValidator) {
        this.customerService = customerService;
        this.customerValidator = customerValidator;
    }

    @InitBinder("customerDto")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(customerValidator);
    }

    @GetMapping
    public String init(Model model) {
        List<Customer> customers = customerService.getAll();

        model.addAttribute(CUSTOMERS_ATTRIBUTE_NAME, customers);
        model.addAttribute(CUSTOMER_DTO_ATTRIBUTE_NAME, new CustomerDto());

        return "/" + CUSTOMERS_CONTEXT_PTAH;
    }

    /**
     * Return all customers in the system
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
     * Create a new customer and add it to the system
     *
     * @param customerDto The DTO of customer to be created
     * @param errors
     * @return
     */
    @PostMapping(name = "createCustomer")
    public String createCustomer(@Validated @ModelAttribute CustomerDto customerDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldErrors().get(0).toString());
        }

        Customer customer = new Customer();
        mapper.map(customerDto, customer);

        customerService.create(customer);
        return "redirect:/" + CUSTOMERS_CONTEXT_PTAH;
    }

}
