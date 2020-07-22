package me.hjeong.mojji.infra.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.hjeong.mojji.module.post.repository.PostRepositoryExtension;
import me.hjeong.mojji.module.post.repository.PostRepositoryExtensionImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
@ComponentScan(basePackages = "me.hjeong.mojji.infra.factory")
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
