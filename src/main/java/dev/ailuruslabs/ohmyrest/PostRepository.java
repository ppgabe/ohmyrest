package dev.ailuruslabs.ohmyrest;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ReactiveCrudRepository<Post, Integer> {}