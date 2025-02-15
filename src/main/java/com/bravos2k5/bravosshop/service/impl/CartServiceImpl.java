package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.config.security.TokenInfo;
import com.bravos2k5.bravosshop.dto.cart.AddToCartDto;
import com.bravos2k5.bravosshop.dto.cart.CartItemDto;
import com.bravos2k5.bravosshop.model.cart.Cart;
import com.bravos2k5.bravosshop.model.cart.CartItem;
import com.bravos2k5.bravosshop.model.product.Product;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.repo.CartItemRepository;
import com.bravos2k5.bravosshop.repo.CartRepository;
import com.bravos2k5.bravosshop.repo.UserRepository;
import com.bravos2k5.bravosshop.service.CartService;
import com.bravos2k5.bravosshop.service.CookieService;
import com.bravos2k5.bravosshop.service.ProductService;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final IdentifyGenerator identifyGenerator;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CookieService cookieService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, IdentifyGenerator identifyGenerator,
                           CartItemRepository cartItemRepository, ProductService productService, CookieService cookieService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.identifyGenerator = identifyGenerator;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.cookieService = cookieService;
    }

    @Override
    public Cart findCartById(Long cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }

    @Override
    public Cart findCartByUsername(String username) {
        return cartRepository.findByUser_Username(username);
    }

    @Override
    public Cart findCartByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) return null;
        return cartRepository.findByUser(user).orElse(null);
    }

    @Override
    public Cart mergeCart(Long guestCartId, Long cartId) {
        Cart guestCart = cartRepository.findById(guestCartId).orElse(null);
        Cart cart = cartRepository.findById(cartId).orElseThrow(IllegalArgumentException::new);

        if(guestCart == null) {
            return cart;
        }

        if(guestCart.getCartItems().isEmpty()) {
            cartRepository.deleteById(guestCartId);
            return cart;
        }

        guestCart.getCartItems().forEach(cartItem -> {
            CartItem guestCartItem = new CartItem();
            guestCartItem.setId(identifyGenerator.generateId());
            guestCartItem.setCart(cart);
            guestCartItem.setProduct(cartItem.getProduct());
            guestCartItem.setQuantity(cartItem.getQuantity());
            cart.getCartItems().remove(cartItem);
            cart.getCartItems().add(guestCartItem);
        });

        return cartRepository.saveAndFlush(cart);
    }

    @Override
    public Cart createGuestCart() {
        Cart cart = new Cart(identifyGenerator.generateId(), null);
        Cookie cookie = new Cookie("guestCardId",String.valueOf(cart.getId()));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3 * 24 * 3600);
        cookieService.addCookie(cookie);
        return cartRepository.saveAndFlush(cart);
    }

    @Override
    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null || cart.getUser() != null) return;
        cartRepository.deleteById(cartId);
    }

    @Override
    public Cart cleanCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) return null;
        cart.getCartItems().clear();
        return cartRepository.saveAndFlush(cart);
    }

    @Override
    public CartItem updateQuantity(Long itemId, Long quantity) {
        if(quantity == null || quantity < 0 || quantity > 999) {
            throw new IllegalArgumentException("Quantity value must between 0 and 999");
        }
        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(IllegalArgumentException::new);
        if(quantity == 0) {
            cartItemRepository.deleteById(itemId);
            return null;
        }
        cartItem.setQuantity(quantity);
        return cartItemRepository.saveAndFlush(cartItem);
    }

    @Override
    public void addToCart(AddToCartDto addToCartDto) {
        Long quantity = addToCartDto.getQuantity();
        Long productId = addToCartDto.getProductId();

        if(quantity < 1) {
            return;
        }

        Product product = productService.findById(productId);
        if(product == null) {
            throw new IllegalArgumentException("ProductId is invalid");
        }

        Cart cart = this.getCartInSession();
        CartItem cartItem = cartItemRepository.findByProductAndCart(product,cart);
        if(cartItem == null) {
            cartItem = new CartItem(identifyGenerator.generateId(),cart,product,quantity);
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cart.getCartItems().add(cartItem);
        cartRepository.saveAndFlush(cart);
    }

    @Override
    public List<CartItemDto> findAllCartItemByCartId(Long cartId) {
        if(cartId == null) {
            return List.of();
        }
        return cartRepository.getAllCartItemId(cartId);
    }

    @Override
    public List<CartItemDto> getCartItemsInSession() {
        Long cartId = this.getCartInSession().getId();
        return this.findAllCartItemByCartId(cartId);
    }

    @Override
    public Cart getCartInSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof TokenInfo tokenInfo) {
            return this.findCartByUsername(tokenInfo.getUsername());
        }
        else {
            Cookie cookie = cookieService.getCookie("guestCartId");
            if(cookie != null) {
                try {
                    Long cartId = Long.parseLong(cookie.getValue());
                    return cartRepository.findById(cartId).orElseGet(this::createGuestCart);
                } catch (NumberFormatException e) {
                    cookieService.deleteCookie("guestCartId");
                }
            }
            return createGuestCart();
        }
    }

}
