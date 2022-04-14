package org.springzhisuan.core.http.test;

import lombok.Getter;
import lombok.Setter;
import org.springzhisuan.core.http.CssQuery;

import java.util.List;

@Getter
@Setter
public class OsChina {

	@CssQuery(value = "head > title", attr = "text")
	private String title;

	@CssQuery(value = "#v_news .page .news", inner = true)
	private List<VNews> vNews;

	@CssQuery(value = ".blog-container .blog-list div", inner = true)
	private List<VBlog> vBlogList;

}
