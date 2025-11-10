package dev.ailuruslabs.ohmyrest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Service
public class PostService {

    private final PostRepository postRepository;

    PostService(PostRepository repo) {
        this.postRepository = repo;
    }

    public Mono<Post> createPost(PostRequest postRequest) {
        return postRepository.save(
            new Post(null, postRequest.title(), postRequest.content(), ZonedDateTime.now(), null)
        );
    }

    public Mono<Post> findPostById(int id) {
        return postRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist.")));
    }

    public Flux<Post> findPosts(Pageable pageable) {
        return postRepository.findAllBy(pageable);
    }

    public Mono<Post> updatePost(int id, PostRequest postRequest) {
        return findPostById(id).flatMap(
            post -> postRepository.save(
                new Post(id, postRequest.title(), postRequest.content(), post.createdAt(), ZonedDateTime.now())
            )
        );
    }

    public Mono<Void> deletePost(int id) {
        return postRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist.")))
            .flatMap(post -> postRepository.delete(post).then());
    }
}
