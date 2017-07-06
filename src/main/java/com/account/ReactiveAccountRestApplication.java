package com.account;

import com.account.domain.entities.Account;
import com.account.domain.repositories.ReactiveAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

/**
 * Created by rodrigo.chaves on 27/06/2017.
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoAuditing
@EnableReactiveMongoRepositories
public class ReactiveAccountRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactiveAccountRestApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(ReactiveAccountRepository reactiveAccountRepository) {
        return (String... p) -> {
            Flux<Account> just = Flux.just(
                    new Account(100d, com.account.domain.enums.Currency.USD),
                    new Account(150d, com.account.domain.enums.Currency.BRL),
                    new Account(200d, com.account.domain.enums.Currency.EUR));
            reactiveAccountRepository.saveAll(just).log();
        };
    }
}
