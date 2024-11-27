package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {
	private Long id;
	private String title;
	private String content;
	private String name;
	private int views;

	public static PostResponse from(Post post) {
		return new PostResponse(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getName(),
			post.getViews()
		);
	}
}
