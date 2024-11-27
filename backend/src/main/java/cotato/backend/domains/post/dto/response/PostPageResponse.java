package cotato.backend.domains.post.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import cotato.backend.domains.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostPageResponse {
	private List<PostSummary> posts;
	private int currentPage;
	private int totalPages;

	public static PostPageResponse from(Page<Post> postPage) {
		List<PostSummary> posts = postPage.getContent().stream()
			.map(PostSummary::from)
			.collect(Collectors.toList());

		return new PostPageResponse(posts, postPage.getNumber(), postPage.getTotalPages());
	}
}
