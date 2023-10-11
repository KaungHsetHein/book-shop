package com.example.bookshop.controller;

import com.example.bookshop.ds.CartItem;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Customer;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final CartService cartService;
    private final BookService bookService;
    private final CustomerService customerService;
    @GetMapping("/books")
    public String listAllBooks(Model model){
        model.addAttribute("books",bookService.findAllBooks());
        return "books";
    }
    @GetMapping("/books/detail")
    public String bookDetails(@RequestParam("id")int id, Model model){
        model.addAttribute("book",bookService.findBookById(id));
        return "bookdetails";
    }

    @GetMapping("/books/add")
    public String addToCart(@RequestParam("id")int id){

        cartService.addToCart(bookService.findBookById(id));
        return "redirect:/book/books/detail?id="+id;
    }
  private   boolean action = false;
    @PostMapping("/checkout")
    public String checkout(Book book,Model model){
        int i= 0;
      for (CartItem cartItem:cartService.listCartItems()){
          cartItem.setQuantity(book.getQuantityList().get(i));
          i++;
      }
      /*  System.out.println(cartService.listCartItems()+"=================================");
        action = true;
        model.addAttribute("action",true);*/

      //  cartService.setValueRedener(true);
        //cartService.clearCart();
        return "redirect:/book/register";
    }
    @GetMapping("/register")
    public String registerCustomer(Model model){
        model.addAttribute("customer",new Customer());
        return "register";
    }
    @PostMapping("/save-customer")
    public String saveCustomer(Customer customer, BindingResult result){
        if (result.hasErrors()){
            return "register";
        }
        else {
            customerService.saveCustomer(customer);
            cartService.clearCart();
            return "redirect:/login";
        }
    }

    @GetMapping("/books/view/cart")
    public String viewCart(Model model){
        action = false;
        model.addAttribute("action",action);
        model.addAttribute("book",new Book());
        // cartService.setValueRedener(false);
        model.addAttribute("cartItems",cartService.listCartItems());
        return "viewCart";
    }
    @GetMapping("/info")
    public String showInfo(Model model){
        model.addAttribute("cartItems",cartService.listCartItems());
        //cartService.clearCart();
        return "info";
    }


    @GetMapping("/delete")
    public String removeCartItem(@RequestParam("id") int id){
        cartService.deleteById(id);
        return "redirect:/book/books/view/cart";
    }
    @GetMapping("/remove/all")
    public String removeAll(){
        cartService.clearCart();
        return "redirect:/book/books/view/cart";
    }

    @ModelAttribute("cartSize")
    public int cartSize(){
        return cartService.cartSize();
    }
    @ModelAttribute("totalPrice")
    public double totoalPrice(){
        return cartService.listCartItems()
                .stream()
                .map(c->{
                    c.setSum(c.getPrice()*c.getQuantity());
                    return c.getSum();
                })
                .mapToDouble(i -> i)
                .sum();
    }

}
