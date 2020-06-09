package common.repository.http.entity.artivles;

import java.io.Serializable;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class ArtivlesArtivlesItemBean implements Serializable {
    private String articleVal;//标题
    private String articleTitle;//副标题
    private String content;//内容
    private String articleImg;//图片
    private String author;//作者
    private String browseVolume;//浏览量
    private String articleLink;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBrowseVolume() {
        return browseVolume;
    }

    public void setBrowseVolume(String browseVolume) {
        this.browseVolume = browseVolume;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }
}
