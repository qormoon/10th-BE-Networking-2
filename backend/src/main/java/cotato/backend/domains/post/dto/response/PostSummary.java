package cotato.backend.domains.post.dto.response;

import cotato.backend.domains.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSummary {
	private Long id;
	private String title;
	private String name;

	public static PostSummary from(Post post) {
		return new PostSummary(post.getId(), post.getTitle(), post.getName());
	}
}
