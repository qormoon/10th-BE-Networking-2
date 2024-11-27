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
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private static final int PAGE_SIZE = 10;

	private final PostService postService;

	@Operation(summary = "엑셀 파일로 게시글 저장", description = "엑셀 파일 경로를 받아 게시글 목록을 저장합니다.")
	@PostMapping("/excel")
	public ResponseEntity<DataResponse<Void>> savePostsByExcel(@RequestBody SavePostsByExcelRequest request) {
		postService.saveEstatesByExcel(request.getPath());

		return ResponseEntity.ok(DataResponse.ok());
	}

	@Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
	@PostMapping("/")
	public ResponseEntity<DataResponse<Void>> createPost(@RequestBody CreatePostRequest request) {
		postService.createPost(request);

		return ResponseEntity.ok(DataResponse.ok());
	}

	@Operation(summary = "게시글 상세 조회", description = "ID를 통해 특정 게시글을 조회합니다.")
	@GetMapping("/{postId}")
	public ResponseEntity<DataResponse<PostResponse>> getPostById(@PathVariable Long postId) {
		PostResponse response = postService.getPostById(postId);
		return ResponseEntity.ok(DataResponse.from(response));
	}

	@Operation(summary = "게시글 목록 조회", description = "게시글을 좋아요 수(likes) 순으로 정렬하여 페이징 처리 후 반환합니다.")
	@GetMapping("/")
	public ResponseEntity<DataResponse<PostPageResponse>> getPostList(
		@RequestParam(defaultValue = "0") int page // 기본 페이지는 0번
	) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("likes").descending());
		PostPageResponse response = postService.getPostList(pageable);
		return ResponseEntity.ok(DataResponse.from(response));
	}

	@Operation(summary = "게시글 삭제", description = "ID를 통해 특정 게시글을 삭제합니다.")
	@DeleteMapping("/{postId}")
	public ResponseEntity<DataResponse<Void>> deletePost(@PathVariable Long postId) {
		postService.deletePost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}


}
