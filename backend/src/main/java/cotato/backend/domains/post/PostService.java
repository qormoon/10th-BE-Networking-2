package cotato.backend.domains.post;

import static cotato.backend.common.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.excel.ExcelUtils;
import cotato.backend.common.exception.ApiException;
import cotato.backend.common.exception.ErrorCode;
import cotato.backend.domains.post.dto.request.CreatePostRequest;
import cotato.backend.domains.post.dto.response.PostPageResponse;
import cotato.backend.domains.post.dto.response.PostResponse;
import cotato.backend.domains.post.entity.Post;
import cotato.backend.domains.post.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
public class PostService {
	private final PostRepository postRepository;

	// 로컬 파일 경로로부터 엑셀 파일을 읽어 Post 엔터티로 변환하고 저장
	public void saveEstatesByExcel(String filePath) {
		try {
			// 엑셀 파일을 읽어 데이터 프레임 형태로 변환
			List<Post> posts = ExcelUtils.parseExcelFile(filePath).stream()
				.map(row -> {
					String title = row.get("title");
					String content = row.get("content");
					String name = row.get("name");

					return Post.builder()
						.title(title)
						.content(content)
						.name(name)
						.build();
				})
				.collect(Collectors.toList());

			postRepository.saveAll(posts);

		} catch (Exception e) {
			log.error("Failed to save estates by excel", e);
			throw ApiException.from(INTERNAL_SERVER_ERROR);
		}
	}

	public void createPost(CreatePostRequest request) {
		Post post = Post.builder()
			.title(request.getTitle())
			.content(request.getContent())
			.name(request.getName())
			.build();
		postRepository.save(post);
	}

	public PostResponse getPostById(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> ApiException.from(ErrorCode.POST_NOT_FOUND));

		post.incrementViews();

		return PostResponse.from(post);
	}

	public PostPageResponse getPostList(Pageable pageable) {
		Page<Post> postPage = postRepository.findAllByOrderByLikesDesc(pageable);
		return PostPageResponse.from(postPage);
	}

	public void deletePost(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> ApiException.from(ErrorCode.POST_NOT_FOUND));
		postRepository.delete(post);
	}
}
