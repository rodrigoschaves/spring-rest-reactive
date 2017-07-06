package com.account.rest;

import com.account.domain.entities.Account;
import com.account.domain.enums.Currency;
import com.account.domain.repositories.ReactiveAccountRepository;
import com.mongodb.async.client.Observer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * Created by rodrigo.chaves on 20/06/2017.
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final ReactiveAccountRepository reactiveAccountRepository;

    public AccountController(ReactiveAccountRepository reactiveAccountRepository) {
        this.reactiveAccountRepository = reactiveAccountRepository;
    }

    @RequestMapping(value = "/search/bycurrency", method = RequestMethod.GET)
    Flux<Account> findByCurrency(@RequestParam String currency) {
        return reactiveAccountRepository.findByCurrency(Currency.fromValue(currency));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    Mono<Account> findById(@PathVariable String id) {
        return reactiveAccountRepository.findById(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    Mono<Account> save(@RequestBody Account account) {
        return reactiveAccountRepository.save(account);
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    Flux<Account> saveAll(@RequestBody Flux<Account> accounts) {
        return reactiveAccountRepository.saveAll(accounts).doOnComplete(System.out::println);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    Flux<Account> findAll() {
        return reactiveAccountRepository.findAll();
    }

    @RequestMapping(value = "/stream", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Account> streamAll() {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(100));
        return Flux.zip(reactiveAccountRepository.findFirst100ByOrderByCreationDateDesc(), interval).map(t -> t.getT1());
    }
}
