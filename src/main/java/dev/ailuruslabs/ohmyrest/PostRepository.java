package dev.ailuruslabs.ohmyrest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PostRepository extends R2dbcRepository<Post, Integer> {

    Flux<Post> findAllBy(Pageable pageable);

}