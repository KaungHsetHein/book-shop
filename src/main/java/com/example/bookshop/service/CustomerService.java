package com.example.bookshop.service;

import com.example.bookshop.dao.CustomerDao;
import com.example.bookshop.ds.CartItem;
import com.example.bookshop.entity.BookItems;
import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Transactional
    public void saveCustomer(Customer customer){
        Optional<Customer> customerOptional = customerDao.findByName(customer.getName());
        if (!customerOptional.isPresent()) {


            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customer.setRole(Role.ROLE_USER);
            addBookItems(customer);

            customerDao.save(customer);
        }
        else {
            Customer customer1 = customerOptional.get();
            customer1.setId(customer1.getId());
            var customerBookItems = customer1.getBookItems();
            var bookItems = cartService.listCartItems()
                            .stream().map(c-> toBookItem(c)).collect(Collectors.toSet());
            for (BookItems items :bookItems){
                items.setCustomer(customer1);
                customerBookItems.add(items);
            }


            addBookItems(customer1);
            customerDao.save(customer);

        }
    }

    private void addBookItems(Customer customer) {
        cartService.listCartItems()
                .stream()
                .forEach(c -> {
                    customer.addBook(toBookItem(c));

                });
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
}
