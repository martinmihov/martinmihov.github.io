package martinBlog.bindingModel;

import javax.validation.constraints.NotNull;

/**
 * Created by Martin on 4/19/2017.
 */
public class ArticleBindingModel {

    @NotNull
    private String title;

    @NotNull
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
