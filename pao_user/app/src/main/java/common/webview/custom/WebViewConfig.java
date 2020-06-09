package common.webview.custom;

/**
 * @author Administrator
 * @date 2017/12/27
 */
public class WebViewConfig {

    // ************** data **************

    private String title;
    private String url;

    private boolean isPush;
    private String jumpToHome;

    private String authMethod;


    private WebViewConfig() {
    }


    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public boolean isPush() {
        return isPush;
    }

    public String getJumpToHome() {
        return jumpToHome;
    }

    public String getAuthMethod() {
        return authMethod;
    }


    public static final class Builder {
        private final WebViewConfig config;

        public Builder() {
            config = new WebViewConfig();
        }

        public Builder setTitle(String title) {
            config.title = title;
            return this;
        }

        public Builder setUrl(String url) {
            config.url = url;
            return this;
        }

        public Builder setIsPush(boolean isPush) {
            config.isPush = isPush;
            return this;
        }

        public Builder setJumpToHome(String jumpToHome) {
            config.jumpToHome = jumpToHome;
            return this;
        }

        public Builder setAuthMethod(String authMethod) {
            config.authMethod = authMethod;
            return this;
        }

        public WebViewConfig build() {
            return config;
        }
    }
}
