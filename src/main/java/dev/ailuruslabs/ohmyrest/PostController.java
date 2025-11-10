package dev.ailuruslabs.ohmyrest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Comparator;
import java.util.function.Predicate;

@RestController
@RequestMapping(value = "/api/posts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private final PostRepository postRepository;

    PostController(PostRepository postRepo) {
        this.postRepository = postRepo;
    }

    @PostMapping
    public Mono<ResponseEntity<Post>> createPost(@RequestBody Mono<Post> newPostMono) {
        return newPostMono.flatMap(postRepository::save)
            .map(savedPost -> {
                URI newPostURI = URI.create("/api/posts/" + savedPost.id());

                return ResponseEntity.created(newPostURI)
                    .body(savedPost);
            });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Post>> getPost(@PathVariable Integer id) {
        return postRepository.findById(id)
            .map(post -> ResponseEntity.ok().body(post))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorResume(throwable -> {
                throwable.printStackTrace();
                return Mono.just(ResponseEntity.internalServerError().build());
            });
    }

    @GetMapping
    public Flux<Post> getPosts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "createdAt") String sort_by
    ) {
        return Mono.fromCallable(() -> switch (sort_by) {
                case "createdAt" -> Comparator.comparing(Post::dateTime);
                case "title" -> Comparator.comparing(Post::title);
                default -> throw new IllegalArgumentException();
            })
            .flatMapMany(comparator -> postRepository.findAll()
                .sort(comparator)
                .skip((long) (page - 1) * limit)
                .take(limit, true)
            )
            .onErrorMap(IllegalArgumentException.class, e ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST)
            )
            .onErrorMap(e -> !(e instanceof ResponseStatusException), e ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            );
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePost(@PathVariable Integer id) {
        return postRepository.findById(id)
            .flatMap(post -> postRepository.delete(post).thenReturn(ResponseEntity.noContent().<Void>build()))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Post>> updatePost(@PathVariable int id, @RequestBody Mono<Post> updatedPostMono) {
        return updatedPostMono.flatMap(updatedPost -> postRepository.findById(id)
            .flatMap(fetchedPost -> {
                var newPost = new Post(fetchedPost.id(), updatedPost.title(), updatedPost.content(), updatedPost.dateTime());
                return postRepository.save(newPost);
            })
            .map(newPost -> ResponseEntity.ok().body(newPost))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorResume(t -> Mono.just(ResponseEntity.internalServerError().build()))
        );
    }
}