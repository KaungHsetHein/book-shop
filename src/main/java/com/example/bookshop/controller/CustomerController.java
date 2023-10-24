package com.example.bookshop.controller;

import com.example.bookshop.dao.CustomerDao;
import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.State;
import com.example.bookshop.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final   CustomerDao dao;
    private final CustomerService service;


    @RequestMapping("/")
    public String listCustomer(Model model, Optional<Integer> id){
        if(id.isPresent()){
            model.addAttribute("customer", dao.findById(id.get()));
        }else{
            model.addAttribute("customer",new Customer(0, "", "", State.KAMAYUT, "", ""));
        }
        model.addAttribute("customerList",dao.findAll());
        List<State> states = Arrays.asList(State.values());
        model.addAttribute("state", states);
        return "customer";
    }

    @PostMapping("/save-customer")
    public String saveCustomer(Customer customer, BindingResult result){
        if(result.hasErrors()){
            return "customer";
        }
        if(customer.getId() == 0){
            dao.save(customer);
        }else {
            var exists = dao.findById(customer.getId()).orElseThrow(EntityNotFoundException::new);
            BeanUtils.copyProperties(customer, exists, "id");
            dao.saveAndFlush(exists);
        }
        return "redirect:/";
    }
    @GetMapping("/find-all")
    public String  findAllCustomer(Model model){
        model.addAttribute("customerList",dao.findAll());
        dao.findAll();
        return "customerForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        if(dao.existsById(id)){
            dao.deleteById(id);
        }
        return "redirect:/";
    }

    @GetMapping("/search")
      public String  findById(@PathVariable Integer id){
         if(dao.existsById(id)){
             dao.findById(id);
         }

         return "redirect:/";
   }

}
