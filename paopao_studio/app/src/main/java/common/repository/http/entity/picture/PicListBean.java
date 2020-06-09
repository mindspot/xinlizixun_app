package common.repository.http.entity.picture;

import java.util.List;

public class PicListBean {

	private String type;//照片类型 1:身份证,2:学历证明,3:工作证明,4:薪资证明,5:财产证明,6、工牌照片、7、个人名片，8、银行卡 100:其它证明
	private String title;//标题
	private String notice;//上传照片提示文案
	private int max_pictures;//最大上传图片数量
	private List<PicInfo> data;//图片信息

	public int getMax_pictures() {
		return max_pictures;
	}
	public void setMax_pictures(int max_pictures) {
		this.max_pictures = max_pictures;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}

	public List<PicInfo> getData() {
		return data;
	}
	public void setData(List<PicInfo> data) {
		this.data = data;
	}
}
