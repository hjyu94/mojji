package me.hjeong.mojji.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.hjeong.mojji.post.PostRepositoryExtension;
import me.hjeong.mojji.post.PostRepositoryExtensionImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
@ComponentScan(basePackages = "me.hjeong.mojji.factory")
public class DataJpaConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    PostRepositoryExtension postRepositoryExtension() {
        return new PostRepositoryExtensionImpl(jpaQueryFactory());
    }
}
