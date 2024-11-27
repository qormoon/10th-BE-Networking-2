package cotato.backend.domains.post;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.domains.post.dto.request.CreatePostRequest;
import cotato.backend.domains.post.dto.request.SavePostsByExcelRequest;
import cotato.backend.domains.post.dto.response.PostPageResponse;
import cotato.backend.domains.post.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private static final int PAGE_SIZE = 10;

	private final PostService postService;

	@PostMapping("/excel")
	public ResponseEntity<DataResponse<Void>> savePostsByExcel(@RequestBody SavePostsByExcelRequest request) {
		postService.saveEstatesByExcel(request.getPath());

		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/")
	public ResponseEntity<DataResponse<Void>> createPost(@RequestBody CreatePostRequest request) {
		postService.createPost(request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/{postId}")
	public ResponseEntity<DataResponse<PostResponse>> getPostById(@PathVariable Long postId) {
		PostResponse response = postService.getPostById(postId);
		return ResponseEntity.ok(DataResponse.from(response));
	}

	@GetMapping("/")
	public ResponseEntity<DataResponse<PostPageResponse>> getPostList(
		@RequestParam(defaultValue = "0") int page // 기본 페이지는 0번
	) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("likes").descending());
		PostPageResponse response = postService.getPostList(pageable);
		return ResponseEntity.ok(DataResponse.from(response));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<DataResponse<Void>> deletePost(@PathVariable Long postId) {
		postService.deletePost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}


}
