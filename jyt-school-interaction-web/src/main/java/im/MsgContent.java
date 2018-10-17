package im;

public class MsgContent {
	    //type=0 代表web url，type=1 代表 本地url）
	    private String type = "0";
	    //private String nameSpace = "service.dev.systoon.com";
	    private String url;
	    private String args = null;
	    private String buttonTitle;

	    public MsgContent(String url, String buttonTitle) {
	        super();
	        this.url = url;
	        this.buttonTitle = buttonTitle;
	    }

	    public String getButtonTitle() {
	        return buttonTitle;
	    }

	    public void setButtonTitle(String buttonTitle) {
	        this.buttonTitle = buttonTitle;
	    }

	    public MsgContent() {
	    }

	    public MsgContent(String args) {
	        this.args=args;
	    }

	    public String getUrl() {
	        return url;
	    }

	    public void setUrl(String url) {
	        this.url = url;
	    }

	    public String getArgs() {
	        return args;
	    }

	    public void setArgs(String args) {
	        this.args = args;
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }
	}
