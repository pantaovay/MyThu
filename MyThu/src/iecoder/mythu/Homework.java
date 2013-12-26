package iecoder.mythu;

/*
 * 作业类（数据结构）
 */
public class Homework {
	public String title;
	public String start;
	public String end;
	public Boolean isSubmitted;
	
	/*
	 * 构造函数
	 * @param title 作业题目
	 * @param start 生效日期
	 * @param end 截止日期
	 * @param isSubmitted 提交状态
	 */
	public Homework(String title, String start, String end, Boolean isSubmitted) {
		this.title = title;
		this.start = start;
		this.end = end;
		this.isSubmitted = isSubmitted;
	}
}
