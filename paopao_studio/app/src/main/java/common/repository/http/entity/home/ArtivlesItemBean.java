package common.repository.http.entity.home;

import java.io.Serializable;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class ArtivlesItemBean implements Serializable {
    private String articleVal;//标题
    private String articleTitle;//副标题
    private String content;//内容
    private String articleImg;//图片
    private String articleLink;//地址

    public String getArticleVal() {
        return articleVal;
    }

    public void setArticleVal(String articleVal) {
        this.articleVal = articleVal;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }
}
