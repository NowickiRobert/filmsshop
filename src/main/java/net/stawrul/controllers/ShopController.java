package net.stawrul.controllers;

import net.stawrul.model.Order;
import net.stawrul.services.OrdersService;
import net.stawrul.services.exceptions.OutOfStockException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * Kontroler obejmujący akcje na zamówieniach.
 */
@RestController
public class ShopController {

    //Komponent realizujący logikę biznesową operacji na zamówieniach
    final OrdersService ordersService;

    //Instancja klasy OrdersService zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public ShopController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    /**
     * Pobieranie listy wszystkich zamówień.
     *
     * @return lista zamówień
     */
    @GetMapping("/orders")
    public List<Order> listOrders() {
        return ordersService.findAll();
    }

    /**
     * Pobieranie informacji o pojedynczym zamówieniu.
     *
     * @param id identyfikator poszukiwanego zamówienia
     * @return odpowiedź 200 OK zawierające dane zamówienia lub odpowiedź 404 Not Found, jeśli id nie występuje w bazie
     * danych
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID id) {
        Order order = ordersService.find(id);
        return isNull(order) ? ResponseEntity.notFound().build() : ResponseEntity.ok(order);
    }

    /**
     * Składanie zamówienia.
     *
     * @param order zamówienie
     * @param uriBuilder pomocniczy obiekt do budowania adresu wskazującego na nowo dodane zamówienie,
     *                   zostanie wstrzyknięty przez framework Spring
     * @return odpowiedź 201 Created zawierająca nagłówek Location z adresem nowego zamówienia lub odpowiedź 422
     * Unprocessable Entity, jeśli zamówienie zostało odrzucone (np. z powodu braku produktów)
     */
    @PostMapping("/orders")
    public ResponseEntity<Void> addOrder(@RequestBody Order order, UriComponentsBuilder uriBuilder) {
        try {
            ordersService.placeOrder(order);
            URI location = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
            return ResponseEntity.created(location).build();

        } catch (OutOfStockException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
