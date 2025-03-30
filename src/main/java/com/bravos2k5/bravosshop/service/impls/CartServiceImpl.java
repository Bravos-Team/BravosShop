package com.bravos2k5.bravosshop.service.impls;

import com.bravos2k5.bravosshop.security.principal.TokenInfo;
import com.bravos2k5.bravosshop.dto.AddToCartDto;
import com.bravos2k5.bravosshop.dto.CartItemDto;
import com.bravos2k5.bravosshop.dto.CartQuantityDto;
import com.bravos2k5.bravosshop.model.cart.Cart;
import com.bravos2k5.bravosshop.model.cart.CartItem;
import com.bravos2k5.bravosshop.model.product.Product;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.repository.CartItemRepository;
import com.bravos2k5.bravosshop.repository.CartRepository;
import com.bravos2k5.bravosshop.repository.UserRepository;
import com.bravos2k5.bravosshop.service.interfaces.CartService;
import com.bravos2k5.bravosshop.service.interfaces.CookieService;
import com.bravos2k5.bravosshop.service.interfaces.ProductService;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    public void mergeCart(String username) {

        String guestCartCookieValue = cookieService.getValue("guestCartId");
        if(guestCartCookieValue == null || guestCartCookieValue.isBlank()) {
            return;
        }

        Long guestCartId;

        try {
            guestCartId = Long.parseLong(guestCartCookieValue);
        } catch (NumberFormatException e) {
            log.error("Wrong type guestCartId");
            return;
        }

        Cart guestCart = cartRepository.findById(guestCartId).orElse(null);

        if(guestCart == null) {
            return;
        }

        if(guestCart.getCartItems().isEmpty()) {
            this.deleteCart(guestCartId);
            return;
        }

        Cart cart = this.findCartByUsername(username);

        Map<Long,CartItem> productCartItemMap = cart.getCartItems().stream()
                .collect(Collectors.toMap(
                        cartItem -> cartItem.getProduct().getId(),
                        cartItem -> cartItem));

        List<CartItem> cartItemUpdate = new ArrayList<>();

        guestCart.getCartItems().forEach(guestCartItem -> {

            if(productCartItemMap.containsKey(guestCartItem.getProduct().getId())) {
                CartItem cartItem = productCartItemMap.get(guestCartItem.getProduct().getId());
                cartItem.setQuantity(guestCartItem.getQuantity());
                cartItemUpdate.add(cartItem);
            } else {
                guestCartItem.setId(identifyGenerator.generateId());
                guestCartItem.setCart(cart);
                cartItemUpdate.add(guestCartItem);
            }

        });

        cartItemRepository.saveAllAndFlush(cartItemUpdate);

        this.deleteCart(guestCartId);
    }

    @Override
    public Cart createGuestCart() {
        Cart cart = new Cart(identifyGenerator.generateId(), null);
        Cookie cookie = new Cookie("guestCartId",String.valueOf(cart.getId()));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3 * 24 * 3600);
        cookie.setPath("/");
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
    public CartQuantityDto updateQuantity(CartQuantityDto cartQuantityDto) {

        if(cartQuantityDto == null) throw new NullPointerException();

        final Long quantity = cartQuantityDto.getQuantity();
        final Long itemId = cartQuantityDto.getItemId();

        if(quantity == null || quantity < 0 || quantity > 999) {
            throw new IllegalArgumentException("Quantity value must between 0 and 999");
        }

        if(itemId == null) {
            throw new IllegalArgumentException("Item id is invalid");
        }

        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(IllegalArgumentException::new);
        if(quantity == 0) {
            cartItemRepository.deleteById(itemId);
            return null;
        }
        cartItem.setQuantity(quantity);
        cartItem = cartItemRepository.saveAndFlush(cartItem);
        return new CartQuantityDto(cartItem.getId(),cartItem.getQuantity());

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
        if(cart == null) {
            cart = this.createGuestCart();
        }
        CartItem cartItem = cartItemRepository.findByProductAndCart(product,cart);
        if(cartItem == null) {
            cartItem = new CartItem(identifyGenerator.generateId(),cart,product,quantity);
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cart.getCartItems().add(cartItem);
        cartItemRepository.saveAndFlush(cartItem);
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
        Cart cart = this.getCartInSession();
        if(cart == null) {
            return List.of();
        }
        return this.findAllCartItemByCartId(cart.getId());
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
                    return cartRepository.findById(cartId).orElse(null);
                } catch (NumberFormatException e) {
                    cookieService.deleteCookie("guestCartId");
                }
            }
            return null;
        }
    }

}
