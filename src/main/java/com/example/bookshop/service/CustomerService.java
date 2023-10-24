package com.example.bookshop.service;

import com.example.bookshop.dao.CustomerDao;
import com.example.bookshop.ds.CartItem;
import com.example.bookshop.entity.BookItems;
import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;



    public List<Customer> findALlCustomer(){
        return customerDao.findAll();
    }



    private BookItems toBookItem(CartItem cartItem){
        return new BookItems(
                cartItem.getId(),
                cartItem.getTitle(),
                cartItem.getAuthor(),
                cartItem.getPrice(),
                cartItem.getQuantity(),
                cartItem.getPrice()* cartItem.getQuantity()
        );
    }

    public List<Customer> findByIdOrName(Optional<Integer> id,Optional<String > name){

        return customerDao.findAll(whichId(id).and(whichName(name)));
    }

    private Specification<Customer> whichId(Optional<Integer> params){
        return params.isEmpty()? Specification.where(null):
                (root, query, cb) -> cb.equal(root.get("customer").get("id"),params.get()) ;
    }

    private Specification<Customer> whichName(Optional<String> param){
        return param.isEmpty()? Specification.where(null):
                (root, query, cb) -> cb.equal(root.get("customer").get("id"),param.get()) ;
    }


}
