package dev.ailuruslabs.ohmyrest;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/posts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private final PostService postService;

    PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Mono<ResponseEntity<Post>> createPost(@Valid @RequestBody Mono<PostRequest> newPostRequestMono) {
        return newPostRequestMono.flatMap(postService::createPost)
            .map(savedPost -> {
                var newPostURI = URI.create("/api/posts/" + savedPost.id());

                return ResponseEntity.created(newPostURI)
                    .body(savedPost);
            });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Post>> getPost(@PathVariable Integer id) {
        return postService.findPostById(id)
            .map(post -> ResponseEntity.ok().body(post));
    }

    @GetMapping
    public Flux<Post> getPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "createdAt") String sort_by,
        @RequestParam(defaultValue = "descending") String order
    ) {
        return Mono.fromCallable(() -> {
                var sortField = switch (sort_by) {
                    case "createdAt" -> "createdAt";
                    case "title" -> "title";
                    default -> throw new IllegalArgumentException(sort_by + " is not a recognized sort field.");
                };

                var direction = switch (order) {
                    case "ascending" -> Sort.Direction.ASC;
                    case "descending" -> Sort.Direction.DESC;
                    default -> throw new IllegalArgumentException(order + " is not a recognized order direction.");
                };

                var sort = Sort.by(direction, sortField);

                return PageRequest.of(page, limit, sort);
            })
            .flatMapMany(postService::findPosts);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePost(@PathVariable Integer id) {
        return postService.deletePost(id).thenReturn(ResponseEntity.noContent().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Post>> updatePost(@PathVariable int id,
                                                 @Valid @RequestBody Mono<PostRequest> updatedPostRequestMono) {
        return updatedPostRequestMono.flatMap(postRequest -> postService.updatePost(id, postRequest))
            .map(updatedPost -> ResponseEntity.ok().body(updatedPost));
    }
}